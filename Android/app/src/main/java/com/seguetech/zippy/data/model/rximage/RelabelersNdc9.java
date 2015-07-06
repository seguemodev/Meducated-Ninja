
package com.seguetech.zippy.data.model.rximage;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RelabelersNdc9 {

    @SerializedName("@sourceNdc9")
    private String SourceNdc9;
    private List<String> ndc9 = new ArrayList<>();

    public String getSourceNdc9() {
        return SourceNdc9;
    }
    public void setSourceNdc9(String SourceNdc9) {
        this.SourceNdc9 = SourceNdc9;
    }
    public List<String> getNdc9() {
        return ndc9;
    }
    public void setNdc9(List<String> ndc9) {
        this.ndc9 = ndc9;
    }

}
