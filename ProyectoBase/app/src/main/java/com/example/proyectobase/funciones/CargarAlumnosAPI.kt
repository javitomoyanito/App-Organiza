// com/example/proyectobase/funciones/CargarAlumnosAPI.kt
package com.example.proyectobase.funciones

import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.proyectobase.API.AlumnosRepository
import kotlinx.coroutines.launch

object CargarAlumnosAPI {

    /** Carga alumnos del API y los pinta en el ListView indicado. */
    fun cargarAlumnos(owner: LifecycleOwner, listView: ListView) {
        owner.lifecycleScope.launch {
            val res = AlumnosRepository.fetchAlumnos()

            res.onSuccess { alumnos ->
                if (alumnos.isEmpty()) {
                    Toast.makeText(
                        listView.context,
                        "Sin datos desde el servicio",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                val datos = alumnos.map { a ->
                    "NOMBRE  : ${a.nombre} ${a.apellido}\n" +
                            "GRUPO   : ${a.grupo}\n" +
                            "SECCIÓN : ${a.seccion}"
                }

                listView.adapter = ArrayAdapter(
                    listView.context,
                    android.R.layout.simple_list_item_1,
                    datos
                )

                android.util.Log.d("WS_ALUMNOS", "items=${alumnos.size}")
            }.onFailure { e ->
                android.util.Log.e("WS_ALUMNOS_ERR", "falló", e)
                Toast.makeText(
                    listView.context,
                    "Error al cargar: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
