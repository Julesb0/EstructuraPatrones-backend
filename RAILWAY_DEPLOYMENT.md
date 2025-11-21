# 🚂 Railway Deployment Guide - HotCash Backend

## Pasos para deployar en Railway:

### 1. Crea cuenta en Railway
- Ve a https://railway.app
- Regístrate con GitHub

### 2. Crea nuevo proyecto
- Click en "New Project"
- Selecciona "Deploy from GitHub repo"
- Elige tu repositorio: `EstructuraPatrones-backend`

### 3. Configura variables de entorno
Railway detectará automáticamente que es un proyecto Java/Maven.

Copia y pega estas variables en Railway:

```env
SUPABASE_URL=https://jmumjdejdhncycnxgkom.supabase.co
SUPABASE_ANON_KEY=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImptdW1qZGVqZGhuY3ljbnhna29tIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjM2NDY0NzksImV4cCI6MjA3OTIyMjQ3OX0.Co1vLYDB8zFNT420HBc8Deqb7i9kzuznfRuIiYBwa14
SUPABASE_SERVICE_ROLE_KEY=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImptdW1qZGVqZGhuY3ljbnhna29tIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTc2MzY0NjQ3OSwiZXhwIjoyMDc5MjIyNDc5fQ.2cVSvlJTD7FNkK9tSeEWhE82WEZgU6m1-3nx61OTFg0
SUPABASE_DB_URL=jdbc:postgresql://db.jmumjdejdhncycnxgkom.supabase.co:5432/postgres
SUPABASE_USER=postgres
SUPABASE_PASSWORD=YDSbBYDMOfS19mtU
JWT_SECRET=xZpmvBNQVl1yagtr3wkGuDbDdiJ3LpTyW+LrThZ/j3UdIdPWPnkid4tjmBVjH7y1EMu7QH55zP/fCwnvR2lFuA==
CORS_ORIGINS=https://estructurapatrones-frontend.vercel.app,http://localhost:3000
PORT=8080
```

### 4. Configura el build
Railway debería detectar automáticamente el `pom.xml` y ejecutar:
```bash
mvn clean package -DskipTests
```

### 5. Configura el comando de inicio
El comando de inicio debería ser:
```bash
java -jar target/*.jar
```

### 6. Deployar
- Click en "Deploy"
- Railway construirá y desplegará automáticamente
- Espera 2-3 minutos para que termine el deployment

### 7. Verifica el deployment
Una vez desplegado, Railway te dará una URL como:
`https://hotcash-backend-production.up.railway.app`

### 8. Actualiza el frontend
Cuando tengas la URL del backend, actualiza el archivo `.env` del frontend:
```env
VITE_API_URL=https://tu-backend-url.railway.app
```

## 🧪 Prueba el backend
Después del deployment, prueba estos endpoints:

```bash
# Health check
GET https://tu-backend-url.railway.app/api/health

# Chatbot (requiere autenticación)
POST https://tu-backend-url.railway.app/api/chatbot/messages
```

## 📋 Notas importantes:
- Railway ofrece $5 USD de crédito gratis mensualmente
- El deployment es automático cuando haces push a GitHub
- Puedes ver los logs en tiempo real en el dashboard de Railway
- Si hay errores, revisa los logs para debugging

## 🔧 Solución de problemas:
- **Build falla**: Verifica que todas las variables de entorno estén configuradas
- **No conecta a Supabase**: Revisa las credenciales de Supabase
- **CORS errors**: Asegúrate de que el frontend esté en la lista CORS_ORIGINS

¡Listo! Tu backend de HotCash estará funcionando en Railway.