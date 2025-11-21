package com.miapp.config;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

@Component
public class ServerPortCustomizer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

    @Override
    public void customize(ConfigurableWebServerFactory factory) {
        // Obtener el puerto de la variable de entorno PORT (Render) o usar 8080 por defecto
        String port = System.getenv("PORT");
        if (port != null && !port.isEmpty()) {
            factory.setPort(Integer.parseInt(port));
        } else {
            factory.setPort(8080);
        }
    }
}