@echo off
set ANT_HOME=tools\apache-ant-1.10.1
call %ANT_HOME%\bin\ant clean jar
pause