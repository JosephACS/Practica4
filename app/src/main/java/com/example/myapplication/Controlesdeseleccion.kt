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
import com.example.myapplication.services.SupabaseErrorHandler
import com.example.myapplication.services.SupabaseManager
import io.github.jan.supabase.exceptions.RestException
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
        actvMaterias.setText("")
        val lstMaterias = ArrayList<String>()
        lifecycleScope.launch {
            try {
                val listaMaterias = ArrayList(
                    SupabaseManager.client
                        .from("materias")
                        .select {
                            filter {
                                eq("nivel", 6)
                            }
                            order("nombre", Order.ASCENDING)
                        }
                        .decodeList<Materia>()
                )
                for (materia in listaMaterias) {
                    lstMaterias.add(materia.nombre ?: "")
                }
                val lstalumno = ArrayList<String>()
                lifecycleScope.launch {
                    try {
                        val listaAlumnos = ArrayList(
                            SupabaseManager.client
                                .from("alumnos")
                                .select {

                                    order("nombre", Order.ASCENDING)
                                }
                                .decodeList<Alumno>()
                        )
                        for (alumno in listaAlumnos) {
                            lstMaterias.add(alumno.nombres ?: "")
                        }
            } catch (e: RestException) {
                SupabaseErrorHandler.show(this@Controlesdeseleccion, e)
                lstMaterias.clear()
            } finally {
                val adapter = ArrayAdapter(
                    this@Controlesdeseleccion,
                    android.R.layout.simple_spinner_dropdown_item,
                    lstMaterias
                )
                actvMaterias.setAdapter(adapter)
                val listV = findViewById<ListView>(R.id.listview)
                val adapter2 = ArrayAdapter(
                    this@Controlesdeseleccion,
                    android.R.layout.simple_spinner_dropdown_item,
                    lstMaterias
                )
                listV.setAdapter(adapter)
            }
        }
    }
}

