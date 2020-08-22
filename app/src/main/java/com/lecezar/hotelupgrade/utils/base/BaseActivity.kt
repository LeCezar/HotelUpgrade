package com.lecezar.hotelupgrade.utils.base

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.lecezar.hotelupgrade.BR
import com.lecezar.hotelupgrade.utils.binding.activityBinding

abstract class BaseActivity<BINDING : ViewDataBinding, VIEW_MODEL : ViewModel>
constructor(@LayoutRes private val layoutResource: Int) : AppCompatActivity() {

    protected val binding by activityBinding<BINDING>(layoutResource)
    protected abstract val viewModel: VIEW_MODEL

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.also {
            it.lifecycleOwner = this
            it.setVariable(BR.viewModel, viewModel)
        }
        setupViews()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    abstract fun setupViews()
}