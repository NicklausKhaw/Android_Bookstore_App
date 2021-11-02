package com.example.bookstoreapp.data

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.bookstoreapp.model.BookModelClass

class DatabaseHandler(context: Context):
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

        companion object {
            private const val DATABASE_VERSION = 2
            private const val DATABASE_NAME = "BookDatabase"
            private const val TABLE_BOOKS = "BookTable"

            private const val KEY_ID = "_id"
            private const val KEY_NAME = "name"
            private const val KEY_IMAGE = "image"
            private const val KEY_AUTHOR = "author"
            private const val KEY_DATE = "date"
            private const val KEY_DESCRIPTION = "description"
        }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_BOOKS_TABLE = ("CREATE TABLE " + TABLE_BOOKS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_IMAGE + " BLOB," + KEY_AUTHOR + " TEXT,"
                + KEY_DATE + " TEXT," + KEY_DESCRIPTION + " TEXT" + ")")
        db?.execSQL(CREATE_BOOKS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKS)
        onCreate(db)
    }

    fun addBook(book: BookModelClass): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, book.name)
        contentValues.put(KEY_IMAGE, book.image)
        contentValues.put(KEY_AUTHOR, book.author)
        contentValues.put(KEY_DATE, book.date)
        contentValues.put(KEY_DESCRIPTION, book.description)

        val success = db.insert(TABLE_BOOKS, null, contentValues)
        db.close()
        return success
    }

    @SuppressLint("Range")
    fun viewBook(): ArrayList<BookModelClass> {
        val bookList: ArrayList<BookModelClass> = ArrayList<BookModelClass>()

        val selectQuery = "SELECT * FROM $TABLE_BOOKS"

        val db = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var name: String
        var image: ByteArray
        var author: String
        var date: String
        var description: String

        if(cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                name = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                image = cursor.getBlob(cursor.getColumnIndex(KEY_IMAGE))
                author = cursor.getString(cursor.getColumnIndex(KEY_AUTHOR))
                date = cursor.getString(cursor.getColumnIndex(KEY_DATE))
                description = cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION))

                val book = BookModelClass(
                    id = id,
                    name = name,
                    image = image,
                    author = author,
                    date = date,
                    description = description)
                bookList.add(book)
            } while (cursor.moveToNext())
        }
        return bookList
    }

    @SuppressLint("Range")
    fun searchBook(id: Int): BookModelClass {
        val db = this.readableDatabase
        val contentValues = ContentValues()

        val selectQuery = "SELECT * FROM $TABLE_BOOKS WHERE $KEY_ID = $id"
        var cursor: Cursor? = null
        cursor = db.rawQuery(selectQuery, null)

        if(cursor != null) {
            cursor.moveToFirst()
        }

        var id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
        var name = cursor.getString(cursor.getColumnIndex(KEY_NAME))
        var image = cursor.getBlob(cursor.getColumnIndex(KEY_IMAGE))
        var author = cursor.getString(cursor.getColumnIndex(KEY_AUTHOR))
        var date = cursor.getString(cursor.getColumnIndex(KEY_DATE))
        var description = cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION))

        return BookModelClass(
            id = id,
            name = name,
            image = image,
            author = author,
            date = date,
            description = description
        )
    }

    fun updateBook(book: BookModelClass): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, book.name)
        contentValues.put(KEY_IMAGE, book.image)
        contentValues.put(KEY_AUTHOR, book.author)
        contentValues.put(KEY_DATE, book.date)
        contentValues.put(KEY_DESCRIPTION, book.description)

        val success = db.update(TABLE_BOOKS, contentValues, KEY_ID + "=" + book.id, null)

        db.close()
        return success
    }

    fun deleteBook(book: BookModelClass): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, book.id)

        val success = db.delete(TABLE_BOOKS, KEY_ID + "=" + book.id, null)
        db.close()
        return success
    }
}
