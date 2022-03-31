package com.guptshabd;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shabdamsdk.ShabdamSplashActivity;
import com.shabdamsdk.ToastUtils;

public class UserDetailActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final int RC_SIGN_IN = 1;
    private TextView tv_google_sign_in;
    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        inIt();

        googleSignIn();


    }

    private void inIt() {
        tv_google_sign_in = findViewById(R.id.tv_google_sign_in);

        tv_google_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserDetailActivity.this != null) {
                    if (ToastUtils.checkInternetConnection(UserDetailActivity.this)) {
                        googleSignInFunctionality();

                    } else {
                        Toast.makeText(UserDetailActivity.this, getString(com.shabdamsdk.R.string.ensure_internet), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void googleSignInFunctionality() {
        try {
            if (googleApiClient != null) {
                if (!googleApiClient.isConnected()) {
                    googleApiClient.connect();
                }
                if (googleApiClient.isConnected()) {
                    Auth.GoogleSignInApi.signOut(googleApiClient);
                    Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                    startActivityForResult(intent, RC_SIGN_IN);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void googleSignIn() {
        try {
            if (gso == null)
                gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();
            if (googleApiClient == null && UserDetailActivity.this != null) {
                googleApiClient = new GoogleApiClient.Builder(this)
                        .enableAutoManage(this, this)
                        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                        .build();
                googleApiClient.connect();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (googleApiClient!=null && UserDetailActivity.this!=null){
            googleApiClient.stopAutoManage(this);
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }

    }


  /*  private void gotoMainActivity() {
        Intent intent = new Intent(UserDetailActivity.this, ShabdamSplashActivity.class);
        intent.putExtra("user_id", etId.getText().toString());
        intent.putExtra("name",etUserName.getText().toString());
        intent.putExtra("uname","vikash");
        intent.putExtra("email","vikash@mailinator.com");
        intent.putExtra("profile_image","");
        startActivity(intent);
        finish();
    }*/

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            firebaseAuthWithGoogle(result);
        }

       /* // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }*/
    }

    /*private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            Toast.makeText(this, ""+account, Toast.LENGTH_SHORT).show();
            //updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            //Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }
*/
    private void firebaseAuthWithGoogle(GoogleSignInResult result) {
        Toast.makeText(this, ""+result, Toast.LENGTH_SHORT).show();

        if (mAuth != null) {
            if (mAuth.getCurrentUser() == null) {
                mAuth.signInAnonymously().addOnCompleteListener(task -> {
                    Toast.makeText(this, ""+result, Toast.LENGTH_SHORT).show();

                    if (mAuth.getCurrentUser() != null) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        //loginPresenter.handleGoogleResult(result, user.getUid());
                        //  Toast.makeText(getActivity(), "" + user.getUid(), Toast.LENGTH_SHORT).show();
                    } else {
                        //loginPresenter.handleGoogleResult(result, "");
                    }
                });
            } else {
                FirebaseUser user = mAuth.getCurrentUser();
                //loginPresenter.handleGoogleResult(result, user.getUid());
            }
        }
    }
}