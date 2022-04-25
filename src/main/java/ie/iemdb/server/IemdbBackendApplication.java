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
		SpringApplication.run(IemdbBackendApplication.class, args);
	}

}
