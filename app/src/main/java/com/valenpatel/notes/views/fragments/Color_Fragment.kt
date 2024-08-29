package com.valenpatel.notes.views.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.valenpatel.notes.R
import com.valenpatel.notes.adapter.ColorAdapter
import com.valenpatel.notes.model.ColorItem


class Color_Fragment : BottomSheetDialogFragment() {

    interface OnColorSelectedListener {
        fun onColorSelected(color: Int)
    }

    private var colorSelectedListener: OnColorSelectedListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnColorSelectedListener) {
            colorSelectedListener = context
        } else {
            throw RuntimeException("$context must implement OnColorSelectedListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_color, container, false)


        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewForColorSelection)
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val colors = listOf(
            ColorItem(ContextCompat.getColor(requireContext(), R.color.themeDark), "Dark Theme"),
            ColorItem(ContextCompat.getColor(requireContext(), R.color.dark_Red), "Dark Red"),
            ColorItem(ContextCompat.getColor(requireContext(), R.color.dark_Green), "Dark Green"),
            ColorItem(ContextCompat.getColor(requireContext(), R.color.dark_Blue), "Dark Blue"),
            ColorItem(ContextCompat.getColor(requireContext(), R.color.dark_Purple), "Dark Purple"),
            ColorItem(ContextCompat.getColor(requireContext(), R.color.dark_Cyan), "Dark Cyan"),
            ColorItem(ContextCompat.getColor(requireContext(), R.color.dark_Orange), "Dark Orange"),
            ColorItem(ContextCompat.getColor(requireContext(), R.color.dark_Brown), "Dark Brown"),
            ColorItem(ContextCompat.getColor(requireContext(), R.color.dark_Gray), "Dark Gray"),
            )

        val adapter = ColorAdapter(colors, colorSelectedListener!!)
        recyclerView.adapter = adapter

        return view
    }

    override fun onDetach() {
        super.onDetach()
        colorSelectedListener = null
    }
}
