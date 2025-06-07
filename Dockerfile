# Stage 1: Cache dependencies
FROM gradle:8.5-jdk17-alpine AS cache
WORKDIR /app
COPY build.gradle.kts settings.gradle.kts gradle.properties ./
COPY gradle ./gradle/
RUN gradle dependencies --no-daemon

# Stage 2: Build application
FROM gradle:8.5-jdk17-alpine AS builder
WORKDIR /app
COPY --from=cache /home/gradle/.gradle /home/gradle/.gradle
COPY . .
RUN gradle clean bootJar -x test --build-cache --parallel

# Final stage: Run application
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
ENV TZ=Asia/Ho_Chi_Minh

COPY --from=builder /app/build/libs/health-management-*.jar app.jar

RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

EXPOSE 8080

ENTRYPOINT ["java", \
  "-XX:+UseG1GC", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Dspring.profiles.active=prod", \
  "-jar", \
  "app.jar"]
