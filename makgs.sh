#!/bin/bash
rm gstxt
while read line
do
        gs=""
        a=""
	num=`grep -w "$line" localeResources.properties|wc -l`
       	result1=`grep -w "$line" localeResources.properties`
        echo "line7:$result1;num=$num"

	if [ $num -gt 1 ]
	then
		#there are more than 1 string
		#shell split don;t need awk;
		#http://xstarcd.github.io/wiki/shell/IFS.html
		OLD_IFS="$IFS"
		IFS=$'\n'
		arr=($result1)
		IFS="$OLD_IFS"
		
		for ((i=0;i<${#arr[@]};i++))
		do
			name=`echo ${arr[$i]} |awk -F '=' '{print $1}'`
			gs=$gs";"$name
			#echo "line22:${arr[$i]};$name;$gs"
		done
	elif [ $num -eq 0 ]
	then
		#there is no translate string
		gs=$line	
	else 	
        	abce=`echo $result1 |awk -F '=' '{printf("gs=\"%s\";a=\"%s\"",$1,$2)}'`
        	eval $abce
	fi

        echo "line32:$gs;$a"

        if [ -z "$gs" ] ;then
                echo "" >> gstxt
        else
                echo "$gs" >>gstxt
        fi
done<entxt
