@echo off
echo This batch file may not run correctly if executed from a path containing space characters.

if not "%OS%"=="Windows_NT" goto win9xStart

:winNTStart
@setlocal
rem %~dp0 is name of current script under NT
set JEP_HOME=%~dp0
set JEP_HOME=%JEP_HOME%\..
echo JEP_HOME = %JEP_HOME%
set CLASSPATH=%JEP_HOME%\build\
call java.exe org.nfunk.jepexamples.Console
REM call jview org.nfunk.jepexamples.Console
@endlocal
goto mainEnd


:win9xStart
echo No Windows 9x batch support yet...
goto mainEnd


:mainEnd
pause
