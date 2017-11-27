call .\main\initialize.bat

set /a counter=0
for  %%A in (.\contents\image\booting\*) do ( if exist %%A (set /a counter=counter+1) )

set /a ran=%random%*%counter%/32767

java -Dfile.encoding=UTF-8 -splash:.\contents\image\booting\bw%ran%.crb -jar .\main\Crabeam.jar %1
