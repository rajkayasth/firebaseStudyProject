package com.example.firebasestudyproject.firestore

import android.app.Activity
import android.util.Log
import com.example.firebasestudyproject.model.User
import com.example.firebasestudyproject.ui.login.LoginActivity
import com.example.firebasestudyproject.ui.register.RegisterActivity
import com.example.firebasestudyproject.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

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

                //start
                when (activity) {
                    is LoginActivity -> {
                        activity.userLoggedInSuccess(user)
                    }

                }
            }.addOnFailureListener { e ->
                when (activity) {
                    is LoginActivity -> {
                        activity.hideProgressDialog()
                    }

                }
                Log.e(activity.javaClass.simpleName, "getUserDetials: ${e.message}")

            }
    }

}