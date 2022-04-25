package ie.iemdb.server;

import ie.iemdb.exception.CustomException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "ie.iemdb.service")
public class IemdbBackendApplication {
	public static void main(String[] args) throws CustomException {
//		MovieRepo.getInstance().addElement(new Movie("1", "name", "summary", "director", new ArrayList<>(), "1999-10-10", new ArrayList<>(), new ArrayList<>(), 1, 2, 1.1, "lskadf", "lskdjf"));
//		MovieRepo.getInstance().addElement(new Movie("2", "nameeee", "summary", "director", new ArrayList<>(), "1999-10-10", new ArrayList<>(), new ArrayList<>(), 1, 2, 1.1, "lskadf", "lskdjf"));
		SpringApplication.run(IemdbBackendApplication.class, args);
	}

}