package cs301rclass.myapplicationattempt4;


/**
 * Created by The Best Couple Ever on 10/18/2017.
 */
enum Color {GREEN,ORANGE,RED}
enum FoodCategory{MEAT,VEGGIE,FRUIT,GRAIN,SWEET,DAIRY}

public class Food {
    private String name;
    private int daysUntilExp;
    public String expDate;
    private Color color;
    private int iconId;
    private FoodCategory category;

    public Food(String name,int daysUntilExp, Color color,int iconId,String expDate,FoodCategory category){
        this.name = name;
        this.daysUntilExp = daysUntilExp;
        this.color = color;
        this.iconId = iconId;
        this.expDate = expDate;
        this.category = category;
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

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }


}
