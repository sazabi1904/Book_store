@echo off
setlocal
echo ==========================================
echo    BOOK STORE LIBRARY SYSTEM RUNNER
echo ==========================================

:: Set paths
set SRC_DIR=%~dp0src
set LIB_JAR=%~dp0lib\mysql-connector-j-8.3.0.jar
set BIN_DIR=%~dp0bin

:: Clean bin directory for a fresh start
if exist "%BIN_DIR%" (
    echo [0/2] Cleaning old build...
    del /s /q "%BIN_DIR%\*" >nul 2>&1
) else (
    mkdir "%BIN_DIR%"
)

echo [1/2] Compiling project...
javac -d "%BIN_DIR%" -encoding UTF-8 -cp "%LIB_JAR%"^
 "%SRC_DIR%\com\library\model\*.java"^
 "%SRC_DIR%\com\library\database\*.java"^
 "%SRC_DIR%\com\library\dao\*.java"^
 "%SRC_DIR%\com\library\main\*.java"

if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Compilation failed!
    pause
    exit /b %ERRORLEVEL%
)

echo [2/2] Launching application...
echo ------------------------------------------
java -cp "%BIN_DIR%;%LIB_JAR%" com.library.main.LibraryDemo

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERROR] Application crashed or stopped.
    pause
)

endlocal
