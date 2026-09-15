FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY . .

RUN javac clock/*.java common/*.java emergency/*.java hospital/*.java traffic/*.java weather/*.java client/*.java election/*.java ClockClient.java ClockTest.java

CMD ["java", "emergency.EmergencyServer"]
