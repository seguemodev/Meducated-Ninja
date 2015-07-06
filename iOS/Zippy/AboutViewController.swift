//
//  AboutViewController.swift
//  Zippy
//
//  Created by Geoffrey Bender on 6/23/15.
//  Copyright (c) 2015 Segue Technologies, Inc. All rights reserved.
//

import UIKit

class AboutViewController: UIViewController, UITableViewDataSource, UITableViewDelegate
{
    // MARK: - Interface Outlets
    
    @IBOutlet weak var tableView: UITableView!
    
    // MARK: - View Lifecycle Methods
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        
        // For dynamic table row height
        self.tableView.estimatedRowHeight = 80
        self.tableView.rowHeight = UITableViewAutomaticDimension
    }
    
    // MARK: - Table View Methods
    
    func numberOfSectionsInTableView(tableView: UITableView) -> Int
    {
        return 1
    }
    
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        return 2
    }
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell
    {
        let cell = tableView.dequeueReusableCellWithIdentifier("aboutCell", forIndexPath: indexPath) as! UITableViewCell
        
        if indexPath.row == 0
        {
            cell.textLabel!.text = "Meducated is a tool developed for the caregivers of the elderly and infirmed to help improve the quality of care. The tool allows caregivers (both professional as well as family and friends) the ability to stay better informed about the medications their patients and loved ones are taking as well as possible drug and/or food interactions. The tool pulls information from multiple data sources that can be accessed via an easy-to-use search feature on the web, a native iOS app and a native Android app. It also provides an easy way to track current medications being taken by the patient by allowing the caregivers the ability to create virtual “medicine cabinets” to be viewed and shared at any time. Our ultimate goal is to give the caregiver the tools to take control of the coordination and quality of care of those in need."
        }
        else
        {
            cell.textLabel!.text = "Images supplied by the National Library of Medicine (NLM) Office of High Performance Computing and Communications (OHPCC) have been supported by the American Recovery and Reinvestment Act of 2009 (ARRA) and through NIH intramural funding approved by the NLM Board of Scientific Counselors."
        }
        
        return cell
    }
    
    func tableView(tableView: UITableView, viewForHeaderInSection section: Int) -> UIView?
    {
        var headerView: UIView = UIView()
        var headerLabel: UILabel = UILabel(frame:CGRectMake(15, 17, 200, 22))
        
        headerLabel.text = "About Meducated"

        headerLabel.textColor = UIColor(red: 47/255.0, green: 59/255.0, blue: 117/255.0, alpha: 1)
        headerLabel.backgroundColor = UIColor.clearColor()
        
        if let font = UIFont(name:"RobotoSlab-Bold", size:18.0)
        {
            headerLabel.font = font
        }
        
        headerView.addSubview(headerLabel)
        
        return headerView
    }
    
    func tableView(tableView:UITableView, heightForHeaderInSection section:Int) -> CGFloat
    {
        return 50
    }
    
    // MARK: - Navigation Methods
    
    @IBAction func closeWindow(sender: AnyObject)
    {
        self.dismissViewControllerAnimated(true, completion:nil)
    }
}
