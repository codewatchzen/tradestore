Structure of the project

com.dbank.tradestore
|-- config           
|-- controller       
|-- service
│    |-- TradeService.java
|-- model
│    |-- Trade.java
|-- exception
|    |-- TradeException.java
|-- repository
│    |-- TradeSqlRepository.java
│    |-- TradeMongoRepository.java
|-- streaming
│    |-- TradeListener.java
|-- scheduler
│    |-- ExpiryJob.java
|-- tests
│    |-- TradeServiceTests.java
|--pom.xml