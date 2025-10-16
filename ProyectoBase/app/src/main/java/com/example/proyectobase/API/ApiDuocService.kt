package com.example.proyectobase.API

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body

data class AlumnoInsertRequest(
    val nombre: String,
    val apellido: String,
    val grupo: String,
    val seccion: String,
    val archivo: String
)

data class InsertResponse(
    val success: Boolean,
    val message: String?
)

interface ApiDuocService {
    @GET("apiduoc/consulta.php")
    suspend fun getAlumnos(): List<Alumno>

    @POST("apiduoc/insert.php")
    suspend fun insertAlumno(@Body request: AlumnoInsertRequest): InsertResponse
}
