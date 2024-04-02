package com.myth.authy.presentation.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.myth.authy.core.utils.Screen
import com.myth.authy.presentation.components.DataPointComponent
import com.myth.authy.presentation.screens.details.DetailsViewModel
import com.myth.authy.presentation.screens.signup.SignUpViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    navController: NavController,
    detailsViewModel: DetailsViewModel = hiltViewModel(),
    authViewModel: SignUpViewModel = hiltViewModel()
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val options = listOf("Male", "Female", "Other")
    val actions = listOf("Update", "Delete")
    var expanded by remember { mutableStateOf(false) }
    var actionsExpanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0]) }
    var selectedActionText by remember { mutableStateOf(actions[0]) }
    var userHasData by remember { mutableStateOf(false) }
    var deleting by remember { mutableStateOf(false) }

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var town by remember { mutableStateOf("") }


    LaunchedEffect(key1 = Unit) {
        detailsViewModel.getUserDetails { isSuccessful, details, message ->
            if (isSuccessful) {
                details.let { data ->
                    firstName = data!!.firstName
                    lastName = data.lastName
                    age = data.age.toString()
                    town = data.town
                    selectedOptionText = data.gender
                }
                userHasData = true
            } else {
                userHasData = false
            }
        }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(text = "Authy") },
                actions = {
                    IconButton(onClick = {
                        authViewModel.logout { isSuccessful ->
                            if (isSuccessful) {
                                scope.launch {
                                    snackBarHostState.showSnackbar("Logging you out")
                                }
                                navController.navigate(Screen.SignUp.route)
                            } else {
                                scope.launch {
                                    snackBarHostState.showSnackbar("Error logging out")
                                }
                            }
                        }
                    }) {
                        Icon(Icons.Default.Logout, contentDescription = "Logout")
                    }
                },
                colors = topAppBarColors(
                )
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (userHasData) {
                item {
                    DataPointComponent(
                        "First Name",
                        { firstName = it },
                        firstName,
                    )
                }
                item {
                    DataPointComponent(
                        key = "LastName",
                        onValueChange = { lastName = it },
                        value = lastName
                    )
                }
                item {
                    DataPointComponent(
                        key = "Age",
                        onValueChange = { age = it },
                        value = age,
                        number = true
                    )
                }
                item {
                    DataPointComponent(
                        key = "Town",
                        onValueChange = { town = it },
                        value = town
                    )
                }
                item {
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        TextField(
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            readOnly = true,
                            value = selectedOptionText,
                            onValueChange = { selectedOptionText = it },
                            label = { Text("Gender") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                        ) {
                            options.forEach { selectionOption ->
                                DropdownMenuItem(
                                    text = { Text(selectionOption) },
                                    onClick = {
                                        selectedOptionText = selectionOption
                                        expanded = false
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                )
                            }
                        }
                    }
                }
                item {
                    ExposedDropdownMenuBox(
                        expanded = actionsExpanded,
                        onExpandedChange = { actionsExpanded = !actionsExpanded }
                    ) {
                        TextField(
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            readOnly = true,
                            value = selectedActionText,
                            onValueChange = { selectedActionText = it },
                            label = { Text("Action") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = actionsExpanded) }
                        )
                        ExposedDropdownMenu(
                            expanded = actionsExpanded,
                            onDismissRequest = { actionsExpanded = false },
                        ) {
                            actions.forEach { selectionOption ->
                                DropdownMenuItem(
                                    text = { Text(selectionOption) },
                                    onClick = {
                                        deleting = selectionOption == "Delete"
                                        selectedActionText = selectionOption
                                        actionsExpanded = false
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                )
                            }
                        }
                    }
                }
                item {
                    Button(
                        shape = RoundedCornerShape(8.dp),
                        onClick = {
                            if (deleting) {
                                scope.launch {
                                    snackBarHostState.showSnackbar("Deleting Data")
                                    detailsViewModel.deleteDetails { isSuccessful, message ->
                                        if (isSuccessful) {
                                            scope.launch {
                                                snackBarHostState.showSnackbar("Data deleted")
                                            }
                                            userHasData = false
                                        } else {
                                            scope.launch {
                                                snackBarHostState.showSnackbar("$message")
                                            }
                                        }
                                    }
                                }
                            } else {
                                scope.launch {
                                    snackBarHostState.showSnackbar("Updating Data")
                                    detailsViewModel.saveUserDetails(
                                        firstName = firstName,
                                        lastName = lastName,
                                        age = age,
                                        town = town,
                                        gender = selectedOptionText
                                    ) { isSuccessful, message ->
                                        if (isSuccessful) {
                                            scope.launch {
                                                snackBarHostState.showSnackbar("Data Updated")
                                            }
                                        } else {
                                            scope.launch {
                                                snackBarHostState.showSnackbar("$message")
                                            }
                                        }
                                    }
                                }
                            }
                        }) {
                        Text("Perform Action")
                    }
                }
            } else {
                item {
                    Text(text = "No details to show")
                    Spacer(Modifier.height(8.dp))
                    Button(
                        shape = RoundedCornerShape(8.dp),
                        onClick = { navController.navigate(Screen.Detail.route) }
                    ) {
                        Text("Add User Details")
                    }
                }
                
            }
        }
    }
}