@echo off
echo This batch file may not run correctly if executed from a path containing space characters.

if not "%OS%"=="Windows_NT" goto otherStart

REM -- Windows NT & XP -----------------------------------------------
:winNTStart
@setlocal
REM determine the location of JEP
REM %~dp0 is name of current script under NT
set JEP_HOME=%~dp0
set JEP_HOME=%JEP_HOME%\..
echo JEP_HOME = %JEP_HOME%

REM change directories to the examples source code directory
cd "%JEP_HOME%\src\org\nfunk\jepexamples"

REM execute the applet viewer 
appletviewer.exe -J-classpath -J%JEP_HOME%\build\ Evaluator.java
REM jview.exe /cp:a %JEP_HOME%\build\ /a "%JEP_HOME%\src\org\nfunk\jepexamples\Evaluator.java"
@endlocal
goto mainEnd


REM -- Other OS (probably Windows 9x) --------------------------------
:otherStart
echo This batch file only supports Windows NT, 2000 and XP supported...
goto mainEnd


REM -- End -----------------------------------------------------------
:mainEnd
pause
