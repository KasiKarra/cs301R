package cs301rclass.myapplicationattempt4;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class dataPageContent extends Activity {

    private List<Food> foodList = new ArrayList<Food>();

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_data_page);
        populateFoodList();
        populateListView();

    }

    private void populateFoodList() {
        foodList.add(new Food("Banana",5,"green",R.drawable.bananas));
        foodList.add(new Food("apples",5,"green",R.drawable.bananas));
        foodList.add(new Food("curry",5,"green",R.drawable.bananas));
        foodList.add(new Food("swedish fish",5,"green",R.drawable.bananas));
        foodList.add(new Food("sweetpotatoes",5,"green",R.drawable.bananas));
        foodList.add(new Food("chicken",5,"green",R.drawable.bananas));
        foodList.add(new Food("mochi",5,"green",R.drawable.bananas));
        foodList.add(new Food("chicken sandwiches",5,"green",R.drawable.bananas));
    }

    private void populateListView() {
        ArrayAdapter<Food> adapter = new MyListAdapter();
        ListView list = findViewById(R.id.foodListView);
        list.setAdapter(adapter);
    }

    private class MyListAdapter extends ArrayAdapter<Food>{
        public MyListAdapter(){
            super(dataPageContent.this,R.layout.item_view,foodList);
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

            return itemView;
        }

    }
}
