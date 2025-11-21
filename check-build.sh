# Script para verificar y forzar rebuild en Railway

echo "🔍 Verificando archivos críticos..."

# Verificar BusinessPlan.java
echo "📋 Verificando BusinessPlan.java..."
if [ -f "src/main/java/com/miapp/core/domain/BusinessPlan.java" ]; then
    echo "✅ BusinessPlan.java existe"
    grep -n "getStatus\|setStatus" src/main/java/com/miapp/core/domain/BusinessPlan.java
else
    echo "❌ BusinessPlan.java no encontrado"
fi

echo ""

# Verificar AnalyticsService.java
echo "📋 Verificando AnalyticsService.java..."
if [ -f "src/main/java/com/miapp/core/service/AnalyticsService.java" ]; then
    echo "✅ AnalyticsService.java existe"
    grep -n "getStatus" src/main/java/com/miapp/core/service/AnalyticsService.java
else
    echo "❌ AnalyticsService.java no encontrado"
fi

echo ""

# Verificar SupabaseRepository.java
echo "📋 Verificando SupabaseRepository.java..."
if [ -f "src/main/java/com/miapp/core/repository/SupabaseRepository.java" ]; then
    echo "✅ SupabaseRepository.java existe"
    grep -n "deleteById\|update" src/main/java/com/miapp/core/repository/SupabaseRepository.java
else
    echo "❌ SupabaseRepository.java no encontrado"
fi

echo ""
echo "🔧 Build local de prueba..."
mvn clean compile -DskipTests

echo ""
echo "📦 Si el build local funciona, el problema es en Railway."
echo "💡 En Railway, intenta:"
echo "   1. Variables de entorno → Deploy → Redeploy"
echo "   2. O Settings → Deploy → Clear cache and redeploy"
echo "   3. Verificar que esté usando la rama correcta (main/master)"