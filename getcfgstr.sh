cfgkey=$1
grep -w $cfgkey config-names.ts |awk -F ":" '{print $1}' |sed 's/ //g'

