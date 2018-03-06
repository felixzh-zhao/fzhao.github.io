#!/usr/bin/perl -w

$output="namecfgstr.txt";
unlink $output;
open(FILE,">$output");
my $fh;
$file="cfgraw.txt";  #read a text file
open ($fh,'<',$file) or die $!;
while (my $line=<$fh>) {
	if ( $line =~ /(.*): "(.*)"/ )
	{
		$cfgkey = $2;
		#perl call shell is easy, perl can pass var to shell, and result can return to perl;it is easier than shell+awk
		$ret=`/home/fzhao/script/getcfgstr.sh $cfgkey`;
		@arr=split(/\n/,$ret);
		print FILE "$arr[0]\n";
	}
}
close ($fh);
close (FILE);
