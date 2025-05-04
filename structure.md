### Structure of the project

```
com.dbank.tradestore
|-- config 
|   |-- KafkaConsumerConfig.java
|   |-- KafkaProducerConfig.java
|-- consumer
|   |-- TradeListener.java          
|-- controller
|   |-- TradeController.java
|-- dto
|   |-- TradeMessage.java    
|-- service
│   |-- TradeService.java
|   |-- ProducerService.java 
|-- model
│    |-- Trade.java
|-- exception
|    |-- TradeException.java
|-- repository
│    |-- TradeSqlRepository.java
│    |-- TradeMongoRepository.java
|-- scheduler
│    |-- ExpiryJobScheduler.java
|-- tests
│    |-- TradeServiceTests.java
|--pom.xml
|-- TradeStoreApplication.java //only for localtesting purposes
|-- resources/application.properties
|-- docker-compose.yml
|-- .github/workflows/ci.yml, deploy.yml

```