package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.myapplication.Models.Alumno

class AlumnoAdapter(context: Context, private val resource: Int, private val objects: List<Alumno>) :
    ArrayAdapter<Alumno>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(resource, parent, false)

        val alumno = objects[position]

        val imgAlumno = view.findViewById<ImageView>(R.id.imgAlumno)
        val txtNombre = view.findViewById<TextView>(R.id.txtNombre)
        val txtCorreo = view.findViewById<TextView>(R.id.txtCorreo)
        val txtTelefono = view.findViewById<TextView>(R.id.txtTelefono)

        txtNombre.text = alumno.nombres
        txtCorreo.text = alumno.correo
        txtTelefono.text = alumno.telefono

        Glide.with(context)
            .load(alumno.foto)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_menu_report_image)
            .circleCrop()
            .into(imgAlumno)

        return view
    }
}