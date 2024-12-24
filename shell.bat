echo off
git add .
if [%1] == [] goto exit
git commit -m %1
git push -f origin master
echo github push complete
:exit
