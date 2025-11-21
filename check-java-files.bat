@echo off
echo 🔍 Verificando compilación de Java...
echo.

REM Establecer classpath básico
set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-21.0.8.9-hotspot"

REM Verificar archivos problemáticos
echo 📋 Verificando BusinessPlan.java...
if exist "src\main\java\com\miapp\core\domain\BusinessPlan.java" (
    echo ✅ BusinessPlan.java existe
    findstr /n "getStatus\|setStatus" "src\main\java\com\miapp\core\domain\BusinessPlan.java"
) else (
    echo ❌ BusinessPlan.java no encontrado
)

echo.
echo 📋 Verificando AnalyticsService.java...
if exist "src\main\java\com\miapp\core\service\AnalyticsService.java" (
    echo ✅ AnalyticsService.java existe
    findstr /n "getStatus" "src\main\java\com\miapp\core\service\AnalyticsService.java"
) else (
    echo ❌ AnalyticsService.java no encontrado
)

echo.
echo 📋 Verificando BusinessPlanService.java...
if exist "src\main\java\com\miapp\core\service\BusinessPlanService.java" (
    echo ✅ BusinessPlanService.java existe
    findstr /n "setStatus" "src\main\java\com\miapp\core\service\BusinessPlanService.java"
) else (
    echo ❌ BusinessPlanService.java no encontrado
)

echo.
echo 📋 Verificando BusinessPlanController.java...
if exist "src\main\java\com\miapp\core\web\BusinessPlanController.java" (
    echo ✅ BusinessPlanController.java existe
    findstr /n "getStatus" "src\main\java\com\miapp\core\web\BusinessPlanController.java"
) else (
    echo ❌ BusinessPlanController.java no encontrado
)

echo.
echo ✅ Verificación completa
echo.
pause