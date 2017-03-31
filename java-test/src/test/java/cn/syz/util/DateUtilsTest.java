package cn.syz.util;

import static cn.syz.util.DateUtils.dateSequence;
import static org.apache.commons.lang3.time.DateUtils.parseDate;
import static org.apache.commons.lang3.time.DateFormatUtils.format;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.junit.Test;

public class DateUtilsTest {

    @Test
    public void testDateSequence() throws ParseException {
        Date from = parseDate("20160101", "yyyyMMdd");
        Date to = parseDate("20161231", "yyyyMMdd");
        List<Date> seq = dateSequence(from, to);
        for (Date date : seq) {
            System.out.println(format(date, "yyyy年M月d日，EEEE"));
        }
    }

}
