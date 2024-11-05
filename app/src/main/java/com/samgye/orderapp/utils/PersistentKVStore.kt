package com.samgye.orderapp.utils

interface PersistentKVStore {
    fun getString(key: String, fallbackValue: String? = null): String?
    fun getLong(key: String, fallbackValue: Long = 0): Long
    fun putString(key: String, value: String): PersistentKVStore
    fun putLong(key: String, value: Long): PersistentKVStore
    fun remove(key: String): PersistentKVStore
    fun commit(): PersistentKVStore
    fun apply(): PersistentKVStore
}