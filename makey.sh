rm ktagen.txt
out=ktagen.txt

while read line
do
        key=""
        v=""
        result1=`grep -w "$line" localeResources.properties`
        abce=`echo $result1 |awk -F '=' '{printf("key=\"%s\";v=\"%s\"",$1,$2)}'`
        eval $abce
        echo "line12:$key;$v"

        if [ -z "$v" ]  ;then
                echo "" >> $out
        else
                echo "$v" >>$out
        fi
done<ktag.txt
