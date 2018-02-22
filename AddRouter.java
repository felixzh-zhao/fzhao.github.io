package parserouter;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.BufferedReader;  
import java.io.BufferedWriter;  
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.regex.*;

import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;  
import jcifs.smb.SmbAuthException;  
import jcifs.smb.SmbException;

import java.util.Scanner;
import java.util.List;

import java.io.BufferedReader;  
import java.io.IOException;  
import java.io.InputStream;  
import java.io.InputStreamReader;  
  
import ch.ethz.ssh2.Connection;  
import ch.ethz.ssh2.Session;  
import ch.ethz.ssh2.StreamGobbler;  
  
public class AddRouter {

	public AddRouter() {
		// TODO Auto-generated constructor stub
        /* 读入TXT文件 */
        File filename = new File("menu.txt"); // 要读取以上路径的input。txt文件  
    	//read menu.txt
        InputStreamReader reader = null;
        //read/parse json string to find menu/submenu location 
        try {
        	reader = new InputStreamReader(new FileInputStream(filename));
        	BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言 
            menustr = br.readLine(); // 一次读入一行数据 
            submenustr = br.readLine();
            title = br.readLine();
            
	   		 String[] buff = submenustr.split(" ");
	   		 String filename1;
	   		 filename1= String.join("-",buff);
	   		 String[] buff1 = title.split(" ");
	   		 filename1=filename1+"-"+buff1[0];
	   		 tsname=filename1.toLowerCase()+"-page.ts";
	   		 //put tsname in the end of menu.txt
	   		 BufferedWriter out = null;
	         out = new  BufferedWriter( new  OutputStreamWriter(new  FileOutputStream("menu.txt",  true )));
	         out.write("\r\n");
	         out.write(tsname);
	         out.write("\r\n");
	         
	         //menu.txt: menu,submenu,headtitle,filename,classname
  			String[] array1 = title.split(" ");
     		String[] array = submenustr.split(" ");
     		if ( array.length == 1 )
     		{
     			array[0] = array[0].substring(0, 1).toUpperCase() + array[0].substring(1);
     			array[0] = array[0] +array1[0]+"Page";
     		} else if ( array.length == 2 )//local-account-password-page; output is GlobalSecurityPage
     		{
     			array[0] = array[0].substring(0, 1).toUpperCase() + array[0].substring(1);
     			array[1] = array[1].substring(0, 1).toUpperCase() + array[1].substring(1);
     			array[0] = array[0] + array[1]+array1[0]+"Page";
     		} else 
     		{
     			System.out.println("name is great than 2 , it need mannully add array[0]!!!");
     		}
     		classstr = array[0];
     		out.write(classstr);
	   		System.out.println("line95:tsname="+tsname+";classstr="+classstr+";");
	         out.close();
	       
   		 
		 int i;
		 for (i=0;i<navmenu.length;i++)
		 {
			 if ( navmenu[i].contains(menustr.toUpperCase()))
			 {
				 break;
			 }
		 }
		  
		 navigatestr = navmenu[i];
		 String[] arraytmp = submenustr.split(" ");
		 String[] arraytmp11 = title.split(" ");
		 
		 if (arraytmp.length == 2)
		 {
			 subnavigationstr = arraytmp[0].toUpperCase()+"_"+arraytmp[1].toUpperCase()+"_"+arraytmp11[0].toUpperCase()+"_NAV";
		 } 
		 else {
			 subnavigationstr = arraytmp[0].toUpperCase()+arraytmp11[0].toUpperCase()+"_NAV";
		 }
		 System.out.println("line186:"+navigatestr+";subnavigationstr="+subnavigationstr+";title="+title+";");
		 
	   } 
	   catch (IOException e) {
	             e.printStackTrace();
	   } 
	}
	
	/*notes: value  [ ] 会json parse fail ，改成value "";  value is true/false, it is checkbox; option value is [1,2,3..] is drop list, other is input												
	try option is list[]*/
	/* input  is router.txt;
	 * output is  find the insert line key and value*/
	public void ParseRouterJson() {
		//json格式的字符串
        File file = new File("router.txt");
        String str = null;
        //read/parse json string to find menu/submenu location 
        try {
            //read router.txt
            str = FileUtils.readFileToString(file,"GBK");
        } catch (IOException e) {
            e.printStackTrace();
        }
		//! 创建一个Gson对象
		Gson gson = new Gson();
		//deserilization
		Obj obj = gson.fromJson(str, Obj.class);
		//System.out.println(obj.getVars().get(0).getConfigVar().getNodeAndVarName());
		String menu,submenu;
		int childsize=0;
		int i,j;
		
		//7 main menu+3 head
		for (i=0;i<10;i++)
		{
			menu = obj.getRoutes().get(i).getPath();
			if ( menu.equals(menustr.toLowerCase()))
			{
				childsize=obj.getRoutes().get(i).getChildren().size();
				System.out.println("line120:"+childsize);
				break;
			}
		}
		
		for (j=0;j<childsize;j++)
		{
			submenu = obj.getRoutes().get(i).getChildren().get(j).getPath();
			if (submenu.compareTo(submenustr.toLowerCase())>0)
			{
				break;
			}
		}

		lastpath=obj.getRoutes()
				.get(i)
				.getChildren()
				.get(j-1)
				.getPath();
		componentstr=obj.getRoutes()
				.get(i)
				.getChildren()
				.get(j-1)
				.getComponent();
		System.out.println("line144:lastpath="+lastpath+";componentstr="+componentstr+"i="+i+";j="+j);
	}
//java string operator >>c++ string operator;eg: java regex, split, uppercase
	/* app-routing.modules.ts: 这个的第二项，必须用json parse	通过getlinenum.sh生成router.txt						
	admin-pages.module.ts: 不用排序直接输出			java parse router.txt 提取submenu line num						
	navigation.config.ts : json parse			java读/写一个文件将submenu插入到app-routing.modules.ts中						
	option:												
	dashboard.html : 简单排序输出												
	navigation-service.ts 不用排序直接输出	*/
	
	public void WriteRouter() {
		 BufferedWriter out;
		 InputStreamReader reader;
		 BufferedReader br;
		 String newmenustr;
		 String pathstr=null;
		 String filename="app-routing.module.ts";
		 String filenamebak;
		 String filepath;
		 filenamebak=filename+".bak";
		 filepath="C:\\svn\\"+load+"\\ui\\web.angular2\\WebUI\\app\\components\\app\\";
		 String filename1 = filepath+filename;
		 String filename2 = filepath+filenamebak;
		 delFile(filename2);
		 File inputname = new File(filename1); // 要读取以上路径的input。txt文件  
		 File outputname = new File(filename2); // 要读取以上路径的input。txt文件 
		 
	        //read/parse json string to find menu/submenu location 
	        try {
	        	out = new BufferedWriter(new FileWriter(outputname));
	        	newmenustr = menustr.substring(0, 1).toUpperCase() + menustr.substring(1);
	        	String pattern = ".*"+newmenustr+ ".*Component";
	        	System.out.println("line213:lastpath="+lastpath);
	        	//regex { ,it need escape online; it is lower than script escape online; java 转义need \\
	        	String patternpath =".*path: \'"+lastpath+"\',\\s+component: "+componentstr+".*";
	        	System.out.println("line167:"+patternpath);

	        	String filetrim,filetrim1;
        		int len;
        		len=tsname.length();
        		//trim -page.ts
        		filetrim=tsname.substring(0,len-3);
        		filetrim1=tsname.substring(0,len-8);
	            reader = new InputStreamReader(new FileInputStream(inputname)); // 建立一个输入流对象reader  
	            br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言 
	            
	            String line="";
	            line = br.readLine(); // 一次读入一行数据
	            boolean isMatch=false,isMatchpath=false,insertflag=false;
	            while (line != null) {
	            	out.write(line);
	            	out.newLine();
	            	isMatch = Pattern.matches(pattern, line);
	            	if (isMatch)
	            	{           		
	            		//import { LocalAccountsPage } from "../security/local-accounts";
	            		// splitting String by comma, it will return array String[] array = languages.split(",");
	            		String importstr;
	            		//write import file
	            		importstr="import { "+classstr+" } from \"../"+menustr.toLowerCase()+"/"+filetrim+"\";";
	            		out.write(importstr);
	            		out.newLine();
	            	}
	            	isMatchpath = Pattern.matches(patternpath, line);
	            	if (isMatchpath)
	            	{
	            		insertflag =true;
	            	}
	            	
	            	if (insertflag)
	            	{
	            		insertflag = false;
	                    //{path: 'dir-server', component: DirectoryServerPage, canActivate: [ConferencingGuard]},
	            		pathstr="            {path: '"+filetrim1.toLowerCase()+"', component: "+classstr+", canActivate: [ConferencingGuard]},";
	            		System.out.println(pathstr);
	            		out.write(pathstr);
	            		out.newLine();
	            	}
	            	
	                line = br.readLine(); // 一次读入一行数据  
	            }
	            
	            br.close();
	            out.close();
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        System.out.println("line267:filename1="+filename1);
			 delFile(filename1);
			 boolean flag = outputname.renameTo(inputname);
	}
	
	public void WriteDashboard() {
		 BufferedWriter out;
		 BufferedReader br;
		 InputStreamReader reader;
		 String divstr;
		 String filename="dashboard.html";
		 String filenamebak;
		 String filepath;
		 filenamebak=filename+".bak";
		 filepath="C:\\svn\\"+load+"\\ui\\web.angular2\\WebUI\\app\\components\\dashboard\\";
		 String filename1 = filepath+filename;
		 String filename2 = filepath+filenamebak;
		 delFile(filename2);
		 File inputname = new File(filename1); // 要读取以上路径的input。txt文件  
		 File outputname = new File(filename2); // 要读取以上路径的input。txt文件  
	//if menustr are not in array , or return
		 int i;
		 for (i=0;i<dashmenu.length;i++)
		 {
			 if ( dashmenu[i].contains(menustr.toUpperCase()))
			 {
				 	break;
			 }
			 
		 }
		 
		 if ( i==dashmenu.length)
		 {
			 System.out.println("there is no item in dashboard,don;t need add item in dashboard!");
			 return;
		 }
		 
	         try {
	        	//<div class="dashboard-link-section-heading">{{"SECURITY_NAV" | translation}}</div>
	        	boolean isMatchpath=false,insertflag=false,insertendflag=false;
	        	String pattern = ".*"+navigatestr+ ".*";
	        	System.out.println("line302:"+navigatestr);

	        	out = new BufferedWriter(new FileWriter(outputname));
	            reader = new InputStreamReader(new FileInputStream(inputname)); // 建立一个输入流对象reader  
	            br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言 
	            
	            String line="";
	            line = br.readLine(); // 一次读入一行数据
	            
	            while (line != null) {
	            	out.write(line);
	            	out.newLine();
		            if (insertendflag == false)
		            {
		            	isMatchpath = Pattern.matches(pattern, line);
		            	if (isMatchpath)
		            	{
		            		insertflag =true;
		            	}
		            	
		            	if (insertflag)
		            	{
		            		String filetrim;
		            		int len;
		            		len=tsname.length();
		            		filetrim=tsname.substring(0,len-3);
		            		insertflag = false;
		                    //{path: 'dir-server', component: DirectoryServerPage, canActivate: [ConferencingGuard]},
		            		divstr="<div class=\"dashboard-link-section-link\" *ngIf=\"callFeatureObs$ | async\"><a class=\"plcm-hyperlink\" routerLink=\"/";
		            		divstr=divstr+menustr.toLowerCase()+"/"+filetrim+"\">{{\""+subnavigationstr+"\" | translation}}</a></div>";
		            		StringBuilder sb = new StringBuilder(); 
		            		for (int j = 0; j < 20; j++) 
		            			sb.append(' ');
		            		divstr=sb+divstr;
		            		System.out.printf("%s\n",divstr);
		            		System.out.println("line332:tsname="+tsname+";filetrim="+filetrim+";le="+len);
		            		out.write(divstr);
		            		out.newLine();
		            		insertendflag=true;
		            	}  	
		            }    	
	                line = br.readLine(); // 一次读入一行数据  
	            }
	            
	            br.close();
	            out.close();
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	         System.out.println("line347:filename1="+filename1);
			 delFile(filename1);
			 boolean flag = outputname.renameTo(inputname);
	}
	
	public void WriteAdminPage() {
		 BufferedWriter out;
		 BufferedReader br;
		 InputStreamReader reader;
		 String filename ="admin-pages.module.ts";
		 String filenamebak;
		 String filepath;
		 filenamebak=filename+".bak";
		 
		 filepath="C:\\svn\\"+load+"\\ui\\web.angular2\\WebUI\\app\\components\\";
		 String filename1 = filepath+filename;
		 String filename2 = filepath+filenamebak;
		 delFile(filename2);
		 File inputname = new File(filename1); // 要读取以上路径的input。txt文件  
		 File outputname = new File(filename2); // 要读取以上路径的input。txt文件  	 
		// List<String> list = new ArrayList<String>();
		  try {
			    out = new BufferedWriter(new FileWriter(outputname));
	        	String pattern = ".*"+"declarations:.*\\["+ ".*";
	        	//because it don;t need sort ,so hard anchor
	        	String patternimport=".*import \\{ SnmpPage \\} from \"./servers/snmp-page\";.*";

	            reader = new InputStreamReader(new FileInputStream(inputname)); // 建立一个输入流对象reader  
	            br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言 
	            
        		String filetrim;
        		int len;
        		len=tsname.length();
        		filetrim=tsname.substring(0,len-3);
	            String line="";
	            line = br.readLine(); // 一次读入一行数据
	            boolean isMatch=false,isMatchend=false,isMatchImport=false,declarationflag=false;
        		//it is the same with perl regex, it is good for get match
        		Pattern patterntrim = Pattern.compile("\\s+(.*),");
	            //first time, get sort string location
	            while (line != null) {
	            	isMatchImport = Pattern.matches(patternimport, line);
	            	if (isMatchImport)
	            	{
	            		//import { GlobalSecurityPage } from "./security/global-security-page";
	            		out.write("import { "+classstr+" } from \"./"+menustr.toLowerCase()+"/"+filetrim.toLowerCase()+"\";");
    	            	out.newLine();
    	            	System.out.println("line355:"+classstr); 
	            	}
	            	
	            	isMatch = Pattern.matches(pattern, line);
	            	if (isMatch)
	            	{
	            		declarationflag =true;
	            	}
	            	
	            	if (declarationflag)
	            	{
	            		String tmp="";
	            		String patternend= ".*],。*";
	            		
	            		isMatchend = Pattern.matches(patternend, line);
	            		//the new insert classstr is the last one
	            		if (isMatchend)
	            		{
        	            	out.write("        "+classstr);
        	            	out.newLine();
        	            	declarationflag = false;
	            		}
	            		Matcher matcher = patterntrim.matcher(line);
	                    if(matcher.find())
	                    {
	                    	//System.out.println("line373:"+matcher.group(1));
	                        tmp=matcher.group(1);
	                    	
	                        if (tmp.compareTo(classstr)>0) 
	                        {
	        	            	out.write("        "+classstr+",");
	        	            	out.newLine();
	        	            	declarationflag = false;
	                        }
	                    }
	            	}
	            	out.write(line);
	            	out.newLine();
	                line = br.readLine(); // 一次读入一行数据  
	            }
	            br.close();
	            out.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
			 delFile(filename1);
			 boolean flag = outputname.renameTo(inputname);
	}
	//only need write one line without sort
	public void WriteNavService() {
		 BufferedWriter out;
		 BufferedReader br;
		 InputStreamReader reader;
		 String filename ="navigation-service.ts";
		 String filenamebak;
		 String filepath;
		 filenamebak=filename+".bak";
		 
		 filepath="C:\\svn\\"+load+"\\ui\\web.angular2\\WebUI\\app\\services\\";
		 String filename1 = filepath+filename;
		 String filename2 = filepath+filenamebak;
		 delFile(filename2);
		 File inputname = new File(filename1); // 要读取以上路径的input。txt文件  
		 File outputname = new File(filename2); // 要读取以上路径的input。txt文件  
		 
 		String filetrim;
 		int len;
 		len=tsname.length();
 		//trim -page.ts
 		filetrim=tsname.substring(0,len-8);
 		
		  try {
			    out = new BufferedWriter(new FileWriter(outputname));
	        	String pattern = ".*"+"const callFeatureItems = \\["+ ".*";

	            reader = new InputStreamReader(new FileInputStream(inputname)); // 建立一个输入流对象reader  
	            br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言 
	            
	            String line="";
	            line = br.readLine(); // 一次读入一行数据
	            boolean isMatch=false,doFeatureSubscriptionsflag=false;
	            //first time, get sort string location
	            while (line != null) {
	               	isMatch = Pattern.matches(pattern, line);	            	
	            	if (doFeatureSubscriptionsflag)
	            	{
    	            	out.write("            '"+filetrim.toLowerCase()+"',");
    	            	out.newLine();
    	            	doFeatureSubscriptionsflag = false;
	            	}
	            	if (isMatch)
	            	{
	            		doFeatureSubscriptionsflag =true;
	            	}
	            	out.write(line);
	            	out.newLine();
	                line = br.readLine(); // 一次读入一行数据  
	            }
	            br.close();
	            out.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
			 delFile(filename1);
			 boolean flag = outputname.renameTo(inputname);
	}
	
	public void WriteNavConfig() {
		 BufferedWriter out;
		 BufferedReader br;
		 InputStreamReader reader;
		 String filename ="navigation.config.ts";
		 String filenamebak;
		 String filepath;
		 filenamebak=filename+".bak";
		 
		 filepath="C:\\svn\\"+load+"\\ui\\web.angular2\\WebUI\\app\\services\\";
		 String filename1 = filepath+filename;
		 String filename2 = filepath+filenamebak;
		 delFile(filename2);
		 File inputname = new File(filename1); // 要读取以上路径的input。txt文件  
		 File outputname = new File(filename2); // 要读取以上路径的input。txt文件  
		 
		  try {  		  
			    out = new BufferedWriter(new FileWriter(outputname));
			    //navigatestr is not string, it is variable! match varitable
	        	String pattern = ".*text: '"+navigatestr+"'.*";
	        	System.out.println("line515:"+pattern); 
	
	            reader = new InputStreamReader(new FileInputStream(inputname)); // 建立一个输入流对象reader  
	            br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言 
	            
	            String line="";
	            line = br.readLine(); // 一次读入一行数据
	            boolean isMatch=false,doFeatureSubscriptionsflag=false;
	            String tmp;
        		String filetrim;
        		int len;
        		len=tsname.length();
        		//trim -page.ts
        		filetrim=tsname.substring(0,len-8);
        		//it is the same with perl regex, it is good for get match
        		Pattern patterntrim = Pattern.compile(".*text:\\s\"(.*)\".*},");
	            //first time, get sort string location
	            while (line != null) {
	               	isMatch = Pattern.matches(pattern, line);	            	
	            	if (doFeatureSubscriptionsflag)
	            	{
	            		Matcher matcher = patterntrim.matcher(line);
	                    if(matcher.find())
	                    {
	                    //	System.out.println("line424:"+matcher.group(1));
	                        tmp=matcher.group(1);
	                    	
	                        if (tmp.compareTo(subnavigationstr)>0) 
	                        {
	        	            	out.write("            {text: \""+subnavigationstr+"\", route: '"+menustr.toLowerCase()+"/"+filetrim.toLowerCase()+"', uniq: '"+filetrim.toLowerCase()+"'},");
	        	            	out.newLine();
	        	            	doFeatureSubscriptionsflag = false;
	                        }
	                    }
	            	}
	            	if (isMatch)
	            	{
	            		doFeatureSubscriptionsflag =true;
	            	}
	            	out.write(line);
	            	out.newLine();
	                line = br.readLine(); // 一次读入一行数据  
	            }
	            br.close();
	            out.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
			 delFile(filename1);
			 boolean flag = outputname.renameTo(inputname);
	}
	
	public void WriteAutots()  {
		String tspath;
		 File inputname = new File("autots.ts"); // 要读取以上路径的input。txt文件  
		 tspath = "C:\\svn\\"+load+"\\ui\\web.angular2\\WebUI\\app\\components\\"+menustr+"\\";
		 System.out.println("line526:"+tsname);
		 delFile(tspath+tsname);
		 File outputname = new File(tspath+tsname); // 要读取以上路径的input。txt文件  	
		 try {
			 Files.copy(inputname.toPath(), outputname.toPath());
		 } catch(IOException ex){
		        System.out.println (ex.toString());
		  } 
	}
	
	//get head and label and message tips
	//en.txt include head+label+message, it is for en.txt--gstxt--ngtxt to name head and label, and translate strings
	//the aim is divide message, because message don;t need ngtxt
	//control.txt follow label
	//element.txt--en.txt(it need adjust!algorithm is 1:there is no ":" for all heads first(include subhead) ,2:when meet ":" , it means label begin,3 manual(!)  input message tips after the last label with ":", some label without ":", don;t put it as lablels begin or end
	/*for example: 
	Security Profile
	Authentication
	Administrator ID:
	User ID:
	Password must have a minimum length of 9 characters and a minimum of 4 changed characters.
	*/
	//function get head and label, and add ":" for leak ":" label
	public void makeEntxt() {
  		 String filehead ="enhead.txt";
		 String filelabel ="enlabel.txt";
		 BufferedWriter out,outctrl,outhead,outlabel;
		 BufferedReader br;
		 InputStreamReader reader;
		 String filename ="elements.txt";
		 
		 File inputname = new File(filename); // 要读取以上路径的input。txt文件  
		 File outputheadname = new File(filehead); // 要读取以上路径的input。txt文件  
		 File outputlabelname = new File(filelabel); // 要读取以上路径的input。txt文件  
		 File outputnamectrl = new File("control.txt"); // 要读取以上路径的input。txt文件  
		 File outputnamelabeltest= new File("labeltest.txt"); // 要读取以上路径的input。txt文件  
		 
		 String patternstring="", patternlabelstring="";
		 patternstring = "(.*)class=\"x-header-text x-panel-header-text x-panel-header-text-default\" unselectable=\"on\">"+title+"</span></div></div></div></div></div>(.*)";	
		 
		   //it is the same with perl regex, it is good for get match
 		  Pattern patterntrim = Pattern.compile(patternstring);
 				 
		  try {  
			  //test:
			  BufferedWriter testbw;
			  testbw = new BufferedWriter(new FileWriter(outputnamelabeltest)); 
			  
			    outhead = new BufferedWriter(new FileWriter(outputheadname));
			    outlabel = new BufferedWriter(new FileWriter(outputlabelname));
			    outctrl = new BufferedWriter(new FileWriter(outputnamectrl)); 
	
	            reader = new InputStreamReader(new FileInputStream(inputname)); // 建立一个输入流对象reader  
	            br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言       
	            String line="";
	            line = br.readLine(); // 一次读入一行数据

	    		Matcher matcher = patterntrim.matcher(line);
	    		
	    		String trimstr = null;
	    		//must first execute matcher.find ,second assign matcher,group(2)
	            if(matcher.find())
	            {
		    		//matcher.group(2) is below head strings: include all head and all label strings (between two head) 
		    		trimstr = matcher.group(2);
	            	//System.out.println("line660:"+trimstr);
		            Pattern patternlabelstr=Pattern.compile("</label>"); 
		            String[] str=patternlabelstr.split(trimstr);
	       		
		     		//patternlabelstring =".*<label id=\"(.*)-\\d+-.*\" for=.*unselectable=\\\"on\\\">(.*)";
		       	    patternlabelstring =".*<label id=\"(.*)\".*unselectable=\\\"on\\\">(.*)";
		       		Pattern patternlabel=Pattern.compile(patternlabelstring);
		       		       		
		       		outhead.write(title);
	   				outhead.write("\n");
	   				outhead.write(submenustr);
		       		for (int i = 0; i < str.length; i++) {
		       		//	System.out.println("line670:"+str[i]);
		       			testbw.write(str[i]);
		       			testbw.write("\n");
		      		    Matcher matcherlabel = patternlabel.matcher(str[i]);
			            if(matcherlabel.find())
			            {	
				       		String control= matcherlabel.group(1);
			            	String labela = matcherlabel.group(2);
			          //  	System.out.println("line681:"+labela);
			                //skip blank line or exceed line
			            	if (labela.length() == 0 ||  labela.length() >50 || labela.equals(" "))
			            	{
			            		continue;
			            	}
			            	 boolean status = labela.contains(":");
			            	 if(!status){
			            		 labela =labela +":";
			            	 }
			            	outlabel.write(labela);
			            	outlabel.write("\n");
			            	outctrl.write(control);
			            	outctrl.write("\n");
			            }
		       		  }	
		            
			            br.close();
			            outhead.close();
			            outlabel.close();
			            outctrl.close();     
			            //test
			            testbw.close();
			            //merge enhead.txt enlabel.txt into en.txt
			   		     File outputname = new File("en.txt"); // 要读取以上路径的input。txt文件    
				   		 BufferedReader brhead,brlabel;
						 InputStreamReader readerhead,readerlabel;
						 File inputhead = new File(filehead); // 要读取以上路径的input。txt文件 
						 File inputlabel = new File(filelabel); // 要读取以上路径的input。txt文件 
						
						out = new BufferedWriter(new FileWriter(outputname));
			            readerhead = new InputStreamReader(new FileInputStream(inputhead)); // 建立一个输入流对象reader  
			            readerlabel = new InputStreamReader(new FileInputStream(inputlabel)); // 建立一个输入流对象reader  
			            brhead = new BufferedReader(readerhead); // 建立一个对象，它把文件内容转成计算机能读懂的语言       
			            brlabel = new BufferedReader(readerlabel); // 建立一个对象，它把文件内容转成计算机能读懂的语言
			            
			            line = brhead.readLine(); // 一次读入一行数据
			            while (line!= null)
			            {
			            	//prevent the last line is blank
			            	out.write(line);
			            	out.write("\n");
			            	line = brhead.readLine(); // 一次读入一行数据
			            }
			            
			            line = brlabel.readLine(); // 一次读入一行数据
			            while (line!= null)
			            {
			            	out.write(line);
			             	out.write("\n");
			            	line = brlabel.readLine(); // 一次读入一行数据
			            }
			            
			            brhead.close();
			            brlabel.close();
			            out.close();  
	            }
	            else {
		            br.close();
		            outhead.close();
		            outlabel.close();
		            outctrl.close();
		            System.out.println("can't find title,please check you menu.txt");
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	}
//window write ubuntu
	public void smbWriteFile(String filename) {
        try {  
	        	String shfile=shhome+filename;
	     		 BufferedReader br;
	   		    InputStreamReader reader;
	   		   //局域网共享文件，写文件  
	            SmbFile smbFileOut = new SmbFile(shfile);  
	            if(!smbFileOut.exists())
	            	smbFileOut.createNewFile();
	            
	            SmbFileOutputStream out = new SmbFileOutputStream(smbFileOut);  
	            File inputname = new File(filename); // 要读取以上路径的input。txt文件  
		        reader = new InputStreamReader(new FileInputStream(inputname)); // 建立一个输入流对象reader  
	            br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言       
	            String line="";
	            
	            while ((line = br.readLine()) != null) {
	            	//System.out.println("line548:"+line);
	            	 out.write(line.getBytes());
	            	 out.write("\n".getBytes());
	            }
	            br.close();
	            out.close();  
	          //  smbFileOut.delete();
           }  
           catch (SmbAuthException e)  
           {  
        	   e.printStackTrace();  
           }  
           catch (IOException e)  
           {  
        	   e.printStackTrace();  
           }  
        	catch (Exception e) {  
        		e.printStackTrace();  
         }
     } 
	
	//window read file from ubuntu
		public void smbReadFile(String filename) { 
	        try {
	        	String remotename;
	        	remotename=shhome+filename;
	        	
	        	SmbFile smbFile = new  SmbFile(remotename);
	        	
	 	        int  length = smbFile.getContentLength(); // 得到文件的大小   
	 	        byte  buffer[] =  new   byte [length];  
	 	        
	 	       System.out.println("line578:"+filename+";len="+length);
	 	        SmbFileInputStream in = new  SmbFileInputStream(smbFile); 
	 				        
	 			 BufferedWriter out;
	 			 File outputname = new File(filename); // 要读取以上路径的input。txt文件  
	 			 out = new BufferedWriter(new FileWriter(outputname));
	 			delFile(filename);
	 			
		            while ((in.read(buffer)) != - 1 && (in.read(buffer)) !='\n') {
		            	String str = new String(buffer);;
		           // 	System.out.println("line609:str="+str);
		             	out.write(str);
		            // 	out.newLine();
		            }
		            in.close();
		            out.close();  
		          //  smbFileOut.delete();
	           }  
	           catch (SmbAuthException e)  
	           {  
	        	   e.printStackTrace();  
	           }  
	           catch (IOException e)  
	           {  
	        	   e.printStackTrace();  
	           }  
	        	catch (Exception e) {  
	        		e.printStackTrace();  
	         }
	     } 	
	
	
	//it run in windows 
	public void smbReadShell() {
        try {  
        	String shpath=shhome+"makgs.sh";           
        	SmbFile smbFile = new SmbFile(shpath);
            int length = smbFile.getContentLength();//得到文件的大小
            byte buffer[] = new byte[length];
            SmbFileInputStream in = new SmbFileInputStream(smbFile); //建立smb文件输入流
            while ((in.read(buffer)) != -1) {
              System.out.write(buffer);
              System.out.println(buffer.length);
            }
            in.close();
        }
        catch (Exception e) {  
            e.printStackTrace();  
       }  
  } 
	//it run in linux not window
	public void ubuntuRunScript(String filename) {
		String shfile = null;
		Process ps;
		
		shfile=localhome+filename;  	
		try {
			ps = Runtime.getRuntime().exec(shfile);
			int exitVal = ps.waitFor();
			
			BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));  
	        StringBuffer sb = new StringBuffer();  
	        String line;  
	        while ((line = br.readLine()) != null) {  
	            sb.append(line).append("\n");  
	        }  
	        String result = sb.toString();  
	        System.out.println("exitVal="+exitVal+";result="+result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	//it run in window not linux
	public void winRunScript(String filename) {
			Runtime run = Runtime.getRuntime();
			String shfile;
	        shfile=wlocalhome+filename;
			try {
   			    String[] env = new String[] { "PATH=c:\\cygwin64\\bin;%PATH%" };
   			    Process  proc = run.exec(new String[] { "c:\\\\cygwin64\\\\bin\\\\bash.exe" ,"-c",shfile},env);
   			   BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
	   			System.out.println("line825:"+filename+";shfile="+filename);
	   			String line; 
		   		 while((line = br.readLine())!= null) {
		   			 System.out.println("line893:"+line); 
		   		} 		
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	//it run in window not linux
public void winRunLinuxScript(String filename) {	
	  //connect ubuntu server
    String hostname = "10.220.225.105";  
    String username = "ubuntu";  
    String password = "Polycom17!";
    String sciptname;
    String command,commandall;
    
    command="cd "+localhome;
    commandall=command+";/home/ubuntu/fzhao/script/makets.pl";
    sciptname =localhome+filename;
    try {
        /* Create a connection instance */  
        //构造一个连接器，传入一个需要登陆的ip地址，连接服务  
        Connection conn = new Connection(hostname);  

        /* Now connect */  
        conn.connect();  

        /* Authenticate. 
         * If you get an IOException saying something like 
         * "Authentication method password not supported by the server at this stage." 
         * then please check the FAQ. 
         */  
        //用户验证，传入用户名和密码  
        boolean isAuthenticated = conn.authenticateWithPassword(username, password);  

        if (isAuthenticated == false)  
            throw new IOException("Authentication failed.");  

        /* Create a session */  
        //打开一个会话session，执行linux命令  
        Session sess = conn.openSession();
        sess.execCommand(commandall);            
        System.out.println("Here is some information about the remote host:");  

        /*  
         * This basic example does not handle stderr, which is sometimes dangerous 
         * (please read the FAQ). 
         */  
        //接收目标服务器上的控制台返回结果,输出结果。  
        InputStream stdout = new StreamGobbler(sess.getStdout());  

        BufferedReader brnet = new BufferedReader(new InputStreamReader(stdout));  

        while (true)  
        {  
            String line = brnet.readLine();  
            if (line == null)  
                break;  
            System.out.println(line);  
        }  

        /* Show exit status, if available (otherwise "null") */  
        //得到脚本运行成功与否的标志 ：0－成功 非0－失败  
        System.out.println("line160:ExitCode: " + sess.getExitStatus()+";sciptname:"+sciptname);  

        /* Close this session */  
        //关闭session和connection   
        sess.close();  

        /* Close the connection */  

        conn.close(); 	
    }catch (IOException e) {
        e.printStackTrace();
    } 
    
}
	
	//make key tag
	public void makeKeyTag()
	{
		List<String> selectList = new ArrayList();//先实例化一个
		//richardelement.txt is for richard html
		 String filename ="richardelement.txt";
		 BufferedWriter out;
		 File outputname = new File("keytag.txt"); // 要读取以上路径的output。txt文件  

		 String patternstring="",patternendstring ="", patternoptstring="", patternoptendstring="";
		 //must pattern with title
		 patternstring = ".*<select class=.*title=.*";
		 patternendstring = ".*</select>$";
		 patternoptstring = ".*<option value=.*>$";
		 patternoptendstring = ".*</option>$";
				 
 		//it is the same with perl regex, it is good for get match
 		 Pattern patternsel = Pattern.compile(patternstring);
 		 Pattern patternendsel = Pattern.compile(patternendstring);
 		 Pattern patternopt = Pattern.compile(patternoptstring);
 		 Pattern patternendopt = Pattern.compile(patternoptendstring);
 		 
 		 InputStreamReader reader = null;
 		 BufferedReader br; // 建立一个对象，它把文件内容转成计算机能读懂的语言 
 		  
		  try {  		  
	            reader = new InputStreamReader(new FileInputStream(filename)); // 建立一个输入流对象reader  
	            br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
	   		    out = new BufferedWriter(new FileWriter(outputname));
	            String line="";
	            line = br.readLine(); // 一次读入一行数据
	            String optStr="";
	            boolean  selectflag=false;
	            boolean  optflag=false;
	            
	            while (line != null) {      
		    		Matcher matcher = patternsel.matcher(line);
		    		Matcher matcherend = patternendsel.matcher(line);
		    		Matcher matcheropt = patternopt.matcher(line);
		    		Matcher matcheroptend = patternendopt.matcher(line);
		    		boolean bmatcher = matcher.find();
		    		boolean bmatcherend = matcherend.find();
		    		boolean bmatcheropt = matcheropt.find();
		    		boolean bmatcheroptend = matcheroptend.find();		    		
		    		
		    		//match select
		            if(bmatcher)
		            {
		            	selectflag = true;
		            }
		            if(bmatcherend)
		            {    //<select class ... </select>
		            	selectflag = false;	                 
		            }
		            if(bmatcheropt)
		            {    
		            	optflag = true;
		           // 	System.out.println("line962:"+line);
		            }
		            if(bmatcheroptend)
		            {    
		            	optflag = false;
		            }
		            boolean writeflag =false;
		            if(bmatcheropt==false && selectflag && optflag )
		            {
		            	//trim all space
		            	String wline = line.replaceAll(" ", "");            	
		            	if(wline.substring(0, 1).toCharArray()[0]>='A'&&wline.substring(0, 1).toCharArray()[0]<='Z')
		            	{
		            		 int size = selectList.size();//先获取list的长度
		            		 //don;t add duplication element
	            	        for(int i = 0; i < size; i++){
	            	            //从list获取数据可以通过get方法
	            	            if (wline.equals(selectList.get(i)))
	            	            {
	            	            	writeflag =true;
	            	            	break;
	            	            }
	            	        }
		            	  if (writeflag==false)
		            	  {
		            		    selectList.add(wline);
				           // 	System.out.println("line987:"+wline);
				            	out.write(wline);
				            	out.write("\n");  
		            	  }
		            	}
		            }
		            line = br.readLine(); // 一次读入一行数据
	            }
	            br.close();
	            out.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}
	//make key en string
	public void makeKeyEn() {
		 String inputfile ="localeResources.properties";
		 String filename ="keytag.txt";
		 BufferedWriter out;
		 File outputfile = new File("keyen.txt"); // 要读取以上路径的output。txt文件  

		  try {
			  out = new BufferedWriter(new FileWriter(outputfile));
			  //read file to array
			  //keytag.txt
			  Scanner sckey = new Scanner(new File(filename));
			  List<String> listlines = new ArrayList<String>();
			  while (sckey.hasNextLine()) {
				  listlines.add(sckey.nextLine());
			  }

			  String[] arr = listlines.toArray(new String[0]);
			  //localeResources.properties
			  Scanner sclocale = new Scanner(new File(inputfile));
			  List<String> listlocale = new ArrayList<String>();
			  while (sclocale.hasNextLine()) {
				  listlocale.add(sclocale.nextLine());
			  }

			  String[] arrlocale = listlocale.toArray(new String[0]);  
			  String tmpstr;
			  
			  int i=0,j=0;
			  boolean findflag=false;
	          for (i=0;i<arr.length;i++)
	          {
	        	  findflag=false;
	        	  for (j=0;j<arrlocale.length;j++)
	        	  {
	        		  tmpstr=arr[i]+"="; 
	        		  if(arrlocale[j].contains(tmpstr))
	        		  {
	        			  //System.out.println("line854:i="+i+";"+arrlocale[j]);
	        			  String[] array1 = arrlocale[j].split("=");
	        			  System.out.println(array1[1]);
	        			  out.write(array1[1]);
	        			  //the last one will add one blank line
	        			  out.write("\n");
	        			  findflag=true;
	        		  }
	        	  }
	        	  if (findflag == false)
	        	  {
	        		  out.write("NA");
        			  out.write("\n");
	        	  }
	          }
	          out.close();
	          sckey.close();
	          sclocale.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}
	
	public void Complementary() {
		String inputkey="C:\\Users\\fzhao\\eclipse-workspace\\parserouter\\namecfgstr.txt";
		String inputlabel="C:\\Users\\fzhao\\eclipse-workspace\\parserouter\\namelabel.txt";
		//the key begin number, eg: password page need from 13
		int begin=0;
        String a="",b="";
        InputStreamReader reader;
        BufferedReader   br;
        String template;
        BufferedWriter out;
		 File outputname = new File("Complementary.txt"); // 要读取以上路径的output。txt文件  
		
        try {
   		 out = new BufferedWriter(new FileWriter(outputname));	
		 File inputlabelname = new File(inputlabel); // 要读取以上路径的input。txt文件  
		 reader = new InputStreamReader(new FileInputStream(inputlabelname)); // 建立一个输入流对象reader  
		 br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言            
            a = br.readLine(); // 一次读入一行数据
            br.close();
            
   		 File inputkeyname = new File(inputlabel); // 要读取以上路径的input。txt文件  
   		 reader = new InputStreamReader(new FileInputStream(inputkeyname)); // 建立一个输入流对象reader  
   		   br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言            
            b = br.readLine(); // 一次读入一行数据
 
            while (b != null) {
                template = "new PFormConfigItem({"
                		+				 "label: this."+a+","
                		+                "key: this."+b+","
                		+                "fieldType: PFormFieldType."
                		+		       "}),";
	       		 out.write(b);
	    		 out.write("\n"); 
                b = br.readLine();
            }
            br.close();
		  } catch (IOException e) {
            e.printStackTrace();
		  }
	}
	
	public void  delAllFile() {
		String prefix="C:\\svn\\"+load+"\\ui\\web.angular2\\WebUI\\app\\";
		String approutepath=prefix+"components\\app\\";
		String approutename="app-routing.module.ts";
		String dashboardhtmlpath=prefix+"components\\dashboard\\";
		String dashboardhtmlname="dashboard.html";
		String dashboardtspath=prefix+"components\\dashboard\\";
		String dashboardtsname="dashboard.ts";
		String tspath=prefix+"components\\"+menustr+"\\";
		String tsname=submenustr+"-page.ts";
		String adminmodulepath=prefix+"components\\";
		String adminmodulename="admin-pages.module.ts";
		String navservicepath=prefix+"\\services\\";
		String navservicename="navigation-service.ts";
		String navconfigpath =prefix+"\\services\\";
		String navconfigname ="navigation.config.ts";
		String stringpath ="C:\\svn\\"+load+"\\ui\\web.angular2\\WebUI\\assets\\locale\\";
		String stringname ="strings.txt";
		String filename;
		
		List<String> arrlist=new ArrayList<String>();
		arrlist.add(approutepath+approutename);
		arrlist.add(dashboardhtmlpath+dashboardhtmlname);
	//	arrlist.add(dashboardtspath+dashboardtsname);
		arrlist.add(tspath+tsname);
		arrlist.add(adminmodulepath+adminmodulename);
		arrlist.add(navservicepath+navservicename);
		arrlist.add(navconfigpath+navconfigname);
		arrlist.add(stringpath+stringname);
				
		System.out.println("line996:"+stringpath+stringname);
		for(int i = 0;i<arrlist.size(); i++){
			filename=arrlist.get(i);
			delFile(filename);
		}
	}
	
    public static boolean isLinux(){  
        return OS.indexOf("linux")>=0;  
    }  
    
    public static boolean isWindows(){  
        return OS.indexOf("windows")>=0;  
    }  
    
    public boolean delFile(String filename)
    {
		 //remove local file first
	       try{
	            File file = new File(filename);
	            if(file.delete()){
	                System.out.println(file.getName() + " 文件已被删除！");
	            }else{
	                System.out.println("文件删除失败！");
	            }
	        }catch(Exception e){
	            e.printStackTrace();
	        }
           return true;
    }
    
    private String load="pctc-en70854-fzhao";
	private String menustr="";
	private String submenustr="";
	private String title="";
	private String classstr="";
	private String tsname;
	private String navigatestr="";
	private String subnavigationstr="";
	private static String OS = System.getProperty("os.name").toLowerCase();
	private String localhome="/home/ubuntu/fzhao/script/";
	private String wlocalhome="/cygdrive/c/Users/fzhao/eclipse-workspace/parserouter/";
	public String shhome="smb://ubuntu:Polycom17!@10.220.225.105/share/fzhao/script/";
		//the last item
	private String lastpath,componentstr;
	private String[] pathmenu= new String[] {"general","network","call-configuration","security","servers","diagnostics"};
	private String[] dashmenu= new String[] {"GENERAL_SETTINGS_NAV","NETWORK_SETTINGS_NAV","SECURITY_NAV","DIAGNOSTICS_NAV"};
	//if there is new Navigation item, add it in array; name must use space to split ,not other char ,include /,- etc
	private String[] navmenu = new String[] {"GENERAL_SETTINGS_NAV","NETWORK_SETTINGS_NAV","CALL_CONFIGURATION_NAV","SECURITY_NAV","SERVERS_NAV","DIAGNOSTICS_NAV"};
		
	
	public static void main(String[] args)  {
		// TODO Auto-generated method stub
		AddRouter p= new AddRouter();
		if (isWindows())
		{		
			//run step 1
			/*make en.txt,control.txt*/
//  			p.makeEntxt();
//  			//after add message/tips in en.txt ,sync it to ubuntu
//				p.smbWriteFile("en.txt");
//                p.smbWriteFile("menu.txt");
//			    p.smbWriteFile("elements.txt");
////	  			p.smbWriteFile("control.txt"); 
//	    	//run step2
//				p.winRunLinuxScript("makets.pl");
//                //run step 3
//			    /*below txt in linux;if message is NA, it is wrong, because message must can find*/
//			   //cfgraw.txt need manually add or join, because it can;t get all network one time for many subhead; eg: global security			
//		    /*en.txt,gs.txt,ng.txt,namecfgstr.txt,namekey.txt; when name finish ,begin do webpage for first time*/
//			//st1:run nameboth.pl; st2: when gs.txt have NA,manual find string to add in gs.txt; st3: run name.pl; st4: autots.pl
//				p.smbReadFile("gs.txt");
//				p.smbReadFile("gsng.txt");
//			    p.smbReadFile("ng.txt");
//		    	p.smbReadFile("gsng.txt");
//		    	p.smbReadFile("namelabel.txt");
//		    	p.smbReadFile("namekey.txt");
//		     	p.smbReadFile("namecfgstr.txt");
//		    	p.smbReadFile("autots.ts");
			    p.WriteAutots();
//			    //write files
//			    //step4:it is first ,because it made classstr
// 				p.ParseRouterJson();
// 				//router depend parserouter to get lastpath
//				p.WriteRouter();
//				p.WriteDashboard();
//				p.WriteNavService();
//				p.WriteNavConfig();
//				p.WriteAdminPage();
			   //step5 richard web start
				/*step6: after richard web start, then run template.pl in linux*/
			 //enhance don;t add have translated string
			//	p.winRunLinuxScript("template.pl");
				//step7 translate string
//			    p.makeKeyTag();
////			    //have bug ,use makey.sh in linux to run
//			    p.makeKeyEn();
//			    p.smbWriteFile("keytag.txt");
//			    p.smbWriteFile("keyen.txt");
			    //step6 :read template.txt
			//    p.smbReadFile("template.txt");
			    //step7:strings.txt manully  sortstring.sh
				//windows call shell
//    		    p.smbReadFile("namehlabelmsg.txt");
//			    p.winRunScript("sortstring.sh");
  				//step8 Complementary; if one line have more than one controller, controller will more than label
  			//	p.Complementary();
		}
		else if (isLinux())
		{
			//if it is linux ,run below function
				p.ubuntuRunScript("template.pl");
		}
	}
}

//json is {...} eg: {name:“abc”,options:"h323"}
//a json string is a class
class Obj {
	//!!json array map to java list. obj variable is json KEY. Type is List; sometime is String. through key to get value
	//list[0] is MyField
	List<MyField> Routes;

	public List<MyField> getRoutes() {
		return Routes;
	}

	public void setRoutes(List<MyField> Routes) {
		this.Routes = Routes;
	}

}
//!!one json string is one class {...} ;eg: "Routes" : [ {...},{...},,]
class MyField {
	private List<ChildPath> children;
	private String path;
	private String component;
	//it is json format; [{ConferencingGuard: ''}]
	//private String canActivate;
	private String redirectTo;
	
	public MyField() {
	}
	
	public MyField(String path, List<ChildPath> children) {
		super();
		this.children = children;
		this.path = path;
	}

	public List<ChildPath> getChildren() {
		return children;
	}

	public void setChildren(List<ChildPath> children) {
		this.children = children;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
//"configVar" : {...}
class ChildPath {
	//ConfigVar variable is KEY
	private String path;
	private String redirectTo;
	private String pathMatch;
	private String component;
	
	public ChildPath(String path,String redirectTo,String pathMatch) {
		super();
		// TODO Auto-generated constructor stub
		this.path=path;
		this.redirectTo=redirectTo;
		this.pathMatch=pathMatch;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getRedirectTo() {
		return redirectTo;
	}

	public void setRedirectTo(String redirectTo) {
		this.redirectTo = redirectTo;
	}

	public String getPathMatch() {
		return pathMatch;
	}

	public void setPathMatch(String pathMatch) {
		this.pathMatch = pathMatch;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

}
