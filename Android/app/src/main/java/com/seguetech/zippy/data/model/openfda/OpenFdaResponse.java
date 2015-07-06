package com.seguetech.zippy.data.model.openfda;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class OpenFdaResponse implements Parcelable {

    @Expose
    private Meta meta;
    @Expose
    private List<Result> results = new ArrayList<>();

    /**
     * @return The meta
     */
    public Meta getMeta() {
        return meta;
    }

    /**
     * @param meta The meta
     */
    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    /**
     * @return The results
     */
    public List<Result> getResults() {
        return results;
    }

    /**
     * @param results The results
     */
    public void setResults(List<Result> results) {
        this.results = results;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.meta, 0);
        dest.writeTypedList(results);
    }

    public OpenFdaResponse() {
    }

    protected OpenFdaResponse(Parcel in) {
        this.meta = in.readParcelable(Meta.class.getClassLoader());
        this.results = in.createTypedArrayList(Result.CREATOR);
    }

    public static final Parcelable.Creator<OpenFdaResponse> CREATOR = new Parcelable.Creator<OpenFdaResponse>() {
        public OpenFdaResponse createFromParcel(Parcel source) {
            return new OpenFdaResponse(source);
        }

        public OpenFdaResponse[] newArray(int size) {
            return new OpenFdaResponse[size];
        }
    };
}
