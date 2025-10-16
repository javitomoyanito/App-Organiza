package com.example.proyectobase.DB

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object AlumnosLocalRepository {

    suspend fun insert(context: Context, nombre: String, apellido: String, grupo: String, seccion: String): Result<Long> =
        withContext(Dispatchers.IO) {
            runCatching {
                AlumnoDbHelper(context).use { it.insert(nombre, apellido, grupo, seccion) }
            }
        }

    suspend fun getAll(context: Context): Result<List<AlumnoLocal>> =
        withContext(Dispatchers.IO) {
            runCatching {
                AlumnoDbHelper(context).use { it.getAll() }
            }
        }

    suspend fun clear(context: Context): Result<Unit> =
        withContext(Dispatchers.IO) {
            runCatching {
                AlumnoDbHelper(context).use { it.clear() }
            }
        }
}
