package cs301rclass.myapplicationattempt4;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.net.ParseException;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.view.Menu;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static cs301rclass.myapplicationattempt4.R.drawable.dairy;


public class AddItemPage extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        View.OnFocusChangeListener{

    Button addItem;
    Button Cancel;
    EditText Name;
    TextView Title;
    Spinner dropdown;
    Food addition = new Food();
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year,month,day;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("server/saving-data/fireblog");
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_page);

        Name = (EditText)findViewById(R.id.ItemName);
        Name.setOnFocusChangeListener(this);

        Title = (TextView) findViewById(R.id.Title);
        dateView = (EditText) findViewById(R.id.Date);
        dropdown = (Spinner) findViewById(R.id.dropdownMenu);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year,month+1,day);
        dateView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    setDate(v);
                }

            }
        });


        addItem = (Button)findViewById(R.id.SubmitButton);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SanityCheck(Name.getText().toString(), dateView.getText().toString()))
                {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    //add item to firebase then finish activity to return to data page
                    String createdDate = calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.YEAR);
                    String expDate = dateView.getText().toString();
                    int days = getCountOfDays(createdDate,expDate);

                    String foodType = dropdown.getSelectedItem().toString();
                    Food newFood = new Food(Name.getText().toString(),days,getColorType(days),getIcon(foodType),dateView.getText().toString(),getFoodType(foodType));

                    //add to firebase

                    DatabaseReference foodsRef = ref.child(user.getUid());
                    foodsRef.push().setValue(newFood);

                    finish();
                }
                else
                {
                    Title.setText("Error with the information");
                    Title.setTextColor(getResources().getColor(R.color.ErrorCodeRed));
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
        dropdown = (Spinner) findViewById(R.id.dropdownMenu);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.FoodCategoryList, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca",
                Toast.LENGTH_SHORT)
                .show();
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }
    private int getIcon(String foodType)
    {
        switch(foodType){
            case "Dairy":
                return R.drawable.dairy;
            case "Fruit":
                return R.drawable.fruit;
            case "Grain":
                return R.drawable.grain;
            case "Meat":
                return R.drawable.meat;
            case "Sweet":
                return R.drawable.sweet;
            case "Vegetable":
                return R.drawable.veggie;
        }
        return R.drawable.nuts;
    }


    private FoodCategory getFoodType(String foodType)
    {
        switch(foodType){
            case "Dairy":
                return FoodCategory.DAIRY;
            case "Fruit":
                return FoodCategory.FRUIT;
            case "Grain":
                return FoodCategory.GRAIN;
            case "Meat":
                return FoodCategory.MEAT;
            case "Sweet":
                return FoodCategory.SWEET;
            case "Vegetable":
                return FoodCategory.VEGGIE;
        }
        return FoodCategory.DAIRY;
    }
    private Color getColorType(int days)
    {
       if(days < 5)
       {
           return Color.RED;
       }
       else if (days >= 5 && days < 10)
       {
           return Color.ORANGE;
       }
       else if(days >= 10)
       {
           return Color.GREEN;
       }
       return Color.GREEN;
    }
    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }
            };
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
    {
        addition.setCategory((String) parent.getItemAtPosition(pos));
    }

    public void onNothingSelected(AdapterView<?> parent)
    {
        addition.setCategory("Dairy");
    }
    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(month).append("/")
                .append(day).append("/").append(year));
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    //This method will verify that all of the information is in the right format
    //currently doesn't work :)
    public boolean SanityCheck(String name, String date)
    {
        if(name.length() == 0)
            return false;
        if(date.length() == 0)
            return false;

        addition.setName(name);
        addition.setExpDate(date);
        addition.calculateDaysLeft();
        return true;
    }
    @SuppressLint("NewApi")
    public int getCountOfDays(String createdDateString, String expireDateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());

        Date createdConvertedDate = null, expireCovertedDate = null, todayWithZeroTime = null;
        try {
            createdConvertedDate = dateFormat.parse(createdDateString);
            expireCovertedDate = dateFormat.parse(expireDateString);

            Date today = new Date();

            todayWithZeroTime = dateFormat.parse(dateFormat.format(today));
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        int cYear = 0, cMonth = 0, cDay = 0;

        if (createdConvertedDate.after(todayWithZeroTime)) {
            Calendar cCal = Calendar.getInstance();
            cCal.setTime(createdConvertedDate);
            cYear = cCal.get(Calendar.YEAR);
            cMonth = cCal.get(Calendar.MONTH);
            cDay = cCal.get(Calendar.DAY_OF_MONTH);

        } else {
            Calendar cCal = Calendar.getInstance();
            cCal.setTime(todayWithZeroTime);
            cYear = cCal.get(Calendar.YEAR);
            cMonth = cCal.get(Calendar.MONTH);
            cDay = cCal.get(Calendar.DAY_OF_MONTH);
        }


    /*Calendar todayCal = Calendar.getInstance();
    int todayYear = todayCal.get(Calendar.YEAR);
    int today = todayCal.get(Calendar.MONTH);
    int todayDay = todayCal.get(Calendar.DAY_OF_MONTH);
    */

        Calendar eCal = Calendar.getInstance();
        eCal.setTime(expireCovertedDate);

        int eYear = eCal.get(Calendar.YEAR);
        int eMonth = eCal.get(Calendar.MONTH);
        int eDay = eCal.get(Calendar.DAY_OF_MONTH);

        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();

        date1.clear();
        date1.set(cYear, cMonth, cDay);
        date2.clear();
        date2.set(eYear, eMonth, eDay);

        long diff = date2.getTimeInMillis() - date1.getTimeInMillis();

        float dayCount = (float) diff / (24 * 60 * 60 * 1000);

        return (int) dayCount + 1;
    }


        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);

            }

        }
}
