@echo off
setlocal
set ANT_HOME=tools\apache-ant-1.8.4
set "JAVA_HOME=c:\Program Files\Java\jdk1.6.0_45\"
call %ANT_HOME%\bin\ant -f build-custom.xml dist
endlocal
pause