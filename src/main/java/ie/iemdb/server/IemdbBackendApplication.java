package ie.iemdb.server;

import ie.iemdb.exception.CustomException;
import ie.iemdb.model.Actor;
import ie.iemdb.repository.ActorRepo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "ie.iemdb.service")
public class IemdbBackendApplication {
	public static void main(String[] args) throws CustomException {
		ActorRepo.getInstance().addElement(new Actor("1", "a", "1999-09-09", "hello", "sldkjf"));
		SpringApplication.run(IemdbBackendApplication.class, args);
	}

}
