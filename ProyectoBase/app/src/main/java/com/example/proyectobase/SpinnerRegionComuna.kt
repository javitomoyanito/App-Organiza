package com.example.proyectobase

import android.os.Bundle
import android.widget.Button
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proyectobase.ApiRegionComuna.CargarRegionComunaAPI

class SpinnerRegionComuna : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_spinner_region_comuna)

        val spRegion: Spinner = findViewById(R.id.sp_region)
        val spComuna: Spinner = findViewById(R.id.sp_comuna)
        val btnHacerAlgo: Button = findViewById(R.id.btn_hacer_algo)

       // CargarAlumnosAPI.cargarAlumnos(this, listaAlumnos)

        CargarRegionComunaAPI.cargarRegionComuna(this, spRegion)
        CargarRegionComunaAPI.cargarRegionComuna2(this, spComuna)






        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}