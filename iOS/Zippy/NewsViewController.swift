//
//  NewsViewController.swift
//  Zippy
//
//  Created by Geoffrey Bender on 6/26/15.
//  Copyright (c) 2015 Segue Technologies, Inc. All rights reserved.
//

import UIKit

class NewsViewController: UITableViewController, NSXMLParserDelegate
{
    // MARK: - Variables
    
    var arrParsedData = [Dictionary<String, String>]()
    var currentDataDictionary = Dictionary<String, String>()
    var currentElement = ""
    var foundCharacters = ""
    var loadingNotification: MBProgressHUD?
    var readyToLoad = false
    
    // MARK: - View Lifecycle Methods
    
    // When view loads, set up dynamic row height
    override func viewDidLoad()
    {
        super.viewDidLoad()
        
        // For dynamic table row height
        self.tableView.estimatedRowHeight = 115
        self.tableView.rowHeight = UITableViewAutomaticDimension
        
        // Set bar buttons
        var homeBarButton = UIBarButtonItem(title:"Home", style:UIBarButtonItemStyle.Plain, target:self, action:"goHome:")
        var refreshBarButton = UIBarButtonItem(barButtonSystemItem: UIBarButtonSystemItem.Refresh, target: self, action: "loadData:")
        
        self.navigationItem.leftBarButtonItem = homeBarButton
        self.navigationItem.rightBarButtonItem = refreshBarButton
        
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
    
    // When view appears, load data if it's our first time in
    override func viewWillAppear(animated: Bool)
    {
        super.viewWillAppear(animated)
        
        if !self.readyToLoad
        {
            // Load RSS
            self.loadData(self)
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
    
    // MARK: - XML Parser Methods
    
    // Parser encountered a new XML element
    func parser(parser:NSXMLParser, didStartElement elementName:String, namespaceURI:String?, qualifiedName qName:String?, attributes attributeDict: [NSObject : AnyObject])
    {
        self.currentElement = elementName
    }
    
    // Parser finished processing an element
    func parser(parser: NSXMLParser, didEndElement elementName: String, namespaceURI: String?, qualifiedName qName: String?)
    {
        if (self.currentElement == "title" || self.currentElement == "description" || self.currentElement == "pubDate" || self.currentElement == "link") && !self.foundCharacters.isEmpty
        {
            // All fields have a newline character in front that we need to strip
            self.foundCharacters = (self.foundCharacters as NSString).substringFromIndex(1)
            
            // Create dictionary key and strip whitespace characters
            self.currentDataDictionary[currentElement] = self.foundCharacters.stringByTrimmingCharactersInSet(NSCharacterSet.whitespaceCharacterSet())
            
            // Reset found characters
            self.foundCharacters = ""
            
            // If element is pubDate then add the news dictionary item to our table array
            if self.currentElement == "link"
            {
                let linkType = self.currentDataDictionary["link"]?.componentsSeparatedByString(".").last?.lowercaseString

                if linkType != "zip" && linkType != "xml" && self.currentDataDictionary["pubDate"] != nil
                {
                    self.arrParsedData.append(self.currentDataDictionary)
                }
            }
        }
    }
    
    // Parser found charcters in element we're interested in
    func parser(parser:NSXMLParser, foundCharacters string:String?)
    {
        if (self.currentElement == "title" || self.currentElement == "description" || self.currentElement == "pubDate" || self.currentElement == "link")
        {
            self.foundCharacters += string!
        }
    }
    
    // Finished parsing XML
    func parserDidEndDocument(parser: NSXMLParser)
    {
        // Set ready to load so we can refresh table and stop progress
        self.readyToLoad = true
        
        // Reload the table with the new cabinet (using this method for proper autoheight calculation on table cells)
        self.tableView.reloadSections(NSIndexSet(indexesInRange:NSMakeRange(0, self.tableView.numberOfSections())), withRowAnimation:.Fade)
        
        // Hide progress hud
        MBProgressHUD.hideAllHUDsForView(self.navigationController!.view, animated: true)
        
        // Check for new postings count
        if let latestPubdateString = NSUserDefaults.standardUserDefaults().valueForKey("latestPubdateString") as? String
        {
            // Set date formatter
            let dateFormatter = NSDateFormatter()
            dateFormatter.dateFormat = "EEE, dd MMM yyyy, HH:mm:ss ZZZ"
            
            // Convert latest pubdate string to nsdate for comparison
            let latestPubdate = dateFormatter.dateFromString(latestPubdateString)
            
            // Start counting number of new postings
            var newPostings = 0
            for post in self.arrParsedData
            {
                let postDateString = post["pubDate"]
                let postDate = dateFormatter.dateFromString(postDateString!)
                
                if postDate!.isGreaterThanDate(latestPubdate!)
                {
                    newPostings++
                }
                else
                {
                    break
                }
            }
            
            if newPostings > 0
            {
                self.navigationController!.tabBarItem.badgeValue = "\(newPostings)"
            }
            else
            {
                self.navigationController!.tabBarItem.badgeValue = nil
            }
        }
        else
        {
            if self.arrParsedData.count > 0
            {
                self.navigationController!.tabBarItem.badgeValue = "\(self.arrParsedData.count)"
            }
            else
            {
                self.navigationController!.tabBarItem.badgeValue = nil
            }
        }
        
        // Get last pub date as string value and save it
        let lastPubdateString = self.arrParsedData.first!["pubDate"]
        NSUserDefaults.standardUserDefaults().setValue(lastPubdateString, forKey:"latestPubdateString")
    }
    
    // Show error if any
    func parser(parser: NSXMLParser, parseErrorOccurred parseError: NSError)
    {
        println("Error: \(parseError.description)")
    }
    
    // Show error if any
    func parser(parser: NSXMLParser, validationErrorOccurred validationError: NSError)
    {
        println("Error: \(validationError.description)")
    }
    
    // Load RSS Feed from URL
    @IBAction func loadData(sender:AnyObject)
    {
        // If not connected to the Internet
        if reachabilityStatus == kNotReachable
        {
            // Alert user that field is required
            var alert = UIAlertController(title:"Attention Required", message:"This application requires an Internet connection. Please try again once connectivity has been established.", preferredStyle:UIAlertControllerStyle.Alert)
            alert.addAction(UIAlertAction(title:"OK", style:UIAlertActionStyle.Default, handler:nil))
            self.presentViewController(alert, animated:true, completion:nil)
        }
        else
        {
            // Set progress variable
            self.readyToLoad = false
            
            // Set up progress hud
            self.loadingNotification = MBProgressHUD.showHUDAddedTo(self.navigationController!.view, animated: true)
            self.loadingNotification!.mode = MBProgressHUDMode.Indeterminate
            self.loadingNotification!.labelText = "Loading Medication Information"
            self.loadingNotification!.userInteractionEnabled = true
            
            // Start with empty array
            self.arrParsedData.removeAll(keepCapacity: false)
            
            // Feed URL
            let feedURLString = "http://www.fda.gov/AboutFDA/ContactFDA/StayInformed/RSSFeeds/Drugs/rss.xml"
            // let feedURLString = "http://www.fda.gov/AboutFDA/ContactFDA/StayInformed/RSSFeeds/Recalls/rss.xml"
            let feedURL = NSURL(string: feedURLString)
            
            // Set up XML Parser
            var xmlParser = NSXMLParser(contentsOfURL:feedURL)
            xmlParser!.delegate = self
            xmlParser!.parse()
        }
    }

    // MARK: - Table View Methods

    // Number of sections in table
    override func numberOfSectionsInTableView(tableView: UITableView) -> Int
    {
        return 1
    }

    // Number of rows in table (news items)
    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        return self.arrParsedData.count
    }

    // Cell for each row in table (news articles)
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell
    {
        let cell = tableView.dequeueReusableCellWithIdentifier("newsCell", forIndexPath: indexPath) as! UITableViewCell

        var titleLabel = cell.viewWithTag(1) as! UILabel
        var descriptionLabel = cell.viewWithTag(2) as! UILabel
        var publishedLabel = cell.viewWithTag(3) as! UILabel
        
        titleLabel.text = self.arrParsedData[indexPath.row]["title"]
        descriptionLabel.text = self.arrParsedData[indexPath.row]["description"]
        publishedLabel.text = self.arrParsedData[indexPath.row]["pubDate"]
        
        return cell
    }

    // MARK: - Navigation Methods

    // Show article detail
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?)
    {
        let title = self.arrParsedData[self.tableView.indexPathForSelectedRow()!.row]["title"]
        let link = self.arrParsedData[self.tableView.indexPathForSelectedRow()!.row]["link"]
        
        let destinationController = segue.destinationViewController as! NewsDetailViewController
        destinationController.urlString = link
    }
    
    // Shows menu page
    @IBAction func goHome(sender:AnyObject)
    {
        self.dismissViewControllerAnimated(true, completion:nil)
    }
}
