package dev.sentix.squaremarker.paper

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import org.bukkit.NamespacedKey
import org.bukkit.Server
import xyz.jpenilla.squaremap.api.BukkitAdapter
import xyz.jpenilla.squaremap.api.WorldIdentifier
import java.lang.reflect.Type

class PaperWorldIdentifierSerializer(
    private val server: Server,
) : JsonSerializer<WorldIdentifier>,
    JsonDeserializer<WorldIdentifier> {
    override fun serialize(
        src: WorldIdentifier?,
        typeOfSrc: Type,
        context: JsonSerializationContext,
    ): JsonElement {
        if (src == null) return JsonNull.INSTANCE
        return JsonPrimitive(src.asString())
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type,
        context: JsonDeserializationContext,
    ): WorldIdentifier? {
        if (json == null || json is JsonNull) return null
        val tryDeserializeKey = NamespacedKey.fromString(json.asString)
        if (tryDeserializeKey != null) {
            val world = server.getWorld(tryDeserializeKey)
            if (world != null) return WorldIdentifier.parse(json.asString)
        }
        val world = server.getWorld(json.asString)
        if (world == null) {
            try {
                return WorldIdentifier.parse(json.asString)
            } catch (ex: Exception) {
                throw JsonParseException("'${json.asString}' is an invalid WorldIdentifier and no world exists with that name.", ex)
            }
        }
        return BukkitAdapter.worldIdentifier(world)
    }
}
