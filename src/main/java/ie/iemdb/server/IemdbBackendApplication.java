package ie.iemdb.server;

import ie.iemdb.apiConsumer.ActorAPIConsumer;
import ie.iemdb.apiConsumer.CommentAPIConsumer;
import ie.iemdb.apiConsumer.MovieAPIConsumer;
import ie.iemdb.apiConsumer.UserAPIConsumer;
import ie.iemdb.exception.ObjectNotFoundException;
import ie.iemdb.repository.ActorRepo;
import ie.iemdb.repository.CommentRepo;
import ie.iemdb.repository.UserRepo;
import ie.iemdb.util.types.Constant;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;

@SpringBootApplication
@ComponentScan(basePackages = "ie.iemdb.service")
public class IemdbBackendApplication {
//    private static Dotenv dotenv = Dotenv.load();
    private static void fetchData() {
        try {
			if(ActorRepo.getInstance().getAllElements().size() == 0) {
            System.out.println("h1");
            (new ActorAPIConsumer(Constant.FetchApiUrl.BASE_V2 + Constant.FetchApiUrl.ACTOR)).importData();
            System.out.println("h2");
            (new UserAPIConsumer(Constant.FetchApiUrl.BASE_V1 + Constant.FetchApiUrl.USER)).importData();
            System.out.println("h3");
            (new MovieAPIConsumer(Constant.FetchApiUrl.BASE_V2 + Constant.FetchApiUrl.MOVIE)).importData();
            System.out.println("h4");
            (new CommentAPIConsumer(Constant.FetchApiUrl.BASE_V1 + Constant.FetchApiUrl.COMMENT)).importData();
            System.out.println("h5");
			}
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        fetchData();
        SpringApplication app = new SpringApplication(IemdbBackendApplication.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", System.getenv("SERVER_PORT")));
        app.run(args);
    }
}