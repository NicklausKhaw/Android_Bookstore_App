package com.example.bookstoreapp

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
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
import com.example.bookstoreapp.databinding.ActivityAddBinding
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import com.example.bookstoreapp.data.DatabaseHandler
import com.example.bookstoreapp.model.BookModelClass
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class AddActivity : AppCompatActivity() {

    lateinit var binding: ActivityAddBinding
    private val pickImage = 100
    private var imageUri: Uri? = null

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
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        binding.invalidNameTextView.isVisible = false
        binding.invalidAuthorTextView.isVisible = false
        binding.invalidDescriptionTextView.isVisible = false

        binding.bookImageView.setOnClickListener { popUpAlert() }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.done_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.done_action -> {
                if(binding.nameEditText.text.toString() == "" || binding.authorEditText.text.toString() == "" || binding.descriptionEditText.text.toString() == "") {

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
                    addBook()
                    finish()
                    return true
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addBook() {
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val currentDate = sdf.format(Date())

        val bitmap = (binding.bookImageView.drawable as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)

        val image: ByteArray = stream.toByteArray()

        val databaseHandler: DatabaseHandler = DatabaseHandler(this)

        val name = binding.nameEditText.text.toString()
        val author = binding.authorEditText.text.toString()
        val description = binding.descriptionEditText.text.toString()

        val status = databaseHandler.addBook(
            BookModelClass(
                0,
                name,
                image,
                author,
                currentDate,
                description
            ))

        if(status > -1) {
            Toast.makeText(applicationContext, "Book Saved", Toast.LENGTH_LONG).show()
            binding.nameEditText.text.clear()
            binding.authorEditText.text.clear()
            binding.descriptionEditText.text.clear()
        }
    }

    private fun popUpAlert() {
        val dialogBuilder = AlertDialog.Builder(this)

        dialogBuilder.setMessage("Add a Picture")
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

    private fun openGallery() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        getResult.launch(gallery)
    }

    private fun openCamera() {
        val camera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        getPicture.launch(camera)
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