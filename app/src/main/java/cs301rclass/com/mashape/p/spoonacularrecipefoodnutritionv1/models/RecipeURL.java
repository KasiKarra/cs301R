package cs301rclass.com.mashape.p.spoonacularrecipefoodnutritionv1.models;

/**
 * Created by Rebecca on 10/25/2017.
 */

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class RecipeURL
        extends java.util.Observable
        implements java.io.Serializable
{
    private String sourceURL;

    @JsonGetter("sourceUrl")
    public String getSourceURL ( ) {
        return this.sourceURL;
    }

    /** SETTER
     * TODO: Write general description for this method
     */
    @JsonSetter("usedIngredientCount")
    public void setSourceURL (String value) {
        this.sourceURL = value;
        notifyObservers(this.sourceURL);
    }


}
