@echo off
setlocal
chcp 65001 1>nul 2>nul

echo Начинаем публиковку изменений в репозиторий...

git add .

echo Введите коммит-сообщение: 
set /p commit_content=

git commit -m "%commit_content%"
git push origin master

for /f "delims=" %%a in ('git rev-parse --show-toplevel') do set "repo_path=%%a"
for %%b in ("%repo_path%") do set "repo_name=%%~nxb"

echo Изменения успешно опубликованы в репозиторий %repo_path%