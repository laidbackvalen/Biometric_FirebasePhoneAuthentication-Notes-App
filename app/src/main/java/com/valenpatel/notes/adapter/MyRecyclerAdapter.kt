package com.valenpatel.notes.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.valenpatel.notes.model.DataClass
import com.valenpatel.notes.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.valenpatel.notes.views.crud.Create_Update_Data_Activity
import java.io.Serializable

class MyRecyclerAdapter(private val context: Context, var dataClassList: MutableList<DataClass>) :
    RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder>() {

//    companion object {
//        var selectedItemIndex = mutableListOf<Int>()
//        var selectedItems = mutableListOf<DataClass>()
//    }

    private var filteredList: MutableList<DataClass> = dataClassList.toMutableList()

    interface OnItemLongClickListener {
        fun onItemLongClick(data: DataClass)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(currentItem: DataClass) {

//            if (currentItem.title.length >= 2) {
//                itemView.findViewById<TextView>(R.id.titleRetrieveContentRecycle).text =
//                    currentItem.title
//            } else {
//                itemView.findViewById<TextView>(R.id.titleRetrieveContentRecycle).visibility =
//                    View.GONE
//            }
            val titleTextView = itemView.findViewById<TextView>(R.id.titleRetrieveContentRecycle)

            // Check title length and set visibility accordingly
            if (currentItem.title.length >= 2) {
                titleTextView.text = currentItem.title
                titleTextView.visibility = View.VISIBLE
            } else {
                titleTextView.visibility = View.GONE
            }
            itemView.findViewById<TextView>(R.id.descriptionRetrieveContentRecycle).text =
                currentItem.description
            itemView.findViewById<TextView>(R.id.dateRetrieveContentRecycle).text = currentItem.date
            itemView.findViewById<TextView>(R.id.timeRetrieveContentRecycle).text = currentItem.time
            var colorHex = currentItem.backgroundColor
            val color = Color.parseColor(colorHex)
            itemView.findViewById<CardView>(R.id.bg).setCardBackgroundColor(color)
        }

        val view = itemView.findViewById<View>(R.id.constraints)
        val checked = itemView.findViewById<View>(R.id.checked)
        val deleteImg = itemView.findViewById<View>(R.id.deleteImageContentRecycler)

//        val title: TextView = itemView.findViewById<TextView>(R.id.titleRetrieveContentRecycle)
//        val description: TextView =  itemView.findViewById<TextView>(R.id.descriptionRetrieveContentRecycle)
//        val date: TextView = itemView.findViewById<TextView>(R.id.dateRetrieveContentRecycle)
//        val time: TextView = itemView.findViewById<TextView>(R.id.timeRetrieveContentRecycle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.note_content_recycler, parent, false)
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //    val currentItem = dataClassList[position]
//        holder.title.text = currentItem.title
//        holder.description.text = currentItem.description
//        holder.date.text = currentItem.date
//        holder.time.text = currentItem.time

//        if (position >= filteredList.size) {
//            return // Prevent accessing invalid index
//        }


        try {
            val currentItem = filteredList[position]
            holder.bind(currentItem)
            // Sending this data to an activity
            holder.view.setOnClickListener {
                val intent = Intent(context, Create_Update_Data_Activity::class.java)
                intent.putExtra("value", currentItem as Serializable)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            }

            // Set margin for the last item
            val params = holder.itemView.layoutParams as RecyclerView.LayoutParams
            params.bottomMargin = if (position == filteredList.size - 1) 250 else 0
            holder.itemView.layoutParams = params

//            holder.view.setOnLongClickListener {
//                if (selectedItemIndex.contains(position)) {
//                    selectedItemIndex.remove(position)
//                    selectedItems.remove(currentItem)
//                    holder.view.setBackgroundColor(Color.TRANSPARENT) // Deselect item
//                    holder.checked.visibility = View.GONE
//                } else {
//                    selectedItemIndex.add(position)
//                    selectedItems.add(currentItem)
//                    holder.checked.visibility = View.VISIBLE
//                    holder.view.setBackgroundColor(ContextCompat.getColor(context, R.color.selected)) // Highlight selected item
//                }
//                true
//            }

            holder.deleteImg.setOnClickListener {
                daleteNote(currentItem.key)
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    //function
    private fun daleteNote(key: String) {
        val a = key
        Toast.makeText(context, "Item deleted successfully", Toast.LENGTH_SHORT).show()
        var firebaseDatabase = FirebaseDatabase.getInstance()
        var databaseReference = firebaseDatabase.getReference("Notes")
            .child(FirebaseAuth.getInstance().uid.toString()).child(a)
            .removeValue()
        // Update the list after deletion
        dataClassList.removeAll { it.key == key }
        filterList("") // Reset filter
        notifyDataSetChanged()
    }

    override fun getItemCount() = filteredList.size


    fun filterList(query: String) {
        filteredList = if (query.isEmpty()) {
            dataClassList.toMutableList() // Show all items if no query
        } else {
            val filtered = dataClassList.filter {
                it.title.contains(query, ignoreCase = true) ||
                        it.description.contains(query, ignoreCase = true) ||
                        it.date.contains(query, ignoreCase = true) ||
                        it.time.contains(query, ignoreCase = true)
            }
            filtered.toMutableList()
        }
        notifyDataSetChanged()
    }

}