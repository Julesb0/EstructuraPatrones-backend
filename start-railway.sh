#!/bin/bash

echo "🚀 Iniciando aplicación EstructuraPatrones Backend..."
echo "📋 Variables de entorno disponibles:"
env | grep -E "(PORT|SUPABASE|JWT|CORS)" | sed 's/=.*/=***/'

echo "📁 Contenido del directorio:"
ls -la

echo "🔍 Buscando archivo JAR..."
find . -name "*.jar" -type f

echo "📊 Tamaño del archivo JAR:"
if [ -f app.jar ]; then
    ls -lh app.jar
else
    echo "❌ app.jar no encontrado"
fi

echo "🎯 Puerto configurado: ${PORT:-8080}"

echo "🏃 Iniciando aplicación Java..."
java -jar app.jar