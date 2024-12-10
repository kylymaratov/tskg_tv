package com.example.tvapp
import com.example.tvapp.BuildConfig

import android.app.Application
import com.example.tvapp.api.ApiService
import com.example.tvapp.api.RetrofitHelper

class MyApplication : Application() {
      val BASE_URL: String = BuildConfig.BASE_URL
      val request: ApiService = RetrofitHelper.create(BASE_URL)
}