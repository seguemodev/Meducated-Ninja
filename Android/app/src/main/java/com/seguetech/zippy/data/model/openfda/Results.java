package com.seguetech.zippy.data.model.openfda;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class Results implements Parcelable {

    @Expose
    private Integer skip;
    @Expose
    private Integer limit;
    @Expose
    private Integer total;

    /**
     * @return The skip
     */
    public Integer getSkip() {
        return skip;
    }

    /**
     * @param skip The skip
     */
    public void setSkip(Integer skip) {
        this.skip = skip;
    }

    /**
     * @return The limit
     */
    public Integer getLimit() {
        return limit;
    }

    /**
     * @param limit The limit
     */
    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    /**
     * @return The total
     */
    public Integer getTotal() {
        return total;
    }

    /**
     * @param total The total
     */
    public void setTotal(Integer total) {
        this.total = total;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.skip);
        dest.writeValue(this.limit);
        dest.writeValue(this.total);
    }

    public Results() {
    }

    protected Results(Parcel in) {
        this.skip = (Integer) in.readValue(Integer.class.getClassLoader());
        this.limit = (Integer) in.readValue(Integer.class.getClassLoader());
        this.total = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<Results> CREATOR = new Parcelable.Creator<Results>() {
        public Results createFromParcel(Parcel source) {
            return new Results(source);
        }

        public Results[] newArray(int size) {
            return new Results[size];
        }
    };
}
