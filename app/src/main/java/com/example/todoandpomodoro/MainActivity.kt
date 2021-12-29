package com.example.todoandpomodoro

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_new_task.view.*
import java.io.*

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    private val taskList = ArrayList<Task>()
    private lateinit var adapter: RecycleViewAdapter
    private var taskNumber = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        readData()
        taskNumber = taskList.size + 1

        toolbar.title = "TODO"
        setSupportActionBar(toolbar)

        adapter = RecycleViewAdapter(this,taskList)
        recyclerViewTodo.setHasFixedSize(true)
        recyclerViewTodo.layoutManager = LinearLayoutManager(this)
        recyclerViewTodo.adapter = adapter

        buttonNewTask.setOnClickListener {

            val design = layoutInflater.inflate(R.layout.add_new_task,null)
            val newTaskAlert = AlertDialog.Builder(this@MainActivity)
            newTaskAlert.setIcon(R.drawable.ic_baseline_edit_24)
            newTaskAlert.setView(design)
            newTaskAlert.setPositiveButton("Add") { _, _ ->
                val taskText = design.editTextNewTaskTaskName.text
                if (taskText.toString() == "") {
                    Toast.makeText(this@MainActivity,"Please write a task",Toast.LENGTH_SHORT).show()
                }
                else {
                    taskList.add(Task(taskNumber,taskText.toString()))
                    taskNumber++
                    recyclerViewTodo.adapter = adapter
                }

            }
            newTaskAlert.setNegativeButton("Cancel") { _, _ ->
                Toast.makeText(this@MainActivity,"Cancel",Toast.LENGTH_SHORT).show()
            }

            newTaskAlert.create().show()
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu,menu)
        val item = menu!!.findItem(R.id.toolbarMenuSearch)
        val searchView = item.actionView as SearchView
        searchView.setOnQueryTextListener(this)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.toolbarMenuAbout -> {
                Toast.makeText(applicationContext,"This is a TODO application",Toast.LENGTH_SHORT).show()
            }
            R.id.toolbarMenuSettings -> {
                val settingsScreen = Intent(this@MainActivity,SettingsActivity::class.java)
                startActivity(settingsScreen)
            }
            R.id.toolbarMenuExit -> {
                saveData()
                finish()
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }
    //search when click search button
    override fun onQueryTextSubmit(query: String?): Boolean {
        Log.e("onQueryTextSubmit",query.toString())
        return true
    }

    //search in every text change
    override fun onQueryTextChange(newText: String?): Boolean {
        Log.e("onQueryTextChange",newText.toString())
        return true
    }

    fun saveData(){
        val sp = getSharedPreferences("tasks", Context.MODE_PRIVATE)
        val editor = sp.edit()
        val taskHasHSet = HashSet<String>()
        taskList.forEach { item ->
            taskHasHSet.add("${item.taskNumber};${item.task}")
        }
        editor.putStringSet("tasks",taskHasHSet)

        editor.apply()

    }

    fun readData() {
        val sp = getSharedPreferences("tasks", Context.MODE_PRIVATE)
        val list = sp.getStringSet("tasks",null)
        var domain : String?

        if(list != null) {
            for(a in list) {
                domain = a.substringAfterLast(";")
                Log.e("${taskNumber}",domain)
                taskList.add(Task(taskNumber, domain))
                taskNumber++
            }
        }
    }
}