package com.example.senderos4.ui.register.viewmodels

import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.senderos4.network.ApiResponse
import com.example.senderos4.ui.register.RegisterUiStatus
import com.example.senderos4.ui.register.repositories.RegisterRepository
import kotlinx.coroutines.launch

class RegisterViewModel(private val registerRepository:RegisterRepository):ViewModel() {

    var name = MutableLiveData ("")
    var email = MutableLiveData("")
    var phone = MutableLiveData("")
    var user = MutableLiveData("")
    var password = MutableLiveData("")

    private val _status = MutableLiveData<RegisterUiStatus>(RegisterUiStatus.Resume)

    val status :LiveData<RegisterUiStatus>
        get()=_status

    private fun register(name:String,email:String, phone:String, user:String, password:String){
        viewModelScope.launch {
            _status.postValue(
                when(val response =  registerRepository.register(name = name, email = email, phone = phone, user = user, password = password)){
                    is ApiResponse.Error -> RegisterUiStatus.Error(response.exception)
                    is ApiResponse.ErrorWithMessage -> RegisterUiStatus.ErrorWithMessage(response.message)
                    is ApiResponse.Success -> RegisterUiStatus.Success
                }
            )
        }
    }
}