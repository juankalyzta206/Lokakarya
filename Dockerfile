FROM openjdk:8        
EXPOSE 8080                   
label maintainer="lokakarya.net"
ADD target/lokakarya-0.0.1-SNAPSHOT.jar lokakarya-0.0.1-SNAPSHOT.jar 
ENTRYPOINT ["java","-jar","lokakarya-0.0.1-SNAPSHOT.jar"]   