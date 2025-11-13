package com.example.frontorganizapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class Login : AppCompatActivity() {

    private lateinit var mGoogleSingInClient: GoogleSignInClient
    private val singInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                val idToken = account.idToken
                Log.d("GoogleSignIn", "¡Éxito! El ID Token es: $idToken")
                // TODO: Enviar este 'idToken' a tu backend (servidor)

                val nombreUsuario=account.displayName
                val correoUsuario=account.email
                val urlFoto=account.photoUrl.toString()
                val intent = Intent(this, inicio::class.java)

                intent.putExtra("USER_NAME",nombreUsuario)
                intent.putExtra("USER_EMAIL",correoUsuario)
                intent.putExtra("USER_PHOTO",urlFoto)

                Toast.makeText(this, "¡Bienvenido $nombreUsuario!", Toast.LENGTH_SHORT).show()
                startActivity(intent)
                finish()

            } catch (e: ApiException) {
                Log.w("GoogleSignIn", "signInResult:failed code=" + e.statusCode)
            }
        } else {
            Log.w("GoogleSignIn", "El usuario canceló o hubo un error. Code: " + result.resultCode)
        }
    }


    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.loginfront)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("27427988713-cmevol0g9fe2tdcidi2cc85alis2da5p.apps.googleusercontent.com")
            .requestEmail()
            .build()

        mGoogleSingInClient = GoogleSignIn.getClient(this, gso)

        val btnsingup: MaterialButton = findViewById(R.id.btn_signup)
        val btninicia: MaterialButton = findViewById(R.id.btn_login)
        val btnpass: MaterialButton = findViewById(R.id.btn_forgot_password)
        val etEmailLogin: TextInputEditText = findViewById(R.id.et_email)
        val etPasswordLogin: TextInputEditText = findViewById(R.id.et_password)
        val btnGoogleLogin: MaterialButton = findViewById(R.id.btn_google_login)

        btnGoogleLogin.setOnClickListener {
            iniciarGoogleSignIn()
        }
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

            if (emailIngresado.isEmpty() || passwordIngresado.isEmpty()) {
                Toast.makeText(this, "Por favor ingrese correo y contraseña", Toast.LENGTH_SHORT).show()

            } else if (emailIngresado == emailGuardado && passwordIngresado == passwordGuardado) {
                Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, inicio::class.java)
                startActivity(intent)
                finish()

            } else {
                Toast.makeText(this, "Correo y/o contraseña incorrectos", Toast.LENGTH_SHORT).show()
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

    private fun iniciarGoogleSignIn() {
        mGoogleSingInClient.signOut().addOnCompleteListener {
            val signInIntent = mGoogleSingInClient.signInIntent
            singInLauncher.launch(signInIntent)
        }

    }
}