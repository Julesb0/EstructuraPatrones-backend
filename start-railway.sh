#!/bin/bash

# Script de inicio para Railway
# Configuración de variables de entorno y arranque de la aplicación

echo "Iniciando aplicación Spring Boot en Railway..."

# Verificar que las variables de entorno necesarias estén configuradas
if [ -z "$SUPABASE_URL" ]; then
    echo "ERROR: SUPABASE_URL no está configurada"
    exit 1
fi

if [ -z "$SUPABASE_ANON_KEY" ]; then
    echo "ERROR: SUPABASE_ANON_KEY no está configurada"
    exit 1
fi

if [ -z "$SUPABASE_SERVICE_KEY" ]; then
    echo "ERROR: SUPABASE_SERVICE_KEY no está configurada"
    exit 1
fi

if [ -z "$JWT_SECRET" ]; then
    echo "ERROR: JWT_SECRET no está configurada"
    exit 1
fi

echo "Variables de entorno verificadas correctamente"

# Configurar el puerto (Railway asigna un puerto dinámico)
if [ -z "$PORT" ]; then
    export PORT=8080
fi

echo "Puerto configurado: $PORT"

# Ejecutar la aplicación Java
exec java -jar app.jar