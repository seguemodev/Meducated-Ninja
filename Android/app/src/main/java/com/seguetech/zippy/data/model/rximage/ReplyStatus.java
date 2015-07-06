
package com.seguetech.zippy.data.model.rximage;

public class ReplyStatus {
    private Boolean success;
    private String date;
    private Integer imageCount;
    private Integer totalImageCount;
    private MatchedTerms matchedTerms;

    public Boolean getSuccess() {
        return success;
    }
    public void setSuccess(Boolean success) {
        this.success = success;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public Integer getImageCount() {
        return imageCount;
    }
    public void setImageCount(Integer imageCount) {
        this.imageCount = imageCount;
    }
    public Integer getTotalImageCount() {
        return totalImageCount;
    }
    public void setTotalImageCount(Integer totalImageCount) {
        this.totalImageCount = totalImageCount;
    }
    public MatchedTerms getMatchedTerms() {
        return matchedTerms;
    }
    public void setMatchedTerms(MatchedTerms matchedTerms) {
        this.matchedTerms = matchedTerms;
    }

}
