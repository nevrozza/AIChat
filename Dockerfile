
FROM alpine AS skeleton
WORKDIR /staging

COPY . .
RUN find . -type f \
    -not -path "./buildSrc/*" \
    -not \( \
        -name "*.gradle.kts" \
        -or -name "*.properties" \
        -or -name "*.toml" \
        -or -name "gradlew" \
        -or -name "gradle-wrapper.jar" \
    \) -delete

FROM gradle:9.3.1-jdk21-jammy AS deps
WORKDIR /home/gradle/cache
ENV GRADLE_USER_HOME=/home/gradle/.gradle

COPY --from=skeleton /staging .

RUN gradle help --no-daemon


FROM gradle:9.3.1-jdk21-jammy AS builder
WORKDIR /home/gradle/src


RUN apt-get update && apt-get install -y \
    python3 make g++ libstdc++6 ca-certificates \
    && rm -rf /var/lib/apt/lists/*

ENV GRADLE_USER_HOME=/home/gradle/.gradle


# COPY --from=deps /home/gradle/.gradle /home/gradle/.gradle

COPY . .

RUN --mount=type=cache,target=/home/gradle/.gradle \
    gradle :server:buildFatJar --no-daemon

# ARG GRADLE_TASK=:app:web:wasmJsBrowserDistribution
# RUN gradle ${GRADLE_TASK} --no-daemon

# ARG DIST_PATH=app/web/build/dist/wasmJs/productionExecutable/
# RUN mkdir -p /final_dist && cp -r ${DIST_PATH}/* /final_dist/


# Backend
FROM eclipse-temurin:21-jre-alpine AS backend
WORKDIR /server
COPY --from=builder /home/gradle/src/server/build/libs/fat.jar ./server.jar
ENTRYPOINT ["java","-jar","server.jar"]

# Frontend
FROM nginx:stable-alpine AS frontend
RUN rm -rf /usr/share/nginx/html/*
# COPY --from=builder /final_dist /usr/share/nginx/html
CMD ["nginx", "-g", "daemon off;"]