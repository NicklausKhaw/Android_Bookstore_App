package com.example.bookstoreapp

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.example.bookstoreapp.data.DatabaseHandler
import com.example.bookstoreapp.databinding.ActivityEditBinding
import com.example.bookstoreapp.model.BookModelClass
import java.io.ByteArrayOutputStream


class EditActivity : AppCompatActivity() {

    lateinit var binding: ActivityEditBinding
    val databaseHandler: DatabaseHandler = DatabaseHandler(this)
    private val pickImage = 100
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        val book = databaseHandler.searchBook(getId())

        binding.invalidNameTextView.isVisible = false
        binding.invalidAuthorTextView.isVisible = false
        binding.invalidDescriptionTextView.isVisible = false

        binding.bookImageView.setImageBitmap(convertToImage(book.image!!))
        binding.nameEditText.setText(book.name)
        binding.authorEditText.setText(book.author)
        binding.descriptionEditText.setText(book.description)
        binding.bookImageView.setOnClickListener { popUpAlert() }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.done_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.done_action -> {
                if(binding.nameEditText.text.toString() == "" || binding.authorEditText.text.toString() == ""
                    || binding.descriptionEditText.text.toString() == "") {

                    if(binding.nameEditText.text.toString() == "") {
                        binding.invalidNameTextView.isVisible = true
                    } else {
                        binding.invalidNameTextView.isVisible = false
                    }

                    if(binding.authorEditText.text.toString() == "") {
                        binding.invalidAuthorTextView.isVisible = true
                    } else {
                        binding.invalidAuthorTextView.isVisible = false
                    }

                    if(binding.descriptionEditText.text.toString() == "") {
                        binding.invalidDescriptionTextView.isVisible = true
                    } else {
                        binding.invalidDescriptionTextView.isVisible = false
                    }
                } else {
                    updateBook()
                    finish()
                    return true
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateBook() {
        val bitmap = (binding.bookImageView.drawable as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)

        val id = getId()
        val book = databaseHandler.searchBook(id)
        val image: ByteArray = stream.toByteArray()
        val name = binding.nameEditText.text.toString()
        val author = binding.authorEditText.text.toString()
        val description = binding.descriptionEditText.text.toString()

        val status = databaseHandler.updateBook(BookModelClass(
            id,
            name,
            image,
            author,
            book.date,
            description
        ))

        if(status > -1) {
            Toast.makeText(applicationContext, "Book Updated", Toast.LENGTH_LONG).show()
        }

    }

    private fun popUpAlert() {
        val dialogBuilder = AlertDialog.Builder(this)

        dialogBuilder.setMessage("Change Picture")
            .setCancelable(true)
            .setPositiveButton("From Camera", DialogInterface.OnClickListener {
                    dialogInterface, i -> openCamera()
            })
            .setNegativeButton("From Gallery", DialogInterface.OnClickListener {
                    dialogInterface, i -> openGallery()
            })

        val alert = dialogBuilder.create()
        alert.show()
    }

    private fun convertToImage(image: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(image, 0, image.size)
    }

    private fun getId(): Int {
        val id = intent.getIntExtra("id", 0)
        return id
    }

    private fun openGallery() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        getResult.launch(gallery)
    }

    private fun openCamera() {
        val camera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        getPicture.launch(camera)
    }

    private val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            imageUri = it.data?.data
            binding.bookImageView.setImageURI(imageUri)
        }
    }

    private val getPicture = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK && it.data != null) {
            imageUri = it.data?.data
            binding.bookImageView.setImageBitmap(it.data!!.extras?.get("data") as Bitmap)
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    Log.d("focus", "touchevent")
                    v.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

}