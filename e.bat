@echo off
setlocal

:: Получаем абсолютный путь к корневой директории репозитория
for /f "delims=" %%a in ('git rev-parse --show-toplevel') do set "repo_path=%%a"

:: Извлекаем название репозитория (последнюю папку в пути)
for %%b in ("%repo_path%") do set "repo_name=%%~nxb"

echo Название текущего репозитория: %repo_name%

endlocal
pause
