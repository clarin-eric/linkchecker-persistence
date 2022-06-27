package eu.clarin.cmdi.cpa.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import lombok.Data;

public class UrlValidator {
   
   private final static List<String> VALID_PROTOCOLS = Arrays.asList("http", "https", "ftp");
   
   public static ValidationResult validate(String urlString) {
      
      try{
         
         URL url = new URL(urlString); 
         
         String host = url.getHost();
         
         if(host == null || host.length() < 3 || host.contains("localhost") || host.contains("127.0.0")) {
            return new ValidationResult(false, host, "invalid host"); 
         }
         
         if(!VALID_PROTOCOLS.contains(url.getProtocol())){
            return new ValidationResult(false, host, "invalid protocol"); 
         }
         
         return new ValidationResult(true, host, "ok");
      }
      catch(MalformedURLException ex) {
         return new ValidationResult(false, null, "malformed URL");
      }     
   }
   
   @Data
   public static class ValidationResult{
      
      private final boolean isValid;
      private final String message;
      private final String host;
      
      
   }
   
   

}
