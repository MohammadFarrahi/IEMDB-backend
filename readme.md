To run the backend project:
Using maven:
```bash
 ./mvnw package && java -jar target/iemdb-backend.jar
```
Then build the docker container:
```bash
sudo docker build -t iemdb-backend .
```
Then using docker-compose.yml, run the project
```bash
sudo docker-compose up
```