package com.learn.taskfingerprint.framework.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import com.learn.taskfingerprint.business.Note
import com.learn.taskfingerprint.framework.viewmodel.NoteViewModel
import com.learn.taskfingerprint.R
import java.text.SimpleDateFormat
import java.util.*

class AddEditNoteActivity : AppCompatActivity() {

    lateinit var noteTitleEt: EditText
    lateinit var noteDescEt: EditText
    lateinit var applyBtn: Button
    lateinit var viewModel: NoteViewModel
    var noteId = -1

    lateinit var micIV: ImageView

    // on below line we are creating a constant value
    private val REQUEST_CODE_SPEECH_INPUT = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_note)

        noteTitleEt = findViewById(R.id.edit_note_title_et)
        noteDescEt = findViewById(R.id.edit_note_desc_et)
        applyBtn = findViewById(R.id.apply_btn)

        micIV = findViewById(R.id.idIVMic)

        viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(NoteViewModel::class.java)

        val noteType = intent.getStringExtra("noteType")

        if (noteType.equals("Edit")) {
            val noteTitle = intent.getStringExtra("noteTitle")
            val noteDesc = intent.getStringExtra("noteDesc")
            noteId = intent.getIntExtra("noteId", -1)
            applyBtn.setText("Update Note")
            noteTitleEt.setText(noteTitle)
            noteDescEt.setText(noteDesc)

        } else {
            applyBtn.setText("Save Note")
        }

        applyBtn.setOnClickListener {
            val noteTitle = noteTitleEt.text.toString()
            val noteDesc = noteDescEt.text.toString()

            if (noteTitle.isNotEmpty() && noteDesc.isNotEmpty()) {

                if (noteType.equals("Edit")) {
                    if (noteTitle.isNotEmpty() && noteDesc.isNotEmpty()) {
                        val sdf = SimpleDateFormat("dd MMM, yyyy - HH:mm")
                        val currentDate: String = sdf.format(Date())
                        val updateNote = Note(noteTitle, noteDesc, currentDate)
                        updateNote.id = noteId
                        viewModel.updateNote(updateNote)
                        Toast.makeText(this, "Note Updated...", Toast.LENGTH_LONG).show()
                    }
                } else {
                    if (noteTitle.isNotEmpty() && noteDesc.isNotEmpty()) {
                        val sdf = SimpleDateFormat("dd MMM, yyyy - HH:mm")
                        val currentDate: String = sdf.format(Date())
                        viewModel.addNote(Note(noteTitle, noteDesc, currentDate))
                        Toast.makeText(this, "Note Added...", Toast.LENGTH_LONG).show()
                    }
                }

                startActivity(Intent(applicationContext, MainActivity::class.java))
                this.finish()

            } else {
                Toast.makeText(this, "You Must fill Title & Description", Toast.LENGTH_LONG).show()
            }


        }

        micIV.setOnClickListener {
            // on below line we are calling speech recognizer intent.
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

            // on below line we are passing language model
            // and model free form in our intent
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )

            // on below line we are passing our
            // language as a default language.
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault()
            )

            // on below line we are specifying a prompt
            // message as speak to text on below line.
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text")

            // on below line we are specifying a try catch block.
            // in this block we are calling a start activity
            // for result method and passing our result code.
            try {
                startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT)
            } catch (e: Exception) {
                // on below line we are displaying error message in toast
                Toast.makeText(
                    this, " " + e.message, Toast.LENGTH_SHORT
                ).show()
            }
        }


    }

    // on below line we are calling on activity result method.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // in this method we are checking request
        // code with our result code.
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            // on below line we are checking if result code is ok
            if (resultCode == RESULT_OK && data != null) {

                // in that case we are extracting the
                // data from our array list
                val res: ArrayList<String> =
                    data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>

                // on below line we are setting data
                // to our output text view.
                noteDescEt.setText(
                    Objects.requireNonNull(res)[0]
                )
            }
        }
    }

}