package com.myth.authy.presentation.screens.signup

import androidx.lifecycle.ViewModel
import com.myth.authy.domain.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
) : ViewModel() {
    fun registerWithEmailAndPassword(
        email: String,
        password: String,
        repeatPassword: String,
        onSuccess: (success: Boolean, error: String?) -> Unit
    ) {
        authUseCase.registerWithEmailAndPassword(email, password, repeatPassword, onSuccess)
    }

    fun loginWithEmail(
        email: String,
        password: String,
        onSuccess: (success: Boolean, error: String?) -> Unit
    ) {
        authUseCase.loginWithEmail(email, password, onSuccess)
    }

    fun logout(onComplete: (Boolean) -> Unit) {
        authUseCase.logout(onComplete)
    }

    fun resetAccountPassword(email: String, onSuccess: (success: Boolean, error: String?) -> Unit) {
        authUseCase.requestEmailPasswordReset(email, onSuccess)
    }
}