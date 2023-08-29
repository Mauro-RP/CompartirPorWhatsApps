package com.example.enviararchivo

import android.app.Activity
import android.widget.Button
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager

class MainActivity : AppCompatActivity() {
    private val PICK_FILE_REQUEST_CODE = 101

    // Crear un contrato para seleccionar un archivo
    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { selectedFileUri ->
            // Crea una intención para compartir el archivo a través de WhatsApp
            val whatsappIntent = Intent(Intent.ACTION_SEND)
            whatsappIntent.type = "text/plain"
            whatsappIntent.setPackage("com.whatsapp") // Especifica WhatsApp

            // Adjunta el archivo seleccionado
            whatsappIntent.putExtra(Intent.EXTRA_STREAM, selectedFileUri)

            // Inicia la actividad para compartir en WhatsApp
            startActivity(whatsappIntent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val selectFileButton = findViewById<Button>(R.id.selectFileButton)

        selectFileButton.setOnClickListener {
            // Solicita permisos para acceder a los archivos si es necesario
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                PICK_FILE_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PICK_FILE_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permiso concedido, llama al contrato para seleccionar un archivo
            getContent.launch("*/*")
        }
    }
}

