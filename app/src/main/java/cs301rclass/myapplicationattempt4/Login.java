package cs301rclass.myapplicationattempt4;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener
{
    private static final int RC_SIGN_IN = 9001;

    Button loginBtn;
    EditText username;
    EditText password;
    TextView note;
    GoogleApiClient mGoogleApiClient;
    Button GoogleLoginBtn;
    Button RegisterBtn;
    int count = 0;
    int clicked = -1;
    Button ExtraBtn;
    Button Cancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtn = (Button) findViewById(R.id.login);
        loginBtn.setOnClickListener(this);

        RegisterBtn = (Button) findViewById(R.id.Registration);
        RegisterBtn.setOnClickListener(this);

        username  = (EditText) findViewById(R.id.email);
        password  = (EditText) findViewById(R.id.password);

        note =  (TextView) findViewById(R.id.note);

    // Configure sign-in to request the user's ID, email address, and basic
    // profile. ID and basic profile are included in DEFAULT_SIGN_IN
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();
    // Build a GoogleApiClient with access to the Google Sign-In API and the
    // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        GoogleLoginBtn = (Button) findViewById(R.id.GoogleSignIn);
        GoogleLoginBtn.setOnClickListener(this);

        Cancel = (Button) findViewById(R.id.Cancel);
        Cancel.setOnClickListener(this);
        ExtraBtn = (Button) findViewById(R.id.ExtraBtn);
        ExtraBtn.setOnClickListener(this);
    }

    public void switchActivities(boolean success)
    {
        password.setText("");

        if(success) {
            //clear all fields
            note.setText("");
            username.setText("");
            //move to the dataPage
            Intent intent = new Intent(this, dataPage.class);
            startActivity(intent);
        }
        else
            note.setText("Incorrect Username or password");
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult){
        note.setText("Please check your internet connection and try again.");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
         GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
         switchActivities(result.isSuccess());
        }
     }

     public void onClick(View v)
     {
         //clear error text
         note.setText("");
         switch(v.getId())
         {
             case R.id.login:
                 ExtraBtn.setText("Login");
                setUpInvisibleButtons();
                 break;

             case R.id.GoogleSignIn:
                 Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                 startActivityForResult(signInIntent, RC_SIGN_IN);
                 break;

             case R.id.Registration:
                 ExtraBtn.setText("Create Account");
                 setUpInvisibleButtons();
                 break;

             case R.id.Cancel:
                resetInvisibleButtons();
                 break;

             case R.id.ExtraBtn:
                 if(ExtraBtn.getText().length() == 5) //If it says login
                    switchActivities(username.getText().toString().equals("admin") && password.getText().toString().equals("admin"));
                 else
                 {
                     if(username.getText().length() != 0 && password.getText().length() != 0) {
                         //add it to the database.
                         resetInvisibleButtons();
                         switchActivities(true);
                     }
                     else
                         note.setText("Please enter in a username AND password");
                 }
         }
     }

     public void setUpInvisibleButtons()
     {
         //turn on login information or create account information
         ExtraBtn.setVisibility(View.VISIBLE);
         Cancel.setVisibility(View.VISIBLE);
         username.setVisibility(View.VISIBLE);
         password.setVisibility(View.VISIBLE);

         //Turn off the other buttons
         loginBtn.setVisibility(View.INVISIBLE);
         GoogleLoginBtn.setVisibility(View.INVISIBLE);
         RegisterBtn.setVisibility(View.INVISIBLE);

     }

     public void resetInvisibleButtons()
     {
         //turn on big buttons
         ExtraBtn.setVisibility(View.INVISIBLE);
         Cancel.setVisibility(View.INVISIBLE);
         username.setVisibility(View.INVISIBLE);
         password.setVisibility(View.INVISIBLE);
         password.setText("");
         username.setText("");

         //turn off everything else
         loginBtn.setVisibility(View.VISIBLE);
         GoogleLoginBtn.setVisibility(View.VISIBLE);
         RegisterBtn.setVisibility(View.VISIBLE);
     }

}
