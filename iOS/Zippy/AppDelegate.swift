//
//  AppDelegate.swift
//  Zippy
//
//  Created by Geoffrey Bender on 6/18/15.
//  Copyright (c) 2015 Segue Technologies, Inc. All rights reserved.
//

import UIKit
import Fabric
import Crashlytics

// Globally Accessible Variables

let kReachableWithWIFI = "ReachableWithWIFI"
let kReachableWithWWAN = "ReachableWithWWAN"
let kNotReachable = "NotReachable"
var reachability : Reachability?
var reachabilityStatus = kReachableWithWIFI

@UIApplicationMain

class AppDelegate: UIResponder, UIApplicationDelegate
{
    // MARK: - Variables

    var window: UIWindow?
    var internetReach : Reachability?
    
    // MARK: - Lifecycle Methods

    func application(application: UIApplication, didFinishLaunchingWithOptions launchOptions: [NSObject: AnyObject]?) -> Bool
    {
        // Start Crashlytics
        Fabric.with([Crashlytics()])
        
        // Set global tint colors (hex codes from mood board)
        // self.window?.tintColor = UIColor.whiteColor()
        UIApplication.sharedApplication().statusBarStyle = UIStatusBarStyle.LightContent
        
        // Set global fonts and text colors
        if let font = UIFont(name:"RobotoSlab-Bold", size:17.0)
        {
            UINavigationBar.appearance().titleTextAttributes = [NSFontAttributeName:font, NSForegroundColorAttributeName:UIColor.whiteColor()]
            UIBarButtonItem.appearance().setTitleTextAttributes([NSFontAttributeName:font, NSForegroundColorAttributeName:UIColor.whiteColor()], forState:UIControlState.Normal)
        }
        if let font = UIFont(name:"RobotoSlab-Bold", size:11.0)
        {
            UITabBarItem.appearance().setTitleTextAttributes([NSFontAttributeName:font, NSForegroundColorAttributeName:UIColor(white: 0.9, alpha: 1.0)], forState:UIControlState.Normal)
            UITabBarItem.appearance().setTitleTextAttributes([NSFontAttributeName:font, NSForegroundColorAttributeName:UIColor.whiteColor()], forState:UIControlState.Selected)
            UITabBar.appearance().tintColor = UIColor.whiteColor()
        }
        
        // Add reachability listener
        NSNotificationCenter.defaultCenter().addObserver(self, selector:"reachabilityChanged:", name:kReachabilityChangedNotification, object:nil)
        
        // Set up reachability
        self.internetReach = Reachability.reachabilityForInternetConnection()
        self.internetReach?.startNotifier()
        
        // Set initial status
        if internetReach != nil
        {
            self.statusChangedWithReachability(internetReach!)
        }

        return true
    }
    
    // Application will quit
    func applicationWillTerminate(application: UIApplication)
    {
        // Remove reachability listener
        NSNotificationCenter.defaultCenter().removeObserver(self, name:kReachabilityChangedNotification, object:nil)
    }
    
    // MARK: - Reachability Methods
    
    // Outputs current network status log
    func statusChangedWithReachability(currentReachabilityStatus:Reachability)
    {
        // Get the network status code
        var networkStatus : NetworkStatus = currentReachabilityStatus.currentReachabilityStatus()
        
        // Network status enum
        if networkStatus.value == NotReachable.value
        {
            println("Network is not reachable")
            reachabilityStatus = kNotReachable
        }
        else if networkStatus.value == ReachableViaWiFi.value
        {
            println("Network is reachable via WiFi")
            reachabilityStatus = kReachableWithWIFI
        }
        else if networkStatus.value == ReachableViaWWAN.value
        {
            println("Network is reachable via cell network")
            reachabilityStatus = kReachableWithWWAN
        }
        
        // Post global notification
        NSNotificationCenter.defaultCenter().postNotificationName("networkStatusChanged", object:nil)
    }
    
    // Listens for network reachability change notification
    func reachabilityChanged(notification:NSNotification)
    {
        println("Reachability status changed...")
        
        // Set global var and output status message
        reachability = notification.object as? Reachability
        self.statusChangedWithReachability(reachability!)
    }
}