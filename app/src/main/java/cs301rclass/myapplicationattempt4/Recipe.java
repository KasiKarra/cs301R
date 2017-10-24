package cs301rclass.myapplicationattempt4;

/**
 * Created by Rebecca on 10/22/2017.
 */

public class Recipe
{
    private int ID = -1;
    private String name = "";
    private String image = "";

    public Recipe (int id, String n, String i)
    {
        ID = id;
        name = n;
        image = i;
    }

    public Recipe(){}

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
