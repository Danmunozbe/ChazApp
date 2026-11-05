@echo off
echo Compilando ChazApp...
javac -encoding UTF-8 -cp "sqlite-jdbc.jar" -d bin src\*.java
if errorlevel 1 (
    echo ERROR de compilacion. Revisa los archivos en src\
    pause
    exit /b 1
)
echo Iniciando ChazApp...
java -cp "bin;sqlite-jdbc.jar" ChazApp
pause
