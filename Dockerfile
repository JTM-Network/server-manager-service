FROM java:16
VOLUME /tmp
EXPOSE 8999
ADD /build/libs/*.jar app.jar
CMD java -Xms256m -Xmx512m -jar app.jar