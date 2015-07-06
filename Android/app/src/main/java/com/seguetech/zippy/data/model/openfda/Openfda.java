package com.seguetech.zippy.data.model.openfda;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class Openfda implements Parcelable {

    @Expose
    private List<String> unii = new ArrayList<>();
    @SerializedName("spl_id")
    @Expose
    private List<String> splId = new ArrayList<>();
    @SerializedName("product_ndc")
    @Expose
    private List<String> productNdc = new ArrayList<>();
    @SerializedName("substance_name")
    @Expose
    private List<String> substanceName = new ArrayList<>();
    @Expose
    private List<String> rxcui = new ArrayList<>();
    @SerializedName("spl_set_id")
    @Expose
    private List<String> splSetId = new ArrayList<>();
    @SerializedName("original_packager_product_ndc")
    @Expose
    private List<String> originalPackagerProductNdc = new ArrayList<>();
    @SerializedName("product_type")
    @Expose
    private List<String> productType = new ArrayList<>();
    @SerializedName("pharm_class_cs")
    @Expose
    private List<String> pharmClassCs = new ArrayList<>();
    @SerializedName("manufacturer_name")
    @Expose
    private List<String> manufacturerName = new ArrayList<>();
    @SerializedName("brand_name")
    @Expose
    private List<String> brandName = new ArrayList<>();
    @Expose
    private List<String> route = new ArrayList<>();
    @Expose
    private List<String> nui = new ArrayList<>();
    @SerializedName("package_ndc")
    @Expose
    private List<String> packageNdc = new ArrayList<>();
    @SerializedName("pharm_class_epc")
    @Expose
    private List<String> pharmClassEpc = new ArrayList<>();
    @SerializedName("generic_name")
    @Expose
    private List<String> genericName = new ArrayList<>();
    @SerializedName("application_number")
    @Expose
    private List<String> applicationNumber = new ArrayList<>();

    /**
     * @return The unii
     */
    public List<String> getUnii() {
        return unii;
    }

    /**
     * @param unii The unii
     */
    public void setUnii(List<String> unii) {
        this.unii = unii;
    }

    /**
     * @return The splId
     */
    public List<String> getSplId() {
        return splId;
    }

    /**
     * @param splId The spl_id
     */
    public void setSplId(List<String> splId) {
        this.splId = splId;
    }

    /**
     * @return The productNdc
     */
    public List<String> getProductNdc() {
        return productNdc;
    }

    /**
     * @param productNdc The product_ndc
     */
    public void setProductNdc(List<String> productNdc) {
        this.productNdc = productNdc;
    }

    /**
     * @return The substanceName
     */
    public List<String> getSubstanceName() {
        return substanceName;
    }

    /**
     * @param substanceName The substance_name
     */
    public void setSubstanceName(List<String> substanceName) {
        this.substanceName = substanceName;
    }

    /**
     * @return The rxcui
     */
    public List<String> getRxcui() {
        return rxcui;
    }

    /**
     * @param rxcui The rxcui
     */
    public void setRxcui(List<String> rxcui) {
        this.rxcui = rxcui;
    }

    /**
     * @return The splSetId
     */
    public List<String> getSplSetId() {
        return splSetId;
    }

    /**
     * @param splSetId The spl_set_id
     */
    public void setSplSetId(List<String> splSetId) {
        this.splSetId = splSetId;
    }

    /**
     * @return The originalPackagerProductNdc
     */
    public List<String> getOriginalPackagerProductNdc() {
        return originalPackagerProductNdc;
    }

    /**
     * @param originalPackagerProductNdc The original_packager_product_ndc
     */
    public void setOriginalPackagerProductNdc(List<String> originalPackagerProductNdc) {
        this.originalPackagerProductNdc = originalPackagerProductNdc;
    }

    /**
     * @return The productType
     */
    public List<String> getProductType() {
        return productType;
    }

    /**
     * @param productType The product_type
     */
    public void setProductType(List<String> productType) {
        this.productType = productType;
    }

    /**
     * @return The pharmClassCs
     */
    public List<String> getPharmClassCs() {
        return pharmClassCs;
    }

    /**
     * @param pharmClassCs The pharm_class_cs
     */
    public void setPharmClassCs(List<String> pharmClassCs) {
        this.pharmClassCs = pharmClassCs;
    }

    /**
     * @return The manufacturerName
     */
    public List<String> getManufacturerName() {
        return manufacturerName;
    }

    /**
     * @param manufacturerName The manufacturer_name
     */
    public void setManufacturerName(List<String> manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    /**
     * @return The brandName
     */
    public List<String> getBrandName() {
        return brandName;
    }

    /**
     * @param brandName The brand_name
     */
    public void setBrandName(List<String> brandName) {
        this.brandName = brandName;
    }

    /**
     * @return The route
     */
    public List<String> getRoute() {
        return route;
    }

    /**
     * @param route The route
     */
    public void setRoute(List<String> route) {
        this.route = route;
    }

    /**
     * @return The nui
     */
    public List<String> getNui() {
        return nui;
    }

    /**
     * @param nui The nui
     */
    public void setNui(List<String> nui) {
        this.nui = nui;
    }

    /**
     * @return The packageNdc
     */
    public List<String> getPackageNdc() {
        return packageNdc;
    }

    /**
     * @param packageNdc The package_ndc
     */
    public void setPackageNdc(List<String> packageNdc) {
        this.packageNdc = packageNdc;
    }

    /**
     * @return The pharmClassEpc
     */
    public List<String> getPharmClassEpc() {
        return pharmClassEpc;
    }

    /**
     * @param pharmClassEpc The pharm_class_epc
     */
    public void setPharmClassEpc(List<String> pharmClassEpc) {
        this.pharmClassEpc = pharmClassEpc;
    }

    /**
     * @return The genericName
     */
    public List<String> getGenericName() {
        return genericName;
    }

    /**
     * @param genericName The generic_name
     */
    public void setGenericName(List<String> genericName) {
        this.genericName = genericName;
    }

    /**
     * @return The applicationNumber
     */
    public List<String> getApplicationNumber() {
        return applicationNumber;
    }

    /**
     * @param applicationNumber The application_number
     */
    public void setApplicationNumber(List<String> applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.unii);
        dest.writeStringList(this.splId);
        dest.writeStringList(this.productNdc);
        dest.writeStringList(this.substanceName);
        dest.writeStringList(this.rxcui);
        dest.writeStringList(this.splSetId);
        dest.writeStringList(this.originalPackagerProductNdc);
        dest.writeStringList(this.productType);
        dest.writeStringList(this.pharmClassCs);
        dest.writeStringList(this.manufacturerName);
        dest.writeStringList(this.brandName);
        dest.writeStringList(this.route);
        dest.writeStringList(this.nui);
        dest.writeStringList(this.packageNdc);
        dest.writeStringList(this.pharmClassEpc);
        dest.writeStringList(this.genericName);
        dest.writeStringList(this.applicationNumber);
    }

    public Openfda() {
    }

    protected Openfda(Parcel in) {
        this.unii = in.createStringArrayList();
        this.splId = in.createStringArrayList();
        this.productNdc = in.createStringArrayList();
        this.substanceName = in.createStringArrayList();
        this.rxcui = in.createStringArrayList();
        this.splSetId = in.createStringArrayList();
        this.originalPackagerProductNdc = in.createStringArrayList();
        this.productType = in.createStringArrayList();
        this.pharmClassCs = in.createStringArrayList();
        this.manufacturerName = in.createStringArrayList();
        this.brandName = in.createStringArrayList();
        this.route = in.createStringArrayList();
        this.nui = in.createStringArrayList();
        this.packageNdc = in.createStringArrayList();
        this.pharmClassEpc = in.createStringArrayList();
        this.genericName = in.createStringArrayList();
        this.applicationNumber = in.createStringArrayList();
    }

    public static final Parcelable.Creator<Openfda> CREATOR = new Parcelable.Creator<Openfda>() {
        public Openfda createFromParcel(Parcel source) {
            return new Openfda(source);
        }

        public Openfda[] newArray(int size) {
            return new Openfda[size];
        }
    };
}
