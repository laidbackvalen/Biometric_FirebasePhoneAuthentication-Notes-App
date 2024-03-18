package com.example.notes.activities.crud

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CalendarView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.dataclass.DataClass
import com.example.notes.authenticaton.Login_Activity
import com.example.notes.adapter.MyRecyclerAdapter
import com.example.notes.activities.Notifications_Activity
import com.example.notes.R
import com.example.notes.fcm.MyFirebaseMessagingService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import java.text.SimpleDateFormat

class All_Notes_Activity : AppCompatActivity() {

    lateinit var drawer: DrawerLayout
    lateinit var recyclerView: RecyclerView
    lateinit var calenderView :CalendarView
    lateinit var closeTextView :TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        FirebaseMessaging.getInstance().token.addOnCompleteListener {  //this one is generating token
            if(!it.isSuccessful){
                Log.e("TokenDetails", "Toke didn't recieve!")
            }
            val token = it.result
            Log.e("TOKEN", token)
        }

        drawer = findViewById<DrawerLayout>(R.id.drawerLayout) as DrawerLayout //DRAWERLAYOUT

        findViewById<ImageView>(R.id.imageViewBurgerMenuAllNotes).setOnClickListener {
            openDrawer()
            calenderView.visibility = View.GONE //onclick make these visibility
            closeTextView.visibility = View.GONE //onclick make these visibility
        }

        calenderView = findViewById<CalendarView>(R.id.calenderView)
        closeTextView = findViewById<TextView>(R.id.closeCalendarTextView)
        calenderView.visibility = View.GONE   //visibility Gone set by me as default
        closeTextView.visibility = View.GONE

        findViewById<ImageView>(R.id.calenderImageView).setOnClickListener {

            // Toggle visibility of the CalendarView
            if (calenderView.visibility == View.GONE) {   //if calender Visibility is gone
                calenderView.visibility = View.VISIBLE // than make it visible when clicking on calenderImage
                closeTextView.visibility = View.VISIBLE

                findViewById<TextView>(R.id.closeCalendarTextView).setOnClickListener { // on clicking "close" TextView
                if (closeTextView.visibility == View.VISIBLE) {  //if calender Visibility is VISIBLE
                        calenderView.visibility = View.GONE  // make calender Visibility gone
                        closeTextView.visibility = View.GONE // make close textview Visibility gone
                    }
                }
            } else {
                calenderView.visibility = View.GONE    // if not clicked keep it's Visibility gone
                closeTextView.visibility = View.GONE
            }

        }

        findViewById<TextView>(R.id.createNoteTextview).setOnClickListener {  //CreateNote From Drawer
            val intent: Intent = Intent(this, Create_Data_Activity::class.java)
            startActivity(intent)
            calenderView.visibility = View.GONE  //onclick make these visibility
            closeTextView.visibility = View.GONE //onclick make these visibility
        }
        findViewById<View>(R.id.topView).setOnClickListener {  //TopView From Drawer

        }
        findViewById<TextView>(R.id.notificationTextView).setOnClickListener {  //Notifications From Drawer
            startActivity(Intent(this, Notifications_Activity::class.java))
            calenderView.visibility = View.GONE //onclick make these visibility
            closeTextView.visibility = View.GONE //onclick make these visibility
        }
        findViewById<TextView>(R.id.eventReminderTextView).setOnClickListener {  //Settings From Drawer
            calenderView.visibility = View.GONE //onclick make these visibility
            closeTextView.visibility = View.GONE //onclick make these visibility
            // Event start and end time with date
            val startTime = "2022-02-1T09:00:00"
            val endTime = "2022-02-1T12:00:00"

            // Parsing the date and time
            val mSimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val mStartTime = mSimpleDateFormat.parse(startTime)
            val mEndTime = mSimpleDateFormat.parse(endTime)

            val mIntent = Intent(Intent.ACTION_EDIT)
            mIntent.type = "vnd.android.cursor.item/event"
            mIntent.putExtra("beginTime", mStartTime.time)
            mIntent.putExtra("time", true)
            mIntent.putExtra("rule", "FREQ=YEARLY")
            mIntent.putExtra("endTime", mEndTime.time)
            mIntent.putExtra("title", "Write your Event here..")
            startActivity(mIntent)
        }

        findViewById<TextView>(R.id.contactUsTextView).setOnClickListener {
            var contactintent = Intent(Intent.ACTION_DIAL)
            var phoneNumber : String ="+918605886969"
            contactintent.setData(Uri.parse("tel:$phoneNumber" ));
           startActivity(contactintent)
        }

        findViewById<TextView>(R.id.logoutTextView).setOnClickListener {  //logout using / from Drawer
            val firebaseAuth = FirebaseAuth.getInstance()
            firebaseAuth.signOut()
            startActivity(Intent(this, Login_Activity::class.java))
            finish()
            calenderView.visibility = View.GONE //onclick make these visibility
            closeTextView.visibility = View.GONE //onclick make these visibility
        }
        findViewById<View>(R.id.bottomView).setOnClickListener {
            calenderView.visibility = View.GONE //onclick make these visibility
            closeTextView.visibility = View.GONE //onclick make these visibility
        }

//        findViewById<TextView>(R.id.nameAllNotes).setOnClickListener {
//            startActivity(Intent(baseContext, UserName_Activity::class.java))
//        }

        findViewById<ImageView>(R.id.imageEdit).setOnClickListener {
            val intent = Intent(this, Create_Data_Activity::class.java)
            startActivity(intent)
            calenderView.visibility = View.GONE //onclick make these visibility
            closeTextView.visibility = View.GONE //onclick make these visibility
        }
        //RecyclerView
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        var layoutType = LinearLayoutManager(this)
        layoutType.reverseLayout = true
        layoutType.stackFromEnd = true // This line makes sure the items are stacked from the end
        recyclerView.layoutManager = layoutType

//        getData()

        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        var a = auth.uid.toString()
        var firebaseDatabase = FirebaseDatabase.getInstance()
        var databaseReference =
            firebaseDatabase.getReference("Notes").child(a)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val dataClassList = mutableListOf<DataClass>()
                        for (itemSnapshot in snapshot.children) {
                            val item: DataClass? = itemSnapshot.getValue(DataClass::class.java)
                            item?.let {
                                dataClassList.add(it.copy())
                            }
                        }
                        val adapter = MyRecyclerAdapter(baseContext, dataClassList)
                        recyclerView.adapter = adapter
                        adapter.notifyDataSetChanged()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(baseContext, "" + error.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }

                })
    }

    //    private fun getData() {
//        var firebaseDatabase = FirebaseDatabase.getInstance()
//        var databaseReference = firebaseDatabase.getReference("Notes").child("valen")
//            .addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
////                    val a = snapshot.getValue()
////                    Toast.makeText(baseContext, "" + a, Toast.LENGTH_LONG).show()
//
//
//                    //          for (element in snapshot.children) {
//                    val title = snapshot.child("title").value.toString()
//                    val description = snapshot.child("description").value.toString()
//                    val date = snapshot.child("date").value.toString()
//                    val time = snapshot.child("time").value.toString()
//                    findViewById<TextView>(R.id.titleRetrieve).text = title.toString()
//                    findViewById<TextView>(R.id.descriptionRetrieve).text = description.toString()
//                    findViewById<TextView>(R.id.dateRetrieve).text = date.toString()
//                    findViewById<TextView>(R.id.timeRetrieve).text = time.toString()
//                    //        }
//
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//
//                }
//
//            })
//
//    }
// Retrieving data from DataSnapshot


    //OUTSIDE ONCREATE
    fun openDrawer() {
        drawer.openDrawer(GravityCompat.START)
    }

    private fun closeDrawer(drawerLayout: DrawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
    }


//    override fun onPause() {
//        closeDrawer(drawer)
//        super.onPause()
//    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}

  
