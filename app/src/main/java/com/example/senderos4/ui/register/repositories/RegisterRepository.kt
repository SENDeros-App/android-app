package com.example.senderos4.ui.register.repositories

import com.example.senderos4.network.ApiResponse
import com.example.senderos4.network.dto.register.RegisterRequest
import com.example.senderos4.network.service.AuthService
import retrofit2.HttpException
import java.io.IOException

class RegisterRepository(private val api:AuthService) {

    suspend fun register(name: String, email: String,phone:String, user:String, password: String): ApiResponse<String> {
        try{
            val response = api.register(RegisterRequest(name = name, email = email, phoneNumber = phone, user = user, password = password ))
            return ApiResponse.Success(response.message)

        } catch (e: HttpException){

            if(e.code() == 400){
                return ApiResponse.ErrorWithMessage("Invalid fields, name, email or password")
            }
            return ApiResponse.Error(e)
        } catch (e: IOException){
            return ApiResponse.Error(e)

        }
    }
}