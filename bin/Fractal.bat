REM call jview.exe /cp:a d:\java\packages /a Evaluator.java

if not "%OS%"=="Windows_NT" goto win9xStart

:winNTStart
@setlocal
rem %~dp0 is name of current script under NT
set JEP_HOME=%~dp0
set JEP_HOME=%JEP_HOME%\..
echo JEP_HOME = %JEP_HOME%

appletviewer.exe -J-classpath -J%JEP_HOME%\build\ "file://%JEP_HOME%\src\org\nfunk\jepexamples\Fractal.java"
@endlocal
goto mainEnd


:win9xStart
echo No Windows 9x batch support yet...
goto mainEnd


:mainEnd
pause
