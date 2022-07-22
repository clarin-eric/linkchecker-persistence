package eu.clarin.cmdi.cpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



/**
 * @author WolfgangWalter Sauer (wowasa)
 * 
 * the purpose of the class is only to have a SpringBootApplication annotated class, which is necessary to execute all the automatic stuff
 * like loading the configuration, executing the schema.sql, etc.
 *
 */
@SpringBootApplication
public class CpaMockApplication {
   

	public static void main(String[] args) {
		SpringApplication.run(CpaMockApplication.class, args);
	}
}
