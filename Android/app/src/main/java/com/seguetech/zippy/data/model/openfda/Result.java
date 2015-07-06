package com.seguetech.zippy.data.model.openfda;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class Result implements Parcelable {

    /**
     * The Set ID, A globally unique identifier (GUID) for the labeling, stable across all versions or revisions.
     */
    @SerializedName("set_id")
    @Expose
    private String setId;

    /**
     * The document ID, A globally unique identifier (GUID) for the particular revision of a labeling document.
     */
    @Expose
    private String id;

    /**
     * A sequentially increasing number identifying the particular version of a document, starting with 1.
     */
    @Expose
    private String version;

    /**
     * string date YYYYmmdd
     * Date reference to the particular version of the labeling document.
     */
    @SerializedName("effective_time")
    @Expose
    private String effectiveTime;


    @SerializedName("information_for_patients")
    @Expose
    private List<String> informationForPatients = new ArrayList<>();
    @SerializedName("dosage_and_administration_table")
    @Expose
    private List<String> dosageAndAdministrationTable = new ArrayList<>();
    @SerializedName("indications_and_usage")
    @Expose
    private List<String> indicationsAndUsage = new ArrayList<>();
    @Expose
    private List<String> contraindications = new ArrayList<>();
    @SerializedName("how_supplied")
    @Expose
    private List<String> howSupplied = new ArrayList<>();
    @SerializedName("description_table")
    @Expose
    private List<String> descriptionTable = new ArrayList<>();
    @SerializedName("dosage_and_administration")
    @Expose
    private List<String> dosageAndAdministration = new ArrayList<>();

    @SerializedName("package_label_principal_display_panel")
    @Expose
    private List<String> packageLabelPrincipalDisplayPanel = new ArrayList<>();
    @SerializedName("@epoch")
    @Expose
    private Double Epoch;
    @Expose
    private List<String> description = new ArrayList<>();
    @SerializedName("pediatric_use")
    @Expose
    private List<String> pediatricUse = new ArrayList<>();
    @SerializedName("precautions_table")
    @Expose
    private List<String> precautionsTable = new ArrayList<>();
    @Expose
    private Openfda openfda;
    @SerializedName("spl_product_data_elements")
    @Expose
    private List<String> splProductDataElements = new ArrayList<>();
    @Expose
    private List<String> warnings = new ArrayList<>();
    @SerializedName("geriatric_use")
    @Expose
    private List<String> geriatricUse = new ArrayList<>();
    @SerializedName("adverse_reactions")
    @Expose
    private List<String> adverseReactions = new ArrayList<>();
    @Expose
    private List<String> overdosage = new ArrayList<>();
    @SerializedName("general_precautions")
    @Expose
    private List<String> generalPrecautions = new ArrayList<>();
    @SerializedName("drug_interactions")
    @Expose
    private List<String> drugInteractions = new ArrayList<>();
    @SerializedName("drug_interactions_table")
    @Expose
    private List<String> drugInteractionsTable = new ArrayList<>();
    @SerializedName("carcinogenesis_and_mutagenesis_and_impairment_of_fertility")
    @Expose
    private List<String> carcinogenesisAndMutagenesisAndImpairmentOfFertility = new ArrayList<>();
    @Expose
    private List<String> pregnancy = new ArrayList<>();
    @SerializedName("drug_and_or_laboratory_test_interactions")
    @Expose
    private List<String> drugAndOrLaboratoryTestInteractions = new ArrayList<>();
    @Expose
    private List<String> precautions = new ArrayList<>();
    @SerializedName("nursing_mothers")
    @Expose
    private List<String> nursingMothers = new ArrayList<>();
    @SerializedName("laboratory_tests")
    @Expose
    private List<String> laboratoryTests = new ArrayList<>();
    @SerializedName("boxed_warning")
    @Expose
    private List<String> boxedWarning = new ArrayList<>();
    @SerializedName("clinical_pharmacology")
    @Expose
    private List<String> clinicalPharmacology = new ArrayList<>();
    @SerializedName("clinical_pharmacology_table")
    @Expose
    private List<String> clinicalPharmacologyTable = new ArrayList<>();
    @SerializedName("how_supplied_table")
    @Expose
    private List<String> howSuppliedTable = new ArrayList<>();

    /**
     * @return The informationForPatients
     */
    public List<String> getInformationForPatients() {
        return informationForPatients;
    }

    /**
     * @param informationForPatients The information_for_patients
     */
    public void setInformationForPatients(List<String> informationForPatients) {
        this.informationForPatients = informationForPatients;
    }

    /**
     * @return The dosageAndAdministrationTable
     */
    public List<String> getDosageAndAdministrationTable() {
        return dosageAndAdministrationTable;
    }

    /**
     * @param dosageAndAdministrationTable The dosage_and_administration_table
     */
    public void setDosageAndAdministrationTable(List<String> dosageAndAdministrationTable) {
        this.dosageAndAdministrationTable = dosageAndAdministrationTable;
    }

    /**
     * @return The indicationsAndUsage
     */
    public List<String> getIndicationsAndUsage() {
        return indicationsAndUsage;
    }

    /**
     * @param indicationsAndUsage The indications_and_usage
     */
    public void setIndicationsAndUsage(List<String> indicationsAndUsage) {
        this.indicationsAndUsage = indicationsAndUsage;
    }

    /**
     * @return The contraindications
     */
    public List<String> getContraindications() {
        return contraindications;
    }

    /**
     * @param contraindications The contraindications
     */
    public void setContraindications(List<String> contraindications) {
        this.contraindications = contraindications;
    }

    /**
     * @return The howSupplied
     */
    public List<String> getHowSupplied() {
        return howSupplied;
    }

    /**
     * @param howSupplied The how_supplied
     */
    public void setHowSupplied(List<String> howSupplied) {
        this.howSupplied = howSupplied;
    }

    /**
     * @return The descriptionTable
     */
    public List<String> getDescriptionTable() {
        return descriptionTable;
    }

    /**
     * @param descriptionTable The description_table
     */
    public void setDescriptionTable(List<String> descriptionTable) {
        this.descriptionTable = descriptionTable;
    }

    /**
     * @return The dosageAndAdministration
     */
    public List<String> getDosageAndAdministration() {
        return dosageAndAdministration;
    }

    /**
     * @param dosageAndAdministration The dosage_and_administration
     */
    public void setDosageAndAdministration(List<String> dosageAndAdministration) {
        this.dosageAndAdministration = dosageAndAdministration;
    }

    /**
     * @return The version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version The version
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * @return The id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return The packageLabelPrincipalDisplayPanel
     */
    public List<String> getPackageLabelPrincipalDisplayPanel() {
        return packageLabelPrincipalDisplayPanel;
    }

    /**
     * @param packageLabelPrincipalDisplayPanel The package_label_principal_display_panel
     */
    public void setPackageLabelPrincipalDisplayPanel(List<String> packageLabelPrincipalDisplayPanel) {
        this.packageLabelPrincipalDisplayPanel = packageLabelPrincipalDisplayPanel;
    }

    /**
     * @return The Epoch
     */
    public Double getEpoch() {
        return Epoch;
    }

    /**
     * @param Epoch The @epoch
     */
    public void setEpoch(Double Epoch) {
        this.Epoch = Epoch;
    }

    /**
     * @return The description
     */
    public List<String> getDescription() {
        return description;
    }

    /**
     * @param description The description
     */
    public void setDescription(List<String> description) {
        this.description = description;
    }

    /**
     * @return The pediatricUse
     */
    public List<String> getPediatricUse() {
        return pediatricUse;
    }

    /**
     * @param pediatricUse The pediatric_use
     */
    public void setPediatricUse(List<String> pediatricUse) {
        this.pediatricUse = pediatricUse;
    }

    /**
     * @return The precautionsTable
     */
    public List<String> getPrecautionsTable() {
        return precautionsTable;
    }

    /**
     * @param precautionsTable The precautions_table
     */
    public void setPrecautionsTable(List<String> precautionsTable) {
        this.precautionsTable = precautionsTable;
    }

    /**
     * @return The openfda
     */
    public Openfda getOpenfda() {
        return openfda;
    }

    /**
     * @param openfda The openfda
     */
    public void setOpenfda(Openfda openfda) {
        this.openfda = openfda;
    }

    /**
     * @return The splProductDataElements
     */
    public List<String> getSplProductDataElements() {
        return splProductDataElements;
    }

    /**
     * @param splProductDataElements The spl_product_data_elements
     */
    public void setSplProductDataElements(List<String> splProductDataElements) {
        this.splProductDataElements = splProductDataElements;
    }

    /**
     * @return The warnings
     */
    public List<String> getWarnings() {
        return warnings;
    }

    /**
     * @param warnings The warnings
     */
    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }

    /**
     * @return The setId
     */
    public String getSetId() {
        return setId;
    }

    /**
     * @param setId The set_id
     */
    public void setSetId(String setId) {
        this.setId = setId;
    }

    /**
     * @return The geriatricUse
     */
    public List<String> getGeriatricUse() {
        return geriatricUse;
    }

    /**
     * @param geriatricUse The geriatric_use
     */
    public void setGeriatricUse(List<String> geriatricUse) {
        this.geriatricUse = geriatricUse;
    }

    /**
     * @return The adverseReactions
     */
    public List<String> getAdverseReactions() {
        return adverseReactions;
    }

    /**
     * @param adverseReactions The adverse_reactions
     */
    public void setAdverseReactions(List<String> adverseReactions) {
        this.adverseReactions = adverseReactions;
    }

    /**
     * @return The overdosage
     */
    public List<String> getOverdosage() {
        return overdosage;
    }

    /**
     * @param overdosage The overdosage
     */
    public void setOverdosage(List<String> overdosage) {
        this.overdosage = overdosage;
    }

    /**
     * @return The generalPrecautions
     */
    public List<String> getGeneralPrecautions() {
        return generalPrecautions;
    }

    /**
     * @param generalPrecautions The general_precautions
     */
    public void setGeneralPrecautions(List<String> generalPrecautions) {
        this.generalPrecautions = generalPrecautions;
    }

    /**
     * @return The drugInteractions
     */
    public List<String> getDrugInteractions() {
        return drugInteractions;
    }

    /**
     * @param drugInteractions The drug_interactions
     */
    public void setDrugInteractions(List<String> drugInteractions) {
        this.drugInteractions = drugInteractions;
    }

    /**
     * @return The drugInteractionsTable
     */
    public List<String> getDrugInteractionsTable() {
        return drugInteractionsTable;
    }

    /**
     * @param drugInteractionsTable The drug_interactions_table
     */
    public void setDrugInteractionsTable(List<String> drugInteractionsTable) {
        this.drugInteractionsTable = drugInteractionsTable;
    }

    /**
     * @return The carcinogenesisAndMutagenesisAndImpairmentOfFertility
     */
    public List<String> getCarcinogenesisAndMutagenesisAndImpairmentOfFertility() {
        return carcinogenesisAndMutagenesisAndImpairmentOfFertility;
    }

    /**
     * @param carcinogenesisAndMutagenesisAndImpairmentOfFertility The carcinogenesis_and_mutagenesis_and_impairment_of_fertility
     */
    public void setCarcinogenesisAndMutagenesisAndImpairmentOfFertility(List<String> carcinogenesisAndMutagenesisAndImpairmentOfFertility) {
        this.carcinogenesisAndMutagenesisAndImpairmentOfFertility = carcinogenesisAndMutagenesisAndImpairmentOfFertility;
    }

    /**
     * @return The effectiveTime
     */
    public String getEffectiveTime() {
        return effectiveTime;
    }

    /**
     * @param effectiveTime The effective_time
     */
    public void setEffectiveTime(String effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    /**
     * @return The pregnancy
     */
    public List<String> getPregnancy() {
        return pregnancy;
    }

    /**
     * @param pregnancy The pregnancy
     */
    public void setPregnancy(List<String> pregnancy) {
        this.pregnancy = pregnancy;
    }

    /**
     * @return The drugAndOrLaboratoryTestInteractions
     */
    public List<String> getDrugAndOrLaboratoryTestInteractions() {
        return drugAndOrLaboratoryTestInteractions;
    }

    /**
     * @param drugAndOrLaboratoryTestInteractions The drug_and_or_laboratory_test_interactions
     */
    public void setDrugAndOrLaboratoryTestInteractions(List<String> drugAndOrLaboratoryTestInteractions) {
        this.drugAndOrLaboratoryTestInteractions = drugAndOrLaboratoryTestInteractions;
    }

    /**
     * @return The precautions
     */
    public List<String> getPrecautions() {
        return precautions;
    }

    /**
     * @param precautions The precautions
     */
    public void setPrecautions(List<String> precautions) {
        this.precautions = precautions;
    }

    /**
     * @return The nursingMothers
     */
    public List<String> getNursingMothers() {
        return nursingMothers;
    }

    /**
     * @param nursingMothers The nursing_mothers
     */
    public void setNursingMothers(List<String> nursingMothers) {
        this.nursingMothers = nursingMothers;
    }

    /**
     * @return The laboratoryTests
     */
    public List<String> getLaboratoryTests() {
        return laboratoryTests;
    }

    /**
     * @param laboratoryTests The laboratory_tests
     */
    public void setLaboratoryTests(List<String> laboratoryTests) {
        this.laboratoryTests = laboratoryTests;
    }

    /**
     * @return The boxedWarning
     */
    public List<String> getBoxedWarning() {
        return boxedWarning;
    }

    /**
     * @param boxedWarning The boxed_warning
     */
    public void setBoxedWarning(List<String> boxedWarning) {
        this.boxedWarning = boxedWarning;
    }

    /**
     * @return The clinicalPharmacology
     */
    public List<String> getClinicalPharmacology() {
        return clinicalPharmacology;
    }

    /**
     * @param clinicalPharmacology The clinical_pharmacology
     */
    public void setClinicalPharmacology(List<String> clinicalPharmacology) {
        this.clinicalPharmacology = clinicalPharmacology;
    }

    /**
     * @return The clinicalPharmacologyTable
     */
    public List<String> getClinicalPharmacologyTable() {
        return clinicalPharmacologyTable;
    }

    /**
     * @param clinicalPharmacologyTable The clinical_pharmacology_table
     */
    public void setClinicalPharmacologyTable(List<String> clinicalPharmacologyTable) {
        this.clinicalPharmacologyTable = clinicalPharmacologyTable;
    }

    /**
     * @return The howSuppliedTable
     */
    public List<String> getHowSuppliedTable() {
        return howSuppliedTable;
    }

    /**
     * @param howSuppliedTable The how_supplied_table
     */
    public void setHowSuppliedTable(List<String> howSuppliedTable) {
        this.howSuppliedTable = howSuppliedTable;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Result result = (Result) o;

        return getSetId().equals(result.getSetId()) && getId().equals(result.getId()) && getVersion().equals(result.getVersion()) && getEffectiveTime().equals(result.getEffectiveTime());

    }

    @Override
    public int hashCode() {
        int result = getSetId().hashCode();
        result = 31 * result + getId().hashCode();
        result = 31 * result + getVersion().hashCode();
        result = 31 * result + getEffectiveTime().hashCode();
        return result;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.setId);
        dest.writeString(this.id);
        dest.writeString(this.version);
        dest.writeString(this.effectiveTime);
        dest.writeStringList(this.informationForPatients);
        dest.writeStringList(this.dosageAndAdministrationTable);
        dest.writeStringList(this.indicationsAndUsage);
        dest.writeStringList(this.contraindications);
        dest.writeStringList(this.howSupplied);
        dest.writeStringList(this.descriptionTable);
        dest.writeStringList(this.dosageAndAdministration);
        dest.writeStringList(this.packageLabelPrincipalDisplayPanel);
        dest.writeValue(this.Epoch);
        dest.writeStringList(this.description);
        dest.writeStringList(this.pediatricUse);
        dest.writeStringList(this.precautionsTable);
        dest.writeParcelable(this.openfda, flags);
        dest.writeStringList(this.splProductDataElements);
        dest.writeStringList(this.warnings);
        dest.writeStringList(this.geriatricUse);
        dest.writeStringList(this.adverseReactions);
        dest.writeStringList(this.overdosage);
        dest.writeStringList(this.generalPrecautions);
        dest.writeStringList(this.drugInteractions);
        dest.writeStringList(this.drugInteractionsTable);
        dest.writeStringList(this.carcinogenesisAndMutagenesisAndImpairmentOfFertility);
        dest.writeStringList(this.pregnancy);
        dest.writeStringList(this.drugAndOrLaboratoryTestInteractions);
        dest.writeStringList(this.precautions);
        dest.writeStringList(this.nursingMothers);
        dest.writeStringList(this.laboratoryTests);
        dest.writeStringList(this.boxedWarning);
        dest.writeStringList(this.clinicalPharmacology);
        dest.writeStringList(this.clinicalPharmacologyTable);
        dest.writeStringList(this.howSuppliedTable);
    }

    public Result() {
    }

    protected Result(Parcel in) {
        this.setId = in.readString();
        this.id = in.readString();
        this.version = in.readString();
        this.effectiveTime = in.readString();
        this.informationForPatients = in.createStringArrayList();
        this.dosageAndAdministrationTable = in.createStringArrayList();
        this.indicationsAndUsage = in.createStringArrayList();
        this.contraindications = in.createStringArrayList();
        this.howSupplied = in.createStringArrayList();
        this.descriptionTable = in.createStringArrayList();
        this.dosageAndAdministration = in.createStringArrayList();
        this.packageLabelPrincipalDisplayPanel = in.createStringArrayList();
        this.Epoch = (Double) in.readValue(Double.class.getClassLoader());
        this.description = in.createStringArrayList();
        this.pediatricUse = in.createStringArrayList();
        this.precautionsTable = in.createStringArrayList();
        this.openfda = in.readParcelable(Openfda.class.getClassLoader());
        this.splProductDataElements = in.createStringArrayList();
        this.warnings = in.createStringArrayList();
        this.geriatricUse = in.createStringArrayList();
        this.adverseReactions = in.createStringArrayList();
        this.overdosage = in.createStringArrayList();
        this.generalPrecautions = in.createStringArrayList();
        this.drugInteractions = in.createStringArrayList();
        this.drugInteractionsTable = in.createStringArrayList();
        this.carcinogenesisAndMutagenesisAndImpairmentOfFertility = in.createStringArrayList();
        this.pregnancy = in.createStringArrayList();
        this.drugAndOrLaboratoryTestInteractions = in.createStringArrayList();
        this.precautions = in.createStringArrayList();
        this.nursingMothers = in.createStringArrayList();
        this.laboratoryTests = in.createStringArrayList();
        this.boxedWarning = in.createStringArrayList();
        this.clinicalPharmacology = in.createStringArrayList();
        this.clinicalPharmacologyTable = in.createStringArrayList();
        this.howSuppliedTable = in.createStringArrayList();
    }

    public static final Parcelable.Creator<Result> CREATOR = new Parcelable.Creator<Result>() {
        public Result createFromParcel(Parcel source) {
            return new Result(source);
        }

        public Result[] newArray(int size) {
            return new Result[size];
        }
    };
}
