package com.example.c14220135_tasklist

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

lateinit var sp :SharedPreferences
class MainActivity : AppCompatActivity() {

    private lateinit var _rvTask: RecyclerView
    private val listTask = ArrayList<task>() // For local tasks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        // JSON
        val sp = getSharedPreferences("taskSP", MODE_PRIVATE)
        val gson = Gson()
        val isiSP = sp.getString("taskList", null)
        val type = object : TypeToken<ArrayList<task>>() {}.type
        if (isiSP != null) {
            listTask.addAll(gson.fromJson(isiSP, type))
        }

        // Setup RecyclerView
        _rvTask = findViewById(R.id.rvTask)
        _rvTask.layoutManager = LinearLayoutManager(this)
        _rvTask.adapter = adapterRevView(listTask)

        // New Task Button
        val _btnNewTask = findViewById<Button>(R.id.btnNewTask)
        _btnNewTask.setOnClickListener {
            val intent = Intent(this, createTask::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        // Add new task from local creation
        newTask?.let { task ->
            listTask.add(task)
            newTask = null // Clear reference
        }

        // Update task if modified
        updatedTask?.let { task ->
            if (positionUpdate != -1) {
                listTask[positionUpdate] = task
                positionUpdate = -1
                updatedTask = null
            }
        }
        //updateJson
        saveTaskToJson()

        _rvTask.adapter?.notifyDataSetChanged()
    }
    private fun saveTaskToJson() {
        val filteredTasks = listTask.filter { it.statusJson }
        val sharedPreferences = getSharedPreferences("taskSP", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(filteredTasks)
        editor.putString("taskList", json)
        editor.apply()
    }

    companion object {
        var newTask: task? = null
        var updatedTask: task? = null
        var positionUpdate = -1
    }
}
