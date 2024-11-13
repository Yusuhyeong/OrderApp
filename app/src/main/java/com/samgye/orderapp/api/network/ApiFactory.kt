package com.samgye.orderapp.api.network

import com.samgye.orderapp.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object ApiFactory {
    fun withClientAndAdapter(
        url: String,
        clientBuilder: OkHttpClient.Builder,
        factory: CallAdapter.Factory? = null
    ): Retrofit {
        val builder = Retrofit.Builder().baseUrl(url)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(ItsJson.base))
            .client(clientBuilder.build())
        factory?.let {
            builder.addCallAdapterFactory(it)
        }
        return builder.build()

    }

    private val loggingInterceptor by lazy {
        val interceptor = HttpLoggingInterceptor()

        interceptor.level =
            if (Constants.IS_DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.HEADERS
        return@lazy interceptor
    }

    val apiAuth by lazy {
        withClientAndAdapter(
            Constants.BASE_URL,
            OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(100, TimeUnit.SECONDS)
                .callTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS)
                .writeTimeout(100, TimeUnit.SECONDS)
        )
    }

    val apiBearer by lazy {
        withClientAndAdapter(
            Constants.BASE_URL,
            OkHttpClient.Builder()
                .addInterceptor(AccessTokenInterceptor())
                .addInterceptor(loggingInterceptor)
                .connectTimeout(100, TimeUnit.SECONDS)
                .callTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS)
                .writeTimeout(100, TimeUnit.SECONDS)
        )
    }

    val apiBasic by lazy {
        withClientAndAdapter(
            Constants.BASE_URL,
            OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(100, TimeUnit.SECONDS)
                .callTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS)
                .writeTimeout(100, TimeUnit.SECONDS)
        )
    }
}
