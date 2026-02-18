# Add only gradle config files
FROM alpine AS skeleton
WORKDIR /staging
COPY . .
RUN find . -type f ! -name "*.gradle.kts" ! -name "*.properties" ! -name "gradlew" ! -name "gradlew" -delete

# Build (gradle)
FROM gradle:9.3.1-jdk21-jammy AS builder
WORKDIR /home/gradle/src

RUN apt-get update && apt-get install -y \
    python3 \
    make \
    g++ \
    libstdc++6 \
    ca-certificates \
    && rm -rf /var/lib/apt/lists/*

ARG BUILD_TYPE=prod
ENV GRADLE_USER_HOME=/home/gradle/.gradle

COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts settings.gradle.kts ./
COPY buildSrc ./buildSrc

COPY --from=skeleton /staging ./

RUN --mount=type=cache,target=/home/gradle/.gradle,uid=1000,gid=1000 \
    gradle help --no-daemon

COPY . .

RUN --mount=type=cache,target=/home/gradle/.gradle,uid=1000,gid=1000 \
    gradle :server:buildFatJar --no-daemon

# build front (dev: wasmJsDev, prod: wasmJsAndJsProd)
RUN --mount=type=cache,target=/home/gradle/.gradle,uid=1000,gid=1000 \
    if [ "$BUILD_TYPE" = "dev" ]; then \
        gradle :app:web:wasmJsBrowserDevelopmentExecutableDistribution --no-daemon && \
        mkdir -p /final_dist && \
        cp -r app/web/build/dist/wasmJs/developmentExecutable/* /final_dist/; \
    else \
        gradle :app:web:composeCompatibilityBrowserDistribution --no-daemon && \
        mkdir -p /final_dist && \
        cp -r app/web/build/dist/composeWebCompatibility/productionExecutable/* /final_dist/; \
    fi


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