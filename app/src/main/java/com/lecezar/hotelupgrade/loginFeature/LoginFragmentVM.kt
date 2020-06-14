package com.lecezar.hotelupgrade.loginFeature

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.lecezar.hotelupgrade.utils.base.BaseViewModel
import com.lecezar.hotelupgrade.utils.binding.CallbackKt

class LoginFragmentVM : BaseViewModel() {
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    fun signInWithEmailAndPassword(auth: FirebaseAuth, callbackKt: CallbackKt<String>.() -> Unit) {
        email.value?.also { email ->
            password.value?.also { password ->
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        CallbackKt(callbackKt, "Logged in!")
                    } else {
                        it.exception?.also { e ->
                            CallbackKt(callbackKt, e)
                        }
                    }
                }
            }
        }
    }
}