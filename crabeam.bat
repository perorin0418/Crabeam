@echo off
echo @echo off> %~dp0contents\cache\exec.bat
echo cd /d %~dp0>> %~dp0contents\cache\exec.bat
set /a counter=0
for  %%A in (.\contents\image\booting\*) do ( if exist %%A (set /a counter=counter+1) )
set /a ran=%random%%%%counter%
echo set JAVA_OPTS=-splash:.\contents\image\booting\bw%ran%.crb>> %~dp0contents\cache\exec.bat
echo set CLASSPATH=.\src;.\contents\lib\jnativehook\*;.\contents\lib\orange_csv\*;.\contents\lib\poi\*;>> %~dp0contents\cache\exec.bat
if %PROCESSOR_ARCHITECTURE%==AMD64 (echo set CLASSPATH=%%CLASSPATH%%.\contents\lib\win32\x64\*;>> %~dp0contents\cache\exec.bat) else (echo set CLASSPATH=%%CLASSPATH%%.\contents\lib\win32\x86\*;>> %~dp0contents\cache\exec.bat)
if %PROCESSOR_ARCHITECTURE%==AMD64 (echo set Path=%~dp0runtime\java\x64\jdk1.8.0_192\bin\;%%Path%%>> %~dp0contents\cache\exec.bat) else (echo set Path=%~dp0runtime\java\x86\jdk1.8.0_192\bin\;%%Path%%>> %~dp0contents\cache\exec.bat)
if %PROCESSOR_ARCHITECTURE%==AMD64 (echo set JAVA_HOME=%~dp0runtime\java\x64\jdk1.8.0_192\>> %~dp0contents\cache\exec.bat) else (echo set JAVA_HOME=%~dp0runtime\java\x86\jdk1.8.0_192\>> %~dp0contents\cache\exec.bat)
echo call .\runtime\groovy\groovy-2.4.15\bin\groovy.bat -Dfile.encoding=UTF-8 .\src\org\net\perorin\crablaser\Main.groovy %1>> %~dp0contents\cache\exec.bat

echo Set shell = CreateObject("Wscript.Shell")> %~dp0contents\cache\exec.vbs
echo shell.run "cmd /c %~dp0contents\cache\exec.bat", vbHide>> %~dp0contents\cache\exec.vbs
echo Set shell = Nothing>> %~dp0contents\cache\exec.vbs

start /min %~dp0contents\cache\exec.vbs
