package com.example.proyectobase

import android.os.Bundle
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proyectobase.funciones.CargarAlumnosAPI

class LeerWebService : AppCompatActivity() {

    private lateinit var listaAlumnos: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_leer_web_service)

        // Referencias UI
        listaAlumnos = findViewById(R.id.lista_alumnos)
        listaAlumnos.emptyView = findViewById(R.id.empty_view)

        // Cargar alumnos desde el web service (helper)
        CargarAlumnosAPI.cargarAlumnos(this, listaAlumnos)

        // Insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
