//
//  NewsDetailViewController.swift
//  Zippy
//
//  Created by Geoffrey Bender on 6/26/15.
//  Copyright (c) 2015 Segue Technologies, Inc. All rights reserved.
//

import UIKit

class NewsDetailViewController: UIViewController, UIWebViewDelegate
{
    // MARK: - Interface Outlets
    
    @IBOutlet weak var webView: UIWebView!
    @IBOutlet weak var backButton: UIBarButtonItem!
    @IBOutlet weak var forwardButton: UIBarButtonItem!
    @IBOutlet weak var navigationToolbar: UIToolbar!
    
    // MARK: - Variables
    
    var urlString: String?
    var loadingNotification: MBProgressHUD?
    
    // MARK: - View Lifecycle Methods
    
    required init(coder aDecoder: NSCoder)
    {
        super.init(coder:aDecoder)
        
        // Hide tab bar
        self.hidesBottomBarWhenPushed = true
    }
    
    // Set up bar button items on load
    override func viewDidLoad()
    {
        super.viewDidLoad()
        
        var leftBarButton = UIBarButtonItem(title:"Back", style:UIBarButtonItemStyle.Plain, target:self, action:"backNavigate")
        var refreshBarButton = UIBarButtonItem(barButtonSystemItem: UIBarButtonSystemItem.Refresh, target: self, action: "reloadPage:")
        
        self.navigationItem.leftBarButtonItem = leftBarButton
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
        
        // Load URL
        self.loadContent(self)
    }
    
    // If view disappears, stop page loading
    override func viewWillDisappear(animated: Bool)
    {
        super.viewWillDisappear(animated)
        
        self.webView.stopLoading()
    }
    
    // MARK: - Web View Methods
    
    func webView(webView: UIWebView, shouldStartLoadWithRequest request: NSURLRequest, navigationType: UIWebViewNavigationType) -> Bool
    {
        if request.URL == NSURL(string: self.urlString!)
        {
            // Set up progress hud
            self.loadingNotification = MBProgressHUD.showHUDAddedTo(self.navigationController!.view, animated: true)
            self.loadingNotification!.mode = MBProgressHUDMode.Indeterminate
            self.loadingNotification!.labelText = "Loading News Item"
            self.loadingNotification!.userInteractionEnabled = true
        }
        
        return true
    }
    
    // Hide notification when page stops loading
    func webViewDidFinishLoad(webView: UIWebView)
    {
        // Enable buttons depending on navigation
        self.backButton.enabled = self.webView.canGoBack
        self.forwardButton.enabled = self.webView.canGoForward
        
        // Ignore anchors for back navigation
        let urlString = "\(self.webView.request!.URL)"
        let components = urlString.componentsSeparatedByString("#")
        if components[0] == self.urlString
        {
            self.backButton.enabled = false
        }
        
        if !self.webView.loading
        {
            // Scroll to top of web view
            let top = CGPointMake(0, 0)
            self.webView.scrollView.setContentOffset(top, animated:false)
            
            // Hide progress hud
            MBProgressHUD.hideAllHUDsForView(self.navigationController!.view, animated: true)
        }
    }
    
    // Show error message if any
    func webView(webView: UIWebView, didFailLoadWithError error: NSError)
    {
        // Hide progress hud
        MBProgressHUD.hideAllHUDsForView(self.navigationController!.view, animated: true)
        
        var message = ""
        
        // Only goes in if it is -999
        if error.code == NSURLErrorCancelled
        {
            message = "Your page request was canceled."
        }
        else
        {
            message = "A problem occurred trying to load this page: \(error.localizedDescription)"
        }
        
        // Alert user that the cabinet name is required
        var alert = UIAlertController(title:"Attention Required", message:message, preferredStyle:UIAlertControllerStyle.Alert)
        alert.addAction(UIAlertAction(title:"OK", style:UIAlertActionStyle.Default, handler:nil))
        self.presentViewController(alert, animated:true, completion:nil)
    }
    
    // MARK: - My Methods
    
    // Load news item
    @IBAction func loadContent(sender:AnyObject)
    {
        // Cancel a current load operation if any
        if self.webView.loading
        {
            self.webView.stopLoading()
        }
        
        // Set URL and load web view
        let url = NSURL(string: self.urlString!)
        self.webView.loadRequest(NSURLRequest(URL: url!))
    }
    
    @IBAction func reloadPage(sender:UIBarButtonItem)
    {
        // Cancel a current load operation if any
        if self.webView.loading
        {
            self.webView.stopLoading()
        }
        
        self.webView.reload()
    }
    
    // Go back to news items page
    func backNavigate()
    {
        self.navigationController?.popViewControllerAnimated(true)
    }
    
    // Navigation web page backward
    @IBAction func goBack(sender:UIBarButtonItem)
    {
        self.webView.goBack()
    }
    
    // Navigation web page forward
    @IBAction func goForward(sender:UIBarButtonItem)
    {
        self.webView.goForward()
    }
}