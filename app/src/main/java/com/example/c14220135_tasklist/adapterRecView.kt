package com.example.c14220135_tasklist

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.w3c.dom.Text
import java.lang.reflect.Array

class adapterRevView ( private val listTask: ArrayList<task>) : RecyclerView
.Adapter<adapterRevView.ListViewHolder>(){
    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var _nama = itemView.findViewById<TextView>(R.id.tvNama)
        var _tanggal = itemView.findViewById<TextView>(R.id.tvTanggal)
        var _deksripsi = itemView.findViewById<TextView>(R.id.tvDeskripsi)
        var _status = itemView.findViewById<TextView>(R.id.tvStatus)
        var _btnStart = itemView.findViewById<Button>(R.id.btnStart)
        var _btnEdit = itemView.findViewById<Button>(R.id.btnEdit)
        var _btnDelete = itemView.findViewById<Button>(R.id.btnDelete)
        var _btnCheckMark = itemView.findViewById<Button>(R.id.btnCheckmark)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recycler, parent, false)

        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listTask.size    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        var task = listTask[position]
        holder._nama.setText(task.nama)
        holder._tanggal.setText(task.tanggal)
        holder._deksripsi.setText(task.deskripsi)
        holder._status.setText(task.status)
        holder._btnCheckMark.setBackgroundTintList(
            ColorStateList.valueOf(if (task.statusJson) Color.parseColor("#4CAF50") else Color.parseColor("#D3D3D3"))
        )
        when (task.status) {
            "Not Started" -> {
                holder._btnStart.text = "Start"
                holder._btnStart.visibility = View.VISIBLE
                holder._btnEdit.visibility = View.VISIBLE

                holder._btnStart.setOnClickListener {
                    task.status = "In Progress"
                    notifyItemChanged(position)
                }
            }

            "In Progress" -> {
                holder._btnStart.text = "Finish"
                holder._btnStart.visibility = View.VISIBLE
                holder._btnEdit.visibility = View.GONE

                holder._btnStart.setOnClickListener {
                    task.status = "Completed"
                    notifyItemChanged(position)
                }
            }

            "Completed" -> {
                holder._btnStart.visibility = View.GONE
                holder._btnEdit.visibility = View.GONE
            }
        }

        holder._btnDelete.setOnClickListener{
            val context = holder.itemView.context
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Delete Task")
            builder.setMessage("Are you sure you want to delete "+ task.nama +"?")
            builder.setPositiveButton("Yes") { _, _ ->
                listTask.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, listTask.size)
                saveTaskToJson(context)
                Toast.makeText(context, "Task deleted", Toast.LENGTH_SHORT).show()
            }
            builder.setNegativeButton("No", null)
            builder.show()
        }

        holder._btnEdit.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, createTask::class.java)
            intent.putExtra("TASKEDIT", task)
            intent.putExtra("POSITION", position.toString())
            context.startActivity(intent)
        }


        holder._btnCheckMark.setOnClickListener {
            val context = holder.itemView.context
            task.statusJson = !task.statusJson
            saveTaskToJson(context)
            val message = if (task.statusJson) {
                "${task.nama} saved to JSON"
            } else {
                "${task.nama} unsaved from JSON"
            }
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            holder._btnCheckMark.setBackgroundTintList(
                ColorStateList.valueOf(if (task.statusJson) Color.parseColor("#4CAF50") else Color.parseColor("#D3D3D3"))
            )
        }
    }

    private fun saveTaskToJson(context: Context) {
        val filteredTasks = listTask.filter { it.statusJson }
        val sharedPreferences = context.getSharedPreferences("taskSP", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(filteredTasks)
        editor.putString("taskList", json)
        editor.apply()
    }

}