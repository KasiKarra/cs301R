package cs301rclass.com.mashape.p.spoonacularrecipefoodnutritionv1;

import com.fasterxml.jackson.core.JsonParser;

/**
 * Created by Rebecca on 10/26/2017.
 */

public class Deserilaizer
{

    public Deserilaizer(){}

    public String Deserialize(String json)
    {
        String[] pieces = json.split(",");
        if(pieces.length < 40)
            return "";

        String finale = "";
        for(int i = 5; i < 40; i++)
            if(pieces[i].contains("sourceUrl")) {
                finale = pieces[i];
                i = 41;
            }

        if(finale.length() < 16)
            return "";
        finale = finale.substring(13, (finale.length() - 1));
        return finale;
    }
}
