package com.example.notes.adapter

import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.dataclass.DataClass
import com.example.notes.R
import com.example.notes.activities.crud.Retriving_All_Data_Related_To_Notes_Activity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.io.Serializable

class MyRecyclerAdapter(private val context: Context, private val dataClassList: List<DataClass>) :
    RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(currentItem: DataClass) {
            itemView.findViewById<TextView>(R.id.titleRetrieveContentRecycle).text =
                currentItem.title
            itemView.findViewById<TextView>(R.id.descriptionRetrieveContentRecycle).text =
                currentItem.description
            itemView.findViewById<TextView>(R.id.dateRetrieveContentRecycle).text = currentItem.date
            itemView.findViewById<TextView>(R.id.timeRetrieveContentRecycle).text = currentItem.time
        }

        val view = itemView.findViewById<View>(R.id.viewContentRecycler)
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
        val currentItem = dataClassList[position]
//        holder.title.text = currentItem.title
//        holder.description.text = currentItem.description
//        holder.date.text = currentItem.date
//        holder.time.text = currentItem.time
        holder.bind(currentItem)

        //sending this data to an activity
        holder.view.setOnClickListener {
            val intent = Intent(context, Retriving_All_Data_Related_To_Notes_Activity::class.java)
            intent.putExtra("value", dataClassList.get(holder.adapterPosition) as Serializable)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
//            Toast.makeText(context, ""+holder.adapterPosition, Toast.LENGTH_SHORT).show()
//            Toast.makeText(context, ""+dataClassList.get(holder.adapterPosition), Toast.LENGTH_SHORT).show()
        }

        holder.deleteImg.setOnClickListener {
            val a = currentItem.key

            val popupMenu = PopupMenu(context, holder.view, Gravity.RIGHT)
            popupMenu.menu.add("Delete")
            popupMenu.setOnMenuItemClickListener { // Toast message on menu item clicked
                Toast.makeText(context, "item deleted successfully", Toast.LENGTH_SHORT).show()
                var firebaseDatabase = FirebaseDatabase.getInstance()
                var databaseReference =
                    firebaseDatabase.getReference("Notes")
                        .child(FirebaseAuth.getInstance().uid.toString()).child(a)
                        .removeValue()
                false
            }
            popupMenu.show()

        }
    }

    override fun getItemCount() = dataClassList.size
}






