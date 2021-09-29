./mvnw clean install -DskipTests
./mvnw spring-boot:build-image -DskipTests -pl microservices/book
./mvnw spring-boot:build-image -DskipTests -pl microservices/rating
./mvnw spring-boot:build-image -DskipTests -pl microservices/review
./mvnw spring-boot:build-image -DskipTests -pl microservices/composite