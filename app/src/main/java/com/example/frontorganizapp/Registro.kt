package com.example.frontorganizapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import android.util.Patterns
import android.widget.Toast

class Registro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)

        val btnregistrado: MaterialButton = findViewById(R.id.btn_register)
        val btniniciaS: MaterialButton = findViewById(R.id.btn_goto_login)

        val nombre: TextInputEditText = findViewById(R.id.et_nombre)
        val apellido: TextInputEditText = findViewById(R.id.et_apellido)
        val email: TextInputEditText = findViewById(R.id.et_email_reg)
        val pass: TextInputEditText = findViewById(R.id.et_password_reg)
        val confirmpass: TextInputEditText = findViewById(R.id.et_confirm_password)

        val tilNombre: TextInputLayout = findViewById(R.id.til_nombre)
        val tilApellido: TextInputLayout = findViewById(R.id.til_apellido)
        val tilEmail: TextInputLayout = findViewById(R.id.til_email_reg)
        val tilPassword: TextInputLayout = findViewById(R.id.til_password_reg)
        val tilConfirmPassword: TextInputLayout = findViewById(R.id.til_confirm_password)

        btnregistrado.setOnClickListener {
            tilNombre.error = null
            tilApellido.error = null
            tilEmail.error = null
            tilPassword.error = null
            tilConfirmPassword.error = null

            val nombreStr = nombre.text.toString().trim()
            val apellidoStr = apellido.text.toString().trim()
            val emailStr = email.text.toString().trim()
            val passStr = pass.text.toString().trim()
            val confirmPassStr = confirmpass.text.toString().trim()

            var esValido = true

            if (nombreStr.isEmpty()) {
                tilNombre.error = "Este campo es requerido"
                esValido = false
            }

            if (apellidoStr.isEmpty()) {
                tilApellido.error = "Este campo es requerido"
                esValido = false
            }

            if (emailStr.isEmpty()) {
                tilEmail.error = "Este campo es requerido"
                esValido = false
            } else if (!Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()) {
                tilEmail.error = "El formato del correo no es válido"
                esValido = false
            }

            if (passStr.isEmpty()) {
                tilPassword.error = "Este campo es requerido"
                esValido = false
            } else if (passStr.length < 6) {
                tilPassword.error = "Debe tener al menos 6 caracteres"
                esValido = false
            }

            if (confirmPassStr.isEmpty()) {
                tilConfirmPassword.error = "Confirma tu contraseña"
                esValido = false
            } else if (passStr != confirmPassStr) {
                tilConfirmPassword.error = "Las contraseñas no coinciden"
                esValido = false
            }

            if (esValido) {
                val sharedpreference = getSharedPreferences ("user_data",MODE_PRIVATE)
                val editor = sharedpreference.edit()

                editor.putString("Email",emailStr)
                editor.putString("Contraseña",passStr)

                val guardadoExitoso=editor.commit()

                Toast.makeText(this, "¡Usuario registrado!", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, Login::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Por favor, corrige los errores", Toast.LENGTH_SHORT).show()
            }
        }

        btniniciaS.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}