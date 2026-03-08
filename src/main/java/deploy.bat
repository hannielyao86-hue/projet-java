@echo off
echo   GYM MANAGER - Script de deploiement
echo.

echo [1/3] Nettoyage...
call mvn clean

echo.
echo [2/3] Compilation et packaging...
call mvn package -DskipTests

echo.
echo [3/3] Verification du JAR...
if exist target\gym-manager.jar (
    echo.
    echo  SUCCES ! JAR cree avec succes
    echo.
    echo Fichier : target\gym-manager.jar
    echo.
    echo Pour lancer l'application :
    echo    java -jar target\gym-manager.jar
    echo.
) else (
    echo.
    echo ERREUR : Le JAR n'a pas ete cree
    echo.
)

pause