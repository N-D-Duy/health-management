# Build stage: dùng Maven để build project
FROM maven:3.9.3-eclipse-temurin-17 AS builder
WORKDIR /app

# Copy source và build
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Extract layers từ jar đã build
FROM eclipse-temurin:17-jre-alpine AS layertool
WORKDIR /layertools
COPY --from=builder /app/target/health-management-0.0.1-SNAPSHOT.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

# Final image
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Timezone (nếu cần)
ENV TZ=Asia/Ho_Chi_Minh

# Copy từng layer
COPY --from=layertool /layertools/dependencies/ ./
COPY --from=layertool /layertools/snapshot-dependencies/ ./
COPY --from=layertool /layertools/spring-boot-loader/ ./
COPY --from=layertool /layertools/application/ ./

# Tùy chọn user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Port và entrypoint
EXPOSE 8080
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
