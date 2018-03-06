#!/usr/bin/perl -w
$output="routjson.txt";
unlink $output;
$input="app-routing.module.ts";


$begin="export const appRoutes: Routes = [";
$end="{path: '**', redirectTo: ''}";

open(FILE,">$output");
my $fh;
open ($fh,'<',$input) or die $!;

$linebegin=0;
$lineend=0;
int linenum=0;

while (my $line=<$fh>) {
	linenum=linenum+1;
	
        if ( $line == $begin )
        {
		linebegin=
		
		
	}
}
close ($fh);
close (FILE);
