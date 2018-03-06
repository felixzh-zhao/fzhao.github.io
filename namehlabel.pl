#!/usr/bin/perl

$output = "namehlabel.txt";
unlink $output;

open IN, "<ngtxt";  #read a text file
open(FILE,">$output");

my @raw=<IN>;           #using @raw store data
chomp(@raw);            #skip enter keys

for ($i=0;$i<$#raw;$i++)
{
	$a="";
	@tmp=split (/_/,$raw[$i]);
	for ($j=0;$j<$#tmp;$j++)
	{
		if ( $j!=0 && $j!=1 )
		{
			$a=$a.$tmp[$j];
		}
	}
         print FILE "$a\n";
}

close (IN);
close (FILE);
