package com.example.david.project1;

import android.graphics.Bitmap;

import java.text.DateFormat;
import java.util.Date;


public class ListItem {

    String currentDateTimeString;
    Bitmap pic;
    String desc;


    ListItem(){
        this.currentDateTimeString = currentDateTimeString;
        this.pic = pic;
        this.desc = desc;
    }

    public String getCurrentDateTimeString() {
        return currentDateTimeString;
    }

    public Bitmap getPic() {
        return pic;
    }

    public String getDesc() {
        return desc;
    }

    public void setCurrentDateTimeString(String currentDateTimeString) {
        this.currentDateTimeString = currentDateTimeString;
    }

    public void setPic(Bitmap pic) {
        this.pic = pic;

    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
