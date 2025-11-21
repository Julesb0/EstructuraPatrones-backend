package com.emprendetube.restapi.chatbot.nlp;

/**
 * Interfaz para el patrón Adapter/Bridge
 * Define el contrato para clientes de procesamiento de lenguaje natural
 */
public interface NlpClient {
    
    /**
     * Envía un prompt al servicio de NLP y obtiene una respuesta
     * 
     * @param prompt El mensaje o pregunta del usuario
     * @return La respuesta generada por el modelo NLP
     * @throws NlpException si ocurre un error en la comunicación con el servicio NLP
     */
    String ask(String prompt) throws NlpException;
    
    /**
     * Obtiene información sobre el cliente NLP (para logging y debugging)
     * 
     * @return Nombre o identificador del cliente
     */
    String getClientInfo();
}