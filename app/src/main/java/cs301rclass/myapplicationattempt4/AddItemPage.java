package cs301rclass.myapplicationattempt4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class AddItemPage extends AppCompatActivity {

    Button addItem;
    Button Cancel;
    EditText Name;
    EditText Date;
    TextView Title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_page);

        Name = (EditText)findViewById(R.id.ItemName);
        Date = (EditText) findViewById(R.id.Date);
        Title = (TextView) findViewById(R.id.Title);

        addItem = (Button)findViewById(R.id.SubmitButton);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SanityCheck(Name.getText().toString(), Date.getText().toString()))
                {
                    //add item to firebase then finish activity to return to data page
                    finish();
                }
                else
                {
                    Title.setText("Error with the information");
                }
            }
        });

        Cancel = (Button) findViewById(R.id.Cancel);
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    //This method will verify that all of the information is in the right format
    //currently doesn't work :)
    public boolean SanityCheck(String name, String date)
    {
//        if(date.length() == 0)
//            return false;
//        try
//        {
//            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
//            df.setLenient(false);
//            df.parse(date);
//        }
//        catch(Exception e)
//        {
//            return false;
//        }
//
//        if(name.length() == 0)
//            return false;
//
//        if(name.equals("Enter Name of Product Here"))
//            return false;

        //or can add item to fire base here
        return true;
    }
}
