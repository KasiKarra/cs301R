package cs301rclass.myapplicationattempt4;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;

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
    private sortingType type = sortingType.ALPHABETICAL;
    private FirebaseAuth auth;
//    private FirebaseAuth fba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_page);
        Toolbar tools = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tools);
        populateFoodList();
        populateListView();
        registerClickCallback();
        auth = FirebaseAuth.getInstance();
        fab = (FloatingActionButton) findViewById(R.id.add_button);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addItemSwitch();
            }
        });

  //      fba = FirebaseAuth.getInstance();
  //      FirebaseUser user = fba.getCurrentUser();
        //UID = user.getUid();

    }

    @Override
    public void onClick(View view)
    {
        A.setChecked(false);
        C.setChecked(false);
        E.setChecked(false);

        switch(view.getId())
        {
            case R.id.Alphabet:
                type = sortingType.ALPHABETICAL;
                A.setChecked(true);
                break;
            case R.id.Category:
                type = sortingType.CATEGORICAL;
                C.setChecked(true);
                break;
            case R.id.Expiration:
                E.setChecked(true);
                type = sortingType.EXPIRATION;
                break;
            case R.id.Select:
                mypopup.dismiss();
                break;
        }
    }


    public void addItemSwitch()
    {
        Intent intent = new Intent(this, AddItemPage.class);
        startActivity(intent);
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
            auth.signOut();
            Intent intent = new Intent(this,Login.class);
            startActivity(intent);
        }
        if(id == R.id.Sort)
            ShowPopupWindow();
        if(id == R.id.RecipeIdeas)
        {
            Intent intent = new Intent(this, RecipeViewer.class);
            startActivity(intent);
        }
        if(id == R.id.FoodTrivia)
        {
            Intent intent = new Intent(this, FoodTrivia.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void populateFoodList() {
        foodList.add(new Food("Banana",5,Color.GREEN,R.drawable.fruit,"10-24-2017",FoodCategory.FRUIT));
        foodList.add(new Food("apples",1,Color.RED,R.drawable.fruit,"10-24-2017",FoodCategory.FRUIT));
        foodList.add(new Food("curry",3,Color.ORANGE,R.drawable.meat,"10-24-2017",FoodCategory.MEAT));
        foodList.add(new Food("swedish fish",5,Color.GREEN,R.drawable.sweet,"10-24-2017",FoodCategory.SWEET));
        foodList.add(new Food("sweetpotatoes",5,Color.GREEN,R.drawable.veggie,"10-24-2017",FoodCategory.FRUIT));
        foodList.add(new Food("chicken",5,Color.GREEN,R.drawable.meat,"10-24-2017",FoodCategory.MEAT));
        foodList.add(new Food("mochi",5,Color.GREEN,R.drawable.sweet,"10-24-2017",FoodCategory.SWEET));
        foodList.add(new Food("chicken sandwiches",5,Color.GREEN,R.drawable.meat,"10-24-2017",FoodCategory.MEAT));
    }

    private void populateListView() {
        ArrayAdapter<Food> adapter = new dataPage.MyListAdapter();
        ListView list = (ListView)findViewById(R.id.RecipeList);
        list.setAdapter(adapter);
    }
    private void registerClickCallback() {
        ListView list = (ListView)findViewById(R.id.RecipeList);
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


    // this listener will be called when there is change in firebase user session
    FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user == null) {
                // user auth state is changed - user is null
                // launch login activity
                startActivity(new Intent(dataPage.this, Login.class));
                finish();
            }
        }
    };
    //create the sorting popup window
    private void ShowPopupWindow(){
        try {
            ImageView btncall, btnsound, btncamera, btnvideo,btngallary,btnwrite;
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.fragment_sortingtypes, null);
            mypopup = new PopupWindow(layout, 750, 500, true);

            //mypopup.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mypopup.setBackgroundDrawable(new ColorDrawable(getResources().getColor((R.color.colorAccent))));
            mypopup.setOutsideTouchable(false);
            mypopup.showAtLocation(layout, Gravity.CENTER, 40, 60);
            //  window.showAtLocation(layout, 17, 100, 100);

            A = (RadioButton) layout.findViewById(R.id.Alphabet);
            C = (RadioButton) layout.findViewById(R.id.Category);
            E = (RadioButton) layout.findViewById(R.id.Expiration);

            A.setOnClickListener(this);
            C.setOnClickListener(this);
            E.setOnClickListener(this);

            if(type == sortingType.ALPHABETICAL)
                A.setChecked(true);
            else if(type == sortingType.CATEGORICAL)
                C.setChecked(true);
            else
                E.setChecked(true);

            popupSelect = (Button) layout.findViewById(R.id.Select);
            popupSelect.setOnClickListener(this);

        }catch (Exception e){}
    }
}
