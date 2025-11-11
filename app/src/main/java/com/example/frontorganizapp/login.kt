package com.example.frontorganizapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.loginfront)

        // Esto es correcto, asumiendo que el XML usa <com.google...MaterialButton>
        val btnsingup: MaterialButton = findViewById(R.id.btn_signup)
        val btninicia: MaterialButton = findViewById(R.id.btn_login)
        val btnpass: MaterialButton = findViewById(R.id.btn_forgot_password)

        val etEmailLogin: TextInputEditText = findViewById(R.id.et_email)
        val etPasswordLogin: TextInputEditText = findViewById(R.id.et_password)


        btnsingup.setOnClickListener {
            val intent = Intent(this, Registro::class.java)
            startActivity(intent)
        }

        btninicia.setOnClickListener {
            val emailIngresado = etEmailLogin.text.toString().trim()
            val passwordIngresado = etPasswordLogin.text.toString().trim()

            val sharedpreferences = getSharedPreferences("user_data", MODE_PRIVATE)

            val emailGuardado = sharedpreferences.getString("Email", null)
            val passwordGuardado = sharedpreferences.getString("Contraseña", null)
            
            if(emailIngresado.isEmpty() || passwordIngresado.isEmpty()){
                Toast.makeText(this,"Por favor ingrese correo y contraseña", Toast.LENGTH_SHORT).show()

            }else if(emailIngresado == emailGuardado && passwordIngresado == passwordGuardado){
                Toast.makeText(this,"Bienvenido", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()


            }else{
                Toast.makeText(this,"Correo y/o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }

        btnpass.setOnClickListener {
            val intent = Intent(this, olvideContra::class.java)
            startActivity(intent)
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}