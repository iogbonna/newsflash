package icom.com.news.Helper;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Charlie on 11/14/2015.
 */
public class DateHelper {
    //"Mon, 14 Dec 2015 16:29:05 GMT"
    public static Date convertToDate(String dateTime){
        String[] values=dateTime.split(" ");

        String[] months = new String[]{"Jan", "Feb", "Mar","Apr", "May", "Jun", "Jul", "Aug", "Sep","Oct", "Nov", "Dec"};
        String dateValue=values[3]+"-"+(Arrays.asList(months).indexOf(values[2])+1)+"-"+values[1]+" "+values[4];
        String[] timeValue=values[4].split(":");
        Calendar c=new GregorianCalendar(Integer.parseInt(values[3]),(Arrays.asList(months).indexOf(values[2])+1),Integer.parseInt(values[1]),Integer.parseInt(timeValue[0]),Integer.parseInt(timeValue[1]),Integer.parseInt(timeValue[2]));
        Date d=null;
        if (dateTime==null){
            return null;

        }
        else{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd HH:MM:SS", Locale.ENGLISH);
            //Calendar calendar = Calendar.getInstance();
            try {
                d=dateFormat.parse(dateTime);
            } catch (ParseException e) {

                e.printStackTrace();
            }
        }

        return d;

    }


	/*public static String Format(Date date){
		return String.valueOf(date.getDay());
	}*/

    public static String Format(Date date) {

        String[] months = new String[]{"January", "February", "March","April", "May", "June", "July", "August", "September","October", "November", "December"};
        String[] d = new String[]{"Sunday","Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        Date curDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd HH:MM:SS.SSS",Locale.ENGLISH);
        //dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        int curDay = curDate.getDate();
        String result=dateFormat.format(date);
        int curMonth = curDate.getMonth();
        int curYear = curDate.getYear();
        if (date.getDate() == curDay && curMonth == date.getMonth() && curYear == date.getYear()) {
            int hours = (curDate.getHours() - date.getHours());
            int minutes = (curDate.getMinutes() - date.getMinutes());
            if (hours > 1 ) {
                result= (curDate.getHours() - date.getHours()) + "h";
            }
            else if (hours==1) {
                result= hours + " h";
            }
            else if (hours<1 && minutes>0) {
                result=(curDate.getMinutes() - date.getMinutes()) + "m";
                //return (curDate.getMinutes() - date.getMinutes()) + " minutes ago";
            }
            else if (hours < 1 && minutes < 0) {
                result= (curDate.getSeconds() - date.getSeconds()) + "s";
            }

        }
        else if (curMonth == date.getMonth() && curYear == date.getYear()) {
            int days = curDate.getDay() - date.getDay();
            if (days == 1)
                result= "Yesterday";
            else if (days > 1 && days <= 3) {
                result= days + "d";
            }
            else if (days>3) {
                result= d[days];
            }

        }
        else if (curYear == date.getYear()) {
            int m = curMonth - date.getMonth();
            if (m > 1 && m <= 3) {
                result= date.getDate() + " " + months[m];
            }
            else if (m > 3)
                result= m + "m";

        } else if ((curYear-date.getYear())>1) {
            result= (curYear - date.getYear()) + " years ago";
        }
        else if ((curYear - date.getYear()) == 1) {
            result= "1 year ago";
        } return result;

    }

}
