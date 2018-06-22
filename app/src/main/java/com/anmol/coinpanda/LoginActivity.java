package com.anmol.coinpanda;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.flags.impl.DataUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "LoginActivity";
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private CallbackManager callbackManager;

    //Data for Google Sign In
    private Button googleSignIn;
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    ProgressBar pgr;
    Animation anim,anim2;
    TypeWriter tw;
    //Data retrieved from social media method of sign in
    RelativeLayout referlayout;
    EditText refercode;
    Button refersubmit;
    Button skip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");
        googleSignIn = (Button) findViewById(R.id.google_sign_in);
        referlayout = (RelativeLayout)findViewById(R.id.referrallayout);
        refersubmit = (Button)findViewById(R.id.submitref);
        refercode = (EditText) findViewById(R.id.referralcode);
        skip = (Button)findViewById(R.id.skip);
        googleSignIn.setVisibility(View.INVISIBLE);
        tw = (TypeWriter)findViewById(R.id.typewriter);
        anim = AnimationUtils.loadAnimation(this,
                R.anim.fade_in);
        anim2 = AnimationUtils.loadAnimation(this,R.anim.fade_out);
        final Transition fade = getWindow().getEnterTransition();
        fade.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {

            }

            @Override
            public void onTransitionEnd(Transition transition) {
                googleSignIn.startAnimation(anim);
                tw.setVisibility(View.INVISIBLE);
                tw.setText("");
                tw.setCharacterDelay(150);
                tw.animateText("Please login to keep CryptoNews at your fingertips.");
                fade.removeListener(this);
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });
        pgr = (ProgressBar)findViewById(R.id.pgr);
        //Instantiate Google Login
        instantiateGoogleLogin();
        refersubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String referalcode = refercode.getText().toString();
                final DatabaseReference  databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot data:dataSnapshot.getChildren()){
                            if(data.getValue(String.class).equals(referalcode)){
                                if(!data.getKey().equals(mAuth.getCurrentUser().getUid())){
                                    final String referrerid = data.getKey();
                                    final Map<String,Object> map = new HashMap<>();
                                    map.put(mAuth.getCurrentUser().getUid(),true);
                                    databaseReference.child("referrers").child(referrerid).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            databaseReference.child("referrers").child(referrerid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    long k = dataSnapshot.getChildrenCount();
                                                    Map<String,Object> map1 = new HashMap<>();
                                                    map1.put("count",k-1);
                                                    databaseReference.child("referrers").child(referrerid).updateChildren(map1)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    Intent intent = new Intent(LoginActivity.this,LoadingActivity.class);
                                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                    startActivity(intent);
                                                                    finish();
                                                                    overridePendingTransition(R.anim.slide_left_in,R.anim.slide_left_out);
                                                                }
                                                            });
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
//                                            databaseReference.child("referrers").child(referrerid).child("count").addListenerForSingleValueEvent(new ValueEventListener() {
//                                                @Override
//                                                public void onDataChange(DataSnapshot dataSnapshot) {
//                                                    if(dataSnapshot.exists()){
//                                                        Integer counter = dataSnapshot.getValue(Integer.class);
//                                                        counter = counter + 1;
//                                                        Map<String,Object> map1 = new HashMap<>();
//                                                        map1.put("count",counter);
//                                                        databaseReference.child("referrers").child(referrerid).updateChildren(map1).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                            @Override
//                                                            public void onComplete(@NonNull Task<Void> task) {
//
//                                                            }
//                                                        });
//                                                    }
//                                                    else{
//                                                        Map<String,Object> map1 = new HashMap<>();
//                                                        map1.put("count",1);
//                                                        databaseReference.child("referrers").child(referrerid).updateChildren(map1).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                            @Override
//                                                            public void onComplete(@NonNull Task<Void> task) {
//                                                                Intent intent = new Intent(LoginActivity.this,LoadingActivity.class);
//                                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                                                startActivity(intent);
//                                                                finish();
//                                                                overridePendingTransition(R.anim.slide_left_in,R.anim.slide_left_out);
//                                                            }
//                                                        });
//                                                    }
//                                                }
//
//                                                @Override
//                                                public void onCancelled(DatabaseError databaseError) {
//
//                                                }
//                                            });
                                        }
                                    });
                                }
                                else{
                                    System.out.println("refererror internal");
                                    Toast.makeText(LoginActivity.this,"Invalid referral code",Toast.LENGTH_SHORT).show();
                                }
                            }


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,LoadingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_left_in,R.anim.slide_left_out);
            }
        });
    }
    private void instantiateGoogleLogin(){



        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
                Toast.makeText(LoginActivity.this,"Please wait while we are authenticating your credentials",Toast.LENGTH_LONG).show();
                pgr.setVisibility(View.VISIBLE);
            }
        });
    }
//    private void instantiateFacebookLogin(){
//
//        callbackManager = CallbackManager.Factory.create();
//
//        facebookLoginButton = (Button) findViewById(R.id.login_button);
//        facebookLoginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startFacebookLogin();
//                Toast.makeText(LoginActivity.this,"Please wait while we are authenticating your credentials",Toast.LENGTH_LONG).show();
//            }
//        });
//
//        // Callback registration
//        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                fetchData(loginResult.getAccessToken());
//                handleFacebookAccessToken(loginResult.getAccessToken());
//            }
//
//            @Override
//            public void onCancel() {
//
//            }
//
//            @Override
//            public void onError(FacebookException exception) {
//
//            }
//        });
//
//
//    }
//
//    private void startFacebookLogin(){
//        LoginManager.getInstance().logInWithReadPermissions(
//                this,
//                Arrays.asList("user_photos", "email", "user_birthday", "public_profile")
//        );
//    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // [START_EXCLUDE]
                // [END_EXCLUDE]
            }
        }
//        else {
//            callbackManager.onActivityResult(requestCode, resultCode, data);
//        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        // [END_EXCLUDE]


        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            pgr.setVisibility(View.GONE);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            pgr.setVisibility(View.GONE);

                        }


                    }
                });
    }
//    private void handleFacebookAccessToken(AccessToken token) {
//        Log.d(TAG, "handleFacebookAccessToken:" + token);
//
//        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithCredential:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//
//                            updateUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            if(task.getException() instanceof FirebaseAuthUserCollisionException) {
//                                Toast.makeText(getApplicationContext(), "User with Email id already exists",
//                                        Toast.LENGTH_SHORT).show();
//                            }
//                            LoginManager.getInstance().logOut();
//                        }
//
//                        // ...
//                    }
//                });
//    }
    private void updateUI(FirebaseUser currentUser){
        if(currentUser!=null)       //Someone is logged in
        {   pgr.setVisibility(View.INVISIBLE);
            googleSignIn.startAnimation(anim2);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    googleSignIn.setVisibility(View.GONE);
                    referlayout.startAnimation(anim);
                }
            },1000);
            Log.i(TAG,"Login was successful in Firebase");
            Log.i(TAG,"UID "+ currentUser.getUid());


        }
        else
        {
            Log.i(TAG,"No user is logged in ");
        }
    }
    private void fetchData(AccessToken accessToken){

        GraphRequest graphRequest = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Log.i(TAG,"JSON Result "+ object.toString());
                Log.i(TAG,"GraphResponse Result "+ object.toString());

                try {
                    String id = String.valueOf(object.get("id"));




                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();
    }




    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAndRemoveTask();
        overridePendingTransition(R.anim.still,R.anim.slide_out_down);
    }
    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(10);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }
}
