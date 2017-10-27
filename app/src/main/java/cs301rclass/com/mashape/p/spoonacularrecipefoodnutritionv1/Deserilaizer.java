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
        if(pieces.length < 16)
            return "";
        String finale = pieces[14];
        if(finale.length() < 16)
            return "";
        finale = finale.substring(13, (finale.length() - 1));
        return finale;
    }
}
