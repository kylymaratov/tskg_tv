package com.example.tvapp

import android.app.Application
import com.example.tvapp.api.RetrofitHelper
import com.example.tvapp.api.Repository

class MyApplication : Application() {

    lateinit var repository: Repository

    override fun onCreate() {
        super.onCreate()

        init()
    }

    private fun init(){
        val service = RetrofitHelper.create()
        repository = Repository(service)
    }
}