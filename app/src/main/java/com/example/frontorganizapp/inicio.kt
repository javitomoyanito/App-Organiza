package com.example.frontorganizapp

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide

class inicio : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val ivFoto: ImageView = findViewById(R.id.iv_foto_perfil)
        val tvNombre: TextView = findViewById(R.id.tv_nombre_usuario)

        val nombreUsuario = intent.getStringExtra("USER_NAME")
        val correoUsuario = intent.getStringExtra("USER_EMAIL")
        val urlFoto = intent.getStringExtra("USER_PHOTO")


        tvNombre.text = nombreUsuario

        Glide.with(this)
            .load(urlFoto)
            .circleCrop()
            .into(ivFoto)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}