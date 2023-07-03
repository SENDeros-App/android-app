
package com.example.senderos4.ui.login.repositories

import com.example.senderos4.data.User
import com.example.senderos4.network.ApiResponse
import com.example.senderos4.network.dto.login.LoginData
import com.example.senderos4.network.dto.login.LoginRequest
import com.example.senderos4.network.dto.login.LoginResponse
import com.example.senderos4.network.retrofit.RetrofitInstance
import com.example.senderos4.network.service.AuthService
import com.example.senderos4.ui.login.LoginUiStatus
import retrofit2.HttpException
import java.io.IOException

class LoginRepository(private val api: AuthService) {

    suspend fun login(user: String, password: String): ApiResponse<LoginData>{
        try {
            val response = api.login(LoginRequest(user, password))
            val token = response.token
            val user = response.user
            val loginData = LoginData(token, user)
            return ApiResponse.Success(loginData)

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

