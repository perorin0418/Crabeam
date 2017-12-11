cd %~dp0

call .\main\initialize.bat

set /a counter=0
for  %%A in (%~dp0contents\image\booting\*) do ( if exist %%A (set /a counter=counter+1) )

set /a ran=%random%*%counter%/32767

java -Dfile.encoding=UTF-8 -splash:%~dp0contents\image\booting\bw%ran%.crb -jar %~dp0main\Crabeam.jar %1
