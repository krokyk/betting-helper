@echo off
if exist "@application.dir@" rmdir "@application.dir@" /s /q
echo Extracting archive @setup.zip@ into @application.dir@...
unzip.exe @setup.zip@ -d "@application.dir@" > NUL
echo Extracting DB...
unzip.exe "@application.dir@\@db.zip@" -d "@application.dir@" > NUL
del "@application.dir@\@db.zip@"
echo Extracting JRE...
unzip.exe "@application.dir@\@jre.zip@" -d "@application.dir@" > NUL
del "@application.dir@\@jre.zip@"
echo Extracting LIBS...
unzip.exe "@application.dir@\@lib.zip@" -d "@application.dir@" > NUL
del "@application.dir@\@lib.zip@"
echo Done.
echo Use @script.start@ or @script.start.no.console@ located in @application.dir@ directory to start the application
pause