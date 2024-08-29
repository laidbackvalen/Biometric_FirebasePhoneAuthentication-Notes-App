package com.valenpatel.notes.views.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.valenpatel.notes.R


class AddResource_Fragment : BottomSheetDialogFragment() {
    interface OnBottomSheetItemClickListener {
        fun onItemSelected()
    }

    private var listener: OnBottomSheetItemClickListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_resource, container, false)

        // Assuming you have a button or view for item selection
        view.findViewById<View>(R.id.addimageViewBottomSheet).setOnClickListener {
            listener?.onItemSelected()
            dismiss() // Optionally dismiss the bottom sheet after selection
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