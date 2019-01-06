package com.example.personal.until;

import java.util.Calendar;
import java.util.Random;

public class TimeTool {
    public static String getTime() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH) + 1;
        int d = calendar.get(Calendar.DATE);
        String month = "";
        String date = "";
        if (m < 10) {
            month = "0" + String.valueOf(m);
        } else {
            month = String.valueOf(m);
        }
        if (d < 10) {
            date = "0" + String.valueOf(d);
        } else {
            date = String.valueOf(d);
        }
        return String.valueOf(year) + month + date;
    }

    /**
     * 生成指定范围的整数
     * @param max
     * @param min
     * @return
     */
    public static String createIntValue(Integer max, Integer min) {
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return String.valueOf(s);
    }
}
