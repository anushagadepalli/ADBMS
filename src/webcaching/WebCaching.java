/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcaching;

/**
 *
 * @author Naresh
 */
import java.awt.Desktop;
import java.io.BufferedReader;  
import java.io.BufferedWriter;  
import java.io.File;  
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;  
import java.io.IOException;  
import java.io.InputStreamReader;  
import java.net.MalformedURLException;  
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;  
import java.net.URLConnection;  
import java.util.List;
import java.util.Map;
public class WebCaching {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
   
    try{
    //taking the URL from user
    BufferedReader brx = new BufferedReader(new InputStreamReader(System.in));
    String GivenURL;  
    //System.out.println("Enter the URL: ");
    //GivenURL = br.readLine();
    while(true){
        System.out.println("Enter the URL:(Type EXIT to quit)");
        GivenURL=brx.readLine();
        if(GivenURL.equals("EXIT")){
            break;
        }
      
   //display header details and get LastModified value
   String LastModified=ResponseHeaderUtil(GivenURL);  
    
   //check in cache index wheather given url is there and verify last modified details
   
   boolean found=CheckCache(GivenURL,LastModified);
   System.out.println("found: "+ found);
   //
   if(found){
       //open from cache;
       openWebpage(new URL("file:///C:/Users/Naresh/ADBMSWebCache/test.html"));
   }
   else{
       //openWebpage(new URL("http://www.javaandj2eetutor.blogspot.com"));
       openWebpage(new URL(GivenURL));
       //access original url 
       //save this page to cache
       //update in index file
       //add url and last modified to index file   
       savePage(GivenURL); 
       appendToCheckbook(GivenURL+";"+LastModified);
        
   }
   
   
   
          
   
   //passing that url and downloading that page
    }
    
    }
    catch(Exception e){
        e.printStackTrace();
    }
  
  //access url and save it       
  /*     
  
  //display saved webpage
  try{
  //openWebpage(new URL("http://www.javaandj2eetutor.blogspot.com"));
  openWebpage(new URL("file:///C:/Users/Naresh/test.html"));
  //openWebpage(new URL("www.google.com"));
  }
    catch(Exception e){
        e.printStackTrace();
    }*/
  //sample header response
  //ResponseHeaderUtil();
  
  }
    public static void openWebpage(URI uri) {
    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
        try {
            desktop.browse(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

public static void openWebpage(URL url) {
    try {
        openWebpage(url.toURI());
    } catch (URISyntaxException e) {
        e.printStackTrace();
    }
}
public static String ResponseHeaderUtil(String GivenURL){
     try {

	URL obj = new URL(GivenURL);
	URLConnection conn = obj.openConnection();
	Map<String, List<String>> map = conn.getHeaderFields();

	System.out.println("Printing Response Header...\n");

	for (Map.Entry<String, List<String>> entry : map.entrySet()) {
		System.out.println("Key : " + entry.getKey() 
                           + " ,Value : " + entry.getValue());
	}

	System.out.println("\nGet Response Header By Key ...\n");
	String server = conn.getHeaderField("Server");

	if (server == null) {
		System.out.println("Key 'Server' is not found!");
	} else {
		System.out.println("Server - " + server);
	}
        
        String LastModified = conn.getHeaderField("Last-Modified");

	if (LastModified == null) {
		System.out.println("Key 'Last-Modified' is not found!");
	} else {
		System.out.println("LastModified - " + LastModified);
                return LastModified;
	}
        //return "null";
	//System.out.println("\n Done");

    } catch (Exception e) {
	e.printStackTrace();
    }
    return "null";
}
public static void appendToCheckbook (String s) {
 
      BufferedWriter bw = null;
 
      try {
         // APPEND MODE SET HERE
         bw = new BufferedWriter(new FileWriter("/users/Naresh/ADBMSWebCache/index.txt", true));
     bw.write(s);
     bw.newLine();
     bw.flush();
      } catch (IOException ioe) {
     ioe.printStackTrace();
      } finally {                       // always close the file
     if (bw != null) try {
        bw.close();
     } catch (IOException ioe2) {
        // just ignore it
     }
      } // end try/catch/finally
 
   } // end test()

    public static boolean CheckCache(String GivenURL,String LastModified){
    // The name of the file to open.
        String fileName = "/users/Naresh/ADBMSWebCache/index.txt";

        // This will reference one line at a time
        String line = null;

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = 
                new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = 
                new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                String attributeNames[] = line.split(";");
                if(attributeNames[0].equals(GivenURL)&&attributeNames[1].equals(LastModified))
                {
                    return true;
                }
                System.out.println(attributeNames[0]+" lmmm "+attributeNames[1]);
                
                System.out.println(line);
            }   

            // Always close files.
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");                  
            // Or we could just do this: 
            // ex.printStackTrace();
        }
        return false;
    }
    public static void savePage(String GivenURL){
          URL url;       
  try {  
   // enter any url to get its content  
   url = new URL(GivenURL);  
   URLConnection conn = url.openConnection();  
  
   // open the stream and put it into BufferedReader  
   BufferedReader br = new BufferedReader(new InputStreamReader(  
     conn.getInputStream()));  
   String inputLine;  
  
   // save it anywhere in local machine for offline use  
   String fileName = "/users/Naresh/test.html";  
   File file = new File(fileName);  
   if (!file.exists()) {  
    file.createNewFile();  
   }  
  
   FileWriter fw = new FileWriter(file.getAbsoluteFile());  
   BufferedWriter bw = new BufferedWriter(fw);  
  
   while ((inputLine = br.readLine()) != null) {  
    bw.write(inputLine);  
   }  
  
   bw.close();  
   br.close();  
  
   System.out.println("Your file is saved in " + fileName  
     + " location.");  
  
  } catch (MalformedURLException e) {  
   e.printStackTrace();  
  } catch (IOException e) {  
   e.printStackTrace();  
  }
    }
}
