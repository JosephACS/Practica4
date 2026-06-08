package com.example.myapplication

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.Models.Alumno
import com.example.myapplication.Models.Materia
import com.example.myapplication.services.SupabaseManager
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.launch

class Controlesdeseleccion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_controlesdeseleccion)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val actvMaterias = findViewById<AutoCompleteTextView>(R.id.actvlistamaterias)
        val listV = findViewById<ListView>(R.id.listview)

        lifecycleScope.launch {
            try {
                val listaMaterias = SupabaseManager.client
                    .from("materias")
                    .select {
                        filter {
                            eq("nivel", 6)
                        }
                        order("nombre", Order.ASCENDING)
                    }
                    .decodeList<Materia>()

                val lstNombresMaterias = listaMaterias.map { it.nombre ?: "" }
                val adapterMaterias = ArrayAdapter(
                    this@Controlesdeseleccion,
                    android.R.layout.simple_spinner_dropdown_item,
                    lstNombresMaterias
                )
                actvMaterias.setAdapter(adapterMaterias)

                val listaAlumnos = SupabaseManager.client
                    .from("alumnos")
                    .select {
                        order("nombres", Order.ASCENDING)
                    }
                    .decodeList<Alumno>()

                val adapterAlumnos = AlumnoAdapter(
                    this@Controlesdeseleccion,
                    R.layout.item_alumno,
                    listaAlumnos
                )
                listV.adapter = adapterAlumnos

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
