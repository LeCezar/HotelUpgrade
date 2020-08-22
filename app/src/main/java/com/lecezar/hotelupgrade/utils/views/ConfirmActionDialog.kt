package com.lecezar.hotelupgrade.utils.views

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lecezar.hotelupgrade.R

class ConfirmActionDialog(var text: String, private var confirmCallback: () -> Unit, private var cancelCallback: () -> Unit) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = MaterialAlertDialogBuilder(it)
            builder.setMessage(text)
                .setPositiveButton(R.string.confirm, DialogInterface.OnClickListener { dialogInterface, id ->
                    confirmCallback.invoke()
                    this.dismiss()
                })
                .setNegativeButton(R.string.cancel, DialogInterface.OnClickListener { dialog, id ->
                    cancelCallback.invoke()
                    this.dismiss()
                })

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null for dialog to be created")
    }
}