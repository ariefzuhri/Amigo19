package com.ariefzuhri.amigo19.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Meet implements Parcelable {
    private int id;
    private String name;
    private double quantity;
    private boolean isChecked;

    public Meet(int id, String name, double quantity, boolean isChecked) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.isChecked = isChecked;
    }

    public Meet(){}

    private Meet(Parcel in) {
        id = in.readInt();
        name = in.readString();
        quantity = in.readDouble();
        isChecked = in.readByte() != 0;
    }

    public static final Creator<Meet> CREATOR = new Creator<Meet>() {
        @Override
        public Meet createFromParcel(Parcel in) {
            return new Meet(in);
        }

        @Override
        public Meet[] newArray(int size) {
            return new Meet[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeDouble(quantity);
        dest.writeByte((byte) (isChecked ? 1 : 0));
    }
}
