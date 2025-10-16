package com.example.proyectobase.ApiRegionComuna

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object RegionComunaRepository {

    suspend fun fetchRegionComuna(): Result<List<RegionComuna>> = withContext(Dispatchers.IO) {
        try {
            Result.success(ApiDuocRegionComuna.service.getRegionComuna())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}



