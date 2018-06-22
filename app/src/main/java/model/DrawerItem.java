package model;

/**
 * Created by sharda on 8/17/2017.
 */

public class DrawerItem {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_FOOTER = 1;
    public static final int TYPE_ITEM = 2;

    public int type;
    public int data;
    public String text;

    public DrawerItem(int type, String text, int data)
    {
        this.type=type;
        this.data=data;
        this.text=text;
    }

    /*String ItemName;
    int imgResID;

    public DrawerItem(String itemName, int imgResID) {
        super();
        ItemName = itemName;
        this.imgResID = imgResID;
    }

    public String getItemName() {
        return ItemName;
    }
    public void setItemName(String itemName) {
        ItemName = itemName;
    }
    public int getImgResID() {
        return imgResID;
    }
    public void setImgResID(int imgResID) {
        this.imgResID = imgResID;
    }*/

}