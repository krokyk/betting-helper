@echo off
set JAVA_HOME="@JAVA_HOME@"
if not exist %JAVA_HOME% goto javaNotExist
set DB_NAME=BettingDB
if not exist %DB_NAME% goto dbNotExist
%JAVA_HOME%\bin\java.exe -Dderby.system.home=. -jar lib\derbyrun.jar ij @db.update.script@
goto done
:dbNotExist
echo %DB_NAME% does not exists, create a clean DB by running !create_clean_db.bat
goto done
:javaNotExist
echo Please run this script from the root directory of the application (where @script.start@ is located)
goto done
:done
pause