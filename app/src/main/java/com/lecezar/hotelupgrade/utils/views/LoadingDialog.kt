package com.lecezar.hotelupgrade.utils.views

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import com.lecezar.hotelupgrade.databinding.LoadingDialogBinding

class LoadingDialog(context: Context) : Dialog(context) {
    private val binding = LoadingDialogBinding.inflate(LayoutInflater.from(context))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window?.decorView?.setBackgroundColor(Color.TRANSPARENT)
        setCancelable(false)
    }
}
