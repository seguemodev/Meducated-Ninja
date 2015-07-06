//
//  Medication.swift
//  Zippy
//
//  Created by Geoffrey Bender on 6/19/15.
//  Copyright (c) 2015 Segue Technologies, Inc. All rights reserved.
//

import UIKit

class Medication: NSObject, NSCoding
{
    // MARK: - Properties
    
    // The name of the medicine cabinet this medication is saved in
    var medicineCabinet: String?
    
    // ID and version
    var set_id: String?
    var document_id: String?
    var version: String?
    var effective_time: String?
    
    // Abuse and overdosage
    var drug_abuse_and_dependence: [String]?
    var controlled_substance: [String]?
    var abuse: [String]?
    var dependence: [String]?
    var overdosage: [String]?
    
    // Adverse effects and interactions
    var adverse_reactions: [String]?
    var drug_interactions: [String]?
    var drug_and_or_laboratory_test_interactions: [String]?
    
    // Clinical pharmacology
    var clinical_pharmacology: [String]?
    var mechanism_of_action: [String]?
    var pharmacodynamics: [String]?
    var pharmacokinetics: [String]?
    
    // Indications, usage, and dosage
    var indications_and_usage: [String]?
    var contraindications: [String]?
    var dosage_and_administration: [String]?
    var dosage_forms_and_strengths: [String]?
    var purpose: [String]?
    var product_description: [String]?
    var active_ingredient: [String]?
    var inactive_ingredient: [String]?
    var spl_product_data_elements: [String]?
    
    // Patient information
    var spl_patient_package_insert: [String]?
    var information_for_patients: [String]?
    var information_for_owners_or_caregivers: [String]?
    var instructions_for_use: [String]?
    var ask_doctor: [String]?
    var ask_doctor_or_pharmacist: [String]?
    var do_not_use: [String]?
    var keep_out_of_reach_of_children: [String]?
    var other_safety_information: [String]?
    var questions: [String]?
    var stop_use: [String]?
    var when_using: [String]?
    var patient_medication_information: [String]?
    var spl_medguide: [String]?
    
    // Special Populations
    var use_in_specific_populations: [String]?
    var pregnancy: [String]?
    var teratogenic_effects: [String]?
    var nonteratogenic_effects: [String]?
    var labor_and_delivery: [String]?
    var nursing_mothers: [String]?
    var pregnancy_or_breast_feeding: [String]?
    var pediatric_use: [String]?
    var geriatric_use: [String]?
    
    // Nonclinical toxicology
    var nonclinical_toxicology: [String]?
    var carcinogenesis_and_mutagenesis_and_impairment_of_fertility: [String]?
    var animal_pharmacology_and_or_toxicology: [String]?
    
    // References
    var clinical_studies: [String]?
    var references: [String]?
    
    // Supply, storage, and handling
    var how_supplied: [String]?
    var storage_and_handling: [String]?
    var safe_handling_warning: [String]?
    
    // Warnings and precautions
    var boxed_warning: [String]?
    var warnings_and_precautions: [String]?
    var user_safety_warnings: [String]?
    var precautions: [String]?
    var warnings: [String]?
    var general_precautions: [String]?
    
    // Other fields
    var laboratory_tests: [String]?
    var recent_major_changes: [String]?
    var microbiology: [String]?
    var package_label_principal_display_panel: [String]?
    var spl_unclassified_section: [String]?
    
    // Open FDA
    var openfda: NSDictionary?
    
    // MARK: - Methods
    
    required init(coder aDecoder: NSCoder)
    {
        // Saved medicine cabinet
        self.medicineCabinet = aDecoder.decodeObjectForKey("medicineCabinet") as? String
        
        // ID and version
        self.set_id = aDecoder.decodeObjectForKey("set_id") as? String
        self.document_id = aDecoder.decodeObjectForKey("document_id") as? String
        self.version = aDecoder.decodeObjectForKey("version") as? String
        self.effective_time = aDecoder.decodeObjectForKey("effective_time") as? String
        
        // Abuse and overdosage
        self.drug_abuse_and_dependence  = aDecoder.decodeObjectForKey("drug_abuse_and_dependence") as? [String]
        self.controlled_substance  = aDecoder.decodeObjectForKey("controlled_substance") as? [String]
        self.abuse  = aDecoder.decodeObjectForKey("abuse") as? [String]
        self.dependence  = aDecoder.decodeObjectForKey("dependence") as? [String]
        self.overdosage  = aDecoder.decodeObjectForKey("overdosage") as? [String]
        
        // Adverse effects and interactions
        self.adverse_reactions  = aDecoder.decodeObjectForKey("adverse_reactions") as? [String]
        self.drug_interactions  = aDecoder.decodeObjectForKey("drug_interactions") as? [String]
        self.drug_and_or_laboratory_test_interactions  = aDecoder.decodeObjectForKey("drug_and_or_laboratory_test_interactions") as? [String]
        
        // Clinical pharmacology
        self.clinical_pharmacology  = aDecoder.decodeObjectForKey("clinical_pharmacology") as? [String]
        self.mechanism_of_action  = aDecoder.decodeObjectForKey("mechanism_of_action") as? [String]
        self.pharmacodynamics  = aDecoder.decodeObjectForKey("pharmacodynamics") as? [String]
        self.pharmacokinetics  = aDecoder.decodeObjectForKey("pharmacokinetics") as? [String]

        // Indications, usage, and dosage
        self.indications_and_usage = aDecoder.decodeObjectForKey("indications_and_usage") as? [String]
        self.contraindications = aDecoder.decodeObjectForKey("contraindications") as? [String]
        self.dosage_and_administration = aDecoder.decodeObjectForKey("dosage_and_administration") as? [String]
        self.dosage_forms_and_strengths = aDecoder.decodeObjectForKey("dosage_forms_and_strengths") as? [String]
        self.purpose = aDecoder.decodeObjectForKey("purpose") as? [String]
        self.product_description = aDecoder.decodeObjectForKey("product_description") as? [String]
        self.active_ingredient = aDecoder.decodeObjectForKey("active_ingredient") as? [String]
        self.inactive_ingredient = aDecoder.decodeObjectForKey("inactive_ingredient") as? [String]
        self.spl_product_data_elements = aDecoder.decodeObjectForKey("spl_product_data_elements") as? [String]
        
        // Patient information
        self.spl_patient_package_insert = aDecoder.decodeObjectForKey("spl_patient_package_insert") as? [String]
        self.information_for_patients = aDecoder.decodeObjectForKey("information_for_patients") as? [String]
        self.information_for_owners_or_caregivers = aDecoder.decodeObjectForKey("information_for_owners_or_caregivers") as? [String]
        self.instructions_for_use = aDecoder.decodeObjectForKey("instructions_for_use") as? [String]
        self.ask_doctor = aDecoder.decodeObjectForKey("ask_doctor") as? [String]
        self.ask_doctor_or_pharmacist = aDecoder.decodeObjectForKey("ask_doctor_or_pharmacist") as? [String]
        self.do_not_use = aDecoder.decodeObjectForKey("do_not_use") as? [String]
        self.keep_out_of_reach_of_children = aDecoder.decodeObjectForKey("keep_out_of_reach_of_children") as? [String]
        self.other_safety_information = aDecoder.decodeObjectForKey("other_safety_information") as? [String]
        self.questions = aDecoder.decodeObjectForKey("questions") as? [String]
        self.stop_use = aDecoder.decodeObjectForKey("stop_use") as? [String]
        self.when_using = aDecoder.decodeObjectForKey("when_using") as? [String]
        self.patient_medication_information = aDecoder.decodeObjectForKey("patient_medication_information") as? [String]
        self.spl_medguide = aDecoder.decodeObjectForKey("spl_medguide") as? [String]
        
        // Special Populations
        self.use_in_specific_populations = aDecoder.decodeObjectForKey("use_in_specific_populations") as? [String]
        self.pregnancy = aDecoder.decodeObjectForKey("pregnancy") as? [String]
        self.teratogenic_effects = aDecoder.decodeObjectForKey("teratogenic_effects") as? [String]
        self.nonteratogenic_effects = aDecoder.decodeObjectForKey("nonteratogenic_effects") as? [String]
        self.labor_and_delivery = aDecoder.decodeObjectForKey("labor_and_delivery") as? [String]
        self.nursing_mothers = aDecoder.decodeObjectForKey("nursing_mothers") as? [String]
        self.pregnancy_or_breast_feeding = aDecoder.decodeObjectForKey("pregnancy_or_breast_feeding") as? [String]
        self.pediatric_use = aDecoder.decodeObjectForKey("pediatric_use") as? [String]
        self.geriatric_use = aDecoder.decodeObjectForKey("geriatric_use") as? [String]
        
        // Nonclinical toxicology
        self.nonclinical_toxicology = aDecoder.decodeObjectForKey("nonclinical_toxicology") as? [String]
        self.carcinogenesis_and_mutagenesis_and_impairment_of_fertility = aDecoder.decodeObjectForKey("carcinogenesis_and_mutagenesis_and_impairment_of_fertility") as? [String]
        self.animal_pharmacology_and_or_toxicology = aDecoder.decodeObjectForKey("animal_pharmacology_and_or_toxicology") as? [String]
        
        // References
        self.clinical_studies = aDecoder.decodeObjectForKey("clinical_studies") as? [String]
        self.references = aDecoder.decodeObjectForKey("references") as? [String]
        
        // Supply, storage, and handling
        self.how_supplied = aDecoder.decodeObjectForKey("how_supplied") as? [String]
        self.storage_and_handling = aDecoder.decodeObjectForKey("storage_and_handling") as? [String]
        self.safe_handling_warning = aDecoder.decodeObjectForKey("safe_handling_warning") as? [String]
        
        // Warnings and precautions
        self.boxed_warning = aDecoder.decodeObjectForKey("boxed_warning") as? [String]
        self.warnings_and_precautions = aDecoder.decodeObjectForKey("warnings_and_precautions") as? [String]
        self.user_safety_warnings = aDecoder.decodeObjectForKey("user_safety_warnings") as? [String]
        self.precautions = aDecoder.decodeObjectForKey("precautions") as? [String]
        self.warnings = aDecoder.decodeObjectForKey("warnings") as? [String]
        self.general_precautions = aDecoder.decodeObjectForKey("general_precautions") as? [String]
        
        // Other fields
        self.laboratory_tests = aDecoder.decodeObjectForKey("laboratory_tests") as? [String]
        self.recent_major_changes = aDecoder.decodeObjectForKey("recent_major_changes") as? [String]
        self.microbiology = aDecoder.decodeObjectForKey("microbiology") as? [String]
        self.package_label_principal_display_panel = aDecoder.decodeObjectForKey("package_label_principal_display_panel") as? [String]
        self.spl_unclassified_section = aDecoder.decodeObjectForKey("spl_unclassified_section") as? [String]
        
        // OpenFDA
        self.openfda = aDecoder.decodeObjectForKey("openfda") as? NSDictionary
    }
    
    override init()
    {
        // Saved medicine cabinet
        self.medicineCabinet = ""
        
        // ID and version
        self.set_id = ""
        self.document_id = ""
        self.version = ""
        self.effective_time = ""
        
        // Abuse and overdosage
        self.drug_abuse_and_dependence  = [String]()
        self.controlled_substance  = [String]()
        self.abuse  = [String]()
        self.dependence  = [String]()
        self.overdosage  = [String]()
        
        // Adverse effects and interactions
        self.adverse_reactions  = [String]()
        self.drug_interactions  = [String]()
        self.drug_and_or_laboratory_test_interactions  = [String]()
        
        // Clinical pharmacology
        self.clinical_pharmacology  = [String]()
        self.mechanism_of_action  = [String]()
        self.pharmacodynamics  = [String]()
        self.pharmacokinetics  = [String]()
        
        // Indications, usage, and dosage
        self.indications_and_usage = [String]()
        self.contraindications = [String]()
        self.dosage_and_administration = [String]()
        self.dosage_forms_and_strengths = [String]()
        self.purpose = [String]()
        self.product_description = [String]()
        self.active_ingredient = [String]()
        self.inactive_ingredient = [String]()
        self.spl_product_data_elements = [String]()
        
        // Patient information
        self.spl_patient_package_insert = [String]()
        self.information_for_patients = [String]()
        self.information_for_owners_or_caregivers = [String]()
        self.instructions_for_use = [String]()
        self.ask_doctor = [String]()
        self.ask_doctor_or_pharmacist = [String]()
        self.do_not_use = [String]()
        self.keep_out_of_reach_of_children = [String]()
        self.other_safety_information = [String]()
        self.questions = [String]()
        self.stop_use = [String]()
        self.when_using = [String]()
        self.patient_medication_information = [String]()
        self.spl_medguide = [String]()
        
        // Special Populations
        self.use_in_specific_populations = [String]()
        self.pregnancy = [String]()
        self.teratogenic_effects = [String]()
        self.nonteratogenic_effects = [String]()
        self.labor_and_delivery = [String]()
        self.nursing_mothers = [String]()
        self.pregnancy_or_breast_feeding = [String]()
        self.pediatric_use = [String]()
        self.geriatric_use = [String]()
        
        // Nonclinical toxicology
        self.nonclinical_toxicology = [String]()
        self.carcinogenesis_and_mutagenesis_and_impairment_of_fertility = [String]()
        self.animal_pharmacology_and_or_toxicology = [String]()
        
        // References
        self.clinical_studies = [String]()
        self.references = [String]()
        
        // Supply, storage, and handling
        self.how_supplied = [String]()
        self.storage_and_handling = [String]()
        self.safe_handling_warning = [String]()
        
        // Warnings and precautions
        self.boxed_warning = [String]()
        self.warnings_and_precautions = [String]()
        self.user_safety_warnings = [String]()
        self.precautions = [String]()
        self.warnings = [String]()
        self.general_precautions = [String]()
        
        // Other fields
        self.laboratory_tests = [String]()
        self.recent_major_changes = [String]()
        self.microbiology = [String]()
        self.package_label_principal_display_panel = [String]()
        self.spl_unclassified_section = [String]()
        
        // Open FDA
        self.openfda = NSDictionary()
        
        super.init()
    }
    
    func encodeWithCoder(aCoder: NSCoder)
    {
        // Saved medicine cabinet
        aCoder.encodeObject(self.medicineCabinet, forKey: "medicineCabinet")
        
        // ID and version
        aCoder.encodeObject(self.set_id, forKey: "set_id")
        aCoder.encodeObject(self.document_id, forKey: "document_id")
        aCoder.encodeObject(self.version, forKey: "version")
        aCoder.encodeObject(self.effective_time, forKey: "effective_time")
        
        // Abuse and overdosage
        aCoder.encodeObject(self.drug_abuse_and_dependence, forKey: "drug_abuse_and_dependence")
        aCoder.encodeObject(self.controlled_substance, forKey: "controlled_substance")
        aCoder.encodeObject(self.abuse, forKey: "abuse")
        aCoder.encodeObject(self.dependence, forKey: "dependence")
        aCoder.encodeObject(self.overdosage, forKey: "overdosage")
        
        // Adverse effects and interactions
        aCoder.encodeObject(self.adverse_reactions, forKey: "adverse_reactions")
        aCoder.encodeObject(self.drug_interactions, forKey: "drug_interactions")
        aCoder.encodeObject(self.drug_and_or_laboratory_test_interactions, forKey: "drug_and_or_laboratory_test_interactions")
        
        // Clinical pharmacology
        aCoder.encodeObject(self.clinical_pharmacology, forKey: "clinical_pharmacology")
        aCoder.encodeObject(self.mechanism_of_action, forKey: "mechanism_of_action")
        aCoder.encodeObject(self.pharmacodynamics, forKey: "pharmacodynamics")
        aCoder.encodeObject(self.pharmacokinetics, forKey: "pharmacokinetics")
        
        // Indications, usage, and dosage
        aCoder.encodeObject(self.indications_and_usage, forKey: "indications_and_usage")
        aCoder.encodeObject(self.contraindications, forKey: "contraindications")
        aCoder.encodeObject(self.dosage_and_administration, forKey: "dosage_and_administration")
        aCoder.encodeObject(self.dosage_forms_and_strengths, forKey: "dosage_forms_and_strengths")
        aCoder.encodeObject(self.purpose, forKey: "purpose")
        aCoder.encodeObject(self.product_description, forKey: "product_description")
        aCoder.encodeObject(self.active_ingredient, forKey: "active_ingredient")
        aCoder.encodeObject(self.inactive_ingredient, forKey: "inactive_ingredient")
        aCoder.encodeObject(self.spl_product_data_elements, forKey: "spl_product_data_elements")
        
        // Patient information
        aCoder.encodeObject(self.spl_patient_package_insert, forKey: "spl_patient_package_insert")
        aCoder.encodeObject(self.information_for_patients, forKey: "information_for_patients")
        aCoder.encodeObject(self.information_for_owners_or_caregivers, forKey: "information_for_owners_or_caregivers")
        aCoder.encodeObject(self.instructions_for_use, forKey: "instructions_for_use")
        aCoder.encodeObject(self.ask_doctor, forKey: "ask_doctor")
        aCoder.encodeObject(self.ask_doctor_or_pharmacist, forKey: "ask_doctor_or_pharmacist")
        aCoder.encodeObject(self.do_not_use, forKey: "do_not_use")
        aCoder.encodeObject(self.keep_out_of_reach_of_children, forKey: "keep_out_of_reach_of_children")
        aCoder.encodeObject(self.other_safety_information, forKey: "other_safety_information")
        aCoder.encodeObject(self.questions, forKey: "questions")
        aCoder.encodeObject(self.stop_use, forKey: "stop_use")
        aCoder.encodeObject(self.when_using, forKey: "when_using")
        aCoder.encodeObject(self.patient_medication_information, forKey: "patient_medication_information")
        aCoder.encodeObject(self.spl_medguide, forKey: "spl_medguide")
        
        // Special Populations
        aCoder.encodeObject(self.use_in_specific_populations, forKey: "use_in_specific_populations")
        aCoder.encodeObject(self.pregnancy, forKey: "pregnancy")
        aCoder.encodeObject(self.teratogenic_effects, forKey: "teratogenic_effects")
        aCoder.encodeObject(self.nonteratogenic_effects, forKey: "nonteratogenic_effects")
        aCoder.encodeObject(self.labor_and_delivery, forKey: "labor_and_delivery")
        aCoder.encodeObject(self.nursing_mothers, forKey: "nursing_mothers")
        aCoder.encodeObject(self.pregnancy_or_breast_feeding, forKey: "pregnancy_or_breast_feeding")
        aCoder.encodeObject(self.pediatric_use, forKey: "pediatric_use")
        aCoder.encodeObject(self.geriatric_use, forKey: "geriatric_use")
        
        // Nonclinical toxicology
        aCoder.encodeObject(self.nonclinical_toxicology, forKey: "nonclinical_toxicology")
        aCoder.encodeObject(self.carcinogenesis_and_mutagenesis_and_impairment_of_fertility, forKey: "carcinogenesis_and_mutagenesis_and_impairment_of_fertility")
        aCoder.encodeObject(self.animal_pharmacology_and_or_toxicology, forKey: "animal_pharmacology_and_or_toxicology")
        
        // References
        aCoder.encodeObject(self.clinical_studies, forKey: "clinical_studies")
        aCoder.encodeObject(self.references, forKey: "references")
        
        // Supply, storage, and handling
        aCoder.encodeObject(self.how_supplied, forKey: "how_supplied")
        aCoder.encodeObject(self.storage_and_handling, forKey: "storage_and_handling")
        aCoder.encodeObject(self.safe_handling_warning, forKey: "safe_handling_warning")
        
        // Warnings and precautions
        aCoder.encodeObject(self.boxed_warning, forKey: "boxed_warning")
        aCoder.encodeObject(self.warnings_and_precautions, forKey: "warnings_and_precautions")
        aCoder.encodeObject(self.user_safety_warnings, forKey: "user_safety_warnings")
        aCoder.encodeObject(self.precautions, forKey: "precautions")
        aCoder.encodeObject(self.warnings, forKey: "warnings")
        aCoder.encodeObject(self.general_precautions, forKey: "general_precautions")
        
        // Other fields
        aCoder.encodeObject(self.laboratory_tests, forKey: "laboratory_tests")
        aCoder.encodeObject(self.recent_major_changes, forKey: "recent_major_changes")
        aCoder.encodeObject(self.microbiology, forKey: "microbiology")
        aCoder.encodeObject(self.package_label_principal_display_panel, forKey: "package_label_principal_display_panel")
        aCoder.encodeObject(self.spl_unclassified_section, forKey: "spl_unclassified_section")
        
        // Open FDA
        aCoder.encodeObject(self.openfda, forKey: "openfda")
    }
    
    // Initializes object from JSON dict
    init (result: NSDictionary)
    {
        // If we have a medicine cabinet
        self.medicineCabinet = ""
        
        // ID and version
        if let set_id = result["set_id"] as? String{
            self.set_id = set_id
        }
        if let document_id = result["document_id"] as? String{
            self.document_id = document_id
        }
        if let version = result["version"] as? String{
            self.version = version
        }
        if let effective_time = result["effective_time"] as? String{
            self.effective_time = effective_time
        }
        
        // Abuse and overdosage
        if let drug_abuse_and_dependence = result["drug_abuse_and_dependence"] as? [String]{
            self.drug_abuse_and_dependence = drug_abuse_and_dependence
        }
        if let controlled_substance = result["controlled_substance"] as? [String]{
            self.controlled_substance = controlled_substance
        }
        if let abuse = result["abuse"] as? [String]{
            self.abuse = abuse
        }
        if let dependence = result["dependence"] as? [String]{
            self.dependence = dependence
        }
        if let overdosage = result["overdosage"] as? [String]{
            self.overdosage = overdosage
        }
        
        // Adverse effects and interactions
        if let adverse_reactions = result["adverse_reactions"] as? [String]{
            self.adverse_reactions = adverse_reactions
        }
        if let drug_interactions = result["drug_interactions"] as? [String]{
            self.drug_interactions = drug_interactions
        }
        if let drug_and_or_laboratory_test_interactions = result["drug_and_or_laboratory_test_interactions"] as? [String]{
            self.drug_and_or_laboratory_test_interactions = drug_and_or_laboratory_test_interactions
        }
        
        // Clinical pharmacology
        if let clinical_pharmacology = result["clinical_pharmacology"] as? [String]{
            self.clinical_pharmacology = clinical_pharmacology
        }
        if let mechanism_of_action = result["mechanism_of_action"] as? [String]{
            self.mechanism_of_action = mechanism_of_action
        }
        if let pharmacodynamics = result["pharmacodynamics"] as? [String]{
            self.pharmacodynamics = pharmacodynamics
        }
        if let pharmacokinetics = result["pharmacokinetics"] as? [String]{
            self.pharmacokinetics = pharmacokinetics
        }
        
        // Indications, usage, and dosage
        if let indications_and_usage = result["indications_and_usage"] as? [String]{
            self.indications_and_usage = indications_and_usage
        }
        if let contraindications = result["contraindications"] as? [String]{
            self.contraindications = contraindications
        }
        if let dosage_and_administration = result["dosage_and_administration"] as? [String]{
            self.dosage_and_administration = dosage_and_administration
        }
        if let dosage_forms_and_strengths = result["dosage_forms_and_strengths"] as? [String]{
            self.dosage_forms_and_strengths = dosage_forms_and_strengths
        }
        if let purpose = result["purpose"] as? [String]{
            self.purpose = purpose
        }
        if let product_description = result["product_description"] as? [String]{
            self.product_description = product_description
        }
        if let active_ingredient = result["active_ingredient"] as? [String]{
            self.active_ingredient = active_ingredient
        }
        if let inactive_ingredient = result["inactive_ingredient"] as? [String]{
            self.inactive_ingredient = inactive_ingredient
        }
        if let spl_product_data_elements = result["spl_product_data_elements"] as? [String]{
            self.spl_product_data_elements = spl_product_data_elements
        }
        
        // Patient information
        if let spl_patient_package_insert = result["spl_patient_package_insert"] as? [String]{
            self.spl_patient_package_insert = spl_patient_package_insert
        }
        if let information_for_patients = result["information_for_patients"] as? [String]{
            self.information_for_patients = information_for_patients
        }
        if let information_for_owners_or_caregivers = result["information_for_owners_or_caregivers"] as? [String]{
            self.information_for_owners_or_caregivers = information_for_owners_or_caregivers
        }
        if let instructions_for_use = result["instructions_for_use"] as? [String]{
            self.instructions_for_use = instructions_for_use
        }
        if let ask_doctor = result["ask_doctor"] as? [String]{
            self.ask_doctor = ask_doctor
        }
        if let ask_doctor_or_pharmacist = result["ask_doctor_or_pharmacist"] as? [String]{
            self.ask_doctor_or_pharmacist = ask_doctor_or_pharmacist
        }
        if let do_not_use = result["do_not_use"] as? [String]{
            self.do_not_use = do_not_use
        }
        if let keep_out_of_reach_of_children = result["keep_out_of_reach_of_children"] as? [String]{
            self.keep_out_of_reach_of_children = keep_out_of_reach_of_children
        }
        if let other_safety_information = result["other_safety_information"] as? [String]{
            self.other_safety_information = other_safety_information
        }
        if let questions = result["questions"] as? [String]{
            self.questions = questions
        }
        if let stop_use = result["stop_use"] as? [String]{
            self.stop_use = stop_use
        }
        if let when_using = result["when_using"] as? [String]{
            self.when_using = when_using
        }
        if let patient_medication_information = result["patient_medication_information"] as? [String]{
            self.patient_medication_information = patient_medication_information
        }
        if let spl_medguide = result["spl_medguide"] as? [String]{
            self.spl_medguide = spl_medguide
        }
        
        // Special Populations
        if let use_in_specific_populations = result["use_in_specific_populations"] as? [String]{
            self.use_in_specific_populations = use_in_specific_populations
        }
        if let pregnancy = result["pregnancy"] as? [String]{
            self.pregnancy = pregnancy
        }
        if let teratogenic_effects = result["teratogenic_effects"] as? [String]{
            self.teratogenic_effects = teratogenic_effects
        }
        if let nonteratogenic_effects = result["nonteratogenic_effects"] as? [String]{
            self.nonteratogenic_effects = nonteratogenic_effects
        }
        if let labor_and_delivery = result["labor_and_delivery"] as? [String]{
            self.labor_and_delivery = labor_and_delivery
        }
        if let nursing_mothers = result["nursing_mothers"] as? [String]{
            self.nursing_mothers = nursing_mothers
        }
        if let pregnancy_or_breast_feeding = result["pregnancy_or_breast_feeding"] as? [String]{
            self.pregnancy_or_breast_feeding = pregnancy_or_breast_feeding
        }
        if let pediatric_use = result["pediatric_use"] as? [String]{
            self.pediatric_use = pediatric_use
        }
        if let geriatric_use = result["geriatric_use"] as? [String]{
            self.geriatric_use = geriatric_use
        }
        
        // Nonclinical toxicology
        if let nonclinical_toxicology = result["nonclinical_toxicology"] as? [String]{
            self.nonclinical_toxicology = nonclinical_toxicology
        }
        if let carcinogenesis_and_mutagenesis_and_impairment_of_fertility = result["carcinogenesis_and_mutagenesis_and_impairment_of_fertility"] as? [String]{
            self.carcinogenesis_and_mutagenesis_and_impairment_of_fertility = carcinogenesis_and_mutagenesis_and_impairment_of_fertility
        }
        if let animal_pharmacology_and_or_toxicology = result["animal_pharmacology_and_or_toxicology"] as? [String]{
            self.animal_pharmacology_and_or_toxicology = animal_pharmacology_and_or_toxicology
        }
        
        // References
        if let clinical_studies = result["clinical_studies"] as? [String]{
            self.clinical_studies = clinical_studies
        }
        if let references = result["references"] as? [String]{
            self.references = references
        }
        
        // Supply, storage, and handling
        if let how_supplied = result["how_supplied"] as? [String]{
            self.how_supplied = how_supplied
        }
        if let storage_and_handling = result["storage_and_handling"] as? [String]{
            self.storage_and_handling = storage_and_handling
        }
        if let safe_handling_warning = result["safe_handling_warning"] as? [String]{
            self.safe_handling_warning = safe_handling_warning
        }
        
        // Warnings and precautions
        if let boxed_warning = result["boxed_warning"] as? [String]{
            self.boxed_warning = boxed_warning
        }
        if let warnings_and_precautions = result["warnings_and_precautions"] as? [String]{
            self.warnings_and_precautions = warnings_and_precautions
        }
        if let user_safety_warnings = result["user_safety_warnings"] as? [String]{
            self.user_safety_warnings = user_safety_warnings
        }
        if let precautions = result["precautions"] as? [String]{
            self.precautions = precautions
        }
        if let warnings = result["warnings"] as? [String]{
            self.warnings = warnings
        }
        if let general_precautions = result["general_precautions"] as? [String]{
            self.general_precautions = general_precautions
        }
        
        // Other fields
        if let laboratory_tests = result["laboratory_tests"] as? [String]{
            self.laboratory_tests = laboratory_tests
        }
        if let recent_major_changes = result["recent_major_changes"] as? [String]{
            self.recent_major_changes = recent_major_changes
        }
        if let microbiology = result["microbiology"] as? [String]{
            self.microbiology = microbiology
        }
        if let package_label_principal_display_panel = result["package_label_principal_display_panel"] as? [String]{
            self.package_label_principal_display_panel = package_label_principal_display_panel
        }
        if let spl_unclassified_section = result["spl_unclassified_section"] as? [String]{
            self.spl_unclassified_section = spl_unclassified_section
        }
        
        // Open FDA
        if let openfda = result["openfda"] as? NSDictionary{
            self.openfda = openfda
        }
    }
    
    // Returns list of properties in original format
    func getDefinedProperties() -> [String]
    {
        var properties = [String]()
        
        // ID and version
//        if self.set_id != nil{
//            properties.append("set_id")
//        }
//        if self.document_id != nil{
//            properties.append("document_id")
//        }
//        if self.version != nil{
//            properties.append("version")
//        }
//        if self.effective_time != nil{
//            properties.append("effective_time")
//        }
        
        // Abuse and overdosage
        if self.drug_abuse_and_dependence != nil{
            properties.append("drug_abuse_and_dependence")
        }
        if self.controlled_substance != nil{
            properties.append("controlled_substance")
        }
        if self.abuse != nil{
            properties.append("abuse")
        }
        if self.dependence != nil{
            properties.append("dependence")
        }
        if self.overdosage != nil{
            properties.append("overdosage")
        }
        
        // Adverse effects and interactions
        if self.adverse_reactions != nil{
            properties.append("adverse_reactions")
        }
        if self.drug_interactions != nil{
            properties.append("drug_interactions")
        }
        if self.drug_and_or_laboratory_test_interactions != nil{
            properties.append("drug_and_or_laboratory_test_interactions")
        }
        
        // Clinical pharmacology
        if self.clinical_pharmacology != nil{
            properties.append("clinical_pharmacology")
        }
        if self.mechanism_of_action != nil{
            properties.append("mechanism_of_action")
        }
        if self.pharmacodynamics != nil{
            properties.append("pharmacodynamics")
        }
        if self.pharmacokinetics != nil{
            properties.append("pharmacokinetics")
        }
        
        // Indications, usage, and dosage
        if self.indications_and_usage != nil{
            properties.append("indications_and_usage")
        }
        if self.contraindications != nil{
            properties.append("contraindications")
        }
        if self.dosage_and_administration != nil{
            properties.append("dosage_and_administration")
        }
        if self.dosage_forms_and_strengths != nil{
            properties.append("dosage_forms_and_strengths")
        }
        if self.purpose != nil{
            properties.append("purpose")
        }
        if self.product_description != nil{
            properties.append("product_description")
        }
        if self.active_ingredient != nil{
            properties.append("active_ingredient")
        }
        if self.inactive_ingredient != nil{
            properties.append("inactive_ingredient")
        }
        if self.spl_product_data_elements != nil{
            properties.append("spl_product_data_elements")
        }
        
        // Patient information
        if self.spl_patient_package_insert != nil{
            properties.append("spl_patient_package_insert")
        }
        if self.information_for_patients != nil{
            properties.append("information_for_patients")
        }
        if self.information_for_owners_or_caregivers != nil{
            properties.append("information_for_owners_or_caregivers")
        }
        if self.instructions_for_use != nil{
            properties.append("instructions_for_use")
        }
        if self.ask_doctor != nil{
            properties.append("ask_doctor")
        }
        if self.ask_doctor_or_pharmacist != nil{
            properties.append("ask_doctor_or_pharmacist")
        }
        if self.do_not_use != nil{
            properties.append("do_not_use")
        }
        if self.keep_out_of_reach_of_children != nil{
            properties.append("keep_out_of_reach_of_children")
        }
        if self.other_safety_information != nil{
            properties.append("other_safety_information")
        }
        if self.questions != nil{
            properties.append("questions")
        }
        if self.stop_use != nil{
            properties.append("stop_use")
        }
        if self.when_using != nil{
            properties.append("when_using")
        }
        if self.patient_medication_information != nil{
            properties.append("patient_medication_information")
        }
        if self.spl_medguide != nil{
            properties.append("spl_medguide")
        }
        
        // Special Populations
        if self.use_in_specific_populations != nil{
            properties.append("use_in_specific_populations")
        }
        if self.pregnancy != nil{
            properties.append("pregnancy")
        }
        if self.teratogenic_effects != nil{
            properties.append("teratogenic_effects")
        }
        if self.nonteratogenic_effects != nil{
            properties.append("nonteratogenic_effects")
        }
        if self.labor_and_delivery != nil{
            properties.append("labor_and_delivery")
        }
        if self.nursing_mothers != nil{
            properties.append("nursing_mothers")
        }
        if self.pregnancy_or_breast_feeding != nil{
            properties.append("pregnancy_or_breast_feeding")
        }
        if self.pediatric_use != nil{
            properties.append("pediatric_use")
        }
        if self.geriatric_use != nil{
            properties.append("geriatric_use")
        }
        
        // Nonclinical toxicology
        if self.nonclinical_toxicology != nil{
            properties.append("nonclinical_toxicology")
        }
        if self.carcinogenesis_and_mutagenesis_and_impairment_of_fertility != nil{
            properties.append("carcinogenesis_and_mutagenesis_and_impairment_of_fertility")
        }
        if self.animal_pharmacology_and_or_toxicology != nil{
            properties.append("animal_pharmacology_and_or_toxicology")
        }
        
        // References
        if self.clinical_studies != nil{
            properties.append("clinical_studies")
        }
        if self.references != nil{
            properties.append("references")
        }
        
        // Supply, storage, and handling
        if self.how_supplied != nil{
            properties.append("how_supplied")
        }
        if self.storage_and_handling != nil{
            properties.append("storage_and_handling")
        }
        if self.safe_handling_warning != nil{
            properties.append("safe_handling_warning")
        }
        
        // Warnings and precautions
        if self.boxed_warning != nil{
            properties.append("boxed_warning")
        }
        if self.warnings_and_precautions != nil{
            properties.append("warnings_and_precautions")
        }
        if self.user_safety_warnings != nil{
            properties.append("user_safety_warnings")
        }
        if self.precautions != nil{
            properties.append("precautions")
        }
        if self.warnings != nil{
            properties.append("warnings")
        }
        if self.general_precautions != nil{
            properties.append("general_precautions")
        }
        
        // Other fields
        if self.laboratory_tests != nil{
            properties.append("laboratory_tests")
        }
        if self.recent_major_changes != nil{
            properties.append("recent_major_changes")
        }
        if self.microbiology != nil{
            properties.append("microbiology")
        }
        if self.package_label_principal_display_panel != nil{
            properties.append("package_label_principal_display_panel")
        }
        if self.spl_unclassified_section != nil{
            properties.append("spl_unclassified_section")
        }
        
        // Open FDA
        if self.openfda != nil{
            properties.append("openfda")
        }
        
        return properties
    }
    
    // Returns a list of properties in human readable format
    func getPropertyTitles() -> [String]
    {
        var properties = [String]()
        
        // ID and version
//        if self.set_id != nil{
//            properties.append("Set ID")
//        }
//        if self.document_id != nil{
//            properties.append("Document ID")
//        }
//        if self.version != nil{
//            properties.append("Version")
//        }
//        if self.effective_time != nil{
//            properties.append("Effective Time")
//        }
        
        // Abuse and overdosage
        if self.drug_abuse_and_dependence != nil{
            properties.append("Drug Abuse and Dependence")
        }
        if self.controlled_substance != nil{
            properties.append("Controlled Substance")
        }
        if self.abuse != nil{
            properties.append("Abuse")
        }
        if self.dependence != nil{
            properties.append("Dependence")
        }
        if self.overdosage != nil{
            properties.append("Overdosage")
        }
        
        // Adverse effects and interactions
        if self.adverse_reactions != nil{
            properties.append("Adverse Reactions")
        }
        if self.drug_interactions != nil{
            properties.append("Drug Interactions")
        }
        if self.drug_and_or_laboratory_test_interactions != nil{
            properties.append("Drug and/or Laboratory Test Interactions")
        }
        
        // Clinical pharmacology
        if self.clinical_pharmacology != nil{
            properties.append("Clinical Pharmacology")
        }
        if self.mechanism_of_action != nil{
            properties.append("Mechanism of Action")
        }
        if self.pharmacodynamics != nil{
            properties.append("Pharmacodynamics")
        }
        if self.pharmacokinetics != nil{
            properties.append("Pharmacokinetics")
        }
        
        // Indications, usage, and dosage
        if self.indications_and_usage != nil{
            properties.append("Indications and Usage")
        }
        if self.contraindications != nil{
            properties.append("Contraindications")
        }
        if self.dosage_and_administration != nil{
            properties.append("Dosage and Administration")
        }
        if self.dosage_forms_and_strengths != nil{
            properties.append("Dosage Forms and Strengths")
        }
        if self.purpose != nil{
            properties.append("Purpose")
        }
        if self.product_description != nil{
            properties.append("Product Description")
        }
        if self.active_ingredient != nil{
            properties.append("Active Ingredient")
        }
        if self.inactive_ingredient != nil{
            properties.append("Inactive Ingredient")
        }
        if self.spl_product_data_elements != nil{
            properties.append("SPL Product Data Elements")
        }
        
        // Patient information
        if self.spl_patient_package_insert != nil{
            properties.append("SPL Patient Package Insert")
        }
        if self.information_for_patients != nil{
            properties.append("Information For Patients")
        }
        if self.information_for_owners_or_caregivers != nil{
            properties.append("Information For Owners or Caregivers")
        }
        if self.instructions_for_use != nil{
            properties.append("Instructions for use")
        }
        if self.ask_doctor != nil{
            properties.append("Ask doctor")
        }
        if self.ask_doctor_or_pharmacist != nil{
            properties.append("Ask doctor or pharmacist")
        }
        if self.do_not_use != nil{
            properties.append("Do not use")
        }
        if self.keep_out_of_reach_of_children != nil{
            properties.append("Keep out of reach of children")
        }
        if self.other_safety_information != nil{
            properties.append("Other safety information")
        }
        if self.questions != nil{
            properties.append("Questions")
        }
        if self.stop_use != nil{
            properties.append("Stop use")
        }
        if self.when_using != nil{
            properties.append("When using")
        }
        if self.patient_medication_information != nil{
            properties.append("Patient medication information")
        }
        if self.spl_medguide != nil{
            properties.append("SPL Medguide")
        }
        
        // Special Populations
        if self.use_in_specific_populations != nil{
            properties.append("Use in specific populations")
        }
        if self.pregnancy != nil{
            properties.append("Pregnancy")
        }
        if self.teratogenic_effects != nil{
            properties.append("Teratogenic Effects")
        }
        if self.nonteratogenic_effects != nil{
            properties.append("Nonteratogenic Effects")
        }
        if self.labor_and_delivery != nil{
            properties.append("Labor and delivery")
        }
        if self.nursing_mothers != nil{
            properties.append("Nursing mothers")
        }
        if self.pregnancy_or_breast_feeding != nil{
            properties.append("Pregnancy or breast feeding")
        }
        if self.pediatric_use != nil{
            properties.append("Pediatric use")
        }
        if self.geriatric_use != nil{
            properties.append("Geriatric use")
        }
        
        // Nonclinical toxicology
        if self.nonclinical_toxicology != nil{
            properties.append("Nonclinical toxicology")
        }
        if self.carcinogenesis_and_mutagenesis_and_impairment_of_fertility != nil{
            properties.append("Carcinogenesis and Mutagenesis and Impairment of Fertility")
        }
        if self.animal_pharmacology_and_or_toxicology != nil{
            properties.append("Animal pharmacology and or toxicology")
        }
        
        // References
        if self.clinical_studies != nil{
            properties.append("Clinical studies")
        }
        if self.references != nil{
            properties.append("References")
        }
        
        // Supply, storage, and handling
        if self.how_supplied != nil{
            properties.append("How supplied")
        }
        if self.storage_and_handling != nil{
            properties.append("Storage and handling")
        }
        if self.safe_handling_warning != nil{
            properties.append("Safe handling warning")
        }
        
        // Warnings and precautions
        if self.boxed_warning != nil{
            properties.append("Boxed warning")
        }
        if self.warnings_and_precautions != nil{
            properties.append("Warnings and precautions")
        }
        if self.user_safety_warnings != nil{
            properties.append("User safety warnings")
        }
        if self.precautions != nil{
            properties.append("Precautions")
        }
        if self.warnings != nil{
            properties.append("Warnings")
        }
        if self.general_precautions != nil{
            properties.append("General precautions")
        }
        
        // Other fields
        if self.laboratory_tests != nil{
            properties.append("Laboratory tests")
        }
        if self.recent_major_changes != nil{
            properties.append("Recent major changes")
        }
        if self.microbiology != nil{
            properties.append("Microbiology")
        }
        if self.package_label_principal_display_panel != nil{
            properties.append("Package label principal display panel")
        }
        if self.spl_unclassified_section != nil{
            properties.append("spl unclassified section")
        }
        
        // Open FDA
        if self.openfda != nil{
            properties.append("Open FDA")
        }
        
        return properties
    }
}

