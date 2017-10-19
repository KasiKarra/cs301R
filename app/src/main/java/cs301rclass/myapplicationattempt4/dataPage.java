package cs301rclass.myapplicationattempt4;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.util.ArrayList;
import java.util.List;

enum sortingType {ALPHABETICAL, CATEGORICAL, EXPIRATION}

public class dataPage extends AppCompatActivity implements View.OnClickListener {

    private List<Food> foodList = new ArrayList<Food>();
    private boolean googleSignIn = false;
    private GoogleApiClient mGoogleApiClient;
    private FloatingActionButton fab;
    private Button popupSelect;
    private RadioButton A, C, E;  //alphabetical, categorical, expiration
    private PopupWindow mypopup;
    private View customView;
    private sortingType type = sortingType.ALPHABETICAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_page);
        Toolbar tools = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tools);
        populateFoodList();
        populateListView();
        registerClickCallback();

//        googleSignIn = getIntent().getBooleanExtra("usedGoogle", false);

        if (googleSignIn) {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
            mGoogleApiClient.connect();
            super.onStart();
        }

        fab = (FloatingActionButton) findViewById(R.id.add_button);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addItemSwitch();
            }
        });
    }

//        popupSelect = (Button) findViewById(R.id.Select);
//        popupSelect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mypopup.showAtLocation(customView, 0, 0, 0);
//            }
//        });

//        A = (RadioButton) findViewById(R.id.Alphabet);
//        A.setOnClickListener(this);
//        C = (RadioButton) findViewById(R.id.Category);
 //       C.setOnClickListener(this);
//        E = (RadioButton) findViewById(R.id.Expiration);
//        E.setOnClickListener(this);


//        popupSelect = (Button) findViewById(R.id.Select);
//        popupSelect.setOnClickListener(new View.OnClickListener(){
//            @ Override
//            public void onClick(View view){

//                populateFoodList();
//                mypopup.dismiss();
//            }
//        });
//    }


    @Override
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.Alphabet:
                type = sortingType.ALPHABETICAL;
                break;
            case R.id.Category:
                type = sortingType.CATEGORICAL;
                break;
            case R.id.Expiration:
                type = sortingType.EXPIRATION;
                break;
        }
    }


    public void addItemSwitch()
    {
        Intent intent = new Intent(this, AddItemPage.class);
        startActivity(intent);
//        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
//        customView = inflater.inflate(R.layout.fragment_sortingtypes,null);
//        mypopup = new PopupWindow(ActionBar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_general, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

    //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
        //    return true;
        //}
        if(id == R.id.Logoff)
        {
            if(mGoogleApiClient != null) {
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                // ...
                                //Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_SHORT).show();
                                //Intent i=new Intent(getApplicationContext(),Login.class);
                                //startActivity(i);
                            }
                        });
            }
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void populateFoodList() {
        foodList.add(new Food("Banana",5,Color.GREEN,R.drawable.bananas,"10-24-2017",FoodCategory.FRUIT));
        foodList.add(new Food("apples",1,Color.RED,R.drawable.bananas,"10-24-2017",FoodCategory.FRUIT));
        foodList.add(new Food("curry",3,Color.ORANGE,R.drawable.bananas,"10-24-2017",FoodCategory.MEAT));
        foodList.add(new Food("swedish fish",5,Color.GREEN,R.drawable.bananas,"10-24-2017",FoodCategory.SWEET));
        foodList.add(new Food("sweetpotatoes",5,Color.GREEN,R.drawable.bananas,"10-24-2017",FoodCategory.FRUIT));
        foodList.add(new Food("chicken",5,Color.GREEN,R.drawable.bananas,"10-24-2017",FoodCategory.MEAT));
        foodList.add(new Food("mochi",5,Color.GREEN,R.drawable.bananas,"10-24-2017",FoodCategory.SWEET));
        foodList.add(new Food("chicken sandwiches",5,Color.GREEN,R.drawable.bananas,"10-24-2017",FoodCategory.MEAT));
    }

    private void populateListView() {
        ArrayAdapter<Food> adapter = new dataPage.MyListAdapter();
        ListView list = (ListView)findViewById(R.id.foodListView);
        list.setAdapter(adapter);
    }
    private void registerClickCallback() {
        ListView list = (ListView)findViewById(R.id.foodListView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked,int position, long id){
                Food clickedFood = foodList.get(position);
                String message = "you clicked position " + position + " which is the food " + clickedFood.getName();
                Toast.makeText(dataPage.this,message,Toast.LENGTH_LONG).show();
            }
        });
    }
    private class MyListAdapter extends ArrayAdapter<Food>{
        public MyListAdapter(){
            super(dataPage.this,R.layout.item_view,foodList);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            //make sure we have a view to work with(may be given null)
            View itemView = convertView;
            if(itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.item_view,parent,false);
            }
            //find the food
            Food currFood = foodList.get(position);
            //fill the view
            ImageView imageView = (ImageView) itemView.findViewById(R.id.item_icon);
            imageView.setImageResource(currFood.getIconId());
            //name
            TextView counterText = (TextView) itemView.findViewById(R.id.countdown);
            counterText.setTextColor(getCurrFoodColor(currFood));
            counterText.setText(""+currFood.getDaysUntilExp());

            TextView dateText = (TextView) itemView.findViewById(R.id.item_expDate);
            dateText.setText(currFood.getExpDate());

            TextView nameText = (TextView) itemView.findViewById(R.id.item_txtName);
            nameText.setText(currFood.getName());


            return itemView;
        }



        int getCurrFoodColor(Food currFood){
            int color = 0;
            switch(currFood.getColor()){
                case GREEN:
                    color =  -16711936;
                    break;
                case ORANGE:
                    color = android.graphics.Color.rgb(255,165,0);
                    break;
                case RED:
                    color = -65536;
                    break;

            }
            return color;
        }
    }
}
