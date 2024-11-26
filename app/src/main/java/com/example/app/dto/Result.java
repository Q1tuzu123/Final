package com.example.app.dto;

import android.os.Parcel;
import android.os.Parcelable;

public class Result implements Parcelable {
    private int id;
    private String title;
    private String image;

    // Constructor
    public Result(int id, String title, String image) {
        this.id = id;
        this.title = title;
        this.image = image;
    }

    protected Result(Parcel in) {
        id = in.readInt();
        title = in.readString();
        image = in.readString();
    }

    public static final Creator<Result> CREATOR = new Creator<Result>() {
        @Override
        public Result createFromParcel(Parcel in) {
            return new Result(in);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(image);
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }
}

