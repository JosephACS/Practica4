package com.example.myapplication.Models

import kotlinx.serialization.Serializable

@Serializable
class Materia (
    val id: Long,
    val nombre: String?= null,
    val nivel: Int?=null
)
