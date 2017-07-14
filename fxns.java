import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class fxns {
	   double percent;
	Scanner inputscanner =new Scanner(System.in);
	
	private double percentage(long no, long total)			//percent function
	{
		double per = (no*100.0)/total; 

		//Logic to get only two places after decimal (for Larger upLimits)
		per = per * 100;		//Move the 2 digits from right side of decimal to left side
        per = Math.round(per);	//Drop the rest of decimal part
        per = per/100;			//Put the Moved 2 decimals back to their place
        return per;
	}
	
	void encrypt()			//encrypt function
	{
		try{

        	//Get Filename from USER to be Encrypted
			boolean ans1;	
			String filename;
		do
		{	
			System.out.println("Enter Name of the File to be Encrypted:(include path if outside)");
			filename = inputscanner.next();
	        File filechk = new File(filename);
	        ans1=!filechk.isFile();				//check if it is valid FILE
	        if(ans1) System.out.println("'"+filename+"'"+" is not a Valid FILE!");	        
		}while(ans1);
            
      		//Get Directory name where Encrypted file will be saved
			boolean ans;	//To Store value of !dirchk.isDirectory()
			String dirname;
		do
		{			
			
			System.out.println("Enter Name of Directory where Encrypted file will be Stored:");
			dirname = inputscanner.next();
	        File dirchk = new File(dirname);
	        ans=!dirchk.isDirectory();		//check if it is valid DIRECTORY
	        if(ans) System.out.println("'"+dirname+"'"+" is not a Valid Directory!");
		}while(ans);   
	       
	       
		
			RandomAccessFile in = new RandomAccessFile(filename, "rw");  //input file to be encrypted 
	    	RandomAccessFile temp = new RandomAccessFile("tmp.txt", "rw"); //use as intermediate, to hold the file XOR-ed with key
	    	RandomAccessFile out = new RandomAccessFile(dirname+"/enc.txt", "rw"); // used to hold the shuffled the XOR-ed file

	    	long incount=in.length();		//length() returns long    
	    	System.out.println("No of characters in file:"+incount);
	    	
	    	
	    	//Get Desired Private Key from USER
	    	System.out.println("Enter Your Key to encrypt the file(REMEMBER it for Decryption): ");
			String key=inputscanner.next();
	
				//Calculating Sum of the Key
			int len = key.length();
			//System.out.println("The Length of key is:"+len);
			int sum= 0;
			for(int i=0; i<=len-1;i++)
			{
				int k=key.charAt(i);
				sum = sum + k;		//Getting sum of the key =>charAt() returns integer value
			}

			//Encrypt using key
			int chr=0;	
			for(int j=0; j<=incount-1;j++)
			{
				chr=in.read();				//read from input file
				temp.write(chr^sum);		//XOR the char and write to TEMP file
			}
			
			long count=temp.length();		

			//APPLLY SHUFFLING
	    
		   int p=0;
		   double percent;
	    	for(long i=count-1;i>=0;i--)	
			{
	    	
	    		    //Writing on file>>
	    		 temp.seek(i);		//set cursor of TEMP file at last >> i=count-1
				 out.seek(p);		//set cursor of output file to start >> p=0
				int ch =temp.read(); //read() from TEMP
				out.write(ch);		//write it at start of output file				
				 p=p+1;				//increment p
				 

					percent=percentage(p,count);
		    		System.out.println("Encrypting characters to File:"+percent + "%");
		    	
			}
	    	
	    	 System.out.println("\nFile ENCRYPTED Successfully as 'enc.txt', Stored at"+"'"+dirname+"'");
	    
	     
	    	//delete the TEMP file
	    	 File tmp = new File("tmp.txt");	
	    	 tmp.delete();	
	    
	    	 //Release resources
	    	 in.close();
	    	 out.close();
	    	 temp.close();
	    	
	    	}
    	
	catch ( IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	
    }
		
	
	
	void decrypt() //decrypt fxn
	{
		try{
			//Get Filename from USER to be Decrypted
			boolean ans1;	
			String filename;
		do
		{			
			
			System.out.println("Enter Name of the Encrypted File that is to be Decrypted(include path if outside):");
			filename = inputscanner.next();
	        File filechk = new File(filename);
	        ans1=!filechk.isFile();		//check if it is valid FILE
	        if(ans1) System.out.println("'"+filename+"'"+" is not a Valid FILE!");
		}while(ans1);
    		
		
    			 //Get Original Extension for Decryption
			System.out.println("Enter EXTENSION to which file is to be Decrypted(e.g txt,pdf,jpg,mp3,mp4,etc):");
		    String extname = inputscanner.next();
		    extname=extname.substring(extname.lastIndexOf(".") + 1);	//if user provided a '.' with extension
   
	 		//Get Directory name where Decrypted file will be saved
				boolean ans;	//To Store value of !dirchk.isDirectory()
				String dirname;
			do
			{			
				System.out.println("Enter Name of Directory where Decrypted file will be Stored:");
				dirname = inputscanner.next();
		        File dirchk = new File(dirname);
		        ans=!dirchk.isDirectory();	//check if it is valid DIRECTORY	
		        if(ans) System.out.println("'"+dirname+"'"+" is not a Valid Directory. Check Again!");
			}while(ans);   
		       
		    
    		RandomAccessFile in = new RandomAccessFile(filename, "rw");
	    	RandomAccessFile temp = new RandomAccessFile(dirname+"/dtmp.txt", "rw");
	    	RandomAccessFile out = new RandomAccessFile(dirname+"/dec."+extname, "rw");

	    	long incount=in.length();			//length() returns long
	    	System.out.println("No of characters in file:"+incount);
	    		
	    	
	    		//Get Unique Private Key from USER to decrypt
			System.out.println("Enter Your DECRYPTION KEY to decrypt the file: ");
			String key=inputscanner.next();
			
			//Calculating Sum of the Key
			
			int len = key.length();
			//System.out.println("The Length of key is:"+len);
			int sum= 0;
			for(int i=0; i<=len-1;i++)
			{
				int k=key.charAt(i);	
				sum = sum + k;		
			}

			//DEcrypt using key
			int chr=0;	
			for(int j=0; j<=incount-1;j++)
			{
				chr=in.read();
				temp.write(chr^sum);		//Same XOR(^) to Decrypt
			}
			long count=temp.length(); 

			//APLLY DESHUFFLING
		    
			   int p=0;
		    	for(long i=count-1;i>=0;i--)	//Reverse Loop
				{
		    		    //Writing on file>>
		    		 temp.seek(i);		//set cursor of TEMP file at last >> i=count-1
					 out.seek(p);		//set cursor of output file to start >> p=0
					int ch =temp.read(); //read() from TEMP
					out.write(ch);		//write it at start of output file
					
					 p=p+1;		
					 
						percent=percentage(p,count);
					    System.out.println("Decrypting characters to File:"+percent+ "%");
			    		
				}
		    	
		    	
			
	    	 System.out.println("\nFile DECRYPTED Successfully as dec."+extname+", Stored at "+"'"+dirname+"'");
	    	 	
	    	 File f1 = new File(dirname+"/dtmp.txt");	//delete the temporary file
	    	 File f2 = new File(dirname+"/enc.txt");	//DELETE THE ENCRYPTED FILE AFTER DECRYPTION
	    	 if(f1.delete()&&f2.delete())
	    	 {
	    		 System.out.println("Useless Temporary files deleted to save Memory!");
	    	 }
	    	 else 
	    	 {
	    		 System.out.println("\nCouldn't Locate Temp files to delete!");
	    	 }

	    	 //release resources
	    	 in.close();
	    	 out.close();
	    	 temp.close();
	    	
	      	    
	    	}
 
catch ( IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
	}
	
	

}