package dev.sentix.squaremarker

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import xyz.jpenilla.squaremap.api.WorldIdentifier
import java.lang.reflect.Type

object WorldIdentifierSerializer :
    JsonSerializer<WorldIdentifier>,
    JsonDeserializer<WorldIdentifier> {

    override fun serialize(src: WorldIdentifier?, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        if (src == null) return JsonNull.INSTANCE
        return JsonPrimitive(src.asString())
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type, context: JsonDeserializationContext): WorldIdentifier? {
        if (json == null || json is JsonNull) return null
        return WorldIdentifier.parse(json.asString)
    }
}
