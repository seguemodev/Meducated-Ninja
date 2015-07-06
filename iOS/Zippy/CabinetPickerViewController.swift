//
//  CabinetPickerViewController.swift
//  Zippy
//
//  Created by Geoffrey Bender on 6/19/15.
//  Copyright (c) 2015 Segue Technologies, Inc. All rights reserved.
//

import UIKit

class CabinetPickerViewController: UIViewController, UITableViewDelegate, UITableViewDataSource
{
    // MARK: - Interface Outlets
    
    @IBOutlet weak var tableView: UITableView!
    
    // MARK: - Variables
    
    let userDefaults = NSUserDefaults.standardUserDefaults()
    var cabinetArray = [String]()
    var medication: Medication!
    var selectedCabinet = ""
    
    // MARK: - Lifecycle Methods
    
    // Each time view appears
    override func viewWillAppear(animated: Bool)
    {
        super.viewWillAppear(animated)
        
        // For dynamic table row height
        self.tableView.estimatedRowHeight = 80
        self.tableView.rowHeight = UITableViewAutomaticDimension
        
        var leftBarButton = UIBarButtonItem(title:"Cancel", style:UIBarButtonItemStyle.Plain, target:self, action:"cancel")
        self.navigationItem.leftBarButtonItem = leftBarButton
        
        var rightBarButton = UIBarButtonItem(title:"Save", style:UIBarButtonItemStyle.Plain, target:self, action:"saveMedicationToCabinet:")
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
        
        // If we have saved medicine cabinets
        if self.userDefaults.valueForKey("cabinetArray") != nil
        {
            // Set saved cabinets to local array
            self.cabinetArray = self.userDefaults.valueForKey("cabinetArray") as! [String]
        }
    }
    
    // MARK: - Table View Methods
    
    func numberOfSectionsInTableView(tableView: UITableView) -> Int
    {
        return 1
    }
    
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        return self.cabinetArray.count
    }
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell
    {
        let cell = tableView.dequeueReusableCellWithIdentifier("cabinetCell", forIndexPath: indexPath) as! UITableViewCell
        
        cell.textLabel!.text = self.cabinetArray[indexPath.row].capitalizedString
        
        if self.cabinetArray[indexPath.row] == self.selectedCabinet
        {
            cell.accessoryView = UIImageView(image:UIImage(named:"CheckedButton"))
        }
        else
        {
            cell.accessoryView = UIImageView(image:UIImage(named:"UncheckedButton"))
        }
        
        return cell
    }
    
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath)
    {
        // Set selected cabinet
        self.selectedCabinet = self.cabinetArray[indexPath.row]
        
        // Reload the table with the new cabinet (using this method for proper autoheight calculation on table cells)
        self.tableView.reloadSections(NSIndexSet(indexesInRange:NSMakeRange(0, self.tableView.numberOfSections())), withRowAnimation:.None)
        
        self.tableView.scrollToRowAtIndexPath(indexPath, atScrollPosition: UITableViewScrollPosition.Middle, animated:true)
    }
    
    // MARK: - My Methods
    
    // Dismiss view when cancel button is clicked
    func cancel()
    {
        self.navigationController?.popViewControllerAnimated(true)
    }
    
    // Show prompt for user to create new medicine cabinet
    @IBAction func createNewCabinet(sender: AnyObject)
    {
        // Create the alert controller
        var alert = UIAlertController(title:"Create Medicine Cabinet", message:nil, preferredStyle:.Alert)
        
        // Add the text field
        alert.addTextFieldWithConfigurationHandler({(textField) -> Void in
            textField.autocapitalizationType = UITextAutocapitalizationType.Words
            textField.keyboardAppearance = UIKeyboardAppearance.Dark
            textField.text = ""
        })
        
        // Add a cancel button
        alert.addAction(UIAlertAction(title:"Cancel", style:.Default, handler:nil))
        
        // Add a create button with callback
        alert.addAction(UIAlertAction(title:"Create", style:.Default, handler:{(action) -> Void in
            
            // Get text field from alert controller
            let textField = alert.textFields![0] as! UITextField
            
            // Complete creating the tag
            self.completeCreateNewCabinet(textField.text)
            
        }))
        
        // Present the alert view so user can enter the category name
        self.presentViewController(alert, animated:true, completion:nil)
    }
    
    // Completion handler for new cabinet creation
    func completeCreateNewCabinet(fieldText:String)
    {
        // Make sure user entered text
        if fieldText.isEmpty
        {
            // Alert user that the cabinet name is required
            var foundAlert = UIAlertController(title:"Attention Required", message:"Please enter a cabinet name.", preferredStyle:UIAlertControllerStyle.Alert)
            foundAlert.addAction(UIAlertAction(title:"OK", style:UIAlertActionStyle.Default, handler:nil))
            self.presentViewController(foundAlert, animated:true, completion:nil)
        }
        else
        {
            // Trim string
            var cabinet = fieldText.stringByTrimmingCharactersInSet(NSCharacterSet.whitespaceCharacterSet())
            
            // Set variable to track number of duplicates encountered
            var duplicates = 0
            
            // Set variable to track highest duplicate label
            var highestNumber = 0
            
            // Loop our cabinet array to check for duplicates
            for item in self.cabinetArray
            {
                // Get the cabinet name without the parentheses
                let rawStringArray = item.componentsSeparatedByString("(")
                
                // Trim the string to eliminate whitespace
                let trimmedRawString = rawStringArray[0].stringByTrimmingCharactersInSet(NSCharacterSet.whitespaceCharacterSet())
                
                // If our entered text matches a cabinet in our array
                if cabinet.lowercaseString == trimmedRawString
                {
                    // Increase duplicate count
                    duplicates++
                    
                    // If our raw string array has more then one item, then it had parentheses
                    if rawStringArray.count > 1
                    {
                        // Get its number
                        let labelNumber = rawStringArray[1].componentsSeparatedByString(")")[0].toInt()
                        
                        // Track the highest label number
                        if labelNumber > highestNumber
                        {
                            highestNumber = labelNumber!
                        }
                    }
                    
                    if highestNumber >= duplicates
                    {
                        duplicates = highestNumber + 1
                    }
                }
            }
            
            // If we found duplicates
            if duplicates > 0
            {
                // Modify cabinet string
                let modifiedCabinet = "\(cabinet.lowercaseString) (\(duplicates))"
                
                // Append the modified string to our array
                self.cabinetArray.insert(modifiedCabinet, atIndex:0)
            }
            else
            {
                // Append the string to our array
                self.cabinetArray.insert(cabinet.lowercaseString, atIndex:0)
            }
            
            // Set selected cabinet to newly created cabinet
            self.selectedCabinet = self.cabinetArray.first!
            
            // Scroll to first row
            self.tableView.setContentOffset(CGPointZero, animated:true)
            
            // Reload the table with the new cabinet (using this method for proper autoheight calculation on table cells)
            self.tableView.reloadSections(NSIndexSet(indexesInRange:NSMakeRange(0, self.tableView.numberOfSections())), withRowAnimation:.None)
            
            let medToMove = self.cabinetArray.first
            self.cabinetArray.removeAtIndex(0)
            self.cabinetArray.append(medToMove!)
            
            // Save medicine cabinet array to persistent store
            self.userDefaults.setValue(self.cabinetArray, forKey: "cabinetArray")

            // Automatically save the medication to the new cabinet
            self.saveMedicationToCabinet(self)
        }
    }
    
    // Save the medication to the selected medicine cabinet
    @IBAction func saveMedicationToCabinet(sender: AnyObject)
    {
        // Make sure user selected a cabinet
        if self.selectedCabinet.isEmpty
        {
            // Alert user that the cabinet name is required
            var foundAlert = UIAlertController(title:"Attention Required", message:"Please choose a cabinet to save to.", preferredStyle:UIAlertControllerStyle.Alert)
            foundAlert.addAction(UIAlertAction(title:"OK", style:UIAlertActionStyle.Default, handler:nil))
            self.presentViewController(foundAlert, animated:true, completion:nil)
        }
        else
        {
            // Set the medication object's medicine cabinet to selected picker row value
            self.medication.medicineCabinet = self.selectedCabinet
            
            // Create empty saved medication array
            var savedMedications = [Medication]()
            
            // If we have a stored medication array, then load the contents into our empty array
            if let unarchivedObject = NSUserDefaults.standardUserDefaults().objectForKey("medicationArray") as? NSData
            {
                savedMedications = NSKeyedUnarchiver.unarchiveObjectWithData(unarchivedObject) as! [Medication]
            }
            
            // Add saved medication to our array
            savedMedications.append(self.medication)
            
            // Save the modified medication array
            let archivedObject = NSKeyedArchiver.archivedDataWithRootObject(savedMedications as NSArray)
            self.userDefaults.setObject(archivedObject, forKey:"medicationArray")
            self.userDefaults.synchronize()
            
            // Alert user that the medication has been saved
            let indexPath = self.tableView.indexPathForSelectedRow()
            
            // Create confirmation
            var foundAlert = UIAlertController(title:"Success", message:"You have successfully saved this medication to \(self.selectedCabinet.capitalizedString).", preferredStyle:UIAlertControllerStyle.Alert)
            
            // Create handler for OK button
            foundAlert.addAction(UIAlertAction(title:"OK", style:UIAlertActionStyle.Default, handler:{(action) -> Void in
                
                // Dismiss the view
                self.navigationController?.popViewControllerAnimated(true)
            }))
            
            // Show confirmation
            self.presentViewController(foundAlert, animated:true, completion:nil)
        }
    }
}
