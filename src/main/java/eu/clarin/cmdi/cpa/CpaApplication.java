package eu.clarin.cmdi.cpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import eu.clarin.cmdi.cpa.repositories.UrlRepository;

@SpringBootApplication
public class CpaApplication {
   
   @Autowired
   private UrlRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(CpaApplication.class, args);
	}
	

	public void run(String... args) {

	      System.out.println(repository.findById(1L));

	}

}
