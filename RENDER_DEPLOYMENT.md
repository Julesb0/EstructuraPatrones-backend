# 🚀 DESPLIEGUE EN RENDER - BACKEND

## 📋 PASO A PASO PARA DESPLEGAR EN RENDER

### 1️⃣ PREPARACIÓN
1. Asegúrate de que tu código esté actualizado en GitHub
2. Verifica que tengas el archivo `render.yaml` en la raíz
3. Confirma que el archivo `application-render.yml` esté en `src/main/resources/`

### 2️⃣ CONFIGURACIÓN EN RENDER

#### Opción A: Despliegue Manual
1. Ve a https://render.com
2. Crea una cuenta o inicia sesión
3. Click en "New Web Service"
4. Conecta tu repositorio de GitHub
5. Configura:
   - **Name**: `estructurapatrones-backend`
   - **Environment**: Java
   - **Build Command**: `mvn clean package -DskipTests`
   - **Start Command**: `java -jar -Dspring.profiles.active=render target/*.jar`

#### Opción B: Despliegue con Blueprint (RECOMENDADO)
1. Ve a https://render.com/blueprints
2. Click en "New Blueprint Instance"
3. Conecta tu repositorio
4. Render detectará automáticamente el `render.yaml`

### 3️⃣ VARIABLES DE ENTORNO

Copia y pega estas variables en Render:

```env
SUPABASE_URL=https://jmumjdejdhncycnxgkom.supabase.co
SUPABASE_SERVICE_ROLE_KEY=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImptdW1qZGVqZGhuY3ljbnhna29tIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTc2MzY0NjQ3OSwiZXhwIjoyMDc5MjIyNDc5fQ.zc0mJ6XVjJ1p3Q8b5f3zU2f8V7xV5zV5yV3xV5zV5yV3
SUPABASE_JWT_SECRET=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImptdW1qZGVqZGhuY3ljbnhna29tIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTc2MzY0NjQ3OSwiZXhwIjoyMDc5MjIyNDc5fQ.zc0mJ6XVjJ1p3Q8b5f3zU2f8V7xV5zV5yV3xV5zV5yV3
JWT_SECRET=mi-super-secreto-jwt-para-firmar-tokens-2024
JWT_EXPIRATION=86400000
RECAPTCHA_SECRET_KEY=6LeIxAcTAAAAAGG-vFI1TnRWxMZNFuojJ4WifJWe
PLATFORM_TABLES_PROFILES=profiles
PLATFORM_TABLES_BUSINESS_PLANS=business_plans
PLATFORM_TABLES_CHAT_MESSAGES=chat_messages
PLATFORM_USER_COLUMN=user_id
SPRING_PROFILES_ACTIVE=render
```

### 4️⃣ VERIFICACIÓN POST-DESPLIEGUE

Una vez desplegado, verifica:

#### ✅ Health Check
```bash
curl https://tu-app.render.com/api/health
```

#### ✅ Test de Autenticación
```bash
curl -X POST https://tu-app.render.com/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "test123",
    "recaptchaToken": "test-token"
  }'
```

#### ✅ Test de Planes
```bash
curl -X GET https://tu-app.render.com/api/plans \
  -H "Authorization: Bearer TU_TOKEN_JWT"
```

### 5️⃣ SOLUCIÓN DE PROBLEMAS

#### Si el build falla:
1. Verifica que tu `pom.xml` tenga el plugin de Spring Boot:
```xml
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
</plugin>
```

2. Asegúrate de que el Java version sea compatible:
```xml
<properties>
    <java.version>17</java.version>
</properties>
```

#### Si el inicio falla:
1. Revisa los logs en el dashboard de Render
2. Verifica que todas las variables de entorno estén configuradas
3. Asegúrate de que el perfil `render` esté activo

### 6️⃣ URL IMPORTANTES

- **Dashboard Render**: https://dashboard.render.com
- **Tu App**: https://estructurapatrones-backend.onrender.com (ejemplo)
- **Health Check**: https://tu-app.render.com/api/health
- **Swagger** (si lo habilitas): https://tu-app.render.com/swagger-ui.html

### 7️⃣ ACTUALIZACIONES FUTURAS

Para actualizar el código:
1. Sube los cambios a GitHub
2. Render desplegará automáticamente (si tienes auto-deploy activado)
3. O manualmente desde el dashboard de Render

---

## 🎯 ESTADO ACTUAL
✅ Backend configurado para Render
✅ Archivos de configuración creados
✅ Variables de entorno documentadas
✅ Scripts de verificación preparados

¡Listo para desplegar en Render! 🚀