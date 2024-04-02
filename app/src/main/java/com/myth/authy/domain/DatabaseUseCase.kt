package com.myth.authy.domain

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.myth.authy.data.Details
import javax.inject.Inject

class DatabaseUseCase @Inject constructor(
    auth: FirebaseAuth,
    firestore: FirebaseFirestore
) {
    private val userDetails = firestore.collection("userData")
    private val userUID = auth.currentUser?.uid
    private val documentRef = userDetails.document(userUID!!)

    fun saveData(details: Details, callBack: (success: Boolean, message: String?) -> Unit) {
        documentRef.set(details)
            .addOnSuccessListener { callBack(true, null) }
            .addOnFailureListener { e -> callBack(false, e.message) }
    }

    fun retrieveData(callBack: (Boolean, Details?, String?) -> Unit) {
        documentRef.get()
            .addOnSuccessListener { document ->
                if (!document.exists()) {
                    callBack(false, null, "Could not find user data")
                    Log.d("UserData", "Could not find user data")
                    return@addOnSuccessListener
                }

                val data =
                    document.data!! // Use !! only if you're sure the document exists (checked earlier)
                val firstName = data["firstName"].toString()
                val lastName = data["lastName"].toString()
                val age = data["age"].toString().toInt()
                val town = data["town"].toString()
                val gender = data["gender"].toString()

                val details = Details(
                    firstName = firstName,
                    lastName = lastName,
                    age = age,
                    town = town,
                    gender = gender
                )

                Log.d("UserData", details.toString())
                callBack(true, details, null)
            }
            .addOnFailureListener {
                callBack(false, null, it.message)
            }
    }

    fun deleteData(callBack: (success: Boolean, message: String?) -> Unit) {
        documentRef.delete()
            .addOnSuccessListener {
                callBack(true, null)
            }.addOnFailureListener {
                callBack(false, it.message)
            }
    }
}