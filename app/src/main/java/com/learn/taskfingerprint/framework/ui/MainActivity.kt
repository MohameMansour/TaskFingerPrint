package com.learn.taskfingerprint.framework.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.learn.taskfingerprint.R
import com.learn.taskfingerprint.framework.adapter.NoteAdapter
import com.learn.taskfingerprint.framework.viewmodel.NoteViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.learn.taskfingerprint.business.Note

class MainActivity : AppCompatActivity(), NoteAdapter.NoteClickInterface,
    NoteAdapter.NoteClickDeleteInterface {

    lateinit var notesRv: RecyclerView
    lateinit var addFab: FloatingActionButton
    lateinit var viewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notesRv = findViewById(R.id.note_list_rv)
        addFab = findViewById(R.id.add_note_fbtn)

        notesRv.layoutManager = LinearLayoutManager(this)

        val noteAdapter = NoteAdapter(this, this, this)

        notesRv.adapter = noteAdapter

        viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(NoteViewModel::class.java)

        viewModel.allNotes.observe(this, { list ->
            list?.let {
                noteAdapter.updateList(it)
            }
        })

        addFab.setOnClickListener {
            val intent = Intent(this@MainActivity, AddEditNoteActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onDeleteIconClick(note: Note) {
        viewModel.deleteNote(note)
        Toast.makeText(this, "${note.noteTitle} Deleted", Toast.LENGTH_LONG).show()
    }

    override fun onNoteClick(note: Note) {
        val intent = Intent(this@MainActivity, AddEditNoteActivity::class.java)
        intent.putExtra("noteType", "Edit")
        intent.putExtra("noteTitle", note.noteTitle)
        intent.putExtra("noteDesc", note.noteDescription)
        intent.putExtra("noteId", note.id)
        startActivity(intent)
    }

}