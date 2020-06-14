package com.lecezar.hotelupgrade.loginFeature.registerFeature

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.lecezar.hotelupgrade.GlobalVariables
import com.lecezar.hotelupgrade.utils.base.BaseViewModel
import com.lecezar.hotelupgrade.utils.binding.CallbackKt

class RegisterFragmentVM : BaseViewModel() {

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val checkPassword = MutableLiveData<String>()

    fun registerUser(auth: FirebaseAuth, callbackKt: CallbackKt<String>.() -> Unit) {
        email.value?.also { email ->
            password.value?.also { _ ->
                checkPassword.value?.also { checkPassword ->
                    auth.createUserWithEmailAndPassword(email, checkPassword)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                GlobalVariables.currentUserId.set(auth.currentUser?.uid)
                                CallbackKt(callbackKt, "Registered and logged in!")
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
}