package com.ariefzuhri.amigo19.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.ariefzuhri.amigo19.R;
import com.ariefzuhri.amigo19.preference.AssessmentFirestore;
import com.ornach.nobobutton.NoboButton;
import java.util.ArrayList;
import java.util.List;

import static com.ariefzuhri.amigo19.utils.AppUtils.LOG;
import static com.ariefzuhri.amigo19.utils.DateUtils.getCurrentDate;
import static com.ariefzuhri.amigo19.utils.AppUtils.getTransparentNotifBar;
import static com.ariefzuhri.amigo19.utils.AssessmentUtils.getFirstMeet;
import static com.ariefzuhri.amigo19.utils.AssessmentUtils.getIndexSuggestion;
import static com.ariefzuhri.amigo19.utils.AssessmentUtils.getLevelMeet;
import static com.ariefzuhri.amigo19.utils.AssessmentUtils.getPoint;
import static com.ariefzuhri.amigo19.utils.AssessmentUtils.getRangeFirstMeet;
import static com.ariefzuhri.amigo19.utils.AssessmentUtils.getRangeMeet;

public class AssessmentActivity extends AppCompatActivity {
    private final static String TAG = "AssessmentActivity";
    private int totalQuestions = 0, currentQuestions = 0, point = 0;
    private String[] listQuestions, listOptions;
    private TextView tvQuestion;
    private RadioButton rbOption1, rbOption2, rbOption3, rbOption4;
    private RadioGroup rgOptions;
    private NoboButton btnNext;
    private List<Integer> rangeFirstMeet = new ArrayList<>();
    private List<Integer> indexSuggestion = new ArrayList<>();
    private AssessmentFirestore assessmentFirestore;
    private String firstMeet;
    private AlertDialog.Builder dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment);

        // Initialize preference
        assessmentFirestore = new AssessmentFirestore();

        // Atur status bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();
        getTransparentNotifBar(this);

        // Initialize
        tvQuestion = findViewById(R.id.tv_question_assessment);
        rgOptions = findViewById(R.id.rg_options_assessment);
        rbOption1 = findViewById(R.id.rb_option_1_assessment);
        rbOption2 = findViewById(R.id.rb_option_2_assessment);
        rbOption3 = findViewById(R.id.rb_option_3_assessment);
        rbOption4 = findViewById(R.id.rb_option_4_assessment);
        btnNext = findViewById(R.id.btn_next_assessment);

        dialog = new AlertDialog.Builder(this);

        // Muat pertanyaan dan opsi
        listQuestions = getResources().getStringArray(R.array.question_assessment);
        listOptions = getResources().getStringArray(R.array.option_assessment);

        // Atur tampilan
        totalQuestions = listQuestions.length;
        btnNext.setEnabled(false);

        tvQuestion.setText(listQuestions[currentQuestions]);
        rbOption1.setText(listOptions[0]);
        rbOption2.setText(listOptions[1]);
        rbOption3.setText(listOptions[2]);
        rbOption4.setText(listOptions[3]);

        // Tombol
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(rgOptions.getCheckedRadioButtonId() == -1)){
                    /*///////////////////////////////PROGRAM MULAI///////////////////////////////*/
                    int num = currentQuestions+1;
                    int option = -1;
                    if (rbOption1.isChecked()) option = 1;
                    else if (rbOption2.isChecked()) option = 2;
                    else if (rbOption3.isChecked()) option = 3;
                    else if (rbOption4.isChecked()) option = 4;

                    point += getPoint(num, option); // Proses poin, nomor 1-7
                    getIndexSuggestion(indexSuggestion, num, option); // Proses saran,  nomor 3, 4, 5, 9
                    getRangeFirstMeet(rangeFirstMeet, num, option); // Untuk penentuan first belanja, mengetahui rangenya dari hari ini, nomor 8
                    if (num == 9) firstMeet = getFirstMeet(rangeFirstMeet, num, option); // Menentukan first belanja berdasarkan rangeFirstMeet, nomor 9
                    /*//////////////////////////////PROGRAM SELESAI//////////////////////////////*/

                    if (num < totalQuestions){ // Refresh soal
                        currentQuestions++; // Nomor soal selanjutnya

                        // Muat soal baru
                        tvQuestion.setText(listQuestions[currentQuestions]);
                        // Muat opsi berdasarkan soal
                        rbOption1.setText(listOptions[currentQuestions*4]);
                        rbOption2.setText(listOptions[currentQuestions*4+1]);
                        rbOption3.setText(listOptions[currentQuestions*4+2]);
                        rbOption4.setText(listOptions[currentQuestions*4+3]);

                        // Hilangkan check opsi setelah mendapat soal baru
                        rgOptions.clearCheck();

                        // Jika sudah sampai soal terakhir
                        if (num == totalQuestions) btnNext.setText(getString(R.string.end_assessment));
                    } else { // Jika sudah selesai
                        // Beritau hasilnya
                        dialog.setCancelable(false);
                        dialog.setMessage(R.string.dialog_done_assessment);
                        dialog.setNeutralButton(R.string.dialog_save_assessment, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Simpan preferensi
                                        assessmentFirestore.savePreference(
                                                true, getCurrentDate(), firstMeet,
                                                "-", point, indexSuggestion, 0,
                                                getRangeMeet(point), getLevelMeet(point));
                                        Log.d(TAG, LOG + "result: point:" + point + " index sug:" + indexSuggestion);

                                        // Luncurkan main
                                        Intent intent = new Intent(AssessmentActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                        dialog.create().show();
                    }
                }
            }
        });

        rgOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Wajib isi opsi baru bisa next
                if (checkedId == -1) btnNext.setEnabled(false);
                else btnNext.setEnabled(true);
            }
        });
    }
}
