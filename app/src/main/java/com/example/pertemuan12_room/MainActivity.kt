package com.example.pertemuan12_room

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.pertemuan12_room.database.NoteDao
import com.example.pertemuan12_room.database.NoteRoomDatabase
import com.example.pertemuan12_room.database.Notes
import com.example.pertemuan12_room.databinding.ActivityMainBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mNotesDao: NoteDao
    private lateinit var executorService: ExecutorService
    private var updateId: Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        executorService = Executors.newSingleThreadExecutor()
        val db = NoteRoomDatabase.getDatabase(this)
        mNotesDao = db!!.noteDao()!!
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding){
            btnAdd.setOnClickListener(View.OnClickListener {
                insert(
                    Notes(
                        title = txtTitle.text.toString(),
                        description = txtDesc.text.toString(),
                        date = txtDate.text.toString()
                    )
                )
                resetForm()
            })
            btnUpdate.setOnClickListener {
                update(
                    Notes(
                        id = updateId,
                        title = txtTitle.getText().toString(),
                        description = txtDesc.getText().toString(),
                        date = txtDate.getText().toString()
                    )
                )
                updateId = 0
                resetForm()
            }
            listView.setOnItemClickListener { adapterView, _, i, _ ->
                val item = adapterView.adapter.getItem(i) as Notes
                updateId = item.id
                txtTitle.setText(item.title)
                txtDesc.setText(item.description)
                txtDate.setText(item.date)
            }
            listView.onItemLongClickListener =
                AdapterView.OnItemLongClickListener { adapterView, _, i, _ ->
                    val item = adapterView.adapter.getItem(i) as Notes
                    delete(item)
                    true
                }
        }
    }

    private fun getNotes(){
        mNotesDao.allNotes.observe(this) { notes ->
            val adapter: ArrayAdapter<Notes> = ArrayAdapter<Notes>(
                this,
                android.R.layout.simple_list_item_1, notes
            )
            binding.listView.adapter = adapter
        }

    }

    private fun insert(note: Notes) {
        executorService.execute { mNotesDao.insert(note) }
    }
    private fun delete(note: Notes) {
        executorService.execute { mNotesDao.delete(note) }
    }
    private fun update(note: Notes) {
        executorService.execute { mNotesDao.update(note) }
    }

    override fun onResume() {
        super.onResume()
        getNotes()
    }

    private fun resetForm() {
        with(binding){
            txtTitle.setText("")
            txtDate.setText("")
            txtDesc.setText("")
        }
    }



}