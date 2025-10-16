package com.example.proyectobase.ApiRegionComuna

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body

data class InsertResponse(
    val success: Boolean,
    val message: String?
)

interface ApiRegionComunaServicio {
    @GET("apiduoc/consulta_comuna.php")
    suspend fun getRegionComuna(): List<RegionComuna>
}