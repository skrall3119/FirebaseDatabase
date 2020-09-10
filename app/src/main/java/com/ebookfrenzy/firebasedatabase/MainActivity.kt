package com.ebookfrenzy.firebasedatabase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private var users = ArrayList<DataItem>()
    private val adapter = RecyclerAdapter(users)

//    private fun generateDummyList(size:Int): List<DataItem>{
//        val list = ArrayList<DataItem>()
//
//        for(i in 0 until size){
//            val item = DataItem("Item $i")
//            list += item
//        }
//        return list
//    }

    val changeListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.hasChildren()) {
                users.clear()
                for (child in snapshot.children) {
                    val holdData = child.getValue(DataItem::class.java)
                    users.add(holdData!!)
                }
                recycler_view.adapter = RecyclerAdapter(users)
            }
        }

        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }
    }

    fun insertItem(view: View){
        val newEntry = DataItem(editText.text.toString())
        users.add(users.size -1, newEntry)
        adapter.notifyItemInserted(users.indexOf(newEntry))
        database.child(newEntry.itemText.toString()).child("itemText").setValue(newEntry.itemText.toString())


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = Firebase.database.getReference("/users")
        database.addValueEventListener(changeListener)

//      val exampleList = generateDummyList(300)

        Log.i("users", users.toString())

        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)

    }

    override fun onDestroy() {
        super.onDestroy()
        database.removeEventListener(changeListener)
    }
}
