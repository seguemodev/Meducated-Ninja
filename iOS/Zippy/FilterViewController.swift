//
//  FilterViewController.swift
//  Zippy
//
//  Created by Geoffrey Bender on 6/23/15.
//  Copyright (c) 2015 Segue Technologies, Inc. All rights reserved.
//

import UIKit

protocol SearchFilterDelegate
{
    func applySearchFilter()
}

class FilterViewController: UITableViewController
{
    // MARK: - Interface Outlets
    
    @IBOutlet weak var azCell: UITableViewCell!
    @IBOutlet weak var zaCell: UITableViewCell!
    @IBOutlet weak var allCell: UITableViewCell!
    @IBOutlet weak var brandCell: UITableViewCell!
    @IBOutlet weak var genericCell: UITableViewCell!
    
    // MARK: - Variables
    
    var delegate: SearchFilterDelegate?
    
    // MARK: - Lifecycle Methods
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        
        var cancelBarButton = UIBarButtonItem(title:"Cancel", style:UIBarButtonItemStyle.Plain, target:self, action:"closeWindow")
        var filterBarButton = UIBarButtonItem(title:"Apply", style:UIBarButtonItemStyle.Plain, target:self, action:"applyFilter")
        
        self.navigationItem.leftBarButtonItem = cancelBarButton
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
        
        if NSUserDefaults.standardUserDefaults().boolForKey("searchSortDESC")
        {
            self.azCell.accessoryView = UIImageView(image:UIImage(named:"UncheckedButton"))
            self.zaCell.accessoryView = UIImageView(image:UIImage(named:"CheckedButton"))
        }
        else
        {
            self.azCell.accessoryView = UIImageView(image:UIImage(named:"CheckedButton"))
            self.zaCell.accessoryView = UIImageView(image:UIImage(named:"UncheckedButton"))
        }
        
        if NSUserDefaults.standardUserDefaults().boolForKey("filterByBrand")
        {
            self.allCell.accessoryView = UIImageView(image:UIImage(named:"UncheckedButton"))
            self.brandCell.accessoryView = UIImageView(image:UIImage(named:"CheckedButton"))
            self.genericCell.accessoryView = UIImageView(image:UIImage(named:"UncheckedButton"))
        }
        else if NSUserDefaults.standardUserDefaults().boolForKey("filterByGeneric")
        {
            self.allCell.accessoryView = UIImageView(image:UIImage(named:"UncheckedButton"))
            self.brandCell.accessoryView = UIImageView(image:UIImage(named:"UncheckedButton"))
            self.genericCell.accessoryView = UIImageView(image:UIImage(named:"CheckedButton"))
        }
        else
        {
            self.allCell.accessoryView = UIImageView(image:UIImage(named:"CheckedButton"))
            self.brandCell.accessoryView = UIImageView(image:UIImage(named:"UncheckedButton"))
            self.genericCell.accessoryView = UIImageView(image:UIImage(named:"UncheckedButton"))
        }
    }

    // MARK: - Table View Methods
    
    override func tableView(tableView: UITableView, viewForHeaderInSection section: Int) -> UIView?
    {
        var headerView: UIView = UIView()
        var headerLabel: UILabel = UILabel(frame:CGRectMake(10, 17, 200, 22))
        
        if section == 0
        {
            headerLabel.text = "Sort By"
        }
        else
        {
            headerLabel.text = "Filter By"
        }
        headerLabel.textColor = UIColor(red: 47/255.0, green: 59/255.0, blue: 117/255.0, alpha: 1)
        headerLabel.backgroundColor = UIColor.clearColor()
        
        if let font = UIFont(name:"RobotoSlab-Bold", size:16.0)
        {
            headerLabel.font = font
        }
        
        headerView.addSubview(headerLabel)

        return headerView
    }
    
    override func tableView(tableView:UITableView, heightForHeaderInSection section:Int) -> CGFloat
    {
        return 50
    }
    
    override func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath)
    {
        // Selected sort ASC
        if indexPath.section == 0 && indexPath.row == 0
        {
            NSUserDefaults.standardUserDefaults().setBool(true, forKey:"searchSortASC")
            NSUserDefaults.standardUserDefaults().setBool(false, forKey:"searchSortDESC")
            
            self.azCell.accessoryView = UIImageView(image:UIImage(named:"CheckedButton"))
            self.zaCell.accessoryView = UIImageView(image:UIImage(named:"UncheckedButton"))
        }
        else if indexPath.section == 0 && indexPath.row == 1
        {
            // Selected sort DESC
            NSUserDefaults.standardUserDefaults().setBool(true, forKey:"searchSortDESC")
            NSUserDefaults.standardUserDefaults().setBool(false, forKey:"searchSortASC")
            
            self.azCell.accessoryView = UIImageView(image:UIImage(named:"UncheckedButton"))
            self.zaCell.accessoryView = UIImageView(image:UIImage(named:"CheckedButton"))
        }
        else if indexPath.section == 1 && indexPath.row == 0
        {
            // Selected filter by all
            NSUserDefaults.standardUserDefaults().setBool(true, forKey:"filterByAll")
            NSUserDefaults.standardUserDefaults().setBool(false, forKey:"filterByBrand")
            NSUserDefaults.standardUserDefaults().setBool(false, forKey:"filterByGeneric")
            
            self.allCell.accessoryView = UIImageView(image:UIImage(named:"CheckedButton"))
            self.brandCell.accessoryView = UIImageView(image:UIImage(named:"UncheckedButton"))
            self.genericCell.accessoryView = UIImageView(image:UIImage(named:"UncheckedButton"))
        }
        else if indexPath.section == 1 && indexPath.row == 1
        {
            // Selected filter by brand
            NSUserDefaults.standardUserDefaults().setBool(false, forKey:"filterByAll")
            NSUserDefaults.standardUserDefaults().setBool(true, forKey:"filterByBrand")
            NSUserDefaults.standardUserDefaults().setBool(false, forKey:"filterByGeneric")
            
            self.allCell.accessoryView = UIImageView(image:UIImage(named:"UncheckedButton"))
            self.brandCell.accessoryView = UIImageView(image:UIImage(named:"CheckedButton"))
            self.genericCell.accessoryView = UIImageView(image:UIImage(named:"UncheckedButton"))
        }
        else
        {
            // Selected filter by generic
            NSUserDefaults.standardUserDefaults().setBool(false, forKey:"filterByAll")
            NSUserDefaults.standardUserDefaults().setBool(false, forKey:"filterByBrand")
            NSUserDefaults.standardUserDefaults().setBool(true, forKey:"filterByGeneric")
            
            self.allCell.accessoryView = UIImageView(image:UIImage(named:"UncheckedButton"))
            self.brandCell.accessoryView = UIImageView(image:UIImage(named:"UncheckedButton"))
            self.genericCell.accessoryView = UIImageView(image:UIImage(named:"CheckedButton"))
        }
    }
    
    // MARK: - My Methods
    
    // Click handler for cancel button
    func closeWindow()
    {
        // Close window
        self.dismissViewControllerAnimated(true, completion: nil)
    }

    // Click handler for apply filter button
    func applyFilter()
    {
        // If we have a delegate, close the window and fire the delegate method
        if let delegate = self.delegate
        {
            delegate.applySearchFilter()
            self.dismissViewControllerAnimated(true, completion:nil)
        }
    }
}
