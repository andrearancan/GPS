package io.swagger.api;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiParam;
import smile.stat.distribution.MultivariateGaussianDistribution;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2019-06-25T10:41:00.449Z")

@Controller
public class CalculatorApiController implements CalculatorApi {

    private static final Logger log = LoggerFactory.getLogger(CalculatorApiController.class);

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public CalculatorApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.request = request;
    }
    public static ArrayList<MultivariateGaussianDistribution>gaussList=new ArrayList<MultivariateGaussianDistribution>();
    public static double media;
    public static Process rt;
    public ResponseEntity<String> compute(
    		
    		@ApiParam(value = "latitudine",required=true)  @PathVariable("latitude") String latitude,
    		@ApiParam(value = "longitudine",required=true)  @PathVariable("longitude") String longitude,
    		@ApiParam(value="altitudine",required=true) @PathVariable( "altitude") String altitude) 
    
    {
    	
        String accept = request.getHeader("Accept");        
        if (accept != null && accept.contains("")) {
            /*try { 
            	
            	download(Double.parseDouble(latitude),Double.parseDouble(longitude));
            	executeMaperitive();
                extract();
                System.out.println("Completato!");
                File directory = new File("output");
                File[] files = directory.listFiles();
                for (File f : files)
                f.delete();
                */
                String output=latitude + ";" + longitude + ";" + altitude;
                
                return new ResponseEntity<String>(output, HttpStatus.OK);
                
           /* } catch (IOException e) {
                log.error("Couldn't serialize response for content type ", e);
                return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
            } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
        }

        return new ResponseEntity<String>(HttpStatus.NOT_IMPLEMENTED);
    }
    
   /*
    public static void GaussCreate(double x, double y,double varianza)
    {
   	 MultivariateGaussianDistribution mnd=null;
   	 try
   		{
   		 double means[]= new double[2];
   		 means[0]=x;
   		 means[1]=y;		 
   		
   		 mnd=new MultivariateGaussianDistribution(means, varianza);
   		 gaussList.add(mnd);
   		}
   		catch(Exception e)
   		{
   			System.out.println(e.getMessage());
   		}
   	 
    }
   	public static void calcolo(double varianza, String numero) throws IOException
       {
   		
   		System.out.println("calcolo");		
          
           
           FileReader f=new FileReader("output/output.svg");
           BufferedReader b;
           b=new BufferedReader(f);
           String s=b.readLine();
           
            List<String> list;
             Double[] partsD =null;
             
          while(s!=null)
          {
              if(s.contains("<symbol id=\""+numero))
              {
                  
               s=b.readLine();             
                       
              if(s.contains("<path d=\"") )
               {
                                                         
                s=s.replace(" l", " ");
                s=s.replace("<path d=\""+"M", "");
                s=s.replace("\""+" />", "");                            
               String[] parts = s.split(" ");
                list=new ArrayList<String>();
                for(int i=0; i<parts.length;i++)
                                   { 
                                    
                                        list.add(parts[i]); 
                                   
                                   }
                               for(int i =0;i<list.size();i++)
                               {
                                   if(list.get(i).contains("h"))
                                   {
                                       String n=list.get(i-1);
                                       String correct=list.get(i);
                                       correct=correct.replace(" h", " ");                                    
                                       list.set(i, correct);
                                       list.add(i, n);
                                   }
                                   if(list.get(i).contains("v"))
                                   {
                                       String n=list.get(i-2);
                                       String correct=list.get(i);
                                       correct=correct.replace(" v", " ");                                    
                                       list.set(i, correct);
                                       list.add(i, n);
                                   }
                                   if(list.get(i).contains("Z"))
                                   {
                                       String correct=list.get(i);
                                       correct=correct.replace("Z", list.get(0));  
                                       list.set(i, correct);
                                       list.add(list.get(1));
                                   
                                   }
                               }
                              
                               partsD=new Double[list.size()];
                               for(int i=0; i<list.size();i++)
                               {     
                                   
                                   try
                                   {                                   
                                       partsD[i]=Double.parseDouble(list.get(i));
                                       
                                   }
                                   catch(NumberFormatException e)
                                   {
                                       System.out.print(e.getMessage());
                                   }
                               } 
                               
                               
                               for(int i=0; i<partsD.length;i=i+2)
                               {         
                               	
                                   if(i+2<partsD.length) 
                                   {
                                   	
                                   	double x=partsD[i];
                                   	double y=partsD[i+1];
                                   	
   									if(partsD[i]<partsD[i+2] && partsD[i+1]>partsD[i+3])
   									{
   										double segx=(partsD[i+2]-partsD[i])/varianza*2;
   										double segy=(partsD[i+1]-partsD[i+3])/varianza*2;
   										
   										while(x<partsD[i+2] || y>partsD[i+1])
   	                                	{	 
   									
   											x=x+segx;
   											y=y-segy;                               		
   	                                    	x = x*1000;
   	    									x = Math.round(x);
   	    									x = x /1000;
   	    									y = y*1000;
   	    									y = Math.round(y);
   	    									y = y /1000;
   	    									
   	    									GaussCreate(x,y,varianza);	                                    	
   	                                        
   	                                	}
   										x=0;
   										 y=0;									}
   									else if(partsD[i]<partsD[i+2] && partsD[i+1]<partsD[i+3])
   									{
   										double segx=(partsD[i+2]-partsD[i])/varianza*2;
   										double segy=(partsD[i+3]-partsD[i+1])/varianza*2;
   										while(x< partsD[i+2] || y<partsD[i+3])
   	                                	{	
   										
   											x=x+segx;
   											y=y+segy;                               		
   	                                    	x = x*1000;
   	    									x = Math.round(x);
   	    									x = x /1000;
   	    									y = y*1000;
   	    									y = Math.round(y);
   	    									y = y /1000;
   	    									
   	    									GaussCreate(x,y,varianza);
   	    									
   	                                	}
   										x=0;
   										 y=0;
   									}
   									else if(partsD[i]>partsD[i+2] && partsD[i+1]>partsD[i+3])
   									{
   										double segx=(partsD[i]-partsD[i+2])/(varianza*2);
   										double segy=(partsD[i+1]-partsD[i+3])/(varianza*2);
   										
   										while(x>partsD[i+2] || y>partsD[i+3])
   	                                	{	
   																						
   											x=x-segx;
   											y=y-segy; 
   											
   	                                    	x = x*1000;
   	    									x = Math.round(x);
   	    									x = x /1000;
   	    									y = y*1000;
   	    									y = Math.round(y);
   	    									y = y /1000;
   	    									
   	    									GaussCreate(x,y,varianza);
   	                                    	
   	                                	}
   										x=0;
   										 y=0;
   									}
   									else if(partsD[i]>partsD[i+2] && partsD[i+1]<partsD[i+3])
   									{
   										double segx=(partsD[i]-partsD[i+2])/varianza*2;
   										double segy=(partsD[i+3]-partsD[i+1])/varianza*2;
   										while(x>partsD[i] || y<partsD[i+3])
   	                                	{	 
   					
   											x=x-segx;
   											y=y+segy;                               		
   	                                    	x = x*1000;
   	    									x = Math.round(x);
   	    									x = x /1000;
   	    									y = y*1000;
   	    									y = Math.round(y);
   	    									y = y /1000;
   	    									
   	    									GaussCreate(x,y,varianza);
   	                                    	
   	                                	}
   										
   										x=0;
   										 y=0;
   									}
                                   	
   									
                                       
                                   	}
                                        
                                   
                             
                              }
               }
              }
               s=b.readLine();   
          }
         b.close();
         
       }
       @SuppressWarnings("resource")
   	public static void download(double lat,double lon)
       {
            System.out.println("Download mappa...");
                     
           double vicinityRange=0.001;
           DecimalFormat format = new DecimalFormat("##0.000", DecimalFormatSymbols.getInstance(Locale.ENGLISH)); 
   		String left = format.format(lat - vicinityRange);                
   		String bottom = format.format(lon - vicinityRange);                
   		String right = format.format(lat + vicinityRange);                
   		String top = format.format(lon + vicinityRange);
                                  
                   
   		String FILE_URL = "https://api.openstreetmap.org/api/0.6/map?bbox="+bottom+","+left+","+top+","+right;
                   
           try
           { 
               BufferedInputStream in = new BufferedInputStream(new URL(FILE_URL).openStream());
           
               FileOutputStream fileOutputStream;
               fileOutputStream=new FileOutputStream("Maperitive/Mappe/tile.osm");
               byte dataBuffer[] = new byte[1024];
               int bytesRead;
               while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) 
               {
                   fileOutputStream.write(dataBuffer, 0, bytesRead);
               }
               
           }
           catch (IOException e) {
               System.out.println(e);

           }
           
       }
       public static void executeMaperitive() throws IOException, InterruptedException    
       {
       	System.out.println("Conversione...");
           rt= Runtime.getRuntime().exec("Maperitive/Maperitive.exe");
           rt.waitFor();       
       }
   	public static void extract()throws IOException
       {
       	
           System.out.println("Estrazione dati...");
           String numero="";
           double varianza=0;            

           
           FileReader f=new FileReader("output/output.svg");
           BufferedReader b;
           b=new BufferedReader(f);
           String s=b.readLine();   
           while(s!=null)
          {            
           	
                   if(s.contains("<g id=\"Line_artwork") )
                   {  
                                          
                       while(s!=null)
                       {
                       	
                       
                       
                       if(s.contains("<g id=\""+"highway") && !s.contains("border"))
                       {    
                           String type=s;   
                           Pattern p = Pattern.compile("h:(.*?)\\\"");
                           Matcher m = p.matcher(s);
                           if(m.find()) 
                           {
                           varianza=Double.parseDouble((m.group().subSequence(2, m.group().length()-1)).toString())/2;
                           }
                           else
                           {
                           varianza=0;
                           }                                    
                       
                           s=b.readLine();
                           if(s.contains("<use xlink:href=\""))
                           {
                               String[] id=s.split("\"");
                               id[1]=id[1].replace("#", "");
                               numero=id[1];
                               calcolo(varianza,numero);
                           }
                           
                            
                          
                       }
                                     
                       s=b.readLine(); 
                   }
                      
                   }
                   s=b.readLine();      
          }
           b.close();
       } 
   	public String filter(double lat, double lon,MultivariateGaussianDistribution gaussList )
   	{
   		return "";
   	}
      */
}
   

