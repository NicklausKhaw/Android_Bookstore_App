package com.example.bookstoreapp.model

import android.graphics.Bitmap

class BookModelClass(
    val id: Int,
    val name: String,
    val image: ByteArray,
    val author: String,
    val date: String,
    val description: String) {
}