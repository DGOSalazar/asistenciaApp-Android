package com.example.myapplication.data.network

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.myapplication.data.models.LoginResult
import com.example.myapplication.data.models.User
import com.example.myapplication.data.models.*
import com.google.firebase.auth.AuthResult
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseServices @Inject constructor(
    private val firebase: FirebaseClient
){
    private var url: Uri? =null
    var email = "primero"

    suspend fun login(mail: String, pass: String): LoginResult = runCatching {
        this.email = mail
        firebase.auth.signInWithEmailAndPassword(email,pass).await()
     }.toLoginResult()

    suspend fun register(email: String, pass: String) = runCatching {
        this.email = email
        firebase.auth.createUserWithEmailAndPassword(email,pass).await()
    }

    fun uploadPhoto(uri:Uri) = runCatching {
        val ref: StorageReference = firebase.dataStorage.child("image${uri.lastPathSegment}")
        val uploadTask = ref.putFile(uri)

        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            ref.downloadUrl
        }.addOnFailureListener {
            Log.d("Upload", "error to upload image")
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                url = task.result!!
                Log.d("Upload", "successfully upload")
            }
        }
    }
    suspend fun getUrlF(): Uri? = run {
        while (url==null){
            delay(1000)
        }
        url
    }

    fun registerUserData(user: User) = run {
        firebase.userCollection.document(user.email).set(
            hashMapOf(
                "email" to user.email,
                "name" to user.name,
                "lastName1" to user.lastName1,
                "lastName2" to user.lastName2,
                "position" to user.position,
                "birthDate" to user.birthDate,
                "team" to user.team,
                "profilePhoto" to user.profilePhoto,
                "phone" to user.phone,
                "employee" to user.employee,
                "assistDay" to user.assistDay)
        )
    }.isSuccessful

    fun addUserToDay(currentDay:String, emails: ArrayList<String>)= run{
        firebase.dayCollection.document(currentDay).set(
            hashMapOf(
                "currentDay" to currentDay,
                "email" to emails
            )
        )
    }

    fun getUserInfo(listEmail: ArrayList<String>,user:(ArrayList<User>)-> Unit) = runCatching {
        var user1: User
        val list = arrayListOf<User>()
            for (i in listEmail) {
                firebase.userCollection.whereEqualTo("email", i).get().addOnSuccessListener {
                    it.forEach { i ->
                        user1 = User(
                            i.get("email") as String,
                            i.get("name") as String,
                            i.get("lastName1") as String,
                            i.get("lastName2") as String,
                            i.get("position") as String,
                            i.get("birthDate") as String,
                            i.get("team") as String,
                            i.get("profilePhoto") as String,
                            i.get("phone") as String,
                            i.get("employee") as Long
                        )
                        list.add(user1)
                    }
                    user(list)
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getListUsers(day: String, mails:(ArrayList<String>) -> Unit) = runCatching{
        var emails = AttendanceDays(arrayListOf(),"")

        firebase.dayCollection.whereEqualTo("currentDay", day).get()
            .addOnSuccessListener {
                it.forEach { j ->
                    emails = AttendanceDays(
                        j.get("email") as ArrayList<String>,
                        j.get("currentDay") as String
                    )
                }
                mails(emails.emails)
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAllRegistersDays(
        success:(List<AttendanceDays>) -> Unit,
        errorObserver:(String) -> Unit
    ):List<AttendanceDays> {
        val list = mutableListOf<AttendanceDays>()
        firebase.dayCollection
            .get()
            .addOnSuccessListener { result ->
                result.documents.forEach { document ->
                    val day = AttendanceDays(
                        document.get("email") as ArrayList<String>,
                        document.get("currentDay") as String
                    )
                    list.add(day)
                }
                success(list)
            }
            .addOnFailureListener { exception ->
                Log.w("Firebase", "Error getting documents.", exception)
                exception.message?.let { errorObserver(it) }
            }
        return list
    }

    private fun Result<AuthResult>.toLoginResult() =
        when (val result = getOrNull()){
            null -> LoginResult.Error
            else -> {
                val userId = result.user
                checkNotNull(userId)
                LoginResult.Success(result.user?.isEmailVerified ?: false)
            }
        }
}