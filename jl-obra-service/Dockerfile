# Estágio 1: Build (Compilação)
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
# Copia apenas os ficheiros de configuração do Maven primeiro para aproveitar a cache
COPY pom.xml .
RUN mvn dependency:go-offline
# Copia o código fonte e gera o JAR
COPY src ./src
RUN mvn clean package -DskipTests

# Estágio 2: Runtime (Execução)
FROM openjdk:17-jdk-slim
WORKDIR /app
# Copia o JAR gerado no estágio anterior
COPY --from=build /app/target/*.jar app.jar
# Usa a porta que definiste no application.properties
EXPOSE 9093
ENTRYPOINT ["java", "-jar", "app.jar"]