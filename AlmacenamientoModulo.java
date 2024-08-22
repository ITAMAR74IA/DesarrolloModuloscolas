package com.mycompany.rabbitmq;

import com.rabbitmq.client.*;

import java.nio.charset.StandardCharsets;

public class AlmacenamientoModulo {
    private static final String QUEUE_NAME = "formularios_queue";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            System.out.println("[Almacenamiento] Esperando formularios para almacenar.");
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String formularioJson = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println("[Almacenamiento] Formulario almacenado: " + formularioJson);
                // Aquí puedes agregar la lógica para almacenar el formulario en una base de datos o archivo
            };
            channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});
        }
    }
}


