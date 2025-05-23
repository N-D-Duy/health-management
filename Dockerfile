FROM maven:3.9.3-eclipse-temurin-17 AS builder
WORKDIR /app

COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests --batch-mode

FROM eclipse-temurin:17-jre-alpine AS layertool
WORKDIR /layertools
COPY --from=builder /app/target/*.jar app.jar
RUN java -Djarmode=tools -jar app.jar extract --layers --destination extracted

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

ENV TZ=Asia/Ho_Chi_Minh

COPY --from=layertool /layertools/extracted/dependencies/ ./
COPY --from=layertool /layertools/extracted/spring-boot-loader/ ./
COPY --from=layertool /layertools/extracted/snapshot-dependencies/ ./
COPY --from=layertool /layertools/extracted/application/ ./

RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
