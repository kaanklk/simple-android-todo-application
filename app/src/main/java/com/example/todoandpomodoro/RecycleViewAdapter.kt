package com.example.todoandpomodoro

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.add_new_task.view.*
import kotlin.Exception

class RecycleViewAdapter(private val mContext: Context, private val TaskList:ArrayList<Task>)
    : RecyclerView.Adapter<RecycleViewAdapter.CardViewDesignItemHolder>(){
    inner class CardViewDesignItemHolder(view:View) : RecyclerView.ViewHolder(view){

         var rowCardView: CardView
         var rowCheckBox:CheckBox
         var rowText:TextView
         var rowMenu:ImageView

         init {
             rowCardView = view.findViewById(R.id.cardViewTask)
             rowCheckBox = view.findViewById(R.id.checkBoxTodoCheck)
             rowText = view.findViewById(R.id.textViewTodoTask)
             rowMenu = view.findViewById(R.id.imageViewTodoMore)
         }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewDesignItemHolder {
        val design = LayoutInflater.from(mContext).inflate(R.layout.todo_card_design,parent, false)
        return CardViewDesignItemHolder(design)
    }

    override fun onBindViewHolder(holder: CardViewDesignItemHolder, position: Int) {
        val task = TaskList[position]
        holder.rowText.text = task.task

        holder.rowMenu.setOnClickListener {
            setupPopUpMenu(holder,position)
        }
    }

    private fun setupPopUpMenu(holder: CardViewDesignItemHolder,position: Int) {
        val popup = PopupMenu(mContext,holder.rowMenu)
        popup.menuInflater.inflate(R.menu.task_popup_menu,popup.menu)
        popup.show()

        popup.setOnMenuItemClickListener { item->
            when(item.itemId) {
                R.id.taskPopupMenuDelete -> {
                    popUpMenuDeleteButtonBehaviour(position)
                    true
                }
                R.id.taskPopupMenuEdit -> {
                    popUpMenuEditButtonBehaviour(position)
                    true
                }
                else -> false
            }
        }
    }

    private fun popUpMenuEditButtonBehaviour(position: Int) {
        val editedItem = TaskList[position]
        val design = LayoutInflater.from(mContext).inflate(R.layout.add_new_task,null)
        val newTaskAlert = AlertDialog.Builder(mContext)
        newTaskAlert.setIcon(R.drawable.ic_baseline_edit_24)
        newTaskAlert.setView(design)
        newTaskAlert.setPositiveButton("Edit") {_,_ ->
            val taskText = design.editTextNewTaskTaskName.text
            if (taskText.toString() == "") {
                Toast.makeText(mContext,"Please write a task",Toast.LENGTH_SHORT).show()
            }
            else {
                editedItem.task = taskText.toString()
                notifyItemChanged(position)
            }
        }
        newTaskAlert.setNegativeButton("Cancel") {_,_ ->
            Toast.makeText(mContext,"Cancel",Toast.LENGTH_SHORT).show()
        }
        newTaskAlert.create().show()
    }

    private fun popUpMenuDeleteButtonBehaviour(position: Int) {
        try {
            TaskList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position,TaskList.size)
        }catch (e:Exception) {
            Log.e("ERROR","Something went wrong'!")
        }

    }

    override fun getItemCount(): Int {
        return TaskList.size
    }

}