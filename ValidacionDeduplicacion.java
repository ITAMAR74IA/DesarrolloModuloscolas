/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.rabbitmq;

/**
 *
 * @author ALID2022
 */


import com.rabbitmq.client;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

public class ValidacionDeduplicacion {
    private static final String QUEUE_NAME = "formularios_queue";
    private static Set<String> formulariosProcesados = new HashSet<>();

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            System.out.println("[Validador] Esperando mensajes en '" + QUEUE_NAME + "'.");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String formularioJson = new String(delivery.getBody(), StandardCharsets.UTF_8);
                if (formulariosProcesados.add(formularioJson)) {
                    System.out.println("[Validador] Formulario válido, se procesará: " + formularioJson);
                    // Aquí podrías enviar a AlmacenamientoModulo si es necesario
                } else {
                    System.out.println("[Validador] Formulario duplicado ignorado: " + formularioJson);
                }
            };
            channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});
        }
    }
}

