!/usr/bin/perl

if (@ARGV < 1) {
	print "please input branch\n";
	return 0;
}
else
{
	$branch = $ARGV[0];
}

$router = "C:\svn\$branch\ui\web.angular2\WebUI\app\components\app\app-routing.module";

open IN "<$router";  #read router
my @raw=<IN>;           #using @raw store data
chomp(@raw);
./getrouterjsonline.sh
