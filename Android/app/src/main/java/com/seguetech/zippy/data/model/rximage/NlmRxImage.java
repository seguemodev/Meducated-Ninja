
package com.seguetech.zippy.data.model.rximage;

import java.util.ArrayList;
import java.util.List;

public class NlmRxImage {

    private Integer id;
    private String ndc11;
    private Integer part;
    private String matchNdc;
    private List<RelabelersNdc9> relabelersNdc9 = new ArrayList<>();
    private String status;
    private Integer rxcui;
    private String splSetId;
    private String acqDate;
    private String name;
    private String labeler;
    private String imageUrl;
    private Integer imageSize;
    private String attribution;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getNdc11() {
        return ndc11;
    }
    public void setNdc11(String ndc11) {
        this.ndc11 = ndc11;
    }
    public Integer getPart() {
        return part;
    }
    public void setPart(Integer part) {
        this.part = part;
    }
    public String getMatchNdc() {
        return matchNdc;
    }
    public void setMatchNdc(String matchNdc) {
        this.matchNdc = matchNdc;
    }
    public List<RelabelersNdc9> getRelabelersNdc9() {
        return relabelersNdc9;
    }
    public void setRelabelersNdc9(List<RelabelersNdc9> relabelersNdc9) {
        this.relabelersNdc9 = relabelersNdc9;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Integer getRxcui() {
        return rxcui;
    }
    public void setRxcui(Integer rxcui) {
        this.rxcui = rxcui;
    }
    public String getSplSetId() {
        return splSetId;
    }
    public void setSplSetId(String splSetId) {
        this.splSetId = splSetId;
    }
    public String getAcqDate() {
        return acqDate;
    }
    public void setAcqDate(String acqDate) {
        this.acqDate = acqDate;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLabeler() {
        return labeler;
    }
    public void setLabeler(String labeler) {
        this.labeler = labeler;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public Integer getImageSize() {
        return imageSize;
    }
    public void setImageSize(Integer imageSize) {
        this.imageSize = imageSize;
    }
    public String getAttribution() {
        return attribution;
    }
    public void setAttribution(String attribution) {
        this.attribution = attribution;
    }

}
