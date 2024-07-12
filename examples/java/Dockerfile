FROM openjdk:8

RUN apt-get update && apt-get install maven -y

WORKDIR /app
RUN git clone https://github.com/sendgrid/sendgrid-java.git
WORKDIR /app/sendgrid-java/

RUN rm -rf src/main/java/com/sendgrid/rest/*/ \
           src/main/java/com/sendgrid/example/ \
           src/test/

COPY src/main/java/com/sendgrid src/main/java/com/sendgrid/
COPY unit-test/rest integration-test/rest src/test/java/com/sendgrid/

RUN sed -i.bak 's/<dependencies>/<dependencies><dependency><groupId>org.json   <\/groupId><artifactId>json       <\/artifactId><version>20220320<\/version><\/dependency>/g' pom.xml ;

# pipefail prevents errors in a pipeline from being masked.
CMD ["/bin/bash", "-c", "set -o pipefail && mvn clean test"]
