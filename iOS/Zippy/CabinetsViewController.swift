//
//  CabinetsViewController.swift
//  Zippy
//
//  Created by Geoffrey Bender on 6/18/15.
//  Copyright (c) 2015 Segue Technologies, Inc. All rights reserved.
//

import UIKit

class CabinetsViewController: UITableViewController
{
    // MARK: - Variables
    
    let userDefaults = NSUserDefaults.standardUserDefaults()
    var cabinetArray = [String]()
    
    // MARK: - Lifecycle Methods
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        
        // Do this when page load in case coming direct from menu page
        self.setNewsTabBadgeCount()

        // Set bar buttons
        var homeBarButton = UIBarButtonItem(title:"Home", style:UIBarButtonItemStyle.Plain, target:self, action:"goHome:")
        var addBarButton = UIBarButtonItem(barButtonSystemItem: UIBarButtonSystemItem.Add, target: self, action: "createNewCabinet:")
        
        self.navigationItem.leftBarButtonItem = homeBarButton
        self.navigationItem.rightBarButtonItem = addBarButton
        
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
        
        // If we have saved medicine cabinets
        if self.userDefaults.valueForKey("cabinetArray") != nil
        {
            // Set saved cabinets to local array
            self.cabinetArray = self.userDefaults.valueForKey("cabinetArray") as! [String]
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

    // MARK: - Table view data source

    override func numberOfSectionsInTableView(tableView: UITableView) -> Int
    {
        return 1
    }

    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        return self.cabinetArray.count
    }

    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell
    {
        let cell = tableView.dequeueReusableCellWithIdentifier("cabinetCell", forIndexPath: indexPath) as! UITableViewCell
        
        var cabinetLabel = cell.viewWithTag(10) as! UILabel
        cabinetLabel.text = self.cabinetArray[indexPath.row].capitalizedString
        
        // Create a long press gesture recognizer and add it to the table view cell
        cell.contentView.addGestureRecognizer(UILongPressGestureRecognizer(target:self, action:"tableLongPressed:"))

        return cell
    }

    override func tableView(tableView: UITableView, canEditRowAtIndexPath indexPath: NSIndexPath) -> Bool
    {
        return true
    }

    override func tableView(tableView: UITableView, commitEditingStyle editingStyle: UITableViewCellEditingStyle, forRowAtIndexPath indexPath: NSIndexPath)
    {
        if editingStyle == .Delete
        {
            // Create the alert controller
            var alert = UIAlertController(title:"Attention Required", message:"Are you sure you want to delete this medicine cabinet?  Any medications stored in this cabinet will be deleted with it.", preferredStyle:.Alert)
            
            // Add a cancel button
            alert.addAction(UIAlertAction(title:"Cancel", style:.Default, handler:{(alert: UIAlertAction!) in
                self.tableView.reloadRowsAtIndexPaths([indexPath], withRowAnimation:UITableViewRowAnimation.Fade)
            }))
            
            // Add a delete button with callback
            alert.addAction(UIAlertAction(title:"Delete", style: UIAlertActionStyle.Destructive, handler:{(alert: UIAlertAction!) in
                
                // Get cabinet
                let cabinet = self.cabinetArray[indexPath.row]
                
                // If we have saved medications
                if let unarchivedObject = NSUserDefaults.standardUserDefaults().objectForKey("medicationArray") as? NSData
                {
                    // Get saved medications
                    var savedMedications = NSKeyedUnarchiver.unarchiveObjectWithData(unarchivedObject) as! [Medication]
                    
                    // Create array to hold indexes of saved meds to remove
                    var medsToRemove = [Int]()
                    
                    // Loop over saved meds
                    for (index, med) in enumerate(savedMedications)
                    {
                        // Grab indexes of each medication that matches our deleted cabinet
                        if med.medicineCabinet == cabinet
                        {
                            // Add index to delete array
                            medsToRemove.append(index)
                        }
                    }
                    
                    // Reverse sort the delete array if it has elements
                    let sortedArray = medsToRemove.sorted{$0 > $1}

                    // Delete from saved medications array
                    for index in sortedArray
                    {
                        savedMedications.removeAtIndex(index)
                    }
                    
                    // Store modified saved medications array
                    let archivedObject = NSKeyedArchiver.archivedDataWithRootObject(savedMedications as NSArray)
                    self.userDefaults.setObject(archivedObject, forKey:"medicationArray")
                    self.userDefaults.synchronize()
                }
                
                // Delete cabinet from medicine cabinet array
                self.cabinetArray.removeAtIndex(indexPath.row)
                
                // Save array
                self.userDefaults.setValue(self.cabinetArray, forKey:"cabinetArray")
                
                // Delete the row from the data source
                tableView.deleteRowsAtIndexPaths([indexPath], withRowAnimation:.Fade)
            }))
            
            // Present the alert view so user can enter the category name
            self.presentViewController(alert, animated:true, completion:nil)
        }
    }
    
    override func tableView(tableView: UITableView, heightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat
    {
        return UITableViewAutomaticDimension
    }
    
    override func tableView(tableView: UITableView, estimatedHeightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat
    {
        return 60
    }

    // MARK: - Navigation Methods

    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?)
    {
        let indexPath = self.tableView.indexPathForSelectedRow()
        let destinationController = segue.destinationViewController as! CabinetContentsViewController
        
        destinationController.medicineCabinet = self.cabinetArray[indexPath!.row]
    }
    
    // MARK: - My Methods
    
    // Shows menu page
    @IBAction func goHome(sender:AnyObject)
    {
        self.dismissViewControllerAnimated(true, completion:nil)
    }
    
    // Sets the badge count on the news tab if new postings are found
    func setNewsTabBadgeCount()
    {
        // Get news controller
        let newsNavController = self.tabBarController?.viewControllers![2] as! UINavigationController
        let newsController = newsNavController.viewControllers[0] as! NewsViewController
        
        // Fire load
        newsController.loadData(self)
    }
    
    @IBAction func createNewCabinet(sender: AnyObject)
    {
        // Create the alert controller
        var alert = UIAlertController(title:"Create Medicine Cabinet", message:nil, preferredStyle:.Alert)
        
        // Add the text field
        alert.addTextFieldWithConfigurationHandler({(textField) -> Void in
            textField.autocapitalizationType = UITextAutocapitalizationType.Words
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
                    
                    println("Found \(duplicates) dupes so far!")
                    
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
                        
                        println("Found parens, highest number is \(highestNumber)")
                    }
                    
                    if highestNumber >= duplicates
                    {
                        println("Highest number was higher than dupes!")
                        duplicates = highestNumber + 1
                    }
                    
                    println("Final number: \(duplicates)")
                }
            }
            
            // If we found duplicates
            if duplicates > 0
            {
                // Modify cabinet string
                let modifiedCabinet = "\(cabinet.lowercaseString) (\(duplicates))"
                
                // Append the modified string to our array
                self.cabinetArray.append(modifiedCabinet)
            }
            else
            {
                // Append the string to our array
                self.cabinetArray.append(cabinet.lowercaseString)
            }
            
            // Save our array to persistent store
            self.userDefaults.setValue(self.cabinetArray, forKey: "cabinetArray")
            
            // Reload the table with the new cabinet (using this method for proper autoheight calculation on table cells)
            self.tableView.reloadSections(NSIndexSet(indexesInRange:NSMakeRange(0, self.tableView.numberOfSections())), withRowAnimation:.None)
        }
    }
    
    // User touched and held a cell
    func tableLongPressed(sender:UIGestureRecognizer)
    {
        // Just capture starting event
        if sender.state == UIGestureRecognizerState.Began
        {
            // Grab the object at the pressed table row
            let point = sender.locationInView(self.tableView) as CGPoint
            
            if let indexPath : NSIndexPath = self.tableView.indexPathForRowAtPoint(point)
            {
                var cabinet = self.cabinetArray[indexPath.row]
                self.tableView.cellForRowAtIndexPath(indexPath)?.selected = true
                
                // Create the alert controller
                var alert = UIAlertController(title:"Edit Cabinet Name", message:nil, preferredStyle:.Alert)
                
                // Add the text field with the cabinet name
                alert.addTextFieldWithConfigurationHandler({(textField) -> Void in
                    textField.autocapitalizationType = UITextAutocapitalizationType.Words
                    textField.text = self.cabinetArray[indexPath.row].capitalizedString
                })
                
                // Add a cancel button
                alert.addAction(UIAlertAction(title:"Cancel", style:.Default, handler:{(action) -> Void in
                    
                    // Deselect table cell
                    self.tableView.cellForRowAtIndexPath(indexPath)?.selected = false
                }))
                
                // Add a create button with callback
                alert.addAction(UIAlertAction(title:"Save", style:.Default, handler:{(action) -> Void in
                    
                    // Get text field from alert controller
                    let textField = alert.textFields![0] as! UITextField
                    
                    // Deselect table cell
                    self.tableView.cellForRowAtIndexPath(indexPath)?.selected = false
                    
                    // Rename tag
                    self.renameCabinet(textField.text, indexPath:indexPath)
                    
                }))
                
                // Present the alert view so user can enter the category name
                self.presentViewController(alert, animated:true, completion:nil)
            }
        }
    }
    
    // Rename cabinet
    func renameCabinet(fieldText:String, indexPath:NSIndexPath)
    {
        // Make sure user entered text
        if fieldText.isEmpty
        {
            // Alert user that the category already exists
            var foundAlert = UIAlertController(title:"Attention Required", message:"Please enter a cabinet name.", preferredStyle:UIAlertControllerStyle.Alert)
            foundAlert.addAction(UIAlertAction(title:"OK", style:UIAlertActionStyle.Default, handler:nil))
            self.presentViewController(foundAlert, animated:true, completion:nil)
        }
        else
        {
            // Make sure doesn't already exist
            if let index = find(self.cabinetArray, fieldText.lowercaseString)
            {
                // Alert user that the cabinet name is required
                var foundAlert = UIAlertController(title:"Attention Required", message:"Cabinet name already exists, please enter another name.", preferredStyle:UIAlertControllerStyle.Alert)
                foundAlert.addAction(UIAlertAction(title:"OK", style:UIAlertActionStyle.Default, handler:nil))
                self.presentViewController(foundAlert, animated:true, completion:nil)
            }
            else
            {
                self.cabinetArray[indexPath.row] = fieldText.lowercaseString
                userDefaults.setValue(self.cabinetArray, forKey: "cabinetArray")
                self.tableView.reloadRowsAtIndexPaths([indexPath], withRowAnimation:.Fade)
            }
        }
    }
}
