package com.example.myapplication.data.remote.webDS

import android.net.Uri
import com.example.myapplication.R
import com.example.myapplication.core.utils.Resource
import com.example.myapplication.core.utils.statusNetwork.Resource2
import com.example.myapplication.core.utils.statusNetwork.ResponseStatus
import com.example.myapplication.data.remote.api.FirebaseApiService
import com.example.myapplication.data.remote.request.UserRegisterRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRegisterWebDS @Inject constructor(private val service: FirebaseApiService) {

    suspend fun doUserRegister(userRegisterRequest: UserRegisterRequest): Resource<Boolean> =
        withContext(
            Dispatchers.IO
        ) {
            val getRegisterUser = async { service.sendRegisterUser(userRegisterRequest) }
            val getRegisterUserResponse = getRegisterUser.await()

            if (getRegisterUserResponse is ResponseStatus.Success) {
                Resource.success(getRegisterUserResponse.data)
            } else
                Resource.error(R.string.problem_registering_user)
        }

    suspend fun doAuthRegister(email: String, pass: String): Resource<Boolean> =
        withContext(Dispatchers.IO) {
            val registerAuth = async { service.sendAuthRegister(email, pass) }
            val response = registerAuth.await()
            if (response is ResponseStatus.Success) {
                Resource.success(response.data)
            } else {
                Resource.error(R.string.problem_registering_user)
            }
        }


    suspend fun doUploadImage(uri: Uri): Resource<Uri> = withContext(Dispatchers.IO) {
        val uploadImage = async { service.sendUploadImage(uri) }
        val getImageResponse = uploadImage.await()
        if (getImageResponse is ResponseStatus.Success) {
            Resource.success(getImageResponse.data)
        } else {
            Resource.error(R.string.upload_image_error)
        }
    }


    suspend fun getAllPositions() = service.getAllPositions()


    suspend fun getAllTeams() :Flow<Resource2<ArrayList<String>>>{
      return  service.getAllTeams().map {
            it
        }.onEach {

        }
    }

}