#!/usr/bin/perl -w

$output = "ngtxt";
unlink $output;

my $head1 = "GLOBAL";
my $head2 = "SECURITY";
my $head = $head1."_".$head2."_";
my $end = "_LABEL";
my $endH = "_HEAD";

open IN, "<gstxt";  #read a text file
open(FILE,">$output");

my @raw=<IN>;           #using @raw store data
chomp(@raw);            #skip enter keys

$model =0;
#model is 1 for just label; 0 is for head and label
if ( ($#ARGV+1) == 1 )
{
	$model =1
}

print "$#ARGV;model=$model\n";

for ($i = 0 ; $i <= $#raw; $i++) {
	@new = ();
	@newline = ();
	
	@new=split (/_/,$raw[$i]);
	
	for ($j = 0;$j <=$#new; $j++) {
		$loc1 = index($new[$j],"GLOBAL");
		$loc2 = index($new[$j],"SECURITY");
		
		if ( $loc1 != 0 && $loc2 != 0 )
		{
			push @newline, $new[$j];	
		}
	}
	
	$str=join ("_",@newline);
	#"." is the best way for join string
	$a = $head.$str.$end;

     if ( $model == 0)
     {
	if ( $i == 0)
	{
		$a = $head.$str.$endH;
		print FILE "$a\n";
	}
	else
	{
		$a = $head.$str.$end;
		print FILE "$a\n";
	}
    }
    else
    {
                $a = $head.$str.$end;
                print FILE "$a\n";
     }
}
