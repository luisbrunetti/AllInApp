package com.tawa.allinapp.models

data class User(
    val roles: List<String>,
    val companies: List<Company>,
    val latitude: String?,
    val longitude: String?,
    val feModify: String?,
    val idUserModify: String?,
    val status: String,
    val connection: String,
    val ulLatitude: Double,
    val ulLongitude: Double,
    val battery: Double,
    val id: String,
    val user: String,
    val fullName: String,
    val password: String,
    val email: String,
    val feCreated: String,
)