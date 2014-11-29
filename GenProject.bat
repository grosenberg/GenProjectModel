@echo off
rem Generate the project files

SETLOCAL
set STARTTIME=%TIME%

set wkspace=D:\DevFiles\Java\WorkSpaces\Main
set projectName=JsonProject
set packageName=net.certiv.json
set grammarName=Json
set genprjar=%wkspace%\GenProject\jars\GenProject-2.0-complete.jar
set javahome=C:\Program Files\Java\jre7
set javapgm="%javahome%\bin\java"

rem Well-known location for the rule set
set ruleSet=%wkspace%\GenProject\GenProjectRuleSet.json

cd /d %wkspace%
%javapgm% -jar %genprjar% -c -g %grammarName% -n %packageName% -p %wkspace%\%projectName% -r %ruleSet% 

set ENDTIME=%TIME%
set /A STARTTIME=(1%STARTTIME:~6,2%-100)*100 + (1%STARTTIME:~9,2%-100)
set /A ENDTIME=(1%ENDTIME:~6,2%-100)*100 + (1%ENDTIME:~9,2%-100)

if %ENDTIME% LSS %STARTTIME% (
	set /A DURATION=%STARTTIME%-%ENDTIME%
) else (
	set /A DURATION=%ENDTIME%-%STARTTIME%
)

set /A SECS=%DURATION% / 100
set /A REMAINDER=%DURATION% %% 100
echo %SECS%.%REMAINDER% s
ENDLOCAL

timeout 4
