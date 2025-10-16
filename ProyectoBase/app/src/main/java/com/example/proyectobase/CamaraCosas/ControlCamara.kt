package com.example.proyectobase.CamaraCosas

object ControlCamara {
    private lateinit var imageCapture: ImageCapture
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService

    private fun takePhoto(){
        val imageCapture = this.imageCapture ?: return

        val photofile = file(
            outputDirectory
             
        )
    }
}