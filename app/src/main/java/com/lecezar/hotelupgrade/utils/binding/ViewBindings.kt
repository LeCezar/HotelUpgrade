package com.lecezar.hotelupgrade.utils.binding

import android.view.View
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("visibility")
fun View.visibility(visible: Boolean?) {
    this.visibility = if (visible == true) View.VISIBLE else View.GONE
}

@BindingAdapter("android:visibility")
fun setVisibility(view: View, value: Boolean) {
    view.visibility = if (value) View.VISIBLE else View.GONE
}

@BindingAdapter("validateRoomNumber")
fun TextInputLayout.roomNumberValidator(value: String?) {
    if (value != null && value.isBlank()) {
        this.error = "Number required"
        this.isErrorEnabled = true
    } else {
        isErrorEnabled = false
    }
}

@BindingAdapter("errorText")
fun TextInputLayout.errorText(value: String?) {
    if (value != null) {
        this.error = "value"
    }
}

//@BindingAdapter("validateEndNumberFromStartNumber")
//fun TextInputLayout.validateEndNumberFromStartNumber(endNumber: String?) {
//    if (endNumber != null && (endNumber.isBlank() || (this.children.first() as TextInputEditText).text.toString().toInt() > endNumber.toInt())) {
//        this.error = "Too big"
//        this.isErrorEnabled = true
//    } else {
//        isErrorEnabled = false
//    }
//}
//
//@BindingAdapter("validateStartNumberFromEndNumber")
//fun TextInputLayout.validateStartNumberFromEndNumber(startNumber: String?) {
//    if (startNumber != null && (startNumber.isBlank() || (this.children.first() as TextInputEditText).text.toString().toInt() < startNumber.toInt())) {
//        this.error = "Too small"
//        this.isErrorEnabled = true
//    } else {
//        isErrorEnabled = false
//    }
//}