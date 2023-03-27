package com.example.firebasestudyproject.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import com.example.firebasestudyproject.model.User
import com.example.firebasestudyproject.ui.login.LoginActivity
import com.example.firebasestudyproject.ui.profile.ProfileActivity
import com.example.firebasestudyproject.ui.register.RegisterActivity
import com.example.firebasestudyproject.ui.settings.SettingsActivity
import com.example.firebasestudyproject.utils.Constants
import com.example.firebasestudyproject.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FireStoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()


    fun registerUser(activity: RegisterActivity, userInfo: User) {
        mFireStore.collection(Constants.USERS).document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegisterSucesss()
            }.addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e("TAG", "registerUser: Error While registering the User", e)
            }
    }

    fun getCurrentUssrId(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId = ""
        if (currentUser != null) {
            currentUserId = currentUser.uid
        }
        return currentUserId
    }


    fun getUserDetials(activity: Activity) {
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUssrId())
            .get()
            .addOnSuccessListener { document ->
                Log.d(activity.javaClass.simpleName, "getUserDetials: $document")

                val user = document.toObject(User::class.java)

                val sharedPreferences =
                    activity.getSharedPreferences(Constants.APP_PREFERENCE, Context.MODE_PRIVATE)

                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString(
                    //key    :- logged_in_username
                    //values :- user?.firstname and last name
                    Constants.LOGGED_IN_USERNAME,
                    "${user?.firstName} ${user?.lastName}"
                )
                editor.apply()

                //start
                when (activity) {
                    is LoginActivity -> {
                        activity.userLoggedInSuccess(user)
                    }
                    is SettingsActivity -> {
                        activity.userDetailSuccess(user)
                    }

                }
            }.addOnFailureListener { e ->
                when (activity) {
                    is LoginActivity -> {
                        activity.hideProgressDialog()
                    }

                    is SettingsActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e(activity.javaClass.simpleName, "getUserDetials: ${e.message}")

            }
    }

    fun updateUserDetails(activity: Activity, userHashMap: HashMap<String, Any>) {
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUssrId())
            .update(userHashMap)
            .addOnSuccessListener {
                when (activity) {
                    is ProfileActivity -> {
                        activity.userProfileUpdateSuccess()
                    }
                }

            }
            .addOnFailureListener { e ->
                when (activity) {
                    is ProfileActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e("TAG", "updateUserDetails: Error While Updating the Details ", e)
            }
    }

    fun uploadImageToCloudStorage(activity: Activity, imageUri: Uri?) {
        val sRef: StorageReference =
            FirebaseStorage.getInstance().reference.child(
                "${Constants.USER_PROFILE_IMAGE}${System.currentTimeMillis()}.${
                    Utils.getFileExtension(
                        activity,
                        imageUri
                    )
                }"
            )
        sRef.putFile(imageUri!!).addOnSuccessListener { taskSnapShot ->
            //The image upload is Success
            Log.e(
                Constants.FIREBASE_TAG,
                "uploadImageToCloudStorage: ${taskSnapShot.metadata?.reference?.downloadUrl}",
            )
            /** GET DOWNLOADABLE URL FROM TASK SNAPSHOT */
            taskSnapShot.metadata?.reference?.downloadUrl?.addOnSuccessListener { url ->
                Log.e(Constants.FIREBASE_TAG, "download image URL :$url ")
                when (activity) {
                    is ProfileActivity -> {
                        activity.imageUploadSuccess(url.toString())
                    }
                }
            }
        }.addOnFailureListener { exception ->
            when (activity) {
                is ProfileActivity -> {
                    activity.hideProgressDialog()
                }
            }
            Log.e(
                Constants.FIREBASE_TAG,
                "uploadImageToCloudStorage: ${exception.message}",
                exception
            )
        }
    }

}