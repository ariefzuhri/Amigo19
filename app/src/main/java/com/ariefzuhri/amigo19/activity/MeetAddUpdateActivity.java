package com.ariefzuhri.amigo19.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import com.ariefzuhri.amigo19.R;
import com.ariefzuhri.amigo19.model.Meet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ornach.nobobutton.NoboButton;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes;
import static com.ariefzuhri.amigo19.utils.AppUtils.getFixText;
import static com.ariefzuhri.amigo19.utils.AppUtils.getTransparentNotifBar;
import static com.ariefzuhri.amigo19.utils.AppUtils.isMaxChar;
import static com.ariefzuhri.amigo19.utils.AppUtils.isNull;
import static com.ariefzuhri.amigo19.utils.AppUtils.showSnackbar;
import static com.ariefzuhri.amigo19.database.DatabaseContract.MeetColumns.CONTENT_URI_MEET;
import static com.ariefzuhri.amigo19.database.DatabaseContract.MeetColumns.IS_CHECKED;
import static com.ariefzuhri.amigo19.database.DatabaseContract.MeetColumns.NAME;
import static com.ariefzuhri.amigo19.database.DatabaseContract.MeetColumns.QUANTITY;

public class MeetAddUpdateActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int REQUEST_ADD = 700;
    public static final int REQUEST_UPDATE = 701;
    public static final int RESULT_ADD = 702;
    public static final int RESULT_UPDATE = 703;
    public static final int RESULT_DELETE = 704;

    private ExtendedEditText edtName, edtQuantity;
    private TextFieldBoxes boxName, boxQuantity;
    private CheckBox checkBox;

    public static final String EXTRA_MEET = "extra_meet";
    private Meet meet;
    private boolean isUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet_add_update);

        // Atur status bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();
        getTransparentNotifBar(this);

        // Initialize
        FloatingActionButton btnBack = findViewById(R.id.btn_back_mau);
        TextView tvTitle = findViewById(R.id.tv_title_mau);
        edtName = findViewById(R.id.edt_name_mau);
        edtQuantity = findViewById(R.id.edt_quantity_mau);
        checkBox = findViewById(R.id.cb_meet_home);
        boxName = findViewById(R.id.box_name_mau);
        boxQuantity = findViewById(R.id.box_quantity_mau);
        NoboButton btnAdd = findViewById(R.id.btn_add_mau);
        NoboButton btnDelete = findViewById(R.id.btn_delete_mau);

        // Atur listener
        btnAdd.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        meet = getIntent().getParcelableExtra(EXTRA_MEET);
        if (meet != null){
            isUpdate = true;
        } else {
            meet = new Meet();
        }

        if (isUpdate){
            tvTitle.setText(R.string.title_update_mau);
            btnAdd.setText(getString(R.string.save));
            btnDelete.setVisibility(View.VISIBLE);
            edtName.setText(meet.getName());
            edtQuantity.setText(String.valueOf(meet.getQuantity()));
            checkBox.setChecked(meet.isChecked());
        } else {
            tvTitle.setText(R.string.title_add_mau);
            btnAdd.setText(getString(R.string.add));
            btnDelete.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_add_mau:
                String name = getFixText(edtName);
                String quantity = getFixText(edtQuantity);
                Boolean isChecked = checkBox.isChecked();

                if (isNull(name) || isNull(quantity)){
                    showSnackbar(view, getString(R.string.snackbar_not_null));
                    return;
                }

                if (isMaxChar(edtName, boxName) || isMaxChar(edtQuantity, boxQuantity)){
                    showSnackbar(view, getString(R.string.snackbar_not_max));
                    return;
                }

                meet.setName(name);
                meet.setQuantity(Double.parseDouble(quantity));

                ContentValues contentValues = new ContentValues();
                contentValues.put(NAME, name);
                contentValues.put(QUANTITY, quantity);
                contentValues.put(IS_CHECKED, isChecked);

                Intent intent = new Intent();
                if (isUpdate){
                    Uri uri = Uri.parse(CONTENT_URI_MEET + "/" + meet.getId());
                    getContentResolver().update(uri, contentValues, null, null);
                    setResult(RESULT_UPDATE, intent);
                } else{
                    getContentResolver().insert(CONTENT_URI_MEET, contentValues);
                    setResult(RESULT_ADD, intent);
                }
                finish();
                break;

            case R.id.btn_delete_mau:
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setMessage(R.string.dialog_delete_mau);
                dialog.setPositiveButton(R.string.dialog_cancel, null);
                dialog.setNegativeButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Uri uri = Uri.parse(CONTENT_URI_MEET + "/" + meet.getId());
                                getContentResolver().delete(uri, null, null);

                                Intent intentDelete = new Intent();
                                setResult(RESULT_DELETE, intentDelete);
                                finish();
                            }
                        });
                dialog.create().show();
                break;

            case R.id.btn_back_mau:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed(){
        String dialogMessage;
        if (isUpdate) dialogMessage = getString(R.string.dialog_cancel_update_mau);
        else dialogMessage = getString(R.string.dialog_cancel_add_mau);

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(dialogMessage);
        dialog.setPositiveButton(R.string.dialog_no, null);
        dialog.setNegativeButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MeetAddUpdateActivity.super.onBackPressed();
                    }
                });
        dialog.create().show();
    }
}
