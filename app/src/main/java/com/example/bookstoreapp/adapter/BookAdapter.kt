package com.example.bookstoreapp.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookstoreapp.model.BookModelClass
import android.graphics.BitmapFactory
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.bookstoreapp.*
import java.security.AccessController.getContext

class BookAdapter(
    private val context: Context,
    private var dataset: ArrayList<BookModelClass>
    ): RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    class BookViewHolder(private val view: View): RecyclerView.ViewHolder(view) {

        val nameTextView: TextView = view.findViewById(R.id.name_text_view)
        val bookImageView: ImageView = view.findViewById(R.id.book_image_view)
        val authorTextView: TextView = view.findViewById(R.id.author_text_view)
        val dataTextView: TextView = view.findViewById(R.id.date_text_view)
        val bookView = view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.book_item, parent, false)
        return BookViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = dataset[position]
        holder.nameTextView.text = book.name
        holder.bookImageView.setImageBitmap(convertToImage(book.image))
        holder.authorTextView.text = "By " + book.author
        holder.dataTextView.text = book.date
        holder.bookView.setOnClickListener {
            println("${book.name} selected")
            goToDetailActivity(book)
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    private fun convertToImage(image: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(image, 0, image.size)
    }

    private fun goToDetailActivity(book: BookModelClass) {

        val intent = Intent(context,DetailActivity::class.java)
        intent.putExtra("id", book.id)

        context.startActivity(intent)
    }

    fun deleteItem(i: Int) {
        dataset.removeAt(i)
        notifyDataSetChanged()
    }

}