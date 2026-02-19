# Add only gradle config files
FROM alpine AS skeleton
WORKDIR /staging
COPY . .
RUN find . -type f \
    ! -name "*.gradle.kts" \
    ! -name "*.properties" \
    ! -name "gradlew" \
    ! -name "settings.gradle.kts" \
    -delete

# Build (gradle)
FROM gradle:9.3.1-jdk21-jammy AS builder
WORKDIR /home/gradle/src

USER root
RUN apt-get update && apt-get install -y \
    python3 make g++ libstdc++6 ca-certificates \
    && rm -rf /var/lib/apt/lists/*

USER gradle

COPY --from=skeleton --chown=gradle:gradle /staging ./
RUN chmod +x gradlew

RUN --mount=type=cache,target=/home/gradle/.gradle,uid=1000,gid=1000 \
    ./gradlew help --no-daemon

COPY --chown=gradle:gradle . .

RUN --mount=type=cache,target=/home/gradle/.gradle,uid=1000,gid=1000 \
    ./gradlew :server:buildFatJar --no-daemon

RUN --mount=type=cache,target=/home/gradle/.gradle,uid=1000,gid=1000 \
    ./gradlew ${GRADLE_TASK} --no-daemon --info

# 6. Подготовка артефактов
USER root
RUN mkdir -p /final_dist && cp -r ${DIST_PATH}* /final_dist/


# Backend
FROM eclipse-temurin:21-jre-alpine AS backend
WORKDIR /server

COPY --from=builder /home/gradle/src/server/build/libs/fat.jar ./server.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","server.jar"]

# Frontend
FROM nginx:stable-alpine AS frontend

RUN rm -rf /usr/share/nginx/html/*
COPY --from=builder /final_dist /usr/share/nginx/html

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]