package cs301rclass.myapplicationattempt4;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
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
             else
                 note.setText("Try Again");
          }
         });
    }

    public void switchActivities()
    {
        Intent intent = new Intent(this, dataPage.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
