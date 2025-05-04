
Notes for local Testing:
Prereq: Docker should be installed and running.

### Testing Kafka: 
#### Sping docker containers for PostGreSql, MongoDB and Kafka
```
docker compose up
mvn spring-boot run
```

#### Add record  in producer 
```
echo '{"tradeId":"T1","version":1,"counterPartyId":"CP-1","bookId":"B1","maturityDate":"2025-05-10","createdDate":"2025-05-03","expired":"N"}' | docker exec -i tradestore-kafka-1 kafka-console-producer --broker-list localhost:9092 --topic trade-topic
```


#### validate in consumer
```
docker exec -it tradestore-kafka-1 kafka-console-consumer --bootstrap-server localhost:9092 --topic trade-topic --from-beginning
```


### local testing if API is posting Trade object
```
curl -X POST http://localhost:8080/trades -H "Content-Type: application/json" -d '{"tradeId":"T1","version":1,"counterPartyId":"CP-1","bookId":"B1","maturityDate":"2025-05-10","createdDate":"2025-05-03","expired":"N"}'
```
