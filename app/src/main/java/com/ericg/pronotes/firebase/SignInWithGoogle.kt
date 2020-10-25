package com.ericg.pronotes.firebase

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.ericg.pronotes.R
import com.ericg.pronotes.constants.Constants.RC_SIGN_IN
import com.ericg.pronotes.extentions.Extensions.toast
import com.ericg.pronotes.firebase.Utils.firebaseUser
import com.ericg.pronotes.model.PrefsBooleanType
import com.ericg.pronotes.model.PrefsDataStoreBooleans
import com.ericg.pronotes.view.ParentActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * @author eric
 * @date 10/2/20
 */

class SignInWithGoogle : AppCompatActivity() {
    lateinit var gso: GoogleSignInOptions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        laySignInBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_from_btm))
        btnSignInWithGoogle.setOnClickListener {
            signIn()
        }
        initValues()
    }

    private fun initValues() {
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).apply {
            requestIdToken(getString(R.string.OAuth_WebType_client_ID))
            requestEmail()
        }.build()

    }

    private fun signIn() {
        val signInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = signInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: Exception) {
                toast("Failed with exception \n$e")
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        Firebase.auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                firebaseUser?.sendEmailVerification()

                GlobalScope.launch(Dispatchers.IO) {
                    PrefsDataStoreBooleans(this@SignInWithGoogle).setPrefsBoolean(
                        PrefsBooleanType.AUTO_SIGN_IN, true
                    )
                }
                toast("signed in successfully")
                startActivity(Intent(this, ParentActivity::class.java))
                finish()
            } else toast("sign in failed")
        }
    }

    override fun onStart() {
        super.onStart()

        if (firebaseUser != null) {
            /*refresh app */
            startActivity(Intent(this, ParentActivity::class.java))
        } else toast("sign in")
    }

    var canGoBack = false

    @Suppress("DEPRECATION")
    override fun onBackPressed() {
        if (canGoBack) {
            super.onBackPressed()
        } else {
            canGoBack = true
            toast("press again to exit")
            Handler().postDelayed({ canGoBack = false }, 2000)
        }
    }
}