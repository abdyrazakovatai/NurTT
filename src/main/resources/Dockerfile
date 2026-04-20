FROM openjdk:17 as build
WORKDIR /app
COPY . ./
RUN microdnf install findutils
RUN chmod +x gradlew
RUN ./gradlew build -x test

FROM openjdk:17-slim
WORKDIR /app
COPY --from=build /app/build/libs/NurTT-0.0.1-SNAPSHOT-plain.jar .
CMD ["java", "-jar", "NurTT-0.0.1-SNAPSHOT-plain.jar"]
EXPOSE 2025