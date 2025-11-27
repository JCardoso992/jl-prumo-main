# Usa uma imagem base para Java
FROM openjdk:17-jdk-slim

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copia o arquivo JAR do seu projeto para o contêiner
# Assumindo que o arquivo JAR se chama 'seu-backend.jar'
COPY target/seu-backend.jar /app/app.jar

# Expõe a porta que o Spring Boot usa (geralmente 8080)
EXPOSE 8080

# Comando para rodar a aplicação quando o contêiner iniciar
ENTRYPOINT ["java", "-jar", "app.jar"]