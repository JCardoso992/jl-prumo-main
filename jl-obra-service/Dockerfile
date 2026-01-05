# Estágio de Construção (Build)
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

# Copia os arquivos do projeto para dentro do container
COPY . .

# Executa o build do Maven para gerar o .jar
RUN ./mvnw clean package -DskipTests

# Estágio de Execução (Runtime)
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copia o JAR gerado no estágio anterior
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta que definiu no application.properties
EXPOSE 9093

ENTRYPOINT ["java", "-jar", "app.jar"]