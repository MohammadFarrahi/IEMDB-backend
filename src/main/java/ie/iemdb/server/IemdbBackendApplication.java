package ie.iemdb.server;
import ie.iemdb.apiConsumer.ActorAPIConsumer;
import ie.iemdb.apiConsumer.CommentAPIConsumer;
import ie.iemdb.apiConsumer.MovieAPIConsumer;
import ie.iemdb.apiConsumer.UserAPIConsumer;
import ie.iemdb.exception.ObjectNotFoundException;
import ie.iemdb.repository.UserRepo;
import ie.iemdb.util.types.Constant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;

@SpringBootApplication
@ComponentScan(basePackages = "ie.iemdb.service")
public class IemdbBackendApplication {
	public static void main(String[] args) {

		try {
			System.out.println("h1");
//			(new ActorAPIConsumer(Constant.FetchApiUrl.BASE_V2 + Constant.FetchApiUrl.ACTOR)).importData();
			System.out.println("h2");
//			(new MovieAPIConsumer(Constant.FetchApiUrl.BASE_V2 + Constant.FetchApiUrl.MOVIE)).importData();
			System.out.println("h3");
//			(new UserAPIConsumer(Constant.FetchApiUrl.BASE_V1 + Constant.FetchApiUrl.USER)).importData();
			System.out.println("h4");
			(new CommentAPIConsumer(Constant.FetchApiUrl.BASE_V1 + Constant.FetchApiUrl.COMMENT)).importData();
			System.out.println("h6");
		} catch (IOException e) {
			e.printStackTrace();
		}
//		try {
//			UserRepo.loggedInUser = UserRepo.getInstance().getElementsById(null).get(1);
//		} catch (ObjectNotFoundException e) {
//			e.printStackTrace();
//		}
		SpringApplication.run(IemdbBackendApplication.class, args);
	}

}