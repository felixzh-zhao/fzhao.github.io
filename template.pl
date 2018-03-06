#!/usr/bin/perl -w
$output="template.txt";
unlink $output;
unlink "ngtxt-1";
unlink "gstxt-1";
unlink "entxt-1";

system("cp ngtxt ngtxt-1");
system("cp gstxt gstxt-1");
system("cp entxt entxt-1");
system("chmod 777 ngtxt-1 gstxt-1 entxt-1");

system("cat ktag.txt>>ngtxt-1");
system("cat ktag.txt>>gstxt-1");
system("cat ktagen.txt>>entxt-1");

open INen, "<entxt-1";  #read a text file
open INgs, "<gstxt-1";  #read a text file
open INng, "<ngtxt-1";  #read a text file

my @rawen=<INen>;           #using @raw store data
my @rawgs=<INgs>;           #using @raw store data
my @rawng=<INng>;           #using @raw store data

chomp(@rawen);            #skip enter keys
chomp(@rawgs);            #skip enter keys
chomp(@rawng);            #skip enter keys

close(INen);
close(INgs);
close(INng);
#print "@raw \n";
open(FILE,">$output");
print FILE "||GS Key||NG Key||English String||\n";

for ($i = 0 ; $i <= $#rawen; $i++) {
        print FILE "|$rawgs[$i]|$rawng[$i]|$rawen[$i]\n";
}

close (FILE);
