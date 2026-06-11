@echo off
set "JAVA_HOME=C:\Program Files\Android\Android Studio\jbr"
set "PATH=%JAVA_HOME%\bin;%PATH%"
set "MVN=%~dp0tools\apache-maven-3.9.6\bin\mvn.cmd"

call "%MVN%" -q javafx:run -f "%~dp0pom.xml"
if %ERRORLEVEL% neq 0 pause
