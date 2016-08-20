package com.kit.cn.smartkit.db_sample;

import android.os.Parcel;
import android.os.Parcelable;

import com.kit.cn.library.db.ORM.annotation.PrimaryKey;
import com.kit.cn.library.db.ORM.annotation.TableName;


/**
 * @author chowen
 * @version 0.1
 * @since 16/8/12
 */
@TableName(table = "orm_list")
public class OrmInfo implements Parcelable{

    @PrimaryKey(column = "id")
    public String id;

    public String name;

    public String sex;

    public String company;

    public void setName(String name) {
        this.name = name;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public String getSex() {
        return sex;
    }

    public String getCompany() {
        return company;
    }

    public OrmInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.sex);
        dest.writeString(this.company);
    }

    protected OrmInfo(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.sex = in.readString();
        this.company = in.readString();
    }

    public static final Creator<OrmInfo> CREATOR = new Creator<OrmInfo>() {
        @Override
        public OrmInfo createFromParcel(Parcel source) {
            return new OrmInfo(source);
        }

        @Override
        public OrmInfo[] newArray(int size) {
            return new OrmInfo[size];
        }
    };
}
