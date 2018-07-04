package com.example.pgeraet81.blabla;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Pascal on 4-7-2018.
 */

public class Month {

    private Integer currentMonth;

    public Integer getCurrentMonth() {
        DateFormat dateFormat = new SimpleDateFormat("MM");
        Date date = new Date();
        switch (dateFormat.format(date)) {
            case "01":
                return R.drawable.januari;
            case "02":
                return R.drawable.februari;
            case "03":
                return R.drawable.maart;
            case "04":
                return R.drawable.april;
            case "05":
                return R.drawable.mei;
            case "06":
               return R.drawable.juni;
            case "07":
                return R.drawable.juli;
            case "08":
                return R.drawable.augustus;
            case "09":
                return R.drawable.september;
            case "10":
                return R.drawable.oktober;
            case "11":
                return R.drawable.november;
            case "12":
                return R.drawable.december;
            default:
                return 0;
        }
    }
}
