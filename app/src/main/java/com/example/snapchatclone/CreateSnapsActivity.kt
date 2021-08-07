package com.example.snapchatclone

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream
import java.util.*


class CreateSnapsActivity : AppCompatActivity() {
    var createSnapImageView: ImageView? = null
    var messageEditText: EditText? = null
    val imagename = UUID.randomUUID().toString() + ".jpg"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_snaps)
        createSnapImageView = findViewById(R.id.chooseImageView)
        messageEditText = findViewById(R.id.messageEditText)
    }

    fun getPhoto() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 1)
    }

    fun chooseImageClicked(view: View) {
        if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED) {
            //permission denied
            requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1)
        } else {
            //permission already granted
            getPhoto()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val selectedImage = data!!.data
        if (requestCode==1&& resultCode==Activity.RESULT_OK&& data!=null){
           try {


            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImage)
            createSnapImageView?.setImageBitmap(bitmap)
        }catch (e: Exception){
               e.printStackTrace()
           }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode==1){
            if (grantResults.size>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getPhoto()
            }
        }
    }
    fun nextClicked(view: View){
        // Get the data from an ImageView as bytes
        // Get the data from an ImageView as bytes

        // Get the data from an ImageView as bytes
        createSnapImageView?.setDrawingCacheEnabled(true)
        createSnapImageView?.buildDrawingCache()
        val bitmap = (createSnapImageView?.getDrawable() as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()


        val uploadTask: UploadTask =  FirebaseStorage.getInstance().getReference().child("images").child(imagename).putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
            Toast.makeText(this,"uploadFailed",Toast.LENGTH_LONG).show()
        }.addOnSuccessListener {
            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
            // ...
            taskSnapshot->
            val downloadUrl = taskSnapshot.metadata




        val intent = Intent(this, chooseUserActivity::class.java)
            intent.putExtra("imageUrl",downloadUrl.toString())
            intent.putExtra("imageName",imagename)
            intent.putExtra("message",messageEditText?.text.toString())
            startActivity(intent)

            }

        }



    }



