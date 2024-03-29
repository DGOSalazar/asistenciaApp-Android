package com.example.myapplication.domain

import com.example.myapplication.R
import com.example.myapplication.core.utils.statusNetwork.ResponseStatus
import com.example.myapplication.sys.utils.Tools
import javax.inject.Inject

class ValidateGeolocationUseCase @Inject constructor(
    private val tools: Tools,
    private val repository: FirebaseRepository
) {
    suspend operator fun invoke(): ResponseStatus<Boolean> {
        val localLocation = tools.getLocation() ?: return ResponseStatus.Error(R.string.error_get_local_location)

        val response = repository.doGetOfficeLocation()

        return if (response is ResponseStatus.Success){
            val firebaseLocation = response.data
            val latitudeRange = firebaseLocation.latitude - 0.00200.. firebaseLocation.latitude + 0.00200
            val longitudeRange = firebaseLocation.longitude - 0.00400.. firebaseLocation.longitude + 0.00400

            val isUserAtOffice = localLocation.latitude in  latitudeRange && localLocation.longitude in longitudeRange

            ResponseStatus.Success(isUserAtOffice)
        }else
            ResponseStatus.Error((response as ResponseStatus.Error).messageId)
    }

}