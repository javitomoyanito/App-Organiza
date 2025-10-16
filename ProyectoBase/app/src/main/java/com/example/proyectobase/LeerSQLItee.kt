package com.example.proyectobase

import android.os.Bundle
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proyectobase.funciones.LeerAlumnosLocalSQL

class LeerSQLItee : AppCompatActivity() {

    private lateinit var lvLeerData: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_leer_sqlitee)

        lvLeerData = findViewById(R.id.lv_data_sqlittee)
        // lvLeerData.emptyView = findViewById(R.id.empty_view_sqlittee) // opcional

        // 🔹 Carga inicial
        LeerAlumnosLocalSQL.cargarEnListView(this, this, lvLeerData)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val sb = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(sb.left, sb.top, sb.right, sb.bottom)
            insets
        }
    }

    override fun onResume() {
        super.onResume()
        // 🔹 Refresca cada vez que vuelve al frente
        LeerAlumnosLocalSQL.cargarEnListView(this, this, lvLeerData)
    }
}
