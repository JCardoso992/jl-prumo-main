# Estágio de Construção (Build) - Usando Temurin para estabilidade
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copia os arquivos do projeto
COPY . .

# Executa o build do Maven. 
# Usamos 'mvn' diretamente ou o wrapper ./mvnw se estiver disponível e com permissão
RUN mvn clean package -DskipTests

# Estágio de Execução (Runtime) - Usando a imagem JRE (mais leve)
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copia o JAR gerado no estágio anterior
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta do seu serviço
EXPOSE 9093

ENTRYPOINT ["java", "-jar", "app.jar"]