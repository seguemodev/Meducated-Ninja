//
//  CabinetContentsViewController.swift
//  Zippy
//
//  Created by Geoffrey Bender on 6/22/15.
//  Copyright (c) 2015 Segue Technologies, Inc. All rights reserved.
//

import UIKit

class CabinetContentsViewController: UITableViewController
{
    // MARK: - Variables
    
    var medicineCabinet: String?
    var savedMedications = [Medication]()
    var cabinetMedications = [Medication]()
    
    // MARK: - View Lifecycle Methods
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        
        // For dynamic table row height
        self.tableView.estimatedRowHeight = 80
        self.tableView.rowHeight = UITableViewAutomaticDimension
        
        var leftBarButton = UIBarButtonItem(title:"Back", style:UIBarButtonItemStyle.Plain, target:self, action:"backNavigate")
        self.navigationItem.leftBarButtonItem = leftBarButton
        
        var normalButtonBackground = UIImage(named:"BlueButtonBackground")!.resizableImageWithCapInsets(UIEdgeInsetsMake(0, 10, 0, 10))
        self.navigationItem.leftBarButtonItem!.setBackgroundImage(normalButtonBackground, forState: UIControlState.Normal, barMetrics: UIBarMetrics.Default)
        
        var pressedButtonBackground = UIImage(named:"GreenButtonBackground")!.resizableImageWithCapInsets(UIEdgeInsetsMake(0, 10, 0, 10))
        self.navigationItem.leftBarButtonItem!.setBackgroundImage(pressedButtonBackground, forState: UIControlState.Highlighted, barMetrics: UIBarMetrics.Default)
        
        if let font = UIFont(name:"RobotoSlab-Bold", size:14.0)
        {
            self.navigationItem.leftBarButtonItem!.setTitleTextAttributes([NSFontAttributeName:font, NSForegroundColorAttributeName:UIColor.whiteColor()], forState:UIControlState.Normal)
        }
    }
    
    override func viewWillAppear(animated: Bool)
    {
        super.viewWillAppear(animated)
        
        self.cabinetMedications.removeAll(keepCapacity: false)
        
        // Unarchive saved medications from persistent store
        if let unarchivedObject = NSUserDefaults.standardUserDefaults().objectForKey("medicationArray") as? NSData
        {
            self.savedMedications = NSKeyedUnarchiver.unarchiveObjectWithData(unarchivedObject) as! [Medication]

            for medication in self.savedMedications
            {
                if medication.medicineCabinet! == self.medicineCabinet!
                {
                    self.cabinetMedications.append(medication)
                }
            }
        }
        
        // Reload the table with the new cabinet (using this method for proper autoheight calculation on table cells)
        self.tableView.reloadSections(NSIndexSet(indexesInRange:NSMakeRange(0, self.tableView.numberOfSections())), withRowAnimation:.None)
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

    override func numberOfSectionsInTableView(tableView: UITableView) -> Int
    {
        return 1
    }

    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        return self.cabinetMedications.count
    }

    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell
    {
        let cell = tableView.dequeueReusableCellWithIdentifier("medicationCell", forIndexPath: indexPath) as! UITableViewCell
        var medicationLabel = cell.viewWithTag(10) as! UILabel
        var manufacturerLabel = cell.viewWithTag(11) as! UILabel

        if let medDict = self.cabinetMedications[indexPath.row].openfda
        {
            if let brandName = medDict.valueForKey("brand_name") as? [String]
            {
                medicationLabel.text = brandName[0].capitalizedString
            }
            else if let genericName = medDict.valueForKey("generic_name") as? [String]
            {
                medicationLabel.text = genericName[0].capitalizedString
            }
            
            if let manufacturerName = medDict.valueForKey("manufacturer_name") as? [String]
            {
                manufacturerLabel.text = manufacturerName[0].capitalizedString
            }
        }

        return cell
    }

    // Override to support conditional editing of the table view
    override func tableView(tableView: UITableView, canEditRowAtIndexPath indexPath: NSIndexPath) -> Bool
    {
        return true
    }

    // Override to support editing the table view
    override func tableView(tableView: UITableView, commitEditingStyle editingStyle: UITableViewCellEditingStyle, forRowAtIndexPath indexPath: NSIndexPath)
    {
        if editingStyle == .Delete
        {
            // Create the alert controller
            var alert = UIAlertController(title:"Attention Required", message:"Are you sure you want to delete this medication from your cabinet?", preferredStyle:.Alert)
            
            // Add a cancel button
            alert.addAction(UIAlertAction(title:"Cancel", style:.Default, handler:{(alert: UIAlertAction!) in
                self.tableView.reloadRowsAtIndexPaths([indexPath], withRowAnimation:UITableViewRowAnimation.Fade)
            }))
            
            // Add a delete button with callback
            alert.addAction(UIAlertAction(title:"Delete", style: UIAlertActionStyle.Destructive, handler:{(alert: UIAlertAction!) in
                
                // Get set_id for deleted medication
                let deletedMed = self.cabinetMedications[indexPath.row]
                
                // Remove the medication from the saved medications array
                for var index = self.savedMedications.count - 1; index >= 0; --index
                {
                    if self.savedMedications[index].set_id == deletedMed.set_id
                    {
                        self.savedMedications.removeAtIndex(index)
                    }
                }
                
                // If our saved medications array is empty, then remove from persistent store
                if self.savedMedications.isEmpty
                {
                    NSUserDefaults.standardUserDefaults().removeObjectForKey("medicationArray")
                }
                else
                {
                    // Otherwise, save the modified saved medications array to persistent store
                    let archivedObject = NSKeyedArchiver.archivedDataWithRootObject(self.savedMedications as NSArray)
                    NSUserDefaults.standardUserDefaults().setObject(archivedObject, forKey:"medicationArray")
                    NSUserDefaults.standardUserDefaults().synchronize()
                }
                
                // Find indexes of deleted med in our cabinet array to remove
                var removedIndexPaths = [NSIndexPath]()
                for var cabIndex = self.cabinetMedications.count - 1; cabIndex >= 0; --cabIndex
                {
                    if self.cabinetMedications[cabIndex].set_id == deletedMed.set_id
                    {
                        self.cabinetMedications.removeAtIndex(cabIndex)
                        removedIndexPaths.append(NSIndexPath(forRow:cabIndex, inSection:0))
                    }
                }
                
                // Delete the rows from the data source
                tableView.deleteRowsAtIndexPaths(removedIndexPaths, withRowAnimation: .Fade)
                
                // Alert user that the cabinet name is required
                var foundAlert = UIAlertController(title:"Medication Removal", message:"You have successfully removed this medication from your medicine cabinet.", preferredStyle:UIAlertControllerStyle.Alert)
                foundAlert.addAction(UIAlertAction(title:"OK", style:UIAlertActionStyle.Default, handler:nil))
                self.presentViewController(foundAlert, animated:true, completion:nil)
            }))
            
            // Present the alert view so user can enter the category name
            self.presentViewController(alert, animated:true, completion:nil)
        }
    }

    // MARK: - Navigation Methods

    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?)
    {
        let indexPath = self.tableView.indexPathForSelectedRow()
        
        if segue.identifier == "drugInfoSegue"
        {
            let destinationController = segue.destinationViewController as! DrugInformationViewController
            destinationController.medication = self.cabinetMedications[indexPath!.row]
            destinationController.removeMedicationMode = true
        }
    }
    
    func backNavigate()
    {
        self.navigationController?.popViewControllerAnimated(true)
    }
}
