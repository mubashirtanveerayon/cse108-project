echo off
git add .
set arg=%1
if [arg] == [] goto exit
git commit -m arg
git push -f origin master
echo github push complete
:exit
