package com.valenpatel.notes.views.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.valenpatel.notes.R

class More_Fragment : BottomSheetDialogFragment() {

    interface OnBottomSheetItemClickListener {
        fun onDelete()
        fun onCopy()
        fun onShare()
    }

    private var listener: OnBottomSheetItemClickListener? = null

    // Method to set the listener from the host activity or fragment
    fun setOnBottomSheetItemClickListener(listener: OnBottomSheetItemClickListener) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_more, container, false)

        // Initialize the views and set click listeners
        view.findViewById<View>(R.id.deleteViewBottomSheet).setOnClickListener {
            listener?.onDelete()
            dismiss() // Close the bottom sheet after action
        }
        view.findViewById<View>(R.id.makeACopyViewBottomSheet).setOnClickListener {
            listener?.onCopy()
            dismiss() // Close the bottom sheet after action
        }
        view.findViewById<View>(R.id.shareViewBottomSheet).setOnClickListener {
            listener?.onShare()
            dismiss() // Close the bottom sheet after action
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Ensure that the activity implements the listener interface
        listener = context as? OnBottomSheetItemClickListener
        if (listener == null) {
            throw ClassCastException("$context must implement OnBottomSheetItemClickListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}
