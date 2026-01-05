# Estágio de Construção (Build) - Atualizado para Java 21
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copia os arquivos do projeto
COPY . .

# Executa o build do Maven
RUN mvn clean package -DskipTests

# Estágio de Execução (Runtime) - Atualizado para Java 21
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copia o JAR gerado
COPY --from=build /app/target/*.jar app.jar

# Porta definida no seu application.properties
EXPOSE 9093

ENTRYPOINT ["java", "-jar", "app.jar"]