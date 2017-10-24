package cs301rclass.myapplicationattempt4;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener
{
    private static final int RC_SIGN_IN = 9001;

    Button loginBtn;
    EditText username;
    EditText password;
    TextView note;
    Button GoogleLoginBtn;
    Button RegisterBtn;
    int count = 0;
    int clicked = -1;
    Button ExtraBtn;
    Button Cancel;
  //  FirebaseAuth fba;
  //  FirebaseUser user = null;
  //  FirebaseAuth.AuthStateListener fbalistener;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

       // fba = FirebaseAuth.getInstance();

        loginBtn = (Button) findViewById(R.id.login);
        loginBtn.setOnClickListener(this);

        RegisterBtn = (Button) findViewById(R.id.Registration);
        RegisterBtn.setOnClickListener(this);

        username  = (EditText) findViewById(R.id.email);
        password  = (EditText) findViewById(R.id.password);

        note =  (TextView) findViewById(R.id.note);

        GoogleLoginBtn = (Button) findViewById(R.id.GoogleSignIn);

        Cancel = (Button) findViewById(R.id.Cancel);
        Cancel.setOnClickListener(this);
        ExtraBtn = (Button) findViewById(R.id.ExtraBtn);
        ExtraBtn.setOnClickListener(this);
    }

    public void switchActivities(boolean success, boolean usedGoogle)
    {
        password.setText("");

        if(success) {
            //clear all fields
            note.setText("");
            username.setText("");
            //move to the dataPage
            resetInvisibleButtons();
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

             case R.id.Registration:
                 ExtraBtn.setText("Create Account");
                 setUpInvisibleButtons();
                 break;

             case R.id.Cancel:
                resetInvisibleButtons();
                 break;

             case R.id.ExtraBtn:
                 if(ExtraBtn.getText().length() == 5) //If it says login
                 {
                     switchActivities(true,false);
/*
                     fba.signInWithEmailAndPassword(username.getText().toString(), password.getText().toString())
                             .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                 @Override
                                 public void onComplete(@NonNull Task<AuthResult> task) {
                                     if (task.isSuccessful()) {
                                         // Sign in success, update UI with the signed-in user's information
                                         user = fba.getCurrentUser();
                                     } else {
                                         // If sign in fails, display a message to the user.
                                         note.setText("Username or Password was incorrect");
                                     }
                                 }
                             });
*/
                 }
                 else
                 {
                     if(username.getText().length() != 0 && password.getText().length() != 0) {
                         //add it to the database.
                        createAccount(username.getText().toString(), password.getText().toString());
                         if(note.getText().length() == 0) //if there was no error with user registration, log in
                         {
                             resetInvisibleButtons();
                             switchActivities(true, false);
                         }
                     }
                     else
                         note.setText("Please enter in a username AND password");
                 }
         }
     }

    private void createAccount(String email, String password) {
        // [START create_user_with_email]
/*
        fba.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user = fba.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            note.setText("Registration Failed");
                        }
                    }
                });
*/
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

         InputMethodManager inputManager = (InputMethodManager)
                 getSystemService(Context.INPUT_METHOD_SERVICE);

         inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                 InputMethodManager.HIDE_NOT_ALWAYS);

     }

}
