package dev.sentix.squaremarker.paper

import cloud.commandframework.Command
import cloud.commandframework.CommandTree
import cloud.commandframework.context.CommandContext
import cloud.commandframework.exceptions.CommandExecutionException
import cloud.commandframework.execution.CommandExecutionCoordinator
import cloud.commandframework.execution.CommandResult
import cloud.commandframework.services.State
import cloud.commandframework.types.tuples.Pair
import java.util.Queue
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutionException
import java.util.concurrent.locks.ReentrantLock
import java.util.function.Function

class ExecutionCoordinator<C>(commandTree: CommandTree<C>) : CommandExecutionCoordinator<C>(commandTree) {
    companion object {
        fun <T> synchronizedOnFolia(): Function<CommandTree<T>, CommandExecutionCoordinator<T>> = Function {
            ExecutionCoordinator(it)
        }
    }

    private val executionLock: ReentrantLock? = if (folia) ReentrantLock() else null

    override fun coordinateExecution(
        commandContext: CommandContext<C>,
        input: Queue<String>
    ): CompletableFuture<CommandResult<C>> {
        val completableFuture = CompletableFuture<CommandResult<C>>()
        try {
            val pair: Pair<Command<C>?, Exception?> =
                commandTree.parse(commandContext, input)
            if (pair.getSecond() != null) {
                completableFuture.completeExceptionally(pair.getSecond())
            } else {
                val command: Command<C> = pair.getFirst()!!
                if (commandTree.commandManager.postprocessContext(commandContext, command) == State.ACCEPTED) {
                    if (this.executionLock != null) {
                        executionLock.lock()
                    }
                    try {
                        command.getCommandExecutionHandler().executeFuture(commandContext).get()
                    } catch (exception: ExecutionException) {
                        val cause = exception.cause
                        if (cause is CommandExecutionException) {
                            completableFuture.completeExceptionally(cause)
                        } else {
                            completableFuture.completeExceptionally(CommandExecutionException(cause!!, commandContext))
                        }
                    } catch (exception: CommandExecutionException) {
                        completableFuture.completeExceptionally(exception)
                    } catch (exception: Exception) {
                        completableFuture.completeExceptionally(CommandExecutionException(exception, commandContext))
                    } finally {
                        if (this.executionLock != null) {
                            executionLock.unlock()
                        }
                    }
                }
                completableFuture.complete(CommandResult(commandContext))
            }
        } catch (e: Exception) {
            completableFuture.completeExceptionally(e)
        }
        return completableFuture
    }
}
