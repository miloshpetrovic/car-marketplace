
# Car Marketplace system


## System description

System is composed of two micro-services representing Inventory of used cars (car-inventory) and used car Marketplace (car-marketplace). Inventory is wrapper around cannonical storage for used cars relaying on traditional Postgres database. On the other hand, Marketplace is highly optimized replica for wide variety of searches backed by Elasticsearch engine. For synchronization purposes, both services are connected via Kafka messaging middleware which represents event bus for relevant events related to basic product inventory operations: creating, updating and deleting a car. Each operation on Inventory results in event sent to Marketplace.  Inventory exposes REST CRUD operations for used cars and Marketplace exposes REST multicriteria search operation vith paging and sorting capabilities. 

System utilizes simple data model with car Manufacturer, car Model and used Car as central notions. Each used Car is of some Model produced by concreete Manufacturer. Car Model defines some general attributes like body type, fuel type or power. Additionaly, used Car have some relevant usage data, like mileage, year of first registration and of course price. Manufacturers and Models are predefined in the system, and those are enumerated in the following listing. 


| Manufacturer | Model | 
| :-------- | :------- | 
| `Volvo` | `S40` | 
| `Volvo` | `S60` | 
| `Volvo` | `S70` | 
| `Volvo` | `S80` | 
| `Volvo` | `S90` | 
| `Toyota` | `Avensis` | 
| `Toyota` | `Corolla` | 
| `Toyota` | `Yaris` | 
| `Toyota` | `Camry` | 
| `Toyota` | `RAV4` | 
| `Ford` | `Fiesta` | 
| `Ford` | `Focus` | 
| `Ford` | `Escort` | 
| `Ford` | `Orion` | 
| `Ford` | `Capri` | 
| `Renault` | `Clio` | 
| `Renault` | `Duster` | 
| `Renault` | `Espace` | 
| `Renault` | `Laguna` | 
| `Renault` | `Logan` | 
| `BMW` | `518` | 
| `BMW` | `520` | 
| `BMW` | `523` | 
| `BMW` | `524` | 
| `BMW` | `525` | 


#### System setup

Technicaly system runtime is organized with 5 networked containers, each for one key component of the system: Inventory, Marketplace, Postgres, Elasticsearch, Kafka. Each component have corresponding DockerHub image which enables utilization of docker-compose tool in order setup system network. Content of docker-compose.yml file follows. 




```
version: "2"

services:
  postgresdb:
    image: postgres
    mem_limit: 512m
    hostname: postgresql
    ports: 
      - "5432:5432"
    environment:
      - POSTGRES_USER=postgres
      - POSTGRES_PASSWORD=postgres
      - POSTGRES_DB=smg
  kafka:
    image: docker.io/bitnami/kafka:3.6
    mem_limit: 1g
    hostname: kafka
    ports:
      - "9092:9092"
      - "9094:9094"
    environment:
      # KRaft settings
      - KAFKA_CFG_NODE_ID=0
      - KAFKA_CFG_PROCESS_ROLES=controller,broker
      - KAFKA_CFG_CONTROLLER_QUORUM_VOTERS=0@kafka:9093
      # Listeners
      - KAFKA_CFG_LISTENERS=PLAINTEXT://:9092,CONTROLLER://:9093,EXTERNAL://0.0.0.0:9094
      - KAFKA_CFG_ADVERTISED_LISTENERS=PLAINTEXT://kafka:9092,EXTERNAL://localhost:9094
      - KAFKA_CFG_LISTENER_SECURITY_PROTOCOL_MAP=CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT,EXTERNAL:PLAINTEXT
      - KAFKA_CFG_CONTROLLER_LISTENER_NAMES=CONTROLLER
      - KAFKA_CFG_INTER_BROKER_LISTENER_NAME=PLAINTEXT
  elasticsearch:
    image: elasticsearch:8.8.0
    mem_limit: 512m  
    hostname: elasticsearch    
    ports:
      - 9200:9200
      - 9300:9300
    environment:
      - discovery.type=single-node
      - xpack.security.enabled=false
  carinventory:
    image: miloshpetrovic/car-inventory:latest
    mem_limit: 512m
    depends_on:
      - postgresdb
      - kafka
    ports:
      - "8080:8080"
    environment:
      - SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092
      - SPRING_DATASOURCE_URL=jdbc:postgresql://postgresdb:5432/smg
  carmarketplace:
    image: miloshpetrovic/car-marketplace:latest
    mem_limit: 512m
    depends_on:
      - kafka
      - elasticsearch
    ports:
      - "8181:8181"
    environment:
      - SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092
      - SPRING_ELASTICSEARCH_URIS=http://elasticsearch:9200
```

Before execution of docker-compose tool, Docker runtime should be started on local machine (e.g. Docker desktop). Next, in directory of docker-compose.yml file, we should execute following comand: 

```
docker-compose up -d
```
If everything is ok, after command execution all 5 containers (services) should be started. We can check that with: 

```
docker ps
```

#### System testing


