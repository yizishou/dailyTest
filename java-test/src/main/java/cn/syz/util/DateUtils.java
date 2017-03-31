package cn.syz.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DateUtils {

    public static List<Date> dateSequence(Date from, Date to) {
        List<Date> seq = new ArrayList<>();
        Date d = from;
        while (d.compareTo(to) <= 0) {
            seq.add(d);
            d = new Date(d.getTime() + 24 * 3600 * 1000);
        }
        return seq;
    }

}
