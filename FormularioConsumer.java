package com.mycompany.rabbitmq;

import com.rabbitmq.client.*;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

public class FormularioConsumer {
    private final static String QUEUE_NAME = "formularios_queue";
    private Set<String> formulariosProcesados = new HashSet<>();

    public void consumirFormularios() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            System.out.println("[Consumidor] Esperando mensajes en '" + QUEUE_NAME + "'. Presiona CTRL+C para salir.");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String mensaje = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println("[Consumidor] Recibido formulario: " + mensaje);

                if (validarFormulario(mensaje)) {
                    if (!formulariosProcesados.contains(mensaje)) {
                        formulariosProcesados.add(mensaje);
                        procesarFormulario(mensaje);
                    } else {
                        System.out.println("[Consumidor] Formulario duplicado. Descartando...");
                    }
                } else {
                    System.out.println("[Consumidor] Formulario inválido. Descartando...");
                }
            };

            channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});
        }
    }

    private boolean validarFormulario(String formulario) {
        return formulario.contains("nombre") && formulario.contains("edad") && formulario.contains("direccion");
    }

    private void procesarFormulario(String formulario) {
        System.out.println("[Consumidor] Formulario válido. Procesando...");
        // Aquí podrías agregar lógica adicional para procesar el formulario, como almacenarlo en una base de datos
    }

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 5; i++) { // Ejecutar 5 consumidores en paralelo
            new Thread(() -> {
                try {
                    FormularioConsumer consumer = new FormularioConsumer();
                    consumer.consumirFormularios();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}


    

