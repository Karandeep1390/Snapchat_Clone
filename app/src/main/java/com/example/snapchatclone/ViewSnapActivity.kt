package com.example.snapchatclone

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ExecutionException

class ViewSnapActivity : AppCompatActivity() {
    var message:TextView ?=null
    var imageView:ImageView?=null
    val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_snap)
        message = findViewById(R.id.textView)
        imageView= findViewById(R.id.imageView2)
        message?.text = intent.getStringExtra("message")
        val imageDownloader = ImageDownloader()
        val myImage: Bitmap
        try {
            myImage =
                imageDownloader.execute(intent.getStringExtra("imageUrl"))
                    .get()!!
            imageView?.setImageBitmap(myImage)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        }

    }
   inner class ImageDownloader :
        AsyncTask<String?, Void?, Bitmap?>() {
       override fun doInBackground(vararg urls: String?): Bitmap? {

               return try {
                   val url = URL(urls[0])
                   val connection =
                       url.openConnection() as HttpURLConnection
                   connection.connect()
                   val `in` = connection.inputStream
                   BitmapFactory.decodeStream(`in`)
               } catch (e: Exception) {
                   e.printStackTrace()
                   null
               }
       }

   }

    override fun onBackPressed() {
        super.onBackPressed()
        FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.currentUser?.uid!!).child("snaps").child(intent.getStringExtra("snapKey")).removeValue()
        FirebaseStorage.getInstance().getReference().child("images").child(intent.getStringExtra("imageName")).delete()
    }




   }

