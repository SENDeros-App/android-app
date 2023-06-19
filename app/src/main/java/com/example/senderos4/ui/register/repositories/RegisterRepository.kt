package com.example.senderos4.ui.register.repositories

import android.util.Log
import com.example.senderos4.network.ApiResponse
import com.example.senderos4.network.dto.register.RegisterRequest
import com.example.senderos4.network.service.AuthService
import retrofit2.HttpException
import java.io.IOException

class RegisterRepository(private val api:AuthService) {

    suspend fun register(name: String, email: String,phone:String, user:String, password: String): ApiResponse<String> {
        try{
            val response = api.register(RegisterRequest( username = user,  email = email, password = password,  name = name, phoneNumber = phone))
            return ApiResponse.Success(response.message)

        } catch (e: HttpException){

            if(e.code() == 400){
                return ApiResponse.ErrorWithMessage("invalid data")
            }
            return ApiResponse.Error(e)
        } catch (e: IOException){
            return ApiResponse.Error(e)

        }
    }
}