package com.ariefzuhri.amigo19.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ariefzuhri.amigo19.R;
import com.ariefzuhri.amigo19.activity.MeetAddUpdateActivity;
import com.ariefzuhri.amigo19.activity.ShoppingModeActivity;
import com.ariefzuhri.amigo19.model.Meet;
import java.util.ArrayList;
import static com.ariefzuhri.amigo19.activity.MeetAddUpdateActivity.EXTRA_MEET;
import static com.ariefzuhri.amigo19.activity.MeetAddUpdateActivity.REQUEST_ADD;
import static com.ariefzuhri.amigo19.activity.MeetAddUpdateActivity.REQUEST_UPDATE;
import static com.ariefzuhri.amigo19.database.DatabaseContract.MeetColumns.CONTENT_URI_MEET;
import static com.ariefzuhri.amigo19.database.DatabaseContract.MeetColumns.IS_CHECKED;
import static com.ariefzuhri.amigo19.database.DatabaseContract.MeetColumns.NAME;
import static com.ariefzuhri.amigo19.database.DatabaseContract.MeetColumns.QUANTITY;

public class MeetAdapter extends RecyclerView.Adapter<MeetAdapter.MeetViewHolder> {
    private static final int VIEW_TYPE_FOOTER = 0;
    private static final int VIEW_TYPE_CELL = 1;

    private Activity activity;
    private AlertDialog.Builder dialog;
    private ArrayList<Meet> listMeets = new ArrayList<>();

    public MeetAdapter(Activity activity){
        this.activity = activity;
        dialog = new AlertDialog.Builder(activity);
    }

    public void setData(ArrayList<Meet> meets){
        listMeets = meets;
        notifyDataSetChanged();
    }

    public ArrayList<Meet> getData(){
        return listMeets;
    }

    @Override
    public int getItemViewType(int position){
        return (position == listMeets.size()) ? VIEW_TYPE_FOOTER : VIEW_TYPE_CELL;
    }

    @NonNull
    @Override
    public MeetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        int layoutMain, layoutDetail;
        if (activity.getLocalClassName().equals("activity." + ShoppingModeActivity.class.getSimpleName())){
            layoutMain = R.layout.item_meet;
            layoutDetail = R.layout.item_meet_add;
        } else {
            layoutMain = R.layout.item_meet_detail;
            layoutDetail = R.layout.item_meet_detail_add;
        }

        if (viewType == VIEW_TYPE_CELL) view = LayoutInflater.from(parent.getContext()).inflate(layoutMain, parent, false);
        else view = LayoutInflater.from(parent.getContext()).inflate(layoutDetail, parent, false);

        return new MeetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MeetViewHolder holder, int position) {
        if (getItemViewType(position)==VIEW_TYPE_CELL){
            final Meet meet = listMeets.get(position);
            holder.bind(meet);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu menu = new PopupMenu(activity, view);
                    MenuInflater inflater = menu.getMenuInflater();
                    inflater.inflate(R.menu.menu_meet, menu.getMenu());

                    if (meet.isChecked()) menu.getMenu().getItem(0).setTitle(R.string.unchecked);
                    else menu.getMenu().getItem(0).setTitle(R.string.checked);

                    menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Intent intent;

                            switch (item.getItemId()){
                                case R.id.menu_check_meet:
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put(NAME, meet.getName());
                                    contentValues.put(QUANTITY, meet.getQuantity());
                                    contentValues.put(IS_CHECKED, !meet.isChecked());

                                    Uri uri = Uri.parse(CONTENT_URI_MEET + "/" + meet.getId());
                                    activity.getContentResolver().update(uri, contentValues, null, null);
                                    break;

                                case R.id.menu_update_meet:
                                    intent = new Intent(activity, MeetAddUpdateActivity.class);
                                    intent.putExtra(EXTRA_MEET, meet);
                                    activity.startActivityForResult(intent, REQUEST_UPDATE);
                                    break;

                                case R.id.menu_delete_meet:
                                    dialog.setMessage(R.string.dialog_delete_meet);
                                    dialog.setPositiveButton(R.string.dialog_cancel, null);
                                    dialog.setNegativeButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Uri uri = Uri.parse(CONTENT_URI_MEET + "/" + meet.getId());
                                                    activity.getContentResolver().delete(uri, null, null);
                                                }
                                            });
                                    dialog.create().show();
                                    break;
                            }

                            return false;
                        }
                    });
                    menu.show();
                }
            });
        } else {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, MeetAddUpdateActivity.class);
                    activity.startActivityForResult(intent, REQUEST_ADD);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listMeets.size()+1;
    }

    static class MeetViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvQuantity;
        ImageView imgCheckBox;

        MeetViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name_meet_home);
            tvQuantity = itemView.findViewById(R.id.tv_quantity_meet_home);
            imgCheckBox = itemView.findViewById(R.id.img_check_box_meet_home);
        }

        void bind(Meet meet) {
            tvName.setText(meet.getName());
            tvQuantity.setText(String.valueOf(meet.getQuantity()));

            if (meet.isChecked()) imgCheckBox.setImageResource(R.drawable.ic_check_box_white_24dp);
            else imgCheckBox.setImageResource(R.drawable.ic_check_box_outline_blank_white_24dp);
        }
    }
}
