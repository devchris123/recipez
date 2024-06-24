# This Dockerfile is used for integration tests
FROM maven:3.9.6

RUN mkdir /recipes

# Copy pom and resolve dependencies
# This reduces execution time for mvn verify on subsequent calls
COPY ./recipez/pom.xml /recipes/
WORKDIR /recipes

RUN mvn dependency:resolve-plugins 
RUN mvn dependency:resolve

# Finally copy ITs as a last layer
COPY ./recipez/src /recipes/src

# For testing copy the sql script into the resources directory
COPY ./sql_migrations/0000_recipes.sql ./recipes/src/main/resources

CMD ["mvn", "spring-boot:run"]