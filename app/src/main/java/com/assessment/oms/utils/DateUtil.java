package com.assessment.oms.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static Date dateFormater(String date){
        try {
            SimpleDateFormat smf = new SimpleDateFormat("dd-MM-yyyy");
            return smf.parse(date);
        }catch (Exception ex){
            return null;
        }
    }
}
