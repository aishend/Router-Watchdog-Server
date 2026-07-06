# ---------- Build dashboard ----------
FROM node:24-bookworm AS dashboard-build

WORKDIR /app/dashboard

COPY dashboard/package*.json ./
RUN npm install

COPY dashboard ./
RUN npm run build


# ---------- Build Spring app ----------
FROM eclipse-temurin:21-jdk AS api-build

WORKDIR /app

COPY api ./api
COPY --from=dashboard-build /app/dashboard/dist ./api/src/main/resources/static

WORKDIR /app/api

RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests


# ---------- Runtime ----------
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=api-build /app/api/target/*.jar app.jar

ENV PORT=8080

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]