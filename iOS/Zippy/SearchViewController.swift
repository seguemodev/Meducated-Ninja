//
//  SearchViewController.swift
//  Zippy
//
//  Created by Geoffrey Bender on 6/18/15.
//  Copyright (c) 2015 Segue Technologies, Inc. All rights reserved.
//

import UIKit

class SearchViewController: UIViewController, UITableViewDataSource, UITableViewDelegate, SearchFilterDelegate, UITextFieldDelegate
{
    // MARK: - Interface Outlets
    
    @IBOutlet weak var drugField: UITextField!
    @IBOutlet weak var medicationTable: UITableView!
    @IBOutlet weak var backgroundView: UIView!
    
    // MARK: - Variables
    
    var drugs = [Medication]()
    var originalResults = [Medication]()
    var loadingNotification: MBProgressHUD?
    var searchSuggestions = [String]()
    var searchStartIndex = 0
    var totalSearchResults = 0
    var isLoading = false
    var scrollToFirstRecord = false
    
    // MARK: - Lifecycle Methods
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        
        // For dynamic table row height
        self.medicationTable.estimatedRowHeight = 80
        self.medicationTable.rowHeight = UITableViewAutomaticDimension
        
        var homeBarButton = UIBarButtonItem(title:"Home", style:UIBarButtonItemStyle.Plain, target:self, action:"goHome:")
        var filterBarButton = UIBarButtonItem(title:"Filter", style:UIBarButtonItemStyle.Plain, target:self, action:"filterSearchResults")
        
        self.navigationItem.leftBarButtonItem = homeBarButton
        self.navigationItem.rightBarButtonItem = filterBarButton
        
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
        
        let gestureRecognizer = UITapGestureRecognizer(target:self, action:"hideKeyboard:")
        self.backgroundView.addGestureRecognizer(gestureRecognizer)
        
        // Load news to set news badge
        self.setNewsTabBadgeCount()
    }
    
    // Important that these remain or there will be bugs!
    override func viewWillAppear(animated: Bool)
    {
        super.viewWillAppear(animated)
        
        // Add an observer to monitor search criteria changes in the search field (must be added and removed as view appears or typing in any uitextfield in other views will trigger this notification)
        NSNotificationCenter.defaultCenter().addObserver(self, selector:"offerSuggestions", name:UITextFieldTextDidChangeNotification, object:nil)
    }
    
    // Important that these remain or there will be bugs!
    override func viewWillDisappear(animated: Bool)
    {
        super.viewWillDisappear(animated)
        
        // Remove typing observer (must be added and removed as view appears of typing in any uitextfield in other views will trigger this notification)
        NSNotificationCenter.defaultCenter().removeObserver(self, name:UITextFieldTextDidChangeNotification, object:nil)
    }
    
    // Reload table contents on device rotation
    override func viewWillTransitionToSize(size: CGSize, withTransitionCoordinator coordinator: UIViewControllerTransitionCoordinator)
    {
        super.viewWillTransitionToSize(size, withTransitionCoordinator: coordinator)
        
        // Completion handler to execute code after transition
        coordinator.animateAlongsideTransition({(context) -> Void in}, completion: {(context) -> Void in
            
            if self.medicationTable != nil
            {
                // Scroll to first row
                self.medicationTable.setContentOffset(CGPointZero, animated:false)
                
                // Reload the table with the new cabinet (using this method for proper autoheight calculation on table cells)
                self.medicationTable.reloadSections(NSIndexSet(indexesInRange:NSMakeRange(0, self.medicationTable.numberOfSections())), withRowAnimation:UITableViewRowAnimation.None)
            }
        })
    }
    
    // MARK: - My Methods
    
    // Sets the badge count on the news tab if new postings are found
    func setNewsTabBadgeCount()
    {
        // Get news controller
        let newsNavController = self.tabBarController?.viewControllers![2] as! UINavigationController
        let newsController = newsNavController.viewControllers[0] as! NewsViewController
        
        // Fire load
        newsController.loadData(self)
    }
    
    // Search for medication results
    @IBAction func searchForInfo(sender:AnyObject)
    {
        // If not connected to the Internet
        if reachabilityStatus == kNotReachable
        {
            // Alert user that field is required
            var alert = UIAlertController(title:"Attention Required", message:"This application requires an Internet connection. Please try again once connectivity has been established.", preferredStyle:UIAlertControllerStyle.Alert)
            alert.addAction(UIAlertAction(title:"OK", style:UIAlertActionStyle.Default, handler:nil))
            self.presentViewController(alert, animated:true, completion:nil)
        }
        else if self.drugField.text.isEmpty
        {
            // Alert user that field is required
            var alert = UIAlertController(title:"Attention Required", message:"Please enter a medication name", preferredStyle:UIAlertControllerStyle.Alert)
            alert.addAction(UIAlertAction(title:"OK", style:UIAlertActionStyle.Default, handler:nil))
            self.presentViewController(alert, animated:true, completion:nil)
        }
        else
        {
            // Hide keyboard
            self.drugField.resignFirstResponder()
            
            // Show table view
            self.medicationTable.hidden = false
            
            // Start with empty array if search start index is set to zero
            if self.searchStartIndex == 0
            {
                self.drugs.removeAll(keepCapacity: false)
            }
            
            // Set up progress hud
            self.loadingNotification = MBProgressHUD.showHUDAddedTo(self.view, animated: true)
            self.loadingNotification!.mode = MBProgressHUDMode.Indeterminate
            self.loadingNotification!.userInteractionEnabled = true
            
            // Customize loading message based on whether it's a new search or a continuing load
            if self.searchStartIndex == 0
            {
                self.loadingNotification!.labelText = "Searching For Medication Information"
            }
            else
            {
                let startIndex = self.searchStartIndex + 1
                self.loadingNotification!.labelText = "Loading Additional Records"
                self.loadingNotification!.detailsLabelText = " \(startIndex) through \(startIndex + 50) of \(self.totalSearchResults)"
            }
            
            let fieldValue = self.drugField.text.stringByReplacingOccurrencesOfString(" ", withString: "+")
            let criteria = fieldValue.stringByAddingPercentEncodingWithAllowedCharacters(.URLHostAllowedCharacterSet())
            let urlString = "http://api.fda.gov/drug/label.json?api_key=0NUtnvRyEkiYwoxhuDTfZbpykcPzIj1viymzqrLl&search=openfda.brand_name:\(criteria!)+openfda.generic_name:\(criteria!)+openfda.product_ndc:\(criteria!)&skip=\(self.searchStartIndex)&limit=50"
            let restURL = NSURL(string: urlString)
            var restRequest = NSMutableURLRequest(URL: restURL!, cachePolicy: NSURLRequestCachePolicy.ReloadIgnoringLocalAndRemoteCacheData, timeoutInterval: 10)
            
            // Make async data call
            NSURLConnection.sendAsynchronousRequest(restRequest, queue: NSOperationQueue.mainQueue(), completionHandler: { (response, data, error) -> Void in
                
                // Parse JSON for storage or use
                var err: NSError
                if let jsonResult = NSJSONSerialization.JSONObjectWithData(data, options:NSJSONReadingOptions.MutableContainers, error: nil) as? NSDictionary
                {
                    // Go create medications
                    self.createMedications(jsonResult)
                }
            })
        }
    }
    
    // Offer search term suggestions
    func offerSuggestions()
    {
        // If not connected to the Internet
        if reachabilityStatus == kNotReachable
        {
            // Alert user that field is required
            var alert = UIAlertController(title:"Attention Required", message:"This application requires an Internet connection. Please try again once connectivity has been established.", preferredStyle:UIAlertControllerStyle.Alert)
            alert.addAction(UIAlertAction(title:"OK", style:UIAlertActionStyle.Default, handler:nil))
            self.presentViewController(alert, animated:true, completion:nil)
        }
        else if !self.drugField.text.isEmpty
        {
            // Unhide table if it is hidden
            self.medicationTable.hidden = false
            
            // Form search request
            let termSuggestionString = "https://staging.meducated.ninja/api/Term/\(self.drugField.text)"
            let termSuggestionURL = NSURL(string: termSuggestionString)
            var termRequest = NSMutableURLRequest(URL: termSuggestionURL!, cachePolicy: NSURLRequestCachePolicy.ReloadIgnoringLocalAndRemoteCacheData, timeoutInterval: 10)
            
            // Make async data call
            NSURLConnection.sendAsynchronousRequest(termRequest, queue: NSOperationQueue.mainQueue(), completionHandler: { (response, data, error) -> Void in
                
                // If no errors
                if error == nil
                {
                    // Populate search array with results
                    if let jsonResult: AnyObject = NSJSONSerialization.JSONObjectWithData(data, options: NSJSONReadingOptions.MutableContainers, error:nil)
                    {
                        self.searchSuggestions = jsonResult as! [String]
                        
                        // Reload table
                        self.medicationTable.reloadData()
                    }
                }
            })
        }
    }
    
    // Perform segue to show filter form
    func filterSearchResults()
    {
        self.performSegueWithIdentifier("filterSegue", sender: self)
    }
    
    // Create medication objects from search results
    func createMedications(jsonObject: NSDictionary)
    {
        // Get total query result count
        if let meta = jsonObject["meta"] as? NSDictionary
        {
            if let results = meta["results"] as? NSDictionary
            {
                if let total = results["total"] as? Int
                {
                    self.totalSearchResults = total
                }
            }
        }
        
        // If we have results
        if let results = jsonObject["results"] as? NSArray
        {
            // Remove search suggestions now that our search has returned data
            self.searchSuggestions.removeAll(keepCapacity: false)
            
            // Parse json data into an object for passing to information page
            for result in results
            {
                if let medication = result as? NSDictionary
                {
                    // Create medication object
                    self.drugs.append(Medication(result: medication))
                }
            }
            
            // Keep an unaltered copy that we can use when applying filters
            self.originalResults = self.drugs
            
            // Sort the drugs array
            self.sortDrugs()
        }
        else
        {
            // Hide progress hud
            MBProgressHUD.hideAllHUDsForView(self.view, animated: true)
            
            // Alert user that field is required
            var alert = UIAlertController(title:"No Results Found", message:"Sorry, but your search had an error. Please try again.", preferredStyle:UIAlertControllerStyle.Alert)
            alert.addAction(UIAlertAction(title:"OK", style:UIAlertActionStyle.Default, handler:nil))
            self.presentViewController(alert, animated:true, completion:nil)
        }
    }
    
    // Sort the drugs array
    func sortDrugs()
    {
        // If filtering
        var filteredArray = [Medication]()
        
        if NSUserDefaults.standardUserDefaults().boolForKey("filterByBrand")
        {
            for med in self.originalResults
            {
                let brandName = med.openfda?.valueForKey("brand_name") as! [String]
                let genericName = med.openfda?.valueForKey("generic_name") as! [String]
                
                if find(genericName,brandName[0]) == nil
                {
                    filteredArray.append(med)
                }
            }
            
            self.drugs = filteredArray
        }
        else if NSUserDefaults.standardUserDefaults().boolForKey("filterByGeneric")
        {
            for med in self.originalResults
            {
                let brandName = med.openfda?.valueForKey("brand_name") as! [String]
                let genericName = med.openfda?.valueForKey("generic_name") as! [String]
                
                if find(genericName,brandName[0]) != nil
                {
                    filteredArray.append(med)
                }
            }
            
            self.drugs = filteredArray
        }
        else
        {
            self.drugs = self.originalResults
        }
        
        // Sort by brand name ASC or DESC
        self.drugs.sort{ item1, item2 in
            
            let brandArray1 = item1.openfda!["brand_name"] as! [String]
            let brandArray2 = item2.openfda!["brand_name"] as! [String]
            
            let brand1 = brandArray1[0] as String
            let brand2 = brandArray2[0] as String
            
            if NSUserDefaults.standardUserDefaults().boolForKey("searchSortDESC")
            {
                return brand1 > brand2
            }
            
            return brand1 < brand2
        }
        
        // Filter was applied and we should scroll to the beginning of the table
        if self.scrollToFirstRecord
        {
            // Scroll to first row
            self.medicationTable.setContentOffset(CGPointZero, animated:false)
            
            // Reset bool since we really only want to do if after applying a filter
            self.scrollToFirstRecord = false
        }
        
        // Reload the table with the search results (using this method for proper autoheight calculation on table cells)
        self.medicationTable.reloadSections(NSIndexSet(indexesInRange:NSMakeRange(0, self.medicationTable.numberOfSections())), withRowAnimation:UITableViewRowAnimation.Fade)

        // No longer loading
        self.isLoading = false
        
        // Hide progress hud
        MBProgressHUD.hideAllHUDsForView(self.view, animated: true)
    }
    
    // Hide keyboard on demand
    @IBAction func hideKeyboard(sender:AnyObject)
    {
        self.drugField.resignFirstResponder()
    }
    
    // Shows menu page
    @IBAction func goHome(sender:AnyObject)
    {
        self.dismissViewControllerAnimated(true, completion:nil)
    }
    
    // MARK: - Text Field Delegate Methods
    
    func textFieldShouldReturn(textField: UITextField) -> Bool
    {
        // Search key pressed, so reset search start index
        self.searchStartIndex = 0
        
        return true
    }
    
    // MARK: - Navigation Methods
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?)
    {
        let indexPath = self.medicationTable.indexPathForSelectedRow()
        
        if segue.identifier == "drugInfoSegue"
        {
            let destinationController = segue.destinationViewController as! DrugInformationViewController
            destinationController.medication = self.drugs[indexPath!.row]
        }
        else if segue.identifier == "filterSegue"
        {
            let destinationController = segue.destinationViewController as! UINavigationController
            let filterController = destinationController.viewControllers[0] as! FilterViewController
            
            filterController.delegate = self
        }
    }
    
    // MARK: - Table View Methods
    
    func numberOfSectionsInTableView(tableView: UITableView) -> Int
    {
        return 1
    }
    
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        if !self.searchSuggestions.isEmpty
        {
            // Return +1 to account for header cell
            return self.searchSuggestions.count + 1
        }
        
        return self.drugs.count
    }
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell
    {
        var cell = UITableViewCell()

        if !self.searchSuggestions.isEmpty && indexPath.row == 0
        {
            cell = tableView.dequeueReusableCellWithIdentifier("suggestionHeaderCell", forIndexPath: indexPath) as! UITableViewCell
            cell.textLabel?.text = "Did you mean?"
        }
        else if !self.searchSuggestions.isEmpty && indexPath.row > 0
        {
            cell = tableView.dequeueReusableCellWithIdentifier("suggestionCell", forIndexPath: indexPath) as! UITableViewCell
            cell.textLabel?.text = self.searchSuggestions[indexPath.row - 1]
        }
        else
        {
            cell = tableView.dequeueReusableCellWithIdentifier("medCell", forIndexPath: indexPath) as! UITableViewCell
            var medicationLabel = cell.viewWithTag(10) as! UILabel
            var manufacturerLabel = cell.viewWithTag(11) as! UILabel
            
            if let medDict = self.drugs[indexPath.row].openfda
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
        }
        
        return cell
    }
    
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath)
    {
        self.drugField.resignFirstResponder()
        
        // If we're selecting a search suggestion, set the text field and perform a search
        if !self.searchSuggestions.isEmpty
        {
            self.searchStartIndex = 0
            self.drugField.text = self.searchSuggestions[indexPath.row - 1]
            self.searchForInfo(indexPath)
        }
        else
        {
            self.performSegueWithIdentifier("drugInfoSegue", sender:self)
        }
    }
    
    func scrollViewDidScroll(scrollView: UIScrollView)
    {
        // UITableView only moves in one direction, y axis
        let currentOffset = scrollView.contentOffset.y
        let maximumOffset = scrollView.contentSize.height - scrollView.frame.size.height
        
        // Make sure total offset is less than -5 and next start point is less than total record count
        if ((maximumOffset - currentOffset == 0) && (self.searchStartIndex + 50 < self.totalSearchResults) && !self.isLoading)
        {
            // We are loading data
            self.isLoading = true
            
            // Start at the beginning of the search
            self.searchStartIndex = self.searchStartIndex + 50
            
            // Go fetch the burial records
            self.searchForInfo(self)
        }
    }
    
    // MARK: - Search Delegate Methods
    
    // Search criteria applied
    func applySearchFilter()
    {
        // Search will research to populate array, do sort, then filter
        if !self.originalResults.isEmpty
        {
            // Reset scroll
            self.scrollToFirstRecord = true
            
            // Set up progress hud
            self.loadingNotification = MBProgressHUD.showHUDAddedTo(self.view, animated: true)
            self.loadingNotification!.mode = MBProgressHUDMode.Indeterminate
            self.loadingNotification!.userInteractionEnabled = true
            self.loadingNotification!.labelText = "Applying Filter Criteria"
            
            self.sortDrugs()
        }
    }
}