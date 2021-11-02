package com.example.bookstoreapp

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.bookstoreapp.adapter.BookAdapter
import com.example.bookstoreapp.data.DatabaseHandler
import com.example.bookstoreapp.databinding.ActivityListBinding
import com.example.bookstoreapp.model.BookModelClass

class ListActivity : AppCompatActivity() {

    lateinit var binding: ActivityListBinding
    private val databaseHandler: DatabaseHandler = DatabaseHandler(this)
    private lateinit var addIntent: Intent
    private lateinit var recyclerViewAdapter: BookAdapter
    private lateinit var deletedBook: BookModelClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListOfDataIntoRecyclerView()

        recyclerViewAdapter = BookAdapter(this, getBooksList())
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)

    }

    private var simpleCallback = object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT)) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            var position = viewHolder.adapterPosition


            when(direction) {
                ItemTouchHelper.LEFT -> {
                    /*
                    deletedBook = getBooksList().get(position)

                    databaseHandler.deleteBook(deletedBook)
                    recyclerViewAdapter.notifyItemRemoved(position)
                    setupListOfDataIntoRecyclerView()
                     */
                    popUpAlert(position)
                }
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.add_action -> {
                Toast.makeText(applicationContext, "Add button Pressed", Toast.LENGTH_LONG).show()
                addIntent = Intent(this, AddActivity::class.java)
                startActivity(addIntent)

                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupListOfDataIntoRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter = BookAdapter(this, getBooksList())
        recyclerView.setHasFixedSize(true)
    }

    private fun getBooksList(): ArrayList<BookModelClass> {
        return databaseHandler.viewBook()
    }

    override fun onResume() {
        super.onResume()
        setupListOfDataIntoRecyclerView()
    }

    private fun popUpAlert(position: Int) {
        val dialogBuilder = AlertDialog.Builder(this)


        dialogBuilder.setMessage("Are you sure you want to delete this book?")
            .setCancelable(false)
            .setPositiveButton("Delete", DialogInterface.OnClickListener {
                    dialogInterface, i -> delete(position)
            })
            .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                    dialogInterface, i ->
                dialogInterface.dismiss()
                setupListOfDataIntoRecyclerView()
            })

        val alert = dialogBuilder.create()
        alert.show()
    }

    private fun delete(position: Int) {
        deletedBook = getBooksList().get(position)

        databaseHandler.deleteBook(deletedBook)
        recyclerViewAdapter.notifyItemRemoved(position)
        setupListOfDataIntoRecyclerView()
    }
}