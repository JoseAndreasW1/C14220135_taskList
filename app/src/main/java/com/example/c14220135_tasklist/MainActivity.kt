package com.example.c14220135_tasklist

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var _rvTask: RecyclerView
    private var arTask = ArrayList<task>()

    private lateinit var _nama : MutableList<String>
    private lateinit var _tanggal : MutableList<String>
    private lateinit var _deskripsi : MutableList<String>
    private lateinit var _status : MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        _nama = resources.getStringArray(R.array.namaTask).toMutableList()
        _tanggal = resources.getStringArray(R.array.tanggalTask).toMutableList()
        _deskripsi = resources.getStringArray(R.array.deskripsiTask).toMutableList()
        _status = resources.getStringArray(R.array.statusTask).toMutableList()

        for (position in _nama.indices) {
            val task = task(
                nama = _nama[position],
                tanggal = _tanggal[position],
                deskripsi = _deskripsi[position],
                status = _status[position]
            )
            arTask.add(task)
        }

        _rvTask = findViewById(R.id.rvTask)

        _rvTask.layoutManager = LinearLayoutManager(this)
        _rvTask.adapter = adapterRevView(arTask)

        val _btnNewTask = findViewById<Button>(R.id.btnNewTask)
        _btnNewTask.setOnClickListener {
            val intent = Intent(this, createTask::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        // Handle new task creation
        newTask?.let { task ->
            arTask.add(task)
            _rvTask.adapter?.notifyItemInserted(arTask.size - 1)
            newTask = null // Clear the reference to avoid duplicate addition
        }

        updatedTask?.let{ task ->
            if(positionUpdate !== -1){
                arTask[positionUpdate] = updatedTask!!
                _rvTask.adapter?.notifyItemChanged(positionUpdate)
                updatedTask = null
                positionUpdate = -1
            }

        }

    }

    companion object {
        var newTask: task? = null
        var updatedTask: task? = null
        var positionUpdate = -1

    }
}
