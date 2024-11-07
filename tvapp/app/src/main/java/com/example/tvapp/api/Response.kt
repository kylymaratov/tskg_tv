
package com.example.tvapp.api

sealed class Response<T>(val data: T? = null, val error: String? = null) {
    class Success<T>(data: T? = null) : Response<T>(data = data)
    class Error<T>(error: String?) : Response<T>(error = error)

}