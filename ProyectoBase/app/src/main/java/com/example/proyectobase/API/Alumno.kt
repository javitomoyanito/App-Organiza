package com.example.proyectobase.API

/**
 * ****** Data class
 * clase construida para representar datos
 * generará funciones automaticamente para toString, equals, copy, component
 */
data class Alumno(
    val id: Int,
    val nombre: String,
    val apellido: String,
    val grupo: String,
    val seccion: String
)
