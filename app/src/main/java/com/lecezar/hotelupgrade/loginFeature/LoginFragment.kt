package com.lecezar.hotelupgrade.loginFeature

import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lecezar.hotelupgrade.GlobalVariables
import com.lecezar.hotelupgrade.MainActivity
import com.lecezar.hotelupgrade.R
import com.lecezar.hotelupgrade.databinding.LoginFragmentBinding
import com.lecezar.hotelupgrade.utils.base.BaseFragment
import com.lecezar.hotelupgrade.utils.makeToast
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment<LoginFragmentBinding, LoginFragmentVM>(R.layout.fragment_login) {
    override val viewModel: LoginFragmentVM by viewModel()

    override fun setupViews() {
        sign_in_button_email.setOnClickListener {
            signInWithEmail()
        }
        sing_in_button.setOnClickListener {
            signInGoogle()
        }
        login_fragment_register_button.setOnClickListener {
            view?.findNavController()?.navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }
    }

    private fun getGoogleSignInClient(): GoogleSignInClient = GoogleSignIn.getClient(
        this.requireActivity(), GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    )

    private fun signInGoogle() {
        val signInIntent = getGoogleSignInClient().signInIntent
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            GoogleSignIn.getSignedInAccountFromIntent(it.data).apply {
                if (this.isSuccessful) {
                    try {
                        handleSignInResult(this.getResult(ApiException::class.java))
                    } catch (e: ApiException) {
                        e.printStackTrace()
                    }
                }
            }
        }.launch(signInIntent)
    }

    private fun signInWithEmail() {
        viewModel.signInWithEmailAndPassword(Firebase.auth) {
            onSuccess = {
                GlobalVariables.currentUserId.set(Firebase.auth.currentUser?.uid)
                makeToast(this@LoginFragment.requireContext(), it)
                view?.findNavController()?.navigate(LoginFragmentDirections.actionLoginFragmentToChooseHotelFragment())
            }
            onFailure = {
                makeToast(this@LoginFragment.requireContext(), it.message ?: it.localizedMessage ?: "Failed register!")
            }
        }
    }


    private fun handleSignInResult(account: GoogleSignInAccount?) {
        Firebase.auth.signInWithCredential(GoogleAuthProvider.getCredential(account!!.idToken, null))
            .addOnCompleteListener(this.requireActivity()) { task ->
                if (task.isSuccessful) {
                    GlobalVariables.currentUserId.set(Firebase.auth.currentUser?.uid)
                    view?.findNavController()?.navigate(LoginFragmentDirections.actionLoginFragmentToChooseHotelFragment())
                } else {
                    Toast.makeText(this.context, "Failed Logging in", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity).hideBottomNavigation()
    }

}