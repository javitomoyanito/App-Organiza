package com.example.proyectobase.ApiRegionComuna

import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch


object CargarRegionComunaAPI {

    /** Carga region comuna del API y los pinta en el spinner indicado. */
    fun cargarRegionComuna(owner: LifecycleOwner, spinner: Spinner) {
        owner.lifecycleScope.launch {
            val res = RegionComunaRepository.fetchRegionComuna()

            res.onSuccess { regionComuna ->
                if (regionComuna.isEmpty()) {
                    Toast.makeText(
                        spinner.context,
                        "Sin datos desde el servicio",
                        Toast.LENGTH_SHORT
                    ).show()
                }


                val datos = regionComuna.map { a ->
                            //"NOMBRE  : ${a.id}\n" +
                            "${a.region}\n"
                            // "SECCIÓN : ${a.region}"
                }


                spinner.adapter = ArrayAdapter(
                    spinner.context,
                    android.R.layout.simple_list_item_1,
                    datos.distinct()
                )

                android.util.Log.d("ws_region_comuna", "items=${regionComuna.size}")

            }.onFailure { e ->
                android.util.Log.e("ws_region_comuna", "falló", e)
                Toast.makeText(
                    spinner.context,
                    "Error al cargar: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    /** Carga region comuna del API y los pinta en el spinner indicado. */
    fun cargarRegionComuna2(owner: LifecycleOwner, spinner: Spinner) {
        owner.lifecycleScope.launch {
            val res = RegionComunaRepository.fetchRegionComuna()

            res.onSuccess { regionComuna ->
                if (regionComuna.isEmpty()) {
                    Toast.makeText(
                        spinner.context,
                        "Sin datos desde el servicio",
                        Toast.LENGTH_SHORT
                    ).show()
                }


                val datos = regionComuna.map { a ->
                    //"NOMBRE  : ${a.id}\n" +
                    "${a.comuna}\n"
                    // "SECCIÓN : ${a.region}"
                }


                spinner.adapter = ArrayAdapter(
                    spinner.context,
                    android.R.layout.simple_list_item_1,
                    datos
                )

                android.util.Log.d("ws_region_comuna", "items=${regionComuna.size}")

            }.onFailure { e ->
                android.util.Log.e("ws_region_comuna", "falló", e)
                Toast.makeText(
                    spinner.context,
                    "Error al cargar: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}