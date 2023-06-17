package com.example.senderos4.ui.login.repositories

import com.example.senderos4.network.ApiResponse
import com.example.senderos4.network.dto.login.LoginRequest
import com.example.senderos4.network.service.AuthService
import retrofit2.HttpException
import java.io.IOException

class LoginRepository(private val api: AuthService) {

    suspend fun login(user: String, password: String): ApiResponse<String>{
        try {
            val response = api.login(LoginRequest(user, password))
            return ApiResponse.Success(response.token)

        } catch (e: HttpException){
            if(e.code() == 400) {

                return ApiResponse.ErrorWithMessage("Invalid email or password hey hey")

            }

            return ApiResponse.Error(e)
        }catch (e: IOException){
            return ApiResponse.Error(e)
        }
    }
}

