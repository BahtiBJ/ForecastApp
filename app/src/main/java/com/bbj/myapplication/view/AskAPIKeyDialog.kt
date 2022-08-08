package com.bbj.myapplication.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.bbj.myapplication.R

class AskAPIKeyDialog : DialogFragment() {

    lateinit var listener : EnterClickInterface

    interface EnterClickInterface {
        fun enter(apiKey : String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as EnterClickInterface
        } catch (e: ClassCastException) {
            throw ClassCastException((context.toString() +
                    " must implement NoticeDialogListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let{
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_ask_api,null)
            val inputKey = view.findViewById<EditText>(R.id.input_key)
            builder.setView(view)
                .setPositiveButton("Ввести"){_,_ ->
                    listener.enter(inputKey.text.toString())
                }
                .setNegativeButton("Отмена"){_,_ -> }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}