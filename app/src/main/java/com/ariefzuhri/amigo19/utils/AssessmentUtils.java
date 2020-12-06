package com.ariefzuhri.amigo19.utils;

import android.util.Log;

import com.ariefzuhri.amigo19.preference.AssessmentFirestore;
import com.ariefzuhri.amigo19.preference.model.AssessmentPreference;

import java.util.List;

import static com.ariefzuhri.amigo19.utils.AppUtils.LOG;
import static com.ariefzuhri.amigo19.utils.DateUtils.addDay;
import static com.ariefzuhri.amigo19.utils.DateUtils.differenceOfDates;
import static com.ariefzuhri.amigo19.utils.DateUtils.getCurrentDate;

public class AssessmentUtils {
    private static final String TAG = "AssessmentUtils";

    // Menghitung poin yang diperoleh berdasarkan opsi pada soal-soal tertentu
    public static int getPoint(int num, int option){
        /*Aturan poin:
        * 1) Poin untuk soal nomor 1-7
        * 2) Soal nomor 1-3, mendapat poin 0, 1, 2, 3
        * 3) Soal nomor 4-5, mendapat poin 0, 2, 4, 6 (dikali 2)
        * 4) Soal nomor 6-7 mendapat poin 0, 0, 1, 1*/

        int dPoint = 0;

        // Nomor 1-5
        if (num >= 1 && num <= 5){
            if (option == 2) dPoint = 1;
            else if (option == 3) dPoint = 2;
            else if (option == 4) dPoint = 3;
            if (num == 4 || num == 5) dPoint *= 2;
        }

        // Nomor 6-7
        if (num >= 6 && num <= 7) if (option == 3 || option == 4) dPoint = 1;

        Log.d(TAG, LOG + "q:" + num + " getPoint:" + dPoint);
        return dPoint;
    }

    // Mendapatkan index saran
    public static List<Integer> getIndexSuggestion(List<Integer> indexSuggestion, int num, int option){
        /*Aturan saran
        * 1) Saran untuk soal nomor 3, 4, 5, 9
        * 2) Soal nomor 3, jika opsi 3/4 mendapat saran 6, 7
        * 3) Soal nomor 4, jika opsi 3/4 mendapat saran 1, 2
        * 4) Soal nomor 9, jika opsi 3/4 mendapat saran 3
        * 5) Soal nomor 3, jika opsi 1/2 mendapat saran 4, 5*/

        if (option == 3 || option == 4){
            if (num == 3){
                addToList(indexSuggestion, 5);// Saran ke-6, index ke-5
                addToList(indexSuggestion, 6);// Saran ke-7, index ke-6
            } else if (num == 4 || num == 5){
                addToList(indexSuggestion, 0);// Saran ke-1, index ke-0
                addToList(indexSuggestion, 1);// Saran ke-2, index ke-1
            } else if (num == 9){
                addToList(indexSuggestion, 2);// Saran ke-3, index ke-2
            }
        } else{
            if (num == 3){
                addToList(indexSuggestion, 3);// Saran ke-4, index ke-3
                addToList(indexSuggestion, 4);// Saran ke-5, index ke-4
            }
        }

        return indexSuggestion;
    }

    // Menambah data int ke list agar tidak duplikat
    private static void addToList(List<Integer> list, int data){
        if (!list.contains(data)) list.add(data);
    }

    // Mendapatkan range/jarak antara jadwal belanja pertama dengan hari ini
    public static void getRangeFirstMeet(List<Integer> rangeFirstMeet, int num, int option){
        /*Aturan range first meet
        * 1) Untuk soal nomor 8
        * 2) Ambil 2 angka di antara kata atau*/
        if (num == 8){ // Kira2, kapan akan belanja lagi?
            if (option == 1){ // Hari ini
                rangeFirstMeet.add(0);
                rangeFirstMeet.add(0);
            } else if (option == 2){ // 1 atau 2 hari ke depan
                rangeFirstMeet.add(1);
                rangeFirstMeet.add(2);
            } else if (option == 3){ // 3 atau 4 hari ke depan
                rangeFirstMeet.add(3);
                rangeFirstMeet.add(4);
            } else if (option == 4){ // >4 hari ke depan
                rangeFirstMeet.add(5);
                rangeFirstMeet.add(5);
            }
        }
    }

    // Menentukan tanggal berapa belanja pertama kali
    public static String getFirstMeet(List<Integer> rangeFirstMeet, int num, int option){
        /*Aturan first meet
        * 1) Untuk soal nomor 9
        * 2) Jika opsi 1/2, ambil data range pada index 0, sebaliknya
        * 3) Penentuan first meet = hari ini + data range
        * 4) Next meet (setelah first meet dilakukan), mengikuti range tetap (3/4/5/6)*/
        String firstMeet = null;
        if (num == 9){ // Suka habis dulu, baru belanja?
            if (option == 1 || option == 2) firstMeet = addDay(getCurrentDate(), rangeFirstMeet.get(1)); // Tidak pernah atau Jarang, ambil kanan
            else if (option == 3 || option == 4) firstMeet = addDay(getCurrentDate(), rangeFirstMeet.get(0)); // Sering atau Selalu, ambil kiri
        }
        return firstMeet;
    }

    // Mendapatkan range tetap, untuk menentukan next meet setelah first meet
    public static int getRangeMeet(int point){
        if (point >= 0 && point <= 5) return 4; // Juga mengikuti saran dari IPB University -> beberapa hari/minggu
        else if (point >= 6 && point <= 10) return 5;
        else if (point >= 11 && point <= 15) return 6;
        else if (point >= 16) return 7; // Saran FDA (Badan pengawas makanan Amerika) -> 1-2 minggu
        return -1;
    }

    // Mendapatkan index tingkat kesulitan memenuhi kebutuhan pokok
    public static int getLevelMeet(int point){
        if (point >= 0 && point <= 5) return 0;
        else if (point >= 6 && point <= 10) return 1;
        else if (point >= 11 && point <= 15) return 2;
        else if (point >= 16) return 3;
        return -1;
    }

    // Kecuali ini, tidak digunakan oleh AssessmentActivity
    public static String getNextMeet(AssessmentFirestore assessmentFirestore, AssessmentPreference assessmentPreference){
        /*Aturan nextMeet
         *1) nextMeet = lastMeet + numberOfDelay (jika ada)
         *2) numberOfDelay bertambah jika pengguna tidak konfirmasi belanja pada hari H
         *3) numberOfDelay juga bertambah jika pengguna memilih opsi tunda besok pada hari H*/
        String nextMeet;

        if (assessmentPreference.getLastMeet().equals("-")) nextMeet = assessmentPreference.getFirstMeet();
        else nextMeet = addDay(assessmentPreference.getLastMeet(), assessmentPreference.getRangeMeet());
        nextMeet = addDay(nextMeet, assessmentPreference.getNumberOfDelay()); // Tambah dengan delay

        // Jika user tidak menandai telah belanja ketika H (kemarin), maka tanggal autoubah ke hari ini
        if (differenceOfDates(nextMeet, getCurrentDate())<0){
            int difference = differenceOfDates(getCurrentDate(), nextMeet);
            assessmentFirestore.savePreference(assessmentPreference.getNumberOfDelay()+difference); // Bertambah sepanjang selisihnya
            nextMeet = addDay(nextMeet, difference);
        }

        return nextMeet;
    }
}
