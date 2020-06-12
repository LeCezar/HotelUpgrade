package com.lecezar.hotelupgrade.utils.binding

import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ActivityBindingProperty<out T : ViewDataBinding>(
    @LayoutRes private val resId: Int
) : ReadOnlyProperty<AppCompatActivity, T> {

    private var binding: T? = null

    override operator fun getValue(
        thisRef: AppCompatActivity,
        property: KProperty<*>
    ): T = binding ?: createBinding(thisRef).also {
        binding = it }

    private fun createBinding(
        activity: AppCompatActivity
    ): T = DataBindingUtil.setContentView(activity, resId)
}

fun <T : ViewDataBinding> activityBinding(
    @LayoutRes resId: Int
) = ActivityBindingProperty<T>(resId)