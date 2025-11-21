# Multi-stage build para optimizar el tamaño de la imagen
FROM maven:3.9-eclipse-temurin-21 AS build

# Establecer directorio de trabajo
WORKDIR /app

# Copiar archivos de configuración de Maven y el script
COPY pom.xml .
COPY src ./src
COPY start-railway.sh ./start-railway.sh

# Construir la aplicación
RUN mvn clean package -DskipTests

# Segunda etapa: imagen de ejecución
FROM eclipse-temurin:21-jre-alpine

# Instalar dependencias del sistema
RUN apk add --no-cache curl

# Crear usuario no-root para seguridad
RUN addgroup -g 1001 -S appuser && adduser -S appuser -u 1001

# Establecer directorio de trabajo
WORKDIR /app

# Copiar el JAR de la etapa de construcción
COPY --from=build /app/target/*.jar app.jar

# Cambiar propietario del directorio
RUN chown -R appuser:appuser /app

# Cambiar al usuario no-root
USER appuser

# Exponer puerto
EXPOSE 8080

# Comando de inicio directo
CMD ["java", "-jar", "app.jar"]