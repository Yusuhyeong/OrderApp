package com.samgye.orderapp.api.network

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.samgye.orderapp.utils.getKoreanFormat
import com.samgye.orderapp.utils.getKstFormat
import java.lang.reflect.Type
import java.text.DateFormat
import java.text.ParseException
import java.util.Date

class GsonDateFormatAdapter : JsonSerializer<Date?>, JsonDeserializer<Date?> {
    private val encodeDateFormat: DateFormat = getKstFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    private val decodeDateFormat: DateFormat = getKoreanFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")

    @Synchronized
    override fun serialize(
        date: Date?,
        type: Type?,
        jsonSerializationContext: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(encodeDateFormat.format(date))
    }

    @Synchronized
    override fun deserialize(
        jsonElement: JsonElement,
        type: Type?,
        jsonDeserializationContext: JsonDeserializationContext?
    ): Date {
        return try {
            decodeDateFormat.parse(jsonElement.asString)
        } catch (e: ParseException) {
            throw JsonParseException(e)
        }
    }
}