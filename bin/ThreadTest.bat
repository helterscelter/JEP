@echo off

if not "%OS%"=="Windows_NT" goto win9xStart

:winNTStart
@setlocal
rem %~dp0 is name of current script under NT
set JEP_HOME=%~dp0
set JEP_HOME=%JEP_HOME%\..
echo JEP_HOME = %JEP_HOME%
set CLASSPATH=%JEP_HOME%\build\
REM java org.nfunk.jepexamples.ThreadTest
call jview org.nfunk.jepexamples.ThreadTest
@endlocal
goto mainEnd


:win9xStart
echo No Windows 9x batch support yet...
goto mainEnd


:mainEnd
pause
