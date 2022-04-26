package ie.iemdb.server;

import ie.iemdb.apiConsumer.ActorAPIConsumer;
import ie.iemdb.apiConsumer.CommentAPIConsumer;
import ie.iemdb.apiConsumer.MovieAPIConsumer;
import ie.iemdb.apiConsumer.UserAPIConsumer;
import ie.iemdb.exception.ObjectNotFoundException;
import ie.iemdb.repository.CommentRepo;
import ie.iemdb.repository.MovieRepo;
import ie.iemdb.util.types.Constant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class IemdbBackendApplication {

	public static void main(String[] args) {

		try {
			(new ActorAPIConsumer(Constant.FetchApiUrl.BASE_V2 + Constant.FetchApiUrl.ACTOR)).importData();
			(new MovieAPIConsumer(Constant.FetchApiUrl.BASE_V2 + Constant.FetchApiUrl.MOVIE)).importData();
			(new UserAPIConsumer(Constant.FetchApiUrl.BASE_V1 + Constant.FetchApiUrl.USER)).importData();
			(new CommentAPIConsumer(Constant.FetchApiUrl.BASE_V1 + Constant.FetchApiUrl.COMMENT)).importData();
		} catch (IOException e) {
			e.printStackTrace();
		}

		SpringApplication.run(IemdbBackendApplication.class, args);
	}

}
