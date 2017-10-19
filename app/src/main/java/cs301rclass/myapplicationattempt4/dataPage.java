package cs301rclass.myapplicationattempt4;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class dataPage extends AppCompatActivity {

    private List<Food> foodList = new ArrayList<Food>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_page);
        Toolbar tools = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tools);
        populateFoodList();
        populateListView();
        registerClickCallback();
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
        if(id == R.id.AddItem)
        {
            Intent intent = new Intent(this, AddItemPage.class);
            startActivity(intent);

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
