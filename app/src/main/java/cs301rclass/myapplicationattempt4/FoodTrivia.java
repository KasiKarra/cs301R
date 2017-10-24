package cs301rclass.myapplicationattempt4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cs301rclass.com.mashape.p.spoonacularrecipefoodnutritionv1.APIHelper;
import cs301rclass.com.mashape.p.spoonacularrecipefoodnutritionv1.Configuration;
import cs301rclass.com.mashape.p.spoonacularrecipefoodnutritionv1.controllers.APIController;
import cs301rclass.com.mashape.p.spoonacularrecipefoodnutritionv1.controllers.BaseController;
import cs301rclass.com.mashape.p.spoonacularrecipefoodnutritionv1.exceptions.APIException;
import cs301rclass.com.mashape.p.spoonacularrecipefoodnutritionv1.http.client.APICallBack;
import cs301rclass.com.mashape.p.spoonacularrecipefoodnutritionv1.http.client.HttpContext;
import cs301rclass.com.mashape.p.spoonacularrecipefoodnutritionv1.http.request.HttpRequest;
import cs301rclass.com.mashape.p.spoonacularrecipefoodnutritionv1.http.response.HttpResponse;
import cs301rclass.com.mashape.p.spoonacularrecipefoodnutritionv1.http.response.HttpStringResponse;
import cs301rclass.com.mashape.p.spoonacularrecipefoodnutritionv1.models.FindByIngredientsModel;

import static cs301rclass.com.mashape.p.spoonacularrecipefoodnutritionv1.controllers.BaseController.getClientInstance;

public class FoodTrivia extends AppCompatActivity {

    Button Next, Return;
    Random r = new Random();
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_trivia);
        Next = (Button) findViewById(R.id.Next);
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeAPICall();
            }
        });
        Return = (Button)findViewById(R.id.Return);
        Return.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv = (TextView) findViewById(R.id.info);
        makeAPICall();
    }

    private void makeAPICall()
    {
        Configuration config = new Configuration();
        config.initialize(this.getApplicationContext());
        APIController apic = new APIController();

        int choice = r.nextInt(1000)%2;
        String baseURL = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/";
        StringBuilder queryBuilder = new StringBuilder(baseURL);


        if(choice == 0) //food trivia
        {
            queryBuilder.append("food/trivia/random");
        }
        else //food joke
        {
            queryBuilder.append("food/jokes/random");
        }

        APICallBack<String> apicb = new APICallBack<String>() {
            @Override
            public void onSuccess(HttpContext context, String response) {
                String update = clean(response);
                tv.setText(update);
            }

            @Override
            public void onFailure(HttpContext context, Throwable error) {
                tv.setText("API Call Failed . . .");
            }
        };
        apic.randomness(queryBuilder, apicb);
    }


    public String clean(String response)
    {
        response = response.replace("\\","");
        String returning = response.substring(9, (response.length()-2));
        returning = returning.substring(0,1).toUpperCase() + returning.substring(1);
        return returning;
    }
}
