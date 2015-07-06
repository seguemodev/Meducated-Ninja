//
//  MenuViewController.swift
//  Zippy
//
//  Created by Geoffrey Bender on 6/23/15.
//  Copyright (c) 2015 Segue Technologies, Inc. All rights reserved.
//

import UIKit

class MenuViewController: UIViewController
{
    var selectedButtonIndex: Int?
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
    }

    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?)
    {
        if segue.identifier == "tabSegue"
        {
            let tabController = segue.destinationViewController as! TabViewController
            tabController.selectedMenuIndex = self.selectedButtonIndex
        }
    }
    
    // MARK: - My Methods
    
    @IBAction func buttonClicked(sender:UIButton)
    {
        self.selectedButtonIndex = sender.tag
        self.performSegueWithIdentifier("tabSegue", sender:self)
    }
}