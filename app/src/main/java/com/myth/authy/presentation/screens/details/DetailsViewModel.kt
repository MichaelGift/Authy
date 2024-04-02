package com.myth.authy.presentation.screens.details

import androidx.lifecycle.ViewModel
import com.myth.authy.data.Details
import com.myth.authy.domain.DatabaseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val database: DatabaseUseCase
) : ViewModel() {
    fun saveUserDetails(
        firstName: String,
        lastName: String,
        age: String,
        town: String,
        gender: String,
        successListener: (Boolean, String?) -> Unit
    ) {
        val details = Details(firstName, lastName, age.toInt(), town, gender)
        database.saveData(details, successListener)
    }

    fun getUserDetails(successListener: (Boolean, Details? ,String?) -> Unit){
         database.retrieveData(successListener)
    }

    fun deleteDetails(successListener: (Boolean,String?) -> Unit){
        database.deleteData(successListener)
    }
}