package com.example.senderos4.ui.register

import com.example.senderos4.network.ApiResponse
import com.example.senderos4.network.dto.register.generalMensaje
import com.example.senderos4.network.dto.register.RegisterRequest
import com.example.senderos4.network.service.AuthService
import com.google.gson.Gson
import retrofit2.HttpException
import java.io.IOException

class RegisterRepository(private val api:AuthService) {

    suspend fun register(name: String, email: String,phone:String, user:String, password: String): ApiResponse<String> {
        try{
            val response = api.register(RegisterRequest( username = user,  email = email, password = password,  name = name, phoneNumber = phone))
            return ApiResponse.Success(response.message)

        } catch (e: HttpException){

            return if(e.code() == 400){

                ApiResponse.ErrorWithMessage("invalid data")

            } else if(e.code()==409){

                val errorMessage = e.response()?.errorBody()?.string().toString()
                val messageError = Gson().fromJson(errorMessage, generalMensaje::class.java)
                ApiResponse.ErrorWithMessage(messageError.message)

            } else{
                ApiResponse.Error(e)
            }

        } catch (e: IOException){

            return ApiResponse.Error(e)

        }
    }
}