Installing Meducated-Ninja
============================

Web Application
---------------
0. Create an empty database in SQL Server 2008 R2 or newer.  Name it whatever you want.
1. In the Zippy folder, copy or rename appSettings.config.sample to appSettings.config
2. In the Zippy folder, copy or rename connectionStrings.config.sample to connectionStrings.config
3. Open the Zippy.sln file in Visual Studio 2013
4. In appSettings.config, replace `YOUR_API_KEY` with the your API Key for the open.fda.gov API.  If you do not have an API key, please visit the [Open FDA API page](https://open.fda.gov/api/reference#your-api-key) to get a key.
5. In connectionStrings.config, edit the values in the DefaultConnection connection string:
  * `Data Source=localhost;` change localhost to point to your SQL Server
  * `Initial Catalog=Zippy;` change Zippy to match the name of the database you created in step 1.
  * If NOT using Integrated Security (IIS does not run under a user account with access to the SQL Server database):
    - `Integrated Security=True;` delete this parameter entirely
    - `User ID=YOUR_USERID;` add this parameter
    - `Password=YOUR_PASSWORD;` add this parameter
6. Build the project - NuGet packages should automatically be restored unless you have disabled that feature off
7. To run the project from within Visual Studio, press Ctrl+F5
  * The first time the project is run, it will create the database tables
  * To publish the project to your favorite .NET hosting platform, select 'Publish' from the Build menu in Visual Studio.


Android App
-----------
In order to compile from source you will need the android tools and sdk downloaded and setup on your machine. For assistance with this, please see [the Android SDK](https://developer.android.com/sdk/index.html). The app is setup to use Android's [gradle based build system](https://developer.android.com/tools/building/configuring-gradle.html).

You will need to setup several local gradle.properties variables on your machine or CI server.  These items are sensitive, and so they should not be included in your public version control repository.

* **`ZIPPY_FABRIC_API_KEY`** - this is needed for Fabric/Crashltics integration. 
* **`ZIPPY_FABRIC_BUILD_SECRET`** - this is also needed for Fabric/Crashlytics integration.
* **`ZIPPY_OPENFDA_API_KEY`** - your OpenFDA API key (you can get one here https://open.fda.gov/api/reference/)

Once this is complete, you can run the gradle wrapper script to pefrom various build functions.

* **_./gradlew assembleDebug_** - create a debug apk (located in app/build/outputs/apk)
* **_./gradlew spoonDebugAndroidTest_** - install the app and run tests on connected devices, generating spoon reports (located in app/build/spoon/debug)
* **_./gradlew crashlyticsUploadDistributionDebug_** - upload the debug build to crashyltics.
* **_./gradlew tasks_** - to see a list of other possible tasks



iOS App
-------
Requires Xcode version 6.4 with the iOS 8.4 SDK (free download available on the Mac app store)

1)  Obtain an [iOS developer or enterprise license](https://developer.apple.com/programs/)

2)  Create a development certificate per [Apple’s instructions](https://developer.apple.com/library/prerelease/ios/documentation/IDEs/Conceptual/AppDistributionGuide/MaintainingCertificates/MaintainingCertificates.html)

3)  Set code signing identity to iOS Developer under the project build settings section

* Open the project
* Click the project name at the very top of the file explorer pane on the left
* Click the Zippy project name in the middle column of the Xcode editor
* Click the build settings menu item at the top of the Xcode editor
* Scroll down to the iOS build options section and set to iOS Developer for both debug and release

4)  Click build (the play button at the top left of the Xcode editor) to build the project to the simulator or device or choose Product -> Archive from the top menu.

