@echo off
echo Running!
:: ----------------------
:: KUDU Deployment Script
:: ----------------------

:: Prerequisites
:: -------------

:: Verify node.js installed
where node 2>nul >nul
IF %ERRORLEVEL% NEQ 0 (
  echo Missing node.js executable, please install node.js, if already installed make sure it can be reached from current environment.
  goto error
)

:: Setup
:: -----

setlocal enabledelayedexpansion

SET ARTIFACTS=%~dp0%artifacts

IF NOT DEFINED DEPLOYMENT_SOURCE (
  SET DEPLOYMENT_SOURCE=%~dp0%.
)

IF NOT DEFINED DEPLOYMENT_TARGET (
  SET DEPLOYMENT_TARGET=%ARTIFACTS%\wwwroot
)

IF NOT DEFINED NEXT_MANIFEST_PATH (
  SET NEXT_MANIFEST_PATH=%ARTIFACTS%\manifest

  IF NOT DEFINED PREVIOUS_MANIFEST_PATH (
    SET PREVIOUS_MANIFEST_PATH=%ARTIFACTS%\manifest
  )
)

IF NOT DEFINED KUDU_SYNC_COMMAND (
  :: Install kudu sync
  echo Installing Kudu Sync
  call npm install kudusync -g --silent
  IF !ERRORLEVEL! NEQ 0 goto error

  :: Locally just running "kuduSync" would also work
  SET KUDU_SYNC_COMMAND=node "%appdata%\npm\node_modules\kuduSync\bin\kuduSync"
)
IF NOT DEFINED DEPLOYMENT_TEMP (
  SET DEPLOYMENT_TEMP=%temp%\___deployTemp%random%
  SET CLEAN_LOCAL_DEPLOYMENT_TEMP=true
)

IF DEFINED CLEAN_LOCAL_DEPLOYMENT_TEMP (
  IF EXIST "%DEPLOYMENT_TEMP%" rd /s /q "%DEPLOYMENT_TEMP%"
  mkdir "%DEPLOYMENT_TEMP%"
)


IF NOT DEFINED MSBUILD_PATH (
  SET MSBUILD_PATH=%WINDIR%\Microsoft.NET\Framework\v4.0.30319\msbuild.exe
)
IF NOT DEFINED MSTEST_PATH (
  SET MSTEST_PATH=%WINDIR%\Microsoft.NET\Framework\v4.0.30319\mstest.exe
)

SET EnableNuGetPackageRestore=true

:: Deployment
:: ----------

echo Handling .NET Web Application deployment.

:: 1. Build solution
echo Build solution
nuget restore "%DEPLOYMENT_SOURCE%\Zippy.sln"
copy %DEPLOYMENT_SOURCE%\Zippy\appSettings.config.sample %DEPLOYMENT_SOURCE%\Zippy\appSettings.config /y
IF !ERRORLEVEL! NEQ 0 goto error
copy %DEPLOYMENT_SOURCE%\Zippy\connectionStrings.config.sample %DEPLOYMENT_SOURCE%\Zippy\connectionStrings.config /y
IF !ERRORLEVEL! NEQ 0 goto error
%MSBUILD_PATH% "%DEPLOYMENT_SOURCE%\Zippy.sln" /nologo /verbosity:m /t:Build /p:_PackageTempDir="%DEPLOYMENT_TEMP%";AutoParameterizationWebConfigConnectionStrings=false;Configuration=Release /p:SolutionDir="%DEPLOYMENT_SOURCE%\.\\" %SCM_BUILD_ARGS%
IF !ERRORLEVEL! NEQ 0 goto error

:: 1a. Build SPA
::echo Building SPA
::cd Zippy.SPA
::call npm install --save-dev
::IF !ERRORLEVEL! NEQ 0 goto error
::call npm run build
::IF !ERRORLEVEL! NEQ 0 goto error
::cd ..
 
:: 2. Running tests
echo Running tests
vstest.console.exe "%DEPLOYMENT_SOURCE%\Zippy.Tests\bin\Release\Zippy.Tests.dll"
IF !ERRORLEVEL! NEQ 0 goto error


:: 3. Build to the temporary path
echo Building solution to deployment temp
%MSBUILD_PATH% "%DEPLOYMENT_SOURCE%\Zippy\Zippy.csproj" /nologo /verbosity:m /t:pipelinePreDeployCopyAllFilesToOneFolder /p:_PackageTempDir="%DEPLOYMENT_TEMP%";AutoParameterizationWebConfigConnectionStrings=false;Configuration=Release
IF !ERRORLEVEL! NEQ 0 goto error
::echo Copying SPA to deployment temp
::xcopy "%DEPLOYMENT_SOURCE%\Zippy.SPA\build" "%DEPLOYMENT_TEMP%" /S /I
::IF !ERRORLEVEL! NEQ 0 goto error

:: 4. KuduSync
echo Kudu Sync from "%DEPLOYMENT_TEMP%" to "%DEPLOYMENT_TARGET%"
call %KUDU_SYNC_COMMAND% -q -f "%DEPLOYMENT_TEMP%" -t "%DEPLOYMENT_TARGET%" -n "%NEXT_MANIFEST_PATH%" -p "%PREVIOUS_MANIFEST_PATH%" -i ".git;.deployment;deploy.cmd" 2>nul
IF !ERRORLEVEL! NEQ 0 goto error

::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

goto end

:error
echo An error has occured during web site deployment.
exit /b 1

:end
echo Finished successfully.

