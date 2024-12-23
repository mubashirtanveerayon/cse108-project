git add .
set arg=%1
if [arg] == [] goto exit
git commit -m %1
git push -f origin master
:exit
