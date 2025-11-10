package com.example.cameraapp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

class MainActivity : AppCompatActivity() {

// Vistas de la cámara
private lateinit var ivPhoto: ImageView
private lateinit var btnOpenCamera: Button
private lateinit var photoUri: Uri
private lateinit var photoFile: File
private val CAMERA_PERMISSION_CODE = 100
private val STORAGE_PERMISSION_CODE = 101

// --- NUEVAS VISTAS PARA LA IA ---
private lateinit var tvRespuesta: TextView
private lateinit var progressBar: ProgressBar

// --- NUEVA VARIABLE PARA GEMINI ---
private lateinit var generativeModel: GenerativeModel

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    // Vistas de la cámara
    ivPhoto = findViewById(R.id.iv_photo)
    btnOpenCamera = findViewById(R.id.btn_open_camera)

    // --- NUEVAS VISTAS (del activity_main.xml) ---
    tvRespuesta = findViewById(R.id.tv_respuesta)
    progressBar = findViewById(R.id.progressBar)

    // --- INICIALIZA GEMINI ---
    // ¿Por qué? Preparamos la conexión a la IA en cuanto
    // la app se abre.
    setupGenerativeModel()

    btnOpenCamera.setOnClickListener {
        checkCameraPermission()
    }
}

// --- NUEVA FUNCIÓN ---
private fun setupGenerativeModel() {
    // ¿Por qué? Leemos la API key que definimos en build.gradle
    // (BuildConfig se genera automáticamente)
    val apiKey = BuildConfig.GEMINI_API_KEY

    // ¿Por qué? Configuramos la IA.
    // "gemini-1.5-flash" es el modelo más rápido, ideal para móviles.
    // "temperature=0.7f" controla la creatividad (0.7 es un buen balance).
    val config = generationConfig {
        temperature = 0.7f
    }

    generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = apiKey,
        generationConfig = config
    )
}

// --- MODIFICAMOS ESTA FUNCIÓN ---
// ¿Por qué? Para llamar a la IA después de que la foto se toma.
private val takePictureResult: ActivityResultLauncher<Intent> =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // 1. Mostrar la foto
            ivPhoto.setImageURI(photoUri)

            // 2. ¡NUEVO! Llamar a la IA para analizar la foto
            analizarImagenConGemini(photoFile)

        } else {
            Toast.makeText(this, "Toma de foto cancelada", Toast.LENGTH_SHORT).show()
        }
    }

// --- NUEVA FUNCIÓN ---
private fun analizarImagenConGemini(foto: File) {

    // ¿Por qué "lifecycleScope.launch"?
    // Inicia una "Corrutina". Es la forma moderna de Android
    // para hacer tareas de fondo (como llamadas a Internet)
    // SIN congelar la app (evita el error NetworkOnMainThreadException).
    lifecycleScope.launch(Dispatchers.IO) { // .IO es el hilo optimizado para red/disco

        try {
            // Preparamos la foto para la IA (Bitmap)
            val bitmap = BitmapFactory.decodeFile(foto.absolutePath)

            // Preparamos el "prompt" (la instrucción)
            val promptTexto = "Eres un experto en organización de interiores. Analiza esta imagen de un espacio desordenado y dame una lista de 5 pasos accionables para ordenarlo, basándote en los objetos que ves."

            // Creamos el contenido (texto + imagen)
            val inputContent = content {
                image(bitmap)
                text(promptTexto)
            }

            // --- Tareas de UI ---
            // ¿Por qué "withContext(Dispatchers.Main)"?
            // Para mostrar/ocultar vistas (ProgressBar, TextView),
            // DEBEMOS volver al Hilo Principal (Main).
            withContext(Dispatchers.Main) {
                tvRespuesta.text = "Analizando tu espacio..."
                progressBar.visibility = View.VISIBLE
                tvRespuesta.visibility = View.GONE // Ocultamos el texto viejo
            }

            // ¡Llamamos a la IA!
            // Esta función "suspende" la corrutina (no la app)
            // mientras espera la respuesta.
            val response = generativeModel.generateContent(inputContent)

            // --- Mostrar el resultado (de vuelta en el Hilo Principal) ---
            withContext(Dispatchers.Main) {
                tvRespuesta.text = response.text // Mostramos la respuesta de Gemini
                progressBar.visibility = View.GONE
                tvRespuesta.visibility = View.VISIBLE // Mostramos el texto nuevo
            }

        } catch (e: Exception) {
            // Manejo de errores (si la API falla, no hay internet, etc.)
            withContext(Dispatchers.Main) {
                tvRespuesta.text = "Error: ${e.message}"
                progressBar.visibility = View.GONE
                tvRespuesta.visibility = View.VISIBLE
            }
        }
    }
}

// --- TU CÓDIGO DE CÁMARA (Sin cambios) ---

private fun checkCameraPermission() {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        != PackageManager.PERMISSION_GRANTED) {
        // Permiso no aceptado, solicitarlo
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
    } else {
        // Permiso ya aceptado
        checkStoragePermission()
    }
}

private fun checkStoragePermission() {
    // El permiso de almacenamiento solo es necesario para Android 9 (API 28) o inferior
    // para FileProvider, aunque nuestra lógica de guardado (externalCacheDir)
    // no siempre lo requiere, es buena práctica tenerlo para compatibilidad.
    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            // Solicitar permiso de almacenamiento
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
        } else {
            // Permiso ya aceptado
            openCamera()
        }
    } else {
        // Para Android 10 (Q) o superior, no se necesita permiso de almacenamiento
        // para guardar en el caché externo de la app.
        openCamera()
    }
}

private fun openCamera() {
    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    try {
        // Crear archivo temporal en cache externo
        photoFile = File.createTempFile("photo_", ".jpg", externalCacheDir)

        // ¿Por qué? Obtenemos la URI segura usando el FileProvider que
        // ya configuramos.
        photoUri = FileProvider.getUriForFile(
            applicationContext, // Usamos applicationContext (más seguro)
            "${applicationContext.packageName}.fileprovider", // Debe coincidir con el Manifest
            photoFile
        )

        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        takePictureResult.launch(intent)

    } catch (e: IOException) {
        e.printStackTrace()
        Toast.makeText(this, "Error al crear el archivo de la foto", Toast.LENGTH_SHORT).show()
    }
}

override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    if (requestCode == CAMERA_PERMISSION_CODE) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permiso de cámara aceptado, chequear almacenamiento
            checkStoragePermission()
        } else {
            Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }
    } else if (requestCode == STORAGE_PERMISSION_CODE) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permiso de almacenamiento aceptado
            openCamera()
        } else {
            Toast.makeText(this, "Permiso de almacenamiento denegado", Toast.LENGTH_SHORT).show()
        }
    }
}
}