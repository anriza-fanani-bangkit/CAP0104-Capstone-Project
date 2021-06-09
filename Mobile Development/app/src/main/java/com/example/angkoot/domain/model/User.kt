package com.example.angkoot.domain.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    val phoneNumber: String = "",
    val username: String = "",
    val password: String = ""
)
