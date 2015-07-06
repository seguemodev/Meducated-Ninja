//
//  TabViewController.swift
//  Zippy
//
//  Created by Geoffrey Bender on 6/23/15.
//  Copyright (c) 2015 Segue Technologies, Inc. All rights reserved.
//

import UIKit

class TabViewController: UITabBarController
{
    var selectedMenuIndex: Int?
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        
        if self.selectedMenuIndex != nil
        {
            self.selectedIndex = self.selectedMenuIndex!
        }
    }
}
