package com.myth.authy.domain

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    fun loginWithEmail(
        email: String,
        password: String,
        onSuccess: (success: Boolean, error: String?) -> Unit
    ) {
        if (email.isEmpty() || password.isEmpty()) {
            onSuccess(false, "Please fill all the fields")
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            onSuccess(false, "Please enter a valid email")
            return
        }
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess(true, null)
            } else {
                onSuccess(false, task.exception?.message)
            }
        }
    }

    fun logout(onComplete: (Boolean) -> Unit) {
        firebaseAuth.signOut()
        if (firebaseAuth.currentUser == null) {
            onComplete(true)
        } else {
            onComplete(false)
        }
    }

    fun registerWithEmailAndPassword(
        email: String,
        password: String,
        repeatPassword: String,
        onSuccess: (success: Boolean, error: String?) -> Unit
    ) {
        if (email.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()) {
            onSuccess(false, "Please fill all the fields")
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            onSuccess(false, "Please enter a valid email")
            return
        }
        if (password != repeatPassword) {
            onSuccess(false, "Passwords do not match")
            return
        }
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess(true, null)
                } else {
                    onSuccess(false, task.exception?.message)
                }
            }
    }

    fun requestEmailPasswordReset(
        email: String,
        onSuccess: (success: Boolean, error: String?) -> Unit
    ) {
        if (email.isEmpty()){
            onSuccess(false, "Fill in your email address")
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            onSuccess(false, "Please enter a valid email")
            return
        }
        firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener {
            onSuccess(true, null)
        }.addOnFailureListener {
            onSuccess(false, it.message)
        }
    }
}