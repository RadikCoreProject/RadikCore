@echo off
setlocal

:: ����砥� ��᮫��� ���� � ��୥��� ��४�ਨ ९������
for /f "delims=" %%a in ('git rev-parse --show-toplevel') do set "repo_path=%%a"

:: ��������� �������� ९������ (��᫥���� ����� � ���)
for %%b in ("%repo_path%") do set "repo_name=%%~nxb"

echo �������� ⥪�饣� ९������: %repo_name%

endlocal
pause
