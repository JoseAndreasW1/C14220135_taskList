package com.example.c14220135_tasklist

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class createTask : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_task)

        val _btnSave = findViewById<Button>(R.id.btnSave)
        val _etNama = findViewById<EditText>(R.id.etNamaTask)
        val _etTanggal = findViewById<EditText>(R.id.etTanggal)
        val _etDeskripsi = findViewById<EditText>(R.id.etDeskripsi)

        val task2 = intent.getParcelableExtra<task>("TASKEDIT")
        val position = intent.getStringExtra("POSITION")  // Retrieve position here

        if (task2 != null && position != null) {
            _etNama.setText(task2.nama)
            _etTanggal.setText(task2.tanggal)
            _etDeskripsi.setText(task2.deskripsi)

            _btnSave.setOnClickListener {
                MainActivity.updatedTask = task(
                    _etNama.text.toString(),
                    _etTanggal.text.toString(),
                    _etDeskripsi.text.toString(),
                    task2.status
                )
                MainActivity.positionUpdate = position.toInt()
                finish()
            }
        } else {
            _btnSave.setOnClickListener {
                MainActivity.newTask = task(
                    _etNama.text.toString(),
                    _etTanggal.text.toString(),
                    _etDeskripsi.text.toString(),
                    "Not Started"
                )
                finish()
            }
        }
    }
}
