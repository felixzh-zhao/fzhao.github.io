#!/bin/bash
#output is the string  between Routes =[...]
output="router.txt"
file="app-routing.module.ts"
rm -f $output

begin="export const appRoutes: Routes = \["
end="{path: '\*\*', redirectTo: ''}"

line1=`grep -n "$begin" $file`
line2=`grep -n "$end" $file`

linebegin=`echo $line1|awk -F ":" '{print $1}'`
lineend=`echo $line2|awk -F ":" '{print $1}'`
echo "linebegin=$linebegin;lineend=$lineend"

let "linebegin+=1"
let "lineend-=1"
echo "{
Routes = [" >$output
perl -ne "print if $linebegin..$lineend" $file >>$output
echo "]}" >>$output
