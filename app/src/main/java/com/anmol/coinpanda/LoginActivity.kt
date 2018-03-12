package com.anmol.coinpanda

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast

import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.GraphResponse
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

import org.json.JSONException
import org.json.JSONObject

import java.util.Arrays

class LoginActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {
    private val mAuth = FirebaseAuth.getInstance()
    private var callbackManager: CallbackManager? = null
    private var facebookLoginButton: Button? = null

    //Data for Google Sign In
    private var googleSignIn: Button? = null
    private var mGoogleApiClient: GoogleApiClient? = null


    //Data retrieved from social media method of sign in
    private var profilePicturePath: String? = null
    private var username: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        title = "Login"

        //Instantiate Google Login
        instantiateGoogleLogin()

    }

    private fun instantiateGoogleLogin() {

        googleSignIn = findViewById<View>(R.id.google_sign_in) as Button

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        mGoogleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()

        googleSignIn!!.setOnClickListener {
            val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
            startActivityForResult(signInIntent, RC_SIGN_IN)
            Toast.makeText(this@LoginActivity, "Please wait while we are authenticating your credentials", Toast.LENGTH_LONG).show()
        }
    }

    private fun instantiateFacebookLogin() {

        callbackManager = CallbackManager.Factory.create()
        facebookLoginButton!!.setOnClickListener {
            startFacebookLogin()
            Toast.makeText(this@LoginActivity, "Please wait while we are authenticating your credentials", Toast.LENGTH_LONG).show()
        }

        // Callback registration
        LoginManager.getInstance().registerCallback(callbackManager!!, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                fetchData(loginResult.accessToken)
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {

            }

            override fun onError(exception: FacebookException) {

            }
        })


    }

    private fun startFacebookLogin() {
        LoginManager.getInstance().logInWithReadPermissions(
                this,
                Arrays.asList("user_photos", "email", "user_birthday", "public_profile")
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                // Google Sign In was successful, authenticate with Firebase
                val account = result.signInAccount
                firebaseAuthWithGoogle(account)
            } else {
                // Google Sign In failed, update UI appropriately
                // [START_EXCLUDE]
                // [END_EXCLUDE]
            }
        } else {
            callbackManager!!.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount?) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct!!.id!!)
        // [START_EXCLUDE silent]
        // [END_EXCLUDE]

        profilePicturePath = acct.photoUrl.toString()
        username = acct.displayName

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success")
                        val user = mAuth.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                        Toast.makeText(this@LoginActivity, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()

                    }
                }
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(TAG, "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success")
                        val user = mAuth.currentUser

                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                        if (task.exception is FirebaseAuthUserCollisionException) {
                            Toast.makeText(applicationContext, "User with Email id already exists",
                                    Toast.LENGTH_SHORT).show()
                        }
                        LoginManager.getInstance().logOut()
                    }

                    // ...
                }
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null)
        //Someone is logged in
        {
            Log.i(TAG, "Login was successful in Firebase")
            Log.i(TAG, "UID " + currentUser.uid)

            val intent = Intent(this, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out)

        } else {
            Log.i(TAG, "No user is logged in ")
        }
    }

    private fun fetchData(accessToken: AccessToken) {

        val graphRequest = GraphRequest.newMeRequest(accessToken) { `object`, response ->
            Log.i(TAG, "JSON Result " + `object`.toString())
            Log.i(TAG, "GraphResponse Result " + `object`.toString())

            try {
                val id = `object`.get("id").toString()

                profilePicturePath = "http://graph.facebook.com/$id/picture?type=large"
                username = `object`.get("name").toString()


            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        val parameters = Bundle()
        parameters.putString("fields", "id,name,link")
        graphRequest.parameters = parameters
        graphRequest.executeAsync()
    }


    override fun onConnectionFailed(connectionResult: ConnectionResult) {

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.still, R.anim.slide_out_down)
    }

    companion object {
        private val TAG = "LoginActivity"
        private val RC_SIGN_IN = 9001
    }
}
