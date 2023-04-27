package com.learn.taskfingerprint.framework.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.learn.taskfingerprint.business.Note
import com.learn.taskfingerprint.R

class NoteAdapter(
    val context: Context,
    val noteClickDeleteInterface: NoteClickDeleteInterface,
    val noteClickInterface: NoteClickInterface
) : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    private val allNotes = ArrayList<Note>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val noteTv = itemView.findViewById<TextView>(R.id.note_title_tv)
        val timeTv = itemView.findViewById<TextView>(R.id.note_time_tv)
        val deleteTv = itemView.findViewById<ImageView>(R.id.delete_iv)

    }


    interface NoteClickDeleteInterface {
        fun onDeleteIconClick(note: Note)
    }

    interface NoteClickInterface {
        fun onNoteClick(note: Note)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_note_rv, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.noteTv.setText(allNotes.get(position).noteTitle)
        holder.timeTv.setText("Last Update : " + allNotes.get(position).noteTime)

        holder.deleteTv.setOnClickListener {
            noteClickDeleteInterface.onDeleteIconClick(allNotes.get(position))
        }


        holder.itemView.setOnClickListener {
            noteClickInterface.onNoteClick(allNotes.get(position))
        }

    }

    override fun getItemCount(): Int {
        return allNotes.size
    }

    fun updateList(newList: List<Note>){
        allNotes.clear()
        allNotes.addAll(newList)
        notifyDataSetChanged()
    }



}