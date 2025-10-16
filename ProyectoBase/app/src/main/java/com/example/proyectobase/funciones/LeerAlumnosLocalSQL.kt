package com.example.proyectobase.funciones

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.proyectobase.DB.AlumnosLocalRepository   // ⬅️ Ajusta a .DB si tu paquete es en mayúscula
import com.example.proyectobase.DB.AlumnoLocal            // ⬅️ Ajusta a .DB si tu paquete es en mayúscula
import kotlinx.coroutines.launch

object LeerAlumnosLocalSQL {

    /** Obtiene todos los alumnos locales (sin tocar UI). */
    suspend fun obtenerTodos(context: Context): Result<List<AlumnoLocal>> {
        return AlumnosLocalRepository.getAll(context)
    }

    /** Carga los alumnos locales y los pinta en un ListView. */
    fun cargarEnListView(
        owner: LifecycleOwner,
        context: Context,
        listView: ListView
    ) {
        owner.lifecycleScope.launch {
            val res = AlumnosLocalRepository.getAll(context)
            res.onSuccess { lista ->
                val datos = lista.map { a ->
                    "${a.nombre} ${a.apellido}\nGrupo: ${a.grupo}  Sección: ${a.seccion}"
                }
                listView.adapter = ArrayAdapter(
                    context,
                    android.R.layout.simple_list_item_1,
                    datos
                )
            }.onFailure { e ->
                Toast.makeText(context, "Error leyendo SQLite: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
