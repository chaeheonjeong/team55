package com.example.project

import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText

class CustomDialog(context: Context) {
        private val dialog = Dialog(context)

    fun myDig() {
        dialog.setContentView(R.layout.custom_dialog)

        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(true)

        val edit = dialog.findViewById<EditText>(R.id.editTextName)
        val btnOK = dialog.findViewById<Button>(R.id.buttonOk)
        val btnCancel = dialog.findViewById<Button>(R.id.buttonCancel)

        btnOK.setOnClickListener {
            onClickListener.onClicked(edit.text.toString())
            dialog.dismiss()
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    interface ButtonClickListener {
        fun onClicked(myName: String)
    }

    private lateinit var onClickListener: ButtonClickListener

    fun setOnClickListener(listener: ButtonClickListener) {
        onClickListener = listener
    }
}