package com.example.snapchatclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.constraintlayout.solver.widgets.Snapshot
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class SnapsActivity : AppCompatActivity() {
    val mAuth = FirebaseAuth.getInstance()
    var snaps:ListView ?=null
    var emails:ArrayList<String> = ArrayList()
    var snapss:ArrayList<DataSnapshot> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_snaps)
        snaps = findViewById(R.id.listView)
        var adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,emails)
        snaps?.adapter = adapter
        FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.currentUser?.uid!!).child("snaps").addChildEventListener(object :ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                emails.add(p0.child("from").value as String)
                snapss.add(p0!!)
                adapter.notifyDataSetChanged()
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                var index =0
                for (snap: DataSnapshot in snapss){
                    if (snap.key==p0?.key){
                        emails.removeAt(index)
                        snapss.removeAt(index)
                    }
                    index++
                }
            }

        })
        snaps?.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            var snapshot = snapss.get(position)
            intent = Intent(this,ViewSnapActivity::class.java)
            intent.putExtra("imageName",snapshot.child("imageName").value as String)
            intent.putExtra("imageUrl",snapshot.child("imageUrl").value as String)
            intent.putExtra("message",snapshot.child("message").value as String)
            intent.putExtra("snapKey",snapshot.key)
            startActivity(intent)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.snaps,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item?.itemId==R.id.snap){
            val intent = Intent(this, CreateSnapsActivity::class.java)
            startActivity(intent)
        }else   if (item?.itemId==R.id.logout){
           mAuth.signOut()
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        mAuth.signOut()
    }
}
