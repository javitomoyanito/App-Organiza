package com.example.proyectobase

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proyectobase.funciones.ValidarConexionWAN


class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)

        //funcion heredada para verificar conexion a internet
        if(ValidarConexionWAN.isOnline(this)){
            println("conectado")
            val toast = Toast.makeText(
                this
                , "CON CONEXION"
                , Toast.LENGTH_SHORT).show()
        }else{
            println("sin conexion")
            val toast = Toast.makeText(
                this
                , "SIN CONEXION"
                , Toast.LENGTH_SHORT).show()
        }

        //ACTIVITY DESTINO
        val msjeBienvenida:TextView = findViewById(R.id.tx_bienvenido)
        //creo variable asigno valor recibido desde otro activity
        val usuarioDesdeOtroActivity = intent.getStringExtra("sesion")
        //seteo un TextView reemplazando el texto por el contenido.
        msjeBienvenida.text = usuarioDesdeOtroActivity.toString()

        val recibeContrasena = intent.getStringExtra("par_contrasena")
        val btnCalculadora: Button = findViewById(R.id.btn_abrir_calculadora)
        val lsMenu: ListView = findViewById(R.id.ls_menu_principal)

        val arrMenuPrincipal = arrayOf(
            "CALCULADORA"
            , "LEER API"
            , "GUARDAR API"
            , "GUARDAR SQL LITE"
            , "LEER SQL LITE")

        val adaptador2 = ArrayAdapter(this
            , android.R.layout.simple_list_item_1
            , arrMenuPrincipal)

        lsMenu.adapter = adaptador2

        lsMenu.setOnItemClickListener { parent, view, position, id ->
            val lsMenuOpcionSeleccionada = parent.getItemAtPosition(position).toString()

            if(lsMenuOpcionSeleccionada.equals("CALCULADORA")){
                val abrirCalculadora = Intent(this, MainActivity4::class.java)
                startActivity(abrirCalculadora)
            }else if (lsMenuOpcionSeleccionada.equals("GUARDAR API")){

                Toast.makeText(this, "NINGUNA OPCION SELECCIONADA", Toast.LENGTH_SHORT).show()

            }else if (lsMenuOpcionSeleccionada.equals("GUARDAR SQL LITE")){
                Toast.makeText(this, "NINGUNA OPCION SELECCIONADA", Toast.LENGTH_SHORT).show()

            }else if (lsMenuOpcionSeleccionada.equals("LEER SQL LITE")) {
                val leerSQLS = Intent(this, LeerSQLItee::class.java)
                startActivity(leerSQLS)
            }
            else if (lsMenuOpcionSeleccionada.equals("LEER API")) {
                val leerapi = Intent(this, LeerWebService::class.java)
                startActivity(leerapi)
            }else{
                Toast.makeText(this, "NINGUNA OPCION SELECCIONADA", Toast.LENGTH_SHORT).show()
            }
        }
        btnCalculadora.setOnClickListener{
            val abrirCalculadora = Intent(this, LeerWebService::class.java)
            startActivity(abrirCalculadora)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}