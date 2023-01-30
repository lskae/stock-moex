FROM openjdk:11
COPY build/libs/stock-moex-0.0.1.jar .
CMD ["java","-jar","/stock-moex-0.0.1.jar"]