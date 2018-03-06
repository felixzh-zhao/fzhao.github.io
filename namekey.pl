#!/usr/bin/perl

$output = "namekey.txt";
unlink $output;

open IN, "<namecfgstr.txt";  #read a text file
open(FILE,">$output");

my @raw=<IN>;           #using @raw store data
chomp(@raw);            #skip enter keys


for ($i = 0 ; $i <= $#raw; $i++) {
	@temp = split(/_/,$raw[$i]);
	push @tmpnames, $temp[-1];
}

#judge if the last string is duplication
%flag="";
for ($i = 0 ; $i <= $#tmpnames; $i++) {
	for ($j = $i+1 ; $j <= $#tmpnames; $j++) {
		if ( $tmpnames[$i] eq $tmpnames[$j] )
		{
			print "line24:i=$i;j=$j;$tmpnames[$i]\n";
			$a=$tmpnames[$i];
			$flag{$a}=1;
		}
	}
}

for ($i = 0 ; $i <= $#raw; $i++) {
        @temp = split(/_/,$raw[$i]);
	$b = $temp[-1];
	#=1 have dupliation;eg ENABLE
	if ($flag{$b}==1)
	{
		$c =$temp[-2].$temp[-1]."KEY";
        	push @names,$c;
	} else {
		$c =$temp[-1]."KEY";
        	push @names,$c;
	}
	print FILE "$c\n";
}

close ($IN);
close ($FILE);
