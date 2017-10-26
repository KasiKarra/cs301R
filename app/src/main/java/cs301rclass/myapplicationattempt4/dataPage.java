//getting left vs right swipes stackoverflow answer
//https://stackoverflow.com/questions/4139288/android-how-to-handle-right-to-left-swipe-gestures


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
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

enum sortingType {ALPHABETICAL, CATEGORICAL, EXPIRATION}

public class dataPage extends AppCompatActivity implements View.OnClickListener {

    private List<Food> foodList = new ArrayList<Food>();
    private Button popupSelect;
    private RadioButton A, C, E;  //alphabetical, categorical, expiration
    private Button Delete,Cancel;
    private PopupWindow deletePopUp;
    private PopupWindow mypopup;
    private sortingType type = sortingType.ALPHABETICAL;
    private FirebaseAuth auth;
    private ListView list;
    ArrayAdapter<Food> adapter;
    private Food selectedFood;
    private TextView sortingTypeTextBox;
    private Button RecipeButton;
    private ImageButton Sort, AddItem;
    private FirebaseUser user;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("server/saving-data/fireblog/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_page);
        Toolbar tools = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tools);
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        RecipeButton = (Button) findViewById (R.id.RecipeButton);
        RecipeButton.setOnClickListener(this);
        Sort = (ImageButton) findViewById(R.id.sort);
        Sort.setOnClickListener(this);
        AddItem = (ImageButton) findViewById(R.id.AddItemButton);
        AddItem.setOnClickListener(this);
        sortingTypeTextBox = (TextView) findViewById(R.id.sortedText);
        adapter = new dataPage.MyListAdapter();
        list = (ListView) findViewById(R.id.RecipeList);
        list.setAdapter(adapter);
        populateFoodList();
        registerClickCallback();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Alphabet:
                A.setChecked(false);
                C.setChecked(false);
                E.setChecked(false);
                type = sortingType.ALPHABETICAL;
                A.setChecked(true);
                break;
            case R.id.Category:
                A.setChecked(false);
                C.setChecked(false);
                E.setChecked(false);
                type = sortingType.CATEGORICAL;
                C.setChecked(true);
                break;
            case R.id.Expiration:
                A.setChecked(false);
                C.setChecked(false);
                E.setChecked(false);
                E.setChecked(true);
                type = sortingType.EXPIRATION;
                break;
            case R.id.Select:
                mypopup.dismiss();
                //change sorted text to reflect what is chosen
                if(type == sortingType.ALPHABETICAL) {
                    sortingTypeTextBox.setText("Sorted Alphabetically");
                    sortAlphabetical();
                    adapter.notifyDataSetChanged();
                }
                else if(type == sortingType.CATEGORICAL) {
                    sortingTypeTextBox.setText("Sorted Categorically");
                    sortCategory();
                    adapter.notifyDataSetChanged();
                }
                else {
                    sortingTypeTextBox.setText("Sorted by Expiration Date");
                    sortDate();
                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.sort:
                ShowPopupWindow();
                break;
            case R.id.RecipeButton:
                getExpiringSoon();
                break;
            case R.id.AddItemButton:
                Intent intent = new Intent(this, AddItemPage.class);
                startActivity(intent);
                break;
            case R.id.Cancel:
                mypopup.dismiss();
                break;
            case R.id.Delete:
                delete(selectedFood);
                mypopup.dismiss();
        }
    }
    private void sortAlphabetical()
    {
        Collections.sort(foodList,new Comparator<Food>(){
            @Override
            public int compare(Food o1, Food o2){
                return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
            }
        });

    }

    private void sortDate()
    {
        Comparator<Food> byDate = new Comparator<Food>(){
            public int compare(Food f1, Food f2){
                if(f1.getDaysUntilExp() < f2.getDaysUntilExp())
                {
                    return -1;
                }
                else
                {
                    return 1;
                }
            }
        };
        Collections.sort(foodList,byDate);
    }
    private void sortCategory()
    {
        Comparator<Food> byCategory = new Comparator<Food>(){
            public int compare(Food f1, Food f2){
                if(f1.getIconId() < f2.getIconId())
                {
                    return -1;
                }
                else
                {
                    return 1;
                }
            }
        };
        Collections.sort(foodList,byCategory);
    }

    private void getExpiringSoon()
    {
        Intent intent = new Intent(this, RecipeViewer.class);
        String ingreds = "";
        if(foodList.size() > 0)
        {
            StringBuilder ingredients = new StringBuilder("");
            for(int i = 0; i < foodList.size(); i++)
                if(foodList.get(i).getDaysUntilExp() < 3)
                    ingredients.append(foodList.get(i).getName() + ",");

            String sending = ingredients.toString();
            ingreds = sending.substring(0, (sending.length() - 1));

        }
        intent.putExtra("Ingredients", ingreds);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_general, menu);
        return true;
    }
    public class CustomComparatorAlpha implements Comparator<Food> {
        @Override
        public int compare(Food o1, Food o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }
    public class CustomComparatorDate implements Comparator<Food> {
        @Override
        public int compare(Food o1, Food o2) {
            if(o1.getDaysUntilExp() < o2.getDaysUntilExp())
            {
                return -1;
            }
            else if(o1.getDaysUntilExp() > o2.getDaysUntilExp())
            {
                return 1;
            }
            return 0;
        }
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
        if (id == R.id.Logoff) {
            auth.signOut();
            Intent intent = new Intent(this, Login.class);
            startActivity(intent); //can we use finish instead of start new activity?
        }
        if(id == R.id.RecipeIdeas)
        {
            Intent intent = new Intent(this, RecipeViewer.class);
            intent.putExtra("Ingredients", "");
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void populateFoodList() {


        ref.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                foodList.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Food newFood = postSnapshot.getValue(Food.class);
                    newFood.setFirebaseid(postSnapshot.getKey());
                    foodList.add(newFood);
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }
    private void delete(Food foodToDelete)
    {
        foodList.remove(foodToDelete);
        ref.child(user.getUid()).child(foodToDelete.getFirebaseid()).removeValue();
        adapter.notifyDataSetChanged();
    }
    private void registerClickCallback() {

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                selectedFood = foodList.get(position);
                ShowDeletePopupWindow();
                return true;
            }
            /*
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                Food clickedFood = foodList.get(position);
                String message = "you clicked position " + position + " which is the food " + clickedFood.getName();
                Toast.makeText(dataPage.this, message, Toast.LENGTH_LONG).show();
            }
            */
        });
    }

    private class MyListAdapter extends ArrayAdapter<Food> {
        public MyListAdapter() {
            super(dataPage.this, R.layout.item_view, foodList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //sort before updating.  Helps during adds
            if(type == sortingType.ALPHABETICAL)
                sortAlphabetical();
            else
            {
                if(type == sortingType.CATEGORICAL)
                    sortCategory();
                else
                    sortDate();
            }


            //make sure we have a view to work with(may be given null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.item_view, parent, false);
            }
            //find the food
            Food currFood = foodList.get(position);
            //fill the view
            ImageView imageView = (ImageView) itemView.findViewById(R.id.item_icon);
            imageView.setImageResource(currFood.getIconId());
            //name
            TextView counterText = (TextView) itemView.findViewById(R.id.countdown);
            counterText.setTextColor(getCurrFoodColor(currFood));
            counterText.setText("" + currFood.getDaysUntilExp());

            TextView dateText = (TextView) itemView.findViewById(R.id.item_expDate);
            dateText.setText(currFood.getExpDate());

            TextView nameText = (TextView) itemView.findViewById(R.id.item_txtName);
            nameText.setText(currFood.getName());


            return itemView;
        }


        int getCurrFoodColor(Food currFood) {
            int color = 0;
            switch (currFood.getColor()) {
                case GREEN:
                    color = -16711936;
                    break;
                case ORANGE:
                    color = android.graphics.Color.rgb(255, 165, 0);
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
    private void ShowPopupWindow() {
        try {
            ImageView btncall, btnsound, btncamera, btnvideo, btngallary, btnwrite;
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

            if (type == sortingType.ALPHABETICAL)
                A.setChecked(true);
            else if (type == sortingType.CATEGORICAL)
                C.setChecked(true);
            else
                E.setChecked(true);

            popupSelect = (Button) layout.findViewById(R.id.Select);
            popupSelect.setOnClickListener(this);

        } catch (Exception e) {
        }
    }

    //create the delete popup window
    private void ShowDeletePopupWindow() {
        try {
            ImageView btncall, btnsound, btncamera, btnvideo, btngallary, btnwrite;
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.content_delete_popup, null);
            mypopup = new PopupWindow(layout, 750, 500, true);


            mypopup.setBackgroundDrawable(new ColorDrawable(getResources().getColor((R.color.colorAccent))));
            mypopup.setOutsideTouchable(false);
            mypopup.showAtLocation(layout, Gravity.CENTER, 40, 60);

            Delete = (Button) layout.findViewById(R.id.Delete);
            Cancel = (Button) layout.findViewById(R.id.Cancel);


            Delete.setOnClickListener(this);
            Cancel.setOnClickListener(this);


        } catch (Exception e) {
        }
    }
}