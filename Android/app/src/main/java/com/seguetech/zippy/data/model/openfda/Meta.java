package com.seguetech.zippy.data.model.openfda;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Meta implements Parcelable {

    @Expose
    private String disclaimer;
    @Expose
    private String license;
    @SerializedName("last_updated")
    @Expose
    private String lastUpdated;
    @Expose
    private Results results;

    /**
     * @return The disclaimer
     */
    public String getDisclaimer() {
        return disclaimer;
    }

    /**
     * @param disclaimer The disclaimer
     */
    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }

    /**
     * @return The license
     */
    public String getLicense() {
        return license;
    }

    /**
     * @param license The license
     */
    public void setLicense(String license) {
        this.license = license;
    }

    /**
     * @return The lastUpdated
     */
    public String getLastUpdated() {
        return lastUpdated;
    }

    /**
     * @param lastUpdated The last_updated
     */
    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    /**
     * @return The results
     */
    public Results getResults() {
        return results;
    }

    /**
     * @param results The results
     */
    public void setResults(Results results) {
        this.results = results;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.disclaimer);
        dest.writeString(this.license);
        dest.writeString(this.lastUpdated);
        dest.writeParcelable(this.results, flags);
    }

    public Meta() {
    }

    protected Meta(Parcel in) {
        this.disclaimer = in.readString();
        this.license = in.readString();
        this.lastUpdated = in.readString();
        this.results = in.readParcelable(Results.class.getClassLoader());
    }

    public static final Parcelable.Creator<Meta> CREATOR = new Parcelable.Creator<Meta>() {
        public Meta createFromParcel(Parcel source) {
            return new Meta(source);
        }

        public Meta[] newArray(int size) {
            return new Meta[size];
        }
    };
}
