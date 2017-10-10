package cs301rclass.myapplicationattempt4;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity {


    Button login;
    EditText username;
    EditText password;
    TextView note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login  = (Button) findViewById(R.id.login);

        username  = (EditText) findViewById(R.id.email);
        password  = (EditText) findViewById(R.id.password);

        note =  (TextView) findViewById(R.id.note);


        login.setOnClickListener(new View.OnClickListener(){
         public void onClick(View v)
          {
             if(username.getText().toString().equals("admin") && password.getText().toString().equals("admin"))
                 switchActivities();
                 //note.setText("awesome");
             else {
                 note.setText("Incorrect Username or Password");
                 password.setText("");
             }
          }
         });
    }

    public void switchActivities()
    {
        //clear all fields
        note.setText("");
        username.setText("");
        password.setText("");

        //move to the dataPage
        Intent intent = new Intent(this, dataPage.class);
        startActivity(intent);
    }



}
