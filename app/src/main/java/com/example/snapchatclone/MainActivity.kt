package com.example.snapchatclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    var emailEditText: EditText?=null
    var passwordEditText: EditText?=null
    val mAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.editText2)
    }

    fun go(view: View){
        mAuth.signInWithEmailAndPassword(emailEditText?.text.toString(), passwordEditText?.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) { // Sign in success, update UI with the signed-in user's information
                    login()
                } else {
                    mAuth.createUserWithEmailAndPassword(emailEditText?.text.toString(), passwordEditText?.text.toString())
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) { FirebaseDatabase.getInstance().getReference().child("users").child(
                                task.result?.user?.uid!!
                            ).child("email").setValue(emailEditText?.text.toString())

                                login()
                            } else {
                                Toast.makeText(this,"Login Failed :(",Toast.LENGTH_LONG).show()

                            }

                            // ...
                        }
                }

                // ...
            }
    }
    fun login(){
        val intent = Intent(this, SnapsActivity::class.java)
        startActivity(intent)
    }
}
