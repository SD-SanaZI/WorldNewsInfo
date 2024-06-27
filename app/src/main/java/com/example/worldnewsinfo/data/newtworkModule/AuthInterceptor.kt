package com.example.worldnewsinfo.data.newtworkModule

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = "6f8704935fd24a949b2fe357c3823a9a"
        val request = chain.request()
        if (token.isNotEmpty()) {
            val newRequest = request
                .newBuilder()
                .header("X-Api-Key", token)
                .build()
            Log.d("Net newRequest", newRequest.url.toString())
            return chain.proceed(newRequest)
        }
        return chain.proceed(request)
    }
}