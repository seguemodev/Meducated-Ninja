//
//  DrugInformationViewController.swift
//  Zippy
//
//  Created by Geoffrey Bender on 6/19/15.
//  Copyright (c) 2015 Segue Technologies, Inc. All rights reserved.
//

import UIKit

class DrugInformationViewController: UITableViewController
{
    // MARK: - Interface Outlets
    
    @IBOutlet weak var saveButton: UIBarButtonItem!
    @IBOutlet weak var cellLabel: UILabel!
    
    // MARK: - Variables
    
    var medication: Medication!
    var cabinetPickerView = UIPickerView()
    var cabinetArray = [String]()
    var selectedCabinet = ""
    var removeMedicationMode = false
    var pillImage: UIImage?
    var loadingNotification: MBProgressHUD?
    var readyToLoad = false
    var sectionHeaders = [String]()
    var sectionHeaderTitles = [String]()
    var expandedSections = [String]()
    
    // MARK: - View Lifecycle Methods
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        
        // For dynamic table row height
        self.tableView.estimatedRowHeight = 115
        self.tableView.rowHeight = UITableViewAutomaticDimension
        
        var leftBarButton = UIBarButtonItem(title:"Back", style:UIBarButtonItemStyle.Plain, target:self, action:"backNavigate")
        var rightBarButton = UIBarButtonItem()
        if self.removeMedicationMode
        {
            rightBarButton = UIBarButtonItem(title:"Remove", style:UIBarButtonItemStyle.Plain, target:self, action:"removeMedication")
        }
        else
        {
            rightBarButton = UIBarButtonItem(title:"Save", style:UIBarButtonItemStyle.Plain, target:self, action:"saveMedication")
        }
        
        self.navigationItem.leftBarButtonItem = leftBarButton
        self.navigationItem.rightBarButtonItem = rightBarButton
        
        var normalButtonBackground = UIImage(named:"BlueButtonBackground")!.resizableImageWithCapInsets(UIEdgeInsetsMake(0, 10, 0, 10))
        self.navigationItem.leftBarButtonItem!.setBackgroundImage(normalButtonBackground, forState: UIControlState.Normal, barMetrics: UIBarMetrics.Default)
        self.navigationItem.rightBarButtonItem!.setBackgroundImage(normalButtonBackground, forState: UIControlState.Normal, barMetrics: UIBarMetrics.Default)
        
        var pressedButtonBackground = UIImage(named:"GreenButtonBackground")!.resizableImageWithCapInsets(UIEdgeInsetsMake(0, 10, 0, 10))
        self.navigationItem.leftBarButtonItem!.setBackgroundImage(pressedButtonBackground, forState: UIControlState.Highlighted, barMetrics: UIBarMetrics.Default)
        self.navigationItem.rightBarButtonItem!.setBackgroundImage(pressedButtonBackground, forState: UIControlState.Highlighted, barMetrics: UIBarMetrics.Default)
        
        if let font = UIFont(name:"RobotoSlab-Bold", size:14.0)
        {
            self.navigationItem.leftBarButtonItem!.setTitleTextAttributes([NSFontAttributeName:font, NSForegroundColorAttributeName:UIColor.whiteColor()], forState:UIControlState.Normal)
            self.navigationItem.rightBarButtonItem!.setTitleTextAttributes([NSFontAttributeName:font, NSForegroundColorAttributeName:UIColor.whiteColor()], forState:UIControlState.Normal)
        }
    }
    
    override func viewWillAppear(animated: Bool)
    {
        super.viewWillAppear(animated)
        
        if !self.readyToLoad
        {
            // Set up progress hud
            self.loadingNotification = MBProgressHUD.showHUDAddedTo(self.navigationController!.view, animated: true)
            self.loadingNotification!.mode = MBProgressHUDMode.Indeterminate
            self.loadingNotification!.labelText = "Loading Medication Information"
            self.loadingNotification!.userInteractionEnabled = true
        }
    }
    
    override func viewDidAppear(animated: Bool)
    {
        super.viewDidAppear(animated)
        
        if !self.readyToLoad
        {
            // Obtain drug images
            let ndcArray = self.medication.openfda!["product_ndc"] as! [String]
            let ndc = ndcArray[0]
            let queryString = "http://rximage.nlm.nih.gov/api/rximage/1/rxnav?ndc=\(ndc)"
            let queryURL = NSURL(string: queryString)
            var request = NSMutableURLRequest(URL: queryURL!, cachePolicy: NSURLRequestCachePolicy.ReloadIgnoringLocalAndRemoteCacheData, timeoutInterval: 10)
            // var result = NSURLConnection.sendSynchronousRequest(request, returningResponse: nil, error: nil)
            
            NSURLConnection.sendAsynchronousRequest(request, queue: NSOperationQueue.mainQueue()) { (response, data, error) -> Void in
                let jsonResult: AnyObject? = NSJSONSerialization.JSONObjectWithData(data, options: NSJSONReadingOptions.MutableContainers, error:nil)
                let imagesArray = jsonResult?.valueForKeyPath("nlmRxImages") as! [NSDictionary]
                
                if !imagesArray.isEmpty
                {
                    for image in imagesArray
                    {
                        if let imageURL = image.valueForKey("imageUrl") as? String
                        {
                            let imageQueryString = imageURL
                            let imageQueryURL = NSURL(string: imageQueryString)
                            var imageRequest = NSMutableURLRequest(URL: imageQueryURL!, cachePolicy: NSURLRequestCachePolicy.ReloadIgnoringLocalAndRemoteCacheData, timeoutInterval: 10)
                            // var imageResult = NSURLConnection.sendSynchronousRequest(imageRequest, returningResponse: nil, error: nil)
                            
                            NSURLConnection.sendAsynchronousRequest(imageRequest, queue: NSOperationQueue.mainQueue(), completionHandler: { (response, data, error) -> Void in
                                var tempImage = UIImage(data:data)
                                var newSize = CGSizeMake(UIScreen.mainScreen().bounds.width-80, UIScreen.mainScreen().bounds.width-80)
                                self.pillImage = tempImage?.resizedImage(newSize, interpolationQuality:kCGInterpolationDefault)
                                
                                self.readyToLoad = true
                                
                                // Reload the table with the new cabinet (using this method for proper autoheight calculation on table cells)
                                self.tableView.reloadData()
                                
                                // Scroll to first cell
                                self.tableView.scrollToRowAtIndexPath(NSIndexPath(forRow: 0, inSection: 0), atScrollPosition: UITableViewScrollPosition.Top, animated: false)
                                
                                // Hide progress hud
                                MBProgressHUD.hideAllHUDsForView(self.navigationController!.view, animated: true)
                            })
                        }
                        else
                        {
                            self.readyToLoad = true
                            
                            // Reload the table with the new cabinet (using this method for proper autoheight calculation on table cells)
                            self.tableView.reloadData()
                            
                            // Scroll to first cell
                            self.tableView.scrollToRowAtIndexPath(NSIndexPath(forRow: 0, inSection: 0), atScrollPosition: UITableViewScrollPosition.Top, animated: false)
                            
                            // Hide progress hud
                            MBProgressHUD.hideAllHUDsForView(self.navigationController!.view, animated: true)
                        }
                    }
                }
                else
                {
                    self.readyToLoad = true
                    
                    // Reload the table with the new cabinet (using this method for proper autoheight calculation on table cells)
                    self.tableView.reloadData()
                    
                    // Scroll to first cell
                    self.tableView.scrollToRowAtIndexPath(NSIndexPath(forRow: 0, inSection: 0), atScrollPosition: UITableViewScrollPosition.Top, animated: false)
                    
                    // Hide progress hud
                    MBProgressHUD.hideAllHUDsForView(self.navigationController!.view, animated: true)
                }
            }
        }
    }
    
    // Reload table contents on device rotation
    override func viewWillTransitionToSize(size: CGSize, withTransitionCoordinator coordinator: UIViewControllerTransitionCoordinator)
    {
        super.viewWillTransitionToSize(size, withTransitionCoordinator: coordinator)
        
        // Completion handler to execute code after transition
        coordinator.animateAlongsideTransition({(context) -> Void in}, completion: {(context) -> Void in
            
            // Scroll to first row
            self.tableView.setContentOffset(CGPointZero, animated:false)
            
            // Reload the table with the new cabinet (using this method for proper autoheight calculation on table cells)
            self.tableView.reloadSections(NSIndexSet(indexesInRange:NSMakeRange(0, self.tableView.numberOfSections())), withRowAnimation:UITableViewRowAnimation.None)
        })
    }

    // MARK: - Table View Methods

    // Return the number of sections
    override func numberOfSectionsInTableView(tableView: UITableView) -> Int
    {
        var numberOfSections = 0
        self.sectionHeaderTitles.removeAll(keepCapacity: false)
        self.sectionHeaders.removeAll(keepCapacity: false)
        
        if self.readyToLoad
        {
            if let field: AnyObject = self.medication.valueForKey("indications_and_usage")
            {
                numberOfSections++
                self.sectionHeaderTitles.append("Indications and Usage")
                self.sectionHeaders.append("indications_and_usage")
            }
            if let field: AnyObject = self.medication.valueForKey("dosage_and_administration")
            {
                numberOfSections++
                self.sectionHeaderTitles.append("Dosage and Administration")
                self.sectionHeaders.append("dosage_and_administration")
            }
            if let field: AnyObject = self.medication.valueForKey("information_for_patients")
            {
                numberOfSections++
                self.sectionHeaderTitles.append("Information for Patients")
                self.sectionHeaders.append("information_for_patients")
            }
            if let field: AnyObject = self.medication.valueForKey("contraindications")
            {
                numberOfSections++
                self.sectionHeaderTitles.append("Contraindications")
                self.sectionHeaders.append("contraindications")
            }
            if let field: AnyObject = self.medication.valueForKey("how_supplied")
            {
                numberOfSections++
                self.sectionHeaderTitles.append("How Supplied")
                self.sectionHeaders.append("how_supplied")
            }
            if let field: AnyObject = self.medication.valueForKey("pediatric_use")
            {
                numberOfSections++
                self.sectionHeaderTitles.append("Pediatric Use")
                self.sectionHeaders.append("pediatric_use")
            }
            if let field: AnyObject = self.medication.valueForKey("precautions")
            {
                numberOfSections++
                self.sectionHeaderTitles.append("Precautions")
                self.sectionHeaders.append("precautions")
            }
            if let field: AnyObject = self.medication.valueForKey("warnings")
            {
                numberOfSections++
                self.sectionHeaderTitles.append("Warnings")
                self.sectionHeaders.append("warnings")
            }
            if let field: AnyObject = self.medication.valueForKey("general_precautions")
            {
                numberOfSections++
                self.sectionHeaderTitles.append("General Precautions")
                self.sectionHeaders.append("general_precautions")
            }
            if let field: AnyObject = self.medication.valueForKey("overdosage")
            {
                numberOfSections++
                self.sectionHeaderTitles.append("Overdosage")
                self.sectionHeaders.append("overdosage")
            }
            if let field: AnyObject = self.medication.valueForKey("boxed_warning")
            {
                numberOfSections++
                self.sectionHeaderTitles.append("Boxed Warning")
                self.sectionHeaders.append("boxed_warning")
            }
            if let field: AnyObject = self.medication.valueForKey("adverse_reactions")
            {
                numberOfSections++
                self.sectionHeaderTitles.append("Adverse Reactions")
                self.sectionHeaders.append("adverse_reactions")
            }
            if let field: AnyObject = self.medication.valueForKey("drug_interactions")
            {
                numberOfSections++
                self.sectionHeaderTitles.append("Drug Interactions")
                self.sectionHeaders.append("drug_interactions")
            }
            if let field: AnyObject = self.medication.valueForKey("pregnancy")
            {
                numberOfSections++
                self.sectionHeaderTitles.append("Pregnancy")
                self.sectionHeaders.append("pregnancy")
            }
            if let field: AnyObject = self.medication.valueForKey("nursing_mothers")
            {
                numberOfSections++
                self.sectionHeaderTitles.append("Nursing Mothers")
                self.sectionHeaders.append("nursing_mothers")
            }
            if let field: AnyObject = self.medication.valueForKey("clinical_pharmacology")
            {
                numberOfSections++
                self.sectionHeaderTitles.append("Clinical Pharmacology")
                self.sectionHeaders.append("clinical_pharmacology")
            }
            
            return numberOfSections++
        }
        
        return 0
    }

    // Return the number of rows in the section
    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        if self.readyToLoad
        {
            if section == 0 && self.pillImage != nil
            {
                return 4
            }
            else if section == 0 && self.pillImage == nil
            {
                return 3
            }
            else
            {
                // If expanded array contains section, then show collapse button, otherwise show expand button
                if let index = find(self.expandedSections, self.sectionHeaders[section - 1])
                {
                    return 1
                }
                else
                {
                    return 0
                }
            }
        }
        
        return 0
    }

    // Configure the cell
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell
    {
        var cell = UITableViewCell()
        
        if indexPath.section == 0 && indexPath.row == 0
        {
            cell = tableView.dequeueReusableCellWithIdentifier("medicationNameCell", forIndexPath: indexPath) as! UITableViewCell
            
            if let brandName = self.medication.openfda!["brand_name"] as? [String]
            {
                cell.textLabel!.text = brandName[0].capitalizedString
            }
            else if let genericName = self.medication.openfda!["generic_name"] as? [String]
            {
                cell.textLabel!.text = genericName[0].capitalizedString
            }
        }
        else if indexPath.section == 0 && indexPath.row == 1 && self.pillImage != nil
        {
            cell = tableView.dequeueReusableCellWithIdentifier("medicationImageCell", forIndexPath: indexPath) as! UITableViewCell
            var pillImageView = cell.viewWithTag(1) as! UIImageView
            pillImageView.image = self.pillImage
        }
        else if indexPath.section == 0 && (indexPath.row == 1 && self.pillImage == nil) || (indexPath.row == 2 && self.pillImage != nil)
        {
            cell = tableView.dequeueReusableCellWithIdentifier("medicationInfoCell", forIndexPath: indexPath) as! UITableViewCell
            
            var propertyNameLabel = cell.viewWithTag(3) as! UILabel
            var propertyValueLabel = cell.viewWithTag(4) as! UILabel
            
            propertyNameLabel.text = "Generic Name"
            
            if let genericArray = self.medication.openfda!["generic_name"] as? [String]
            {
                propertyValueLabel.text = genericArray[0].capitalizedString
            }
            else
            {
                propertyValueLabel.text = "Unavailable"
            }
        }
        else if indexPath.section == 0 && (indexPath.row == 2 && self.pillImage == nil) || (indexPath.row == 3 && self.pillImage != nil)
        {
            cell = tableView.dequeueReusableCellWithIdentifier("medicationInfoCell", forIndexPath: indexPath) as! UITableViewCell
            
            var propertyNameLabel = cell.viewWithTag(3) as! UILabel
            var propertyValueLabel = cell.viewWithTag(4) as! UILabel
            
            propertyNameLabel.text = "Manufacturer"
            
            if let genericArray = self.medication.openfda!["manufacturer_name"] as? [String]
            {
                propertyValueLabel.text = genericArray[0].capitalizedString
            }
            else
            {
                propertyValueLabel.text = "Unavailable"
            }
        }
        else if indexPath.section > 0
        {
            cell = tableView.dequeueReusableCellWithIdentifier("collapsibleCell", forIndexPath: indexPath) as! UITableViewCell
            
            var property = self.sectionHeaders[indexPath.section - 1]
            
            if let propertyValue = self.medication.valueForKey(property) as? [String]
            {
                cell.textLabel!.text = ",".join(propertyValue)
            }
            else if let propertyValue = self.medication.valueForKey(property) as? String
            {
                cell.textLabel!.text = propertyValue
            }
        }

        return cell
    }
    
    // Dynamic heights for cell rows
    override func tableView(tableView: UITableView, heightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat
    {
        return UITableViewAutomaticDimension
    }
    
    override func tableView(tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat
    {
        if section == 0
        {
            return 0
        }

        return 45
    }
    
    // Header for each section
    override func tableView(tableView: UITableView, viewForHeaderInSection section: Int) -> UIView?
    {
        if section == 0
        {
            return nil
        }

        // Create header view
        var headerView = UIView()
        headerView.backgroundColor = UIColor(white:0.9, alpha:1.0)
        headerView.tag = section
        
        // Create header label view
        var headerLabel = UILabel(frame:CGRectMake(10, 11, 250, 22))
        
        // Set label attributes
        headerLabel.textColor = UIColor(red: 47/255.0, green: 59/255.0, blue: 117/255.0, alpha: 1)
        headerLabel.backgroundColor = UIColor.clearColor()
        
        // Set label font
        if let font = UIFont(name:"RobotoSlab-Bold", size:16.0)
        {
            headerLabel.font = font
        }
        headerLabel.text = self.sectionHeaderTitles[section - 1]
        
        // Create header image view
        var headerImageView = UIImageView()
        headerImageView.frame = CGRectMake(self.tableView.bounds.size.width - 45, 0, 45, 45)
        
        // If expanded array contains section, then show collapse button, otherwise show expand button
        if let index = find(self.expandedSections, self.sectionHeaders[section - 1])
        {
            headerImageView.image = UIImage(named: "CollapseCellButton")
        }
        else
        {
            headerImageView.image = UIImage(named: "ExpandCellButton")
        }
        
        // Add header subviews
        headerView.addSubview(headerLabel)
        headerView.addSubview(headerImageView)
        
        // Add gesture recognizer
        let tapRecognizer = UITapGestureRecognizer(target: self, action: "expandCollapseSection:")
        headerView.addGestureRecognizer(tapRecognizer)
        
        // Return header view
        return headerView
    }

    // MARK: - Navigation Methods
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?)
    {
        if segue.identifier == "saveMedicationSegue"
        {
            var pickerViewController =  segue.destinationViewController as! CabinetPickerViewController
            pickerViewController.medication = self.medication
        }
    }
    
    func backNavigate()
    {
        self.navigationController?.popViewControllerAnimated(true)
    }
    
    // MARK: - My Methods
    
    func saveMedication()
    {
        self.performSegueWithIdentifier("saveMedicationSegue", sender:self)
    }
    
    func removeMedication()
    {
        // Create the alert controller
        var alert = UIAlertController(title:"Attention Required", message:"Are you sure you want to delete this medication from your cabinet?", preferredStyle:.Alert)
        
        // Add a cancel button
        alert.addAction(UIAlertAction(title:"Cancel", style:.Default, handler:nil))
        
        // Add a reset password button with callback
        alert.addAction(UIAlertAction(title:"Delete", style: UIAlertActionStyle.Destructive, handler:{(alert: UIAlertAction!) in
            
            if let unarchivedObject = NSUserDefaults.standardUserDefaults().objectForKey("medicationArray") as? NSData
            {
                var savedMedications = NSKeyedUnarchiver.unarchiveObjectWithData(unarchivedObject) as! [Medication]
                
                // Remove the medication from the saved medications array
                for var index = savedMedications.count - 1; index >= 0; --index
                {
                    println("Med ID: \(savedMedications[index].set_id) self ID: \(self.medication.set_id)")
                    if savedMedications[index].set_id == self.medication.set_id
                    {
                        println("Saved: \(savedMedications) index: \(index)")
                        
                        // Remove medication from stored array
                        savedMedications.removeAtIndex(index)
                        
                        // Modify and save or remove stored medications
                        if savedMedications.isEmpty
                        {
                            NSUserDefaults.standardUserDefaults().removeObjectForKey("medicationArray")
                        }
                        else
                        {
                            let archivedObject = NSKeyedArchiver.archivedDataWithRootObject(savedMedications as NSArray)
                            NSUserDefaults.standardUserDefaults().setObject(archivedObject, forKey:"medicationArray")
                            NSUserDefaults.standardUserDefaults().synchronize()
                        }
                        
                        // Alert user that the cabinet name is required
                        var foundAlert = UIAlertController(title:"Medication Removal", message:"You have successfully removed this medication from your medicine cabinet.", preferredStyle:UIAlertControllerStyle.Alert)
                        foundAlert.addAction(UIAlertAction(title:"OK", style:UIAlertActionStyle.Default, handler: {(alertAction) -> Void in
                            self.navigationController?.popViewControllerAnimated(true)
                        }))
                        self.presentViewController(foundAlert, animated:true, completion:nil)
                    }
                }
            }
        }))
        
        // Present the alert view so user can enter the category name
        self.presentViewController(alert, animated:true, completion:nil)
    }
    
    func expandCollapseSection(recognizer:UITapGestureRecognizer)
    {
        if let index = find(self.expandedSections, self.sectionHeaders[recognizer.view!.tag - 1])
        {
            self.expandedSections.removeAtIndex(index)
        }
        else
        {
            self.expandedSections.append(self.sectionHeaders[recognizer.view!.tag - 1])
        }
        
        // Reload the table with the new cabinet (using this method for proper autoheight calculation on table cells)
        self.tableView.reloadSections(NSIndexSet(index: recognizer.view!.tag), withRowAnimation:.Fade)
    }
}
