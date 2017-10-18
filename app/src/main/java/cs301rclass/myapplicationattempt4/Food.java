package cs301rclass.myapplicationattempt4;

/**
 * Created by The Best Couple Ever on 10/18/2017.
 */

public class Food {
    private String name;
    private int daysUntilExp;
    private String color;
    private int iconId;

    public Food(String name,int daysUntilExp, String color,int iconId){
        this.name = name;
        this.daysUntilExp = daysUntilExp;
        this.color = color;
        this.iconId = iconId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDaysUntilExp() {
        return daysUntilExp;
    }

    public void setDaysUntilExp(int daysUntilExp) {
        this.daysUntilExp = daysUntilExp;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

}
