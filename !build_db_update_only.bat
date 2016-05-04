@echo off
set ANT_HOME=tools\apache-ant-1.8.4
call %ANT_HOME%\bin\ant -f build-custom.xml db-update-only
pause