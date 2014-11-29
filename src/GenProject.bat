@echo off
rem Generate the project files

SETLOCAL
set STARTTIME=%TIME%

set wkspace=D:\DevFiles\Java\WorkSpaces\Main
set projectName=GenProjectModel
set packageName=net.certiv.json
set grammarName=Json
set genprjar=%wkspace%\%projectName%\lib\GenProject-2.0-complete.jar
set antlrjar=%wkspace%\%projectName%\lib\antlr-4.4-complete.jar
set javahome=C:\Program Files\Java\jre7
set javapgm="%javahome%\bin\java"

set CLASSPATH=%genjar%;%antlrjar%;%CLASSPATH%

cd /d %wkspace%
%javapgm%  net.certiv.antlr.project.gen.GenProject -c -g %grammarName% -n %packageName% -p %wkspace%\%projectName% -r %wkspace%\%projectName%

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
