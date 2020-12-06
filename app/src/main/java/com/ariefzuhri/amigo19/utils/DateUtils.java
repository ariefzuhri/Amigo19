package com.ariefzuhri.amigo19.utils;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import static com.ariefzuhri.amigo19.utils.AppUtils.locale;

public class DateUtils {
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm";

    // Mendapatkan tanggal hari ini
    public static String getCurrentDate(){
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, locale);
        Date date = new Date();
        return dateFormat.format(date);
    }

    // Mendapatkan waktu saat ini
    public static String getCurrentTime(){
        DateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT, locale);
        Date time = new Date();
        return timeFormat.format(time);
    }

    // Mendapatkan nama hari pada tanggal tertentu
    public static String getDayName(String formattedDate) {
        if (isValidFormat(formattedDate)){
            try {
                Date date = new SimpleDateFormat(DATE_FORMAT, locale).parse(formattedDate);
                return new SimpleDateFormat("EEEE", locale).format(date);
            } catch (ParseException error) {
                error.printStackTrace();
            }
        }
        return formattedDate;
    }

    // Konversi tanggal dari yyyy-MM-dd ke yyyy MMMM dd -> 2020-02-13 to 13 February 2020
    public static String getFullDate(String date){
        if (isValidFormat(date)){
            String[] arrayDate = date.split("-");
            return arrayDate[2] + " " + new DateFormatSymbols().getMonths()[Integer.parseInt(arrayDate[1])-1] + " " + arrayDate[0];
        } else return date;
    }

    // Mendapatkan tanggal baru setelah tanggal sebelumnya ditambah n hari
    static String addDay(String oldDate, int numberOfDays){
        if (isValidFormat(oldDate)){
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, locale);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateFormat.parse(oldDate));
                calendar.add(Calendar.DAY_OF_MONTH, numberOfDays);
                return dateFormat.format(calendar.getTime());
            } catch (ParseException e){
                e.printStackTrace();
            }
        }
        return oldDate;
    }

    // Mencari selisih antara 2 tanggal
    public static int differenceOfDates(String newerDate, String olderDate){
        if (isValidFormat(newerDate) && isValidFormat(olderDate)){
            try{
                SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, locale);
                Date finalDate = dateFormat.parse(newerDate);
                Date currentDate = dateFormat.parse(olderDate);

                // Saya bagi 100rb karena aslinya long
                double difference = (double) ((finalDate.getTime()-currentDate.getTime())/100000); // Jika ingin absolut -> Math.abs()
                return (int) ((difference / (24*60*60*1000))*100000);
            }catch (ParseException e){
                e.printStackTrace();
            }
        }
        return -1;
    }

    // Mendapatkan hari ke-n (nth) dalam suatu tahun tertentu dari suatu tanggal, tidak bisa untuk mencari selisih beda tahun
    public static int getDayOfYear(String formattedDate) {
        if (isValidFormat(formattedDate)){
            try {
                Date date = new SimpleDateFormat(DATE_FORMAT, locale).parse(formattedDate);
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(date);
                return calendar.get(Calendar.DAY_OF_YEAR);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    // Mengecek apakah tanggal sudah sesuai dengan format
    private static boolean isValidFormat(String date){
        if (date != null){
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, locale);
                dateFormat.parse(date);
                return true;
            } catch (ParseException e) {
                return false;
            }
        } else return false;
    }

    // Mengecek apakah waktu sudah sesuai dengan format
    private static boolean isValidTimeFormat(String time){
        if (time != null){
            try {
                SimpleDateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT, locale);
                timeFormat.parse(time);
                return true;
            } catch (ParseException e) {
                return false;
            }
        } else return false;
    }

    public static int timeToMinute(String time){
        if (isValidTimeFormat(time)){
            String[] timeArray = time.split(":");
            int hour = Integer.parseInt(timeArray[0]);
            int minute = Integer.parseInt(timeArray[1]);
            return hour*60 + minute;
        }
        return -1;
    }

    // Mengecek apakah suatu waktu telah berusia 24 jam/lebih, tidak berlaku untuk tanggal di masa depan
    public static boolean is24Hours(String time, String date){
        if (isValidFormat(date) && isValidTimeFormat(time)){
            if (Math.abs(differenceOfDates(date, getCurrentDate())) >= 2) return true; // Sudah dipastikan > 24 jam
            else return (!isToday(date) && (timeToMinute(getCurrentTime()) >= timeToMinute(time)));
        } else return false;
    }

    // Mengecek apakah suatu tanggal adalah tanggal hari ini
    public static boolean isToday(String date){
        if (isValidFormat(date)){
            return differenceOfDates(date, getCurrentDate()) == 0;
        } else return false;
    }
}
