FROM alpine/java:21-jre

WORKDIR /app

# Копирование JAR-файла в контейнер
COPY target/*.jar ema-kafka-consumer.jar

# Команда для запуска JAR-файла
CMD ["java", "-jar", "ema-kafka-consumer.jar"]