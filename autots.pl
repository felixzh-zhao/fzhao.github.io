#!/usr/bin/perl
$output = "autots.ts";
unlink $output;

open INim, "<import.template";  #read import 
open INco, "<component.template";  #read component 
open IN2, "<namecfgstr.txt";  #read cfg key str 
open IN1, "<namekey.txt";  #read cfg key name 
open IN3, "<namehlabel.txt";  #read head label name 
open IN4, "<ngtxt";  #read head label string 

open(FILE,">>$output");

my @rawim=<INim>;           #using @raw store data
chomp(@rawim);
my @rawco=<INco>;           #using @raw store data
chomp(@rawco);

my @raw1=<IN1>;           #using @raw store data
chomp(@raw1);
my @raw2=<IN2>;           #using @raw store data
chomp(@raw2);
my @raw3=<IN3>;           #using @raw store data
chomp(@raw3);
my @raw4=<IN4>;           #using @raw store data
chomp(@raw4);

$num =$#raw1;
$space="    ";
$class = '// this class process the data for call global security
export class GlobalSecurityPage implements OnInit, AfterViewInit {
    @ViewChild(PFormComponent) pFormComponent: PFormComponent;';

@arr=();
@arr=join("\n",@rawim);
print FILE "@arr\n\n";
@arr=();
@arr=join("\n",@rawco);
print FILE "@arr\n";
print FILE $class;
print FILE "\n    //cfg\n";

$b = "private readonly ";
$c = ": string = ";
$d = "configfqnames.";
$a="";

print "num=$num\n";
#name cfg
for ( $i=0; $i<=$num;$i++ )
{
	$a=$a.$b.$raw1[$i].$c.$d.$raw2[$i].";";
	print FILE "    $a\n";
	$a="";
}

print FILE "    //head and label\n";
$a="";
#name head and label
for ( $i=0; $i<=$num;$i++ )
{
        $a=$a.$b.$raw3[$i].$c."'".$raw4[$i]."'".";";
        print FILE "    $a\n";
	$a="";
}

print FILE "\n    private onDestroy\$\: Subject<any>;\n\n";

$constructor="    //constructor
    constructor(private _logger: Logger) {
        this.onDestroy\$ = new Subject<any>();
        this.onDestroy\$\.share();
    }\n";
print FILE $constructor;

$destroy="    //on Destroy
    ngOnDestroy(): void {
        this.onDestroy\$\.complete();
    }\n";
print FILE $destroy;

$ngOnInit="\n$space//on Initialization
    ngOnInit(): void {
$space  this.globalSeucriryItem = [\n";

print FILE $ngOnInit;
$a="";
for ($i=0;$i<=$num;$i++)
{
	$a="$space    new PFormConfigItem({
$space$space  label: this.$raw3[$i],
$space$space  key: this.$raw1[$i],
$space$space  fieldType: PFormFieldType.DROP_DOWN
$space$space})";
	if ($i!=$num)
	{
		$a=$a.",";
	}
	print FILE "$a\n";
}

$endInit="$space  ];
    }\n";
print FILE $endInit;

$formInit="    /**
     * Called when the {@link dialingOptionsPForm} has initialized.
     */
     private handleFormInitialized(): void {
    }\n";
print FILE $formInit;

$afterViewInit="    ngAfterViewInit(): void {
    }\n";

$end="}";
print FILE "$end";

close (INim);
close (INco);
close (IN1);
close (IN2);
close (IN3);
close (IN4);

