
package com.mycompany.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeoutException;

public class FormularioProducer {
    private final static String QUEUE_NAME = "formularios_queue";
    private ConnectionFactory factory;

    public FormularioProducer() {
        factory = new ConnectionFactory();
        factory.setHost("localhost");
    }

    public void enviarFormulario(String formularioJson) {
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.basicPublish("", QUEUE_NAME, null, formularioJson.getBytes());
            System.out.println("[Productor] Formulario enviado correctamente: " + formularioJson);
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    public void generarFormulariosAutomaticamente(int cantidad) {
        for (int i = 0; i < cantidad; i++) {
            try {
                String formularioJson = generarFormularioAleatorio();
                enviarFormulario(formularioJson);
                Thread.sleep(1000); // Espera 1 segundo entre envíos
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private String generarFormularioAleatorio() {
        Random random = new Random();
        int edad = random.nextInt(100);
        String nombre = "Persona" + random.nextInt(100);
        String direccion = "Calle " + random.nextInt(100);
        return "{\"nombre\": \"" + nombre + "\", \"edad\": " + edad + ", \"direccion\": \"" + direccion + "\"}";
    }

    public static void main(String[] args) {
        FormularioProducer producer = new FormularioProducer();
        producer.generarFormulariosAutomaticamente(20); // Generar 20 formularios automáticamente
    }
}


