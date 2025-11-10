# 1. Gradle 빌드 대신 로컬에서 빌드한 jar 사용
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# 🔹 로컬에서 ./gradlew bootJar로 생성된 jar만 복사
COPY build/libs/backend-0.0.1-SNAPSHOT.jar backend.jar

# 🔹 Docker 안에서 Gradle 빌드하지 않고 바로 실행
ENTRYPOINT ["java","-jar","backend.jar"]
