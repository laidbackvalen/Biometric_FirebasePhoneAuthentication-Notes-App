package com.valenpatel.notes.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.valenpatel.notes.R
import com.valenpatel.notes.model.ColorItem
import com.valenpatel.notes.views.fragments.Color_Fragment

class ColorAdapter(
    private val colors: List<ColorItem>,
    private val listener: Color_Fragment.OnColorSelectedListener
) : RecyclerView.Adapter<ColorAdapter.ColorViewHolder>() {

    inner class ColorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val colorView: CardView = itemView.findViewById(R.id.colorListCard)

        fun bind(colorItem: ColorItem) {
            colorView.setCardBackgroundColor(colorItem.color)
            itemView.setOnClickListener {
                listener.onColorSelected(colorItem.color)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.select_color_from_list_layout, parent, false)
        return ColorViewHolder(view)
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        holder.bind(colors[position])
    }

    override fun getItemCount(): Int = colors.size
}

