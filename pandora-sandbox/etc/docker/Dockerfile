FROM openjdk:8-jre
ADD ./target/pandora.jar /app/pandora.jar
ENTRYPOINT ["java", "-cp", "\/app\/pandora.jar", "pandora.sandbox.HelloAgainServer"]
