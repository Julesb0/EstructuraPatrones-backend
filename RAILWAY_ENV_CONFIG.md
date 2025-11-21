# 🚀 Configuración de Railway para Producción

## 📋 Variables de entorno necesarias en Railway:

Ve a Railway → Tu proyecto → EstructuraPatrones-backend → Variables

### 🔑 Credenciales de Supabase (ya deberían estar configuradas):
```
SUPABASE_URL=https://jmumjdejdhncycnxgkom.supabase.co
SUPABASE_ANON_KEY=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImptdW1qZGVqZGhuY3ljbnhna29tIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjM2NDY0NzksImV4cCI6MjA3OTIyMjQ3OX0.Co1vLYDB8zFNT420HBc8Deqb7i9kzuznfRuIiYBwa14
SUPABASE_SERVICE_ROLE_KEY=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImptdW1qZGVqZGhuY3ljbnhna29tIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTc2MzY0NjQ3OSwiZXhwIjoyMDc5MjIyNDc5fQ.2pK4jH7qP3sT8mN9vR5wX1yZ6aB2cD4eF6gH8jK0lM2nO4pQ6r
```

### 🔒 Seguridad y JWT:
```
JWT_SECRET=xZpmvBNQVl1yagtr3wkGuDbDdiJ3LpTyW+LrThZ/j3UdIdPWPnkid4tjmBVjH7y1EMu7QH55zP/fCwnvR2lFuA==
JWT_EXPIRATION=3600
ADMIN_USERNAME=admin
ADMIN_PASSWORD=admin123
```

### 🌐 CORS - IMPORTANTE! Configura tu dominio de Vercel:
```
CORS_ORIGINS=https://estructura-patrones-frontend-ljgk.vercel.app,http://localhost:3000,http://localhost:3001,http://localhost:3002
```

### 📝 Notas:
- El `SUPABASE_SERVICE_ROLE_KEY` es diferente al `SUPABASE_ANON_KEY`
- Puedes obtener el Service Role Key desde Supabase → Settings → API → service_role key
- El `JWT_SECRET` debe ser una cadena larga y segura en producción
- Los puertos locales (3000, 3001, 3002) son para desarrollo

## 🔄 Después de configurar las variables:

1. Guarda los cambios
2. Railway automáticamente hará redeploy
3. Espera a que termine el deployment
4. Verifica que el servicio esté en verde (Running)

## ✅ Verificación final:

Una vez que termine el deploy, prueba:
```
https://tu-backend-url.up.railway.app/health
```

Debería retornar: `{"status":"UP"}`

## 🎯 Siguiente paso:

Cuando el backend esté funcionando, necesitaré la URL exacta de tu backend en Railway para configurarla en el frontend.