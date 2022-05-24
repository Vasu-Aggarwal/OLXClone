package com.example.olx.ui.login

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.olx.BaseActivity
import com.example.olx.MainActivity
import com.example.olx.R
import com.example.olx.utilities.Constants
import com.example.olx.utilities.SharedPref
//import com.facebook.*
//import com.facebook.login.LoginManager
//import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*


class LoginActivity : BaseActivity(), View.OnClickListener{

//    private var callbackManager: CallbackManager? = null
    private val RC_SIGN_IN: Int = 123
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private val EMAIL = "email"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))      //this is not causing any error just ignore it
            .requestEmail()
            .build()

        auth = Firebase.auth
        googleSignInClient = GoogleSignIn.getClient(this, gso)
//        ll_google.setOnClickListener { signIn() }
        signIn()
//        FacebookSdk.sdkInitialize(getApplicationContext())

//        login_button.setReadPermissions(Arrays.asList(EMAIL))
//        callbackManager = CallbackManager.Factory.create()

//        registerFbCallback()
    }

//    private fun registerFbCallback() {
//        LoginManager.getInstance().registerCallback(callbackManager,
//            object: FacebookCallback<LoginResult> {
//                override fun onSuccess(loginResult: LoginResult) {
//                    // App code
//                    handleFacebookAccess(loginResult.accessToken)
//                }
//
//                override fun onCancel() {
//                    // App code
//                }
//
//                override fun onError(exception: FacebookException) {
//                    // App code
//                }
//            })
//    }

//    private fun handleFacebookAccess(accessToken: AccessToken) {
//        val credential = FacebookAuthProvider.getCredential(accessToken.token)
//        auth.signInWithCredential(credential)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    // Sign in success, update UI with the signed-in user's information
//                    Log.d(TAG, "signInWithCredential:success")
//                    val user = auth.currentUser
//                    Toast.makeText(this, user?.displayName, Toast.LENGTH_SHORT).show()
//                    updateUI(user)
//                } else {
//                    // If sign in fails, display a message to the user.
//                    Log.w(TAG, "signInWithCredential:failure", task.exception)
//                    Toast.makeText(baseContext, "Authentication failed.",
//                        Toast.LENGTH_SHORT).show()
//                    updateUI(null)
//                }
//            }
//    }

    override fun onClick(v: View?) {
        when(v?.id){

            R.id.ll_google->{
                ll_google.performClick()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if(currentUser != null){
            val main = Intent(this, MainActivity::class.java)
            startActivity(main)
            finish()
//            Toast.makeText(this, "Logged in from "+currentUser.email, Toast.LENGTH_SHORT).show()
        }
        else{
            ll_google.visibility = View.VISIBLE
            prgbar.visibility = View.GONE
        }
    }


    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
//        else{
//            callbackManager?.onActivityResult(requestCode, resultCode, data);
//        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            // Google Sign In was successful, authenticate with Firebase
            val account = completedTask.getResult(ApiException::class.java)!!
            Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
            firebaseAuthWithGoogle(account)
        } catch (e: ApiException) {
            // Google Sign In failed, update UI appropriately
            Log.w(TAG, "Google sign in failed", e)
        }
    }

    private fun firebaseAuthWithGoogle(acc: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acc.idToken, null)
        ll_google.visibility = View.GONE
        prgbar.visibility = View.VISIBLE

        GlobalScope.launch(Dispatchers.IO) {
            val auth = auth.signInWithCredential(credential).await()
            val firebaseUser = auth.user
            withContext(Dispatchers.Main) {
                if(acc.email!=null){
                    SharedPref(this@LoginActivity).setString(Constants.USER_EMAIL, acc.email!!)
                }
                if(acc.id!=null){
                    SharedPref(this@LoginActivity).setString(Constants.USER_ID, acc.id!!)
                }
                if(acc.displayName!=null){
                    SharedPref(this@LoginActivity).setString(Constants.USER_NAME, acc.displayName!!)
                }
                if(acc.photoUrl!=null){
                    SharedPref(this@LoginActivity).setString(Constants.USER_PHOTO, acc.photoUrl.toString())
                }
                updateUI(firebaseUser)
            }
        }
    }

}