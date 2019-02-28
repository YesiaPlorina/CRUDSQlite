package com.yesia.crudsqlite.model;

import android.os.Parcel;
import android.os.Parcelable;

public class DataDiri implements Parcelable {

    private String name, gender, age, weight, date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private int id;

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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.gender);
        dest.writeString(this.age);
        dest.writeString(this.weight);
        dest.writeString(this.date);
        dest.writeInt(this.id);
    }

    public DataDiri() {
    }

    protected DataDiri(Parcel in) {
        this.name = in.readString();
        this.gender = in.readString();
        this.age = in.readString();
        this.weight = in.readString();
        this.date = in.readString();
        this.id = in.readInt();
    }

    public static final Parcelable.Creator<DataDiri> CREATOR = new Parcelable.Creator<DataDiri>() {
        @Override
        public DataDiri createFromParcel(Parcel source) {
            return new DataDiri(source);
        }

        @Override
        public DataDiri[] newArray(int size) {
            return new DataDiri[size];
        }
    };
}
