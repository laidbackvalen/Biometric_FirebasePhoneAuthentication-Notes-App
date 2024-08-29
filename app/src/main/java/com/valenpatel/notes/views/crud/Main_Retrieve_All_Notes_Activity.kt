package com.valenpatel.notes.views.crud

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.valenpatel.notes.model.DataClass
import com.valenpatel.notes.authenticaton.Login_Activity
import com.valenpatel.notes.adapter.MyRecyclerAdapter
import com.valenpatel.notes.views.activity.Notifications_Activity
import com.valenpatel.notes.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import com.valenpatel.notes.views.fragments.UserInfo_Fragment
import com.valenpatel.notes.model.UserInfo
import com.valenpatel.notes.views.activity.Accounts_Activity
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.Locale

class Main_Retrieve_All_Notes_Activity : AppCompatActivity() {

    lateinit var drawer: DrawerLayout
    lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyRecyclerAdapter
    lateinit var searchView: SearchView

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.all_notes_activity)

        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)

        adapter = MyRecyclerAdapter(this, mutableListOf())
        recyclerView.adapter = adapter


        val lottieAnimationView: LottieAnimationView = findViewById<LottieAnimationView>(R.id.lottieAnimationviewMainhere)
        val noNotesFoundTextView: TextView = findViewById<TextView>(R.id.noNotesFoundTextView)

        findViewById<ImageView>(R.id.userInfo).setOnClickListener {
            UserInfo_Fragment().show(supportFragmentManager, null)
        }
        findViewById<TextView>(R.id.userNameMain).setOnClickListener {
            UserInfo_Fragment().show(supportFragmentManager, null)
        }
        findViewById<TextView>(R.id.accountTextView).setOnClickListener {
            val intent = Intent(this, Accounts_Activity::class.java)
            startActivity(intent)
            finish()
        }

        retrieveUserInfo()
        getData()

        findViewById<TextView>(R.id.helpandSupportTextView).setOnClickListener {
            sendEmail()
        }
        findViewById<TextView>(R.id.commentsAndFeedbackTextView).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data =
                    Uri.parse("https://play.google.com/store/apps/details?id=com.valenpatel.notes&pcampaignid=web_share")
            }
            // Check if there's an app to handle this intent
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                // Handle the case where no app can handle the intent
                Toast.makeText(this, "No app found to open this link", Toast.LENGTH_SHORT).show()
            }

        }

        findViewById<TextView>(R.id.privacyPolicyTextView).setOnClickListener {
            val uri = "https://laidbackvalen.github.io/NotesPP/"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(uri))
            startActivity(intent)
        }
        findViewById<TextView>(R.id.termsAndConditionTextView).setOnClickListener {
            val uri = "https://laidbackvalen.github.io/NotesTnC/"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(uri))
            startActivity(intent)
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener {  //this one is generating token
            if (!it.isSuccessful) {
                Log.e("TokenDetails", "Toke didn't recieve!")
            }
            val token = it.result
            Log.e("TOKEN", token)
        }

        drawer = findViewById<DrawerLayout>(R.id.drawerLayout) as DrawerLayout //DRAWERLAYOUT

        findViewById<ImageView>(R.id.imageViewBurgerMenuAllNotes).setOnClickListener {
            openDrawer()
        }
        findViewById<View>(R.id.view).setOnClickListener {
            closeDrawer(drawer)
        }


        findViewById<TextView>(R.id.createNoteTextview).setOnClickListener {  //CreateNote From Drawer
            val intent: Intent = Intent(this, Create_Update_Data_Activity::class.java)
            startActivity(intent)
        }
        findViewById<View>(R.id.topView).setOnClickListener {  //TopView From Drawer
        }
        findViewById<View>(R.id.view5).setOnClickListener {  //TopView From Drawer
        }
        findViewById<TextView>(R.id.notificationTextView).setOnClickListener {  //Notifications From Drawer
            startActivity(Intent(this, Notifications_Activity::class.java))

        }
        findViewById<TextView>(R.id.eventReminderTextView).setOnClickListener {  //Settings From Drawer
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

        findViewById<TextView>(R.id.logoutTextView).setOnClickListener {  //logout using / from Drawer
            val firebaseAuth = FirebaseAuth.getInstance()
            firebaseAuth.signOut()
            startActivity(Intent(this, Login_Activity::class.java))
            finish()
        }
        findViewById<View>(R.id.bottomView).setOnClickListener {
        }

        findViewById<FloatingActionButton>(R.id.imageEdit).setOnClickListener {
            val intent = Intent(this, Create_Update_Data_Activity::class.java)
            startActivity(intent)
        }
// RecyclerView
        var layoutType = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        recyclerView.layoutManager = layoutType


        var isListView = false
        val layoutImage = findViewById<ImageView>(R.id.imageView4)
        layoutImage.setImageDrawable(getDrawable(R.drawable.viewrow))

        layoutImage.setOnClickListener {
            if (isListView) {
                recyclerView.layoutManager =
                    StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
                layoutImage.setImageDrawable(getDrawable(R.drawable.viewrow))
            } else {
                // Switch to ListView
                recyclerView.layoutManager = LinearLayoutManager(this)
                layoutImage.setImageDrawable(getDrawable(R.drawable.grid))
            }
            isListView = !isListView
        }

        searchView = findViewById<SearchView>(R.id.searchViewMain)
        var removeSearch = findViewById<ImageView>(R.id.imageView6)
        setupSearchView(searchView)

        findViewById<View>(R.id.search).setOnClickListener {
            if (searchView.visibility == View.GONE) {
                searchView.visibility = View.VISIBLE
                removeSearch.visibility = View.VISIBLE
            } else if (searchView.visibility == View.VISIBLE) {
                searchView.visibility = View.GONE
                removeSearch.visibility = View.GONE
                searchView.setQuery("", false)
            }
        }
            removeSearch.setOnClickListener {
            searchView.visibility = View.GONE
            removeSearch.visibility = View.GONE
            searchView.setQuery("", false)
        }
    }

    //OUTSIDE ONCREATE
    fun getData() {
        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        val userId = auth.uid.toString()
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val databaseReference = firebaseDatabase.getReference("Notes").child(userId)

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataClassList = mutableListOf<DataClass>()
                var hasNotes = false // Track if there are any notes

                for (itemSnapshot in snapshot.children) {
                    // Filter out the UserInfo node
                    if (itemSnapshot.key != "UserInfo") {
                        val item: DataClass? = itemSnapshot.getValue(DataClass::class.java)
                        item?.let {
                            dataClassList.add(0, it.copy()) // Add item to the start of the list
                            hasNotes = true // Found at least one note
                        }
                    }
                }

                // Update UI based on whether there are notes
                if (hasNotes) {
                    findViewById<LottieAnimationView>(R.id.lottieAnimationviewMainhere).visibility =
                        View.GONE
                    findViewById<TextView>(R.id.noNotesFoundTextView).visibility = View.GONE
                } else {
                    findViewById<LottieAnimationView>(R.id.lottieAnimationviewMainhere).visibility =
                        View.VISIBLE
                    findViewById<TextView>(R.id.noNotesFoundTextView).visibility = View.VISIBLE
                }
                // Update data list and filter
                adapter.dataClassList = dataClassList
                adapter.filterList("") // Reset filter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(baseContext, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun retrieveUserInfo() {
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser?.uid.toString()
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val databaseReference = firebaseDatabase.getReference("Notes").child(user)

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userInfoClass = mutableListOf<UserInfo>()
                for (itemSnapshot in snapshot.children) {
                    // Filter out the UserInfo node
                    if (itemSnapshot.key == "UserInfo") {
                        val item: UserInfo? = itemSnapshot.getValue(UserInfo::class.java)
                        item?.let {
                            userInfoClass.add(it.copy())
                            findViewById<TextView>(R.id.userNameMain).setText("Hey, " + it.username + "!")

                            Glide.with(applicationContext)
                                .asBitmap()
                                .load(it.userProfileImage)
                                .placeholder(R.drawable.baseline_person_24)
                                .into(findViewById<ImageView>(R.id.imageMain))
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun sendEmail() {
        val emailIntent =
            Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "valen.patel1@gmail.com", null))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "This is my subject text")
        startActivity(Intent.createChooser(emailIntent, null))
    }

    fun openDrawer() {
        drawer.openDrawer(GravityCompat.START)
    }

    private fun closeDrawer(drawerLayout: DrawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
    }

    override fun onPause() {
        closeDrawer(drawer)
        super.onPause()
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun setupSearchView(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                handleSearchQuery(newText)
                return true
            }
        })
    }

    private fun handleSearchQuery(query: String?) {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val searchText = query?.lowercase(Locale.getDefault()) ?: ""
        val myRecyclerAdapter = recyclerView.adapter as MyRecyclerAdapter
        myRecyclerAdapter.filterList(searchText)
        myRecyclerAdapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        Log.d("MenuTest", "onCreateOptionsMenu called") // Verify this logs
        menuInflater.inflate(R.menu.bottom_nav_menu, menu)
        Toast.makeText(baseContext, "hey", Toast.LENGTH_SHORT).show()
        return true
    }

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

