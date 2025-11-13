package com.example.frontorganizapp

// importaciones de chat
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.chat.ContentPart
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.aallam.openai.api.http.Timeout
// otras importaciones
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import android.util.Base64
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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

    // --- NUEVA VARIABLE PARA ChatsitoGpt ---
    private lateinit var openAI: OpenAI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        // --- INICIALIZAR CLIENTE DE OPENAI ---
        openAI = OpenAI(
            token = BuildConfig.OPENAI_API_KEY,
            timeout = Timeout(socket = kotlin.time.Duration.parse("60s"))
        )


        // Vistas de la cámara
        ivPhoto = findViewById(R.id.iv_photo)
        btnOpenCamera = findViewById(R.id.btn_open_camera)

        // --- NUEVAS VISTAS (del activity_main.xml) ---
        tvRespuesta = findViewById(R.id.tv_respuesta)
        progressBar = findViewById(R.id.progressBar)

        btnOpenCamera.setOnClickListener {
            checkCameraPermission()
        }
    }


    // --- MODIFICAMOS ESTA FUNCIÓN ---
    // ¿Por qué? Para llamar a la IA después de que la foto se toma.
    private val takePictureResult: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // 1. Mostrar la foto
                ivPhoto.setImageURI(photoUri)

                // 2. ¡NUEVO! Llamar a la IA para analizar la foto
                analizarImagenConChatsito(photoFile)

            } else {
                Toast.makeText(this, "Toma de foto cancelada", Toast.LENGTH_SHORT).show()
            }
        }

    // --- NUEVA FUNCIÓN ---
    private fun analizarImagenConChatsito(foto: File) {

        lifecycleScope.launch(Dispatchers.IO) {

            try {
                withContext(Dispatchers.Main) {
                    tvRespuesta.text = "Analizando tu espacio con ChatGPT..."
                    progressBar.visibility = View.VISIBLE
                    tvRespuesta.visibility = View.GONE
                }

                // convertir la imagen a base64
                val imageBytes = foto.readBytes()
                val base64Image = Base64.encodeToString(imageBytes, Base64.NO_WRAP)

                val promptTexto = """
                Eres un experto en organización de interiores.
                Analiza esta imagen de un espacio desordenado y dame una lista de 5
                pasos accionables para ordenarlo, basándote en los objetos que ves.
            """.trimIndent()


                val chatRequest = ChatCompletionRequest(
                    model = ModelId("gpt-4o"),
                    messages = listOf(
                        ChatMessage(
                            role = ChatRole.User,
                            content = listOf(
                                ContentPart.Text(promptTexto),
                                ContentPart.Image(imageDataUrl)
                            )
                        )
                    ),
                    maxTokens = 500
                )

                // --- 6. Llamar a ChatsitoGPT ---
                val response = openAI.chatCompletions(chatRequest)

                // --- 7. Extraer respuesta ---
                val aiResponse = response.choices.firstOrNull()
                    ?.message?.content?.joinToString("") ?: "No se recibió una respuesta"

                withContext(Dispatchers.Main) {
                    tvRespuesta.text = aiResponse
                    progressBar.visibility = View.GONE
                    tvRespuesta.visibility = View.VISIBLE
                }

            } catch (e: Exception) {
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



