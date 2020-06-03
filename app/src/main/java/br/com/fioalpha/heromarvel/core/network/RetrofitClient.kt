package br.com.fioalpha.heromarvel.core.network

import android.accounts.NetworkErrorException
import android.util.Log
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.lang.Exception
import java.net.ConnectException

class RetrofitClient(
    private val baseUrl: String,
    private val client: OkHttpClient,
    private val service: Class<Service>,
    private val gson: Gson
) {

    fun client(): Service = Retrofit.Builder()
        .client(client)
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(service)
}

fun createHttpLogging() = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }

fun createConvertJson() = Gson()

fun createHttpClient(logging: HttpLoggingInterceptor) = OkHttpClient.Builder()
    .apply {
        addInterceptor(HandleError())
        addInterceptor(logging)
    }.build()

class HandleError: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        return try {
            chain.proceed(request)
        } catch(e: IOException) {
            if(e is ConnectException) {
                throw NetworkErrorException("Voce não tem internet")
            }
            throw e
        }
    }
}
