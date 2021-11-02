package com.example.bookstoreapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import com.example.bookstoreapp.data.DatabaseHandler
import com.example.bookstoreapp.databinding.ActivityDetailBinding


class DetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityDetailBinding
    private val databaseHandler: DatabaseHandler = DatabaseHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        binding.nameEditText.isFocusable = false
        binding.authorEditText.isFocusable = false
        binding.descriptionEditText.isFocusable = false

        val id = getId()
        val book = databaseHandler.searchBook(id)

        println(book)

        val name = book.name
        val image = book.image
        val author = book.author
        val date = book.date
        val description = book.description

        binding.bookImageView.setImageBitmap(convertToImage(image!!))
        binding.nameEditText.setText(name)
        binding.authorEditText.setText(author)
        binding.descriptionEditText.setText(description)
    }


    override fun onResume() {
        super.onResume()

        val id = getId()
        val book = databaseHandler.searchBook(id)

        val name = book.name
        val image = book.image
        val author = book.author
        val date = book.date
        val description = book.description

        binding.bookImageView.setImageBitmap(convertToImage(image!!))
        binding.nameEditText.setText(name)
        binding.authorEditText.setText(author)
        binding.descriptionEditText.setText(description)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_menu, menu)

        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.edit_action -> {
                val intent = Intent(this,EditActivity::class.java)
                intent.putExtra("id", getId())

                this.startActivity(intent)
                finish()
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun convertToImage(image: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(image, 0, image.size)
    }

    private fun getId(): Int {
        return intent.getIntExtra("id", 0)
    }
}