To run the backend project:
Using maven:
```bash
 ./mvnw package && java -jar target/iemdb-backend.jar
```
Then build the docker container:
```bash
sudo docker build -t iemdb-backend .
```
Also, create the database user in mysql container:
```bash
$ sudo docker exec -it mysqldb bash
/# mysql -uroot -proot
mysql> CREATE USER 'root'@'172.21.0.4' IDENTIFIED BY '1234IEMDB.ir';
mysql> GRANT ALL PRIVILEGES ON *.* TO 'root'@'172.21.0.4' WITH GRANT OPTION;
mysql> flush privileges; 
mysql> exit
```

Then using docker-compose.yml, run the project
```bash
sudo docker-compose up
```