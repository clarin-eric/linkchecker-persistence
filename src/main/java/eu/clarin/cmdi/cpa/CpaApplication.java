package eu.clarin.cmdi.cpa;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import eu.clarin.cmdi.cpa.repositories.UrlRepository;
import eu.clarin.cmdi.cpa.services.LinkService;

@SpringBootApplication
public class CpaApplication {
   

	public static void main(String[] args) {
		SpringApplication.run(CpaApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner runnner(UrlRepository uRep) {
	   
	   return args -> {
	      //service.save("http://wowasa.com", "origin", "providerGroup", "expectedMimeType", "source");
	      
	      //uRep.findAllUrlContextContextByProviderGroupName("providerGroup");
	   };

	}

}
