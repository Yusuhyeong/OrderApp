package com.samgye.orderapp.api.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.Date

object ItsJson {

    // GsonBuilder를 singleton으로 처리하기 위한 internalBuilder
    private val internalBuilder = GsonBuilder()
        .registerTypeAdapter(Date::class.java, GsonDateFormatAdapter())
//        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)

    /**
     * @suppress
     */
    val base: Gson = internalBuilder.create()

    /**
     * @suppress
     */
    val pretty: Gson = internalBuilder.setPrettyPrinting().create()

    fun <T> listFromJson(string: String, type: Class<T>): List<T> =
        base.fromJson(string, TypeToken.getParameterized(List::class.java, type).type)

    fun <T> parameterizedFromJson(string: String, type1: Type, type2: Type): T =
        base.fromJson(string, TypeToken.getParameterized(type1, type2).type)


    fun <T> toJson(model: T): String = base.toJson(model)
    fun <T> fromJson(string: String, type1: Type): T = base.fromJson(string, type1)
}