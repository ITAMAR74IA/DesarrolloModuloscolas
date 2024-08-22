package com.mycompany.rabbitmq;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMQManager {
    private ConnectionFactory factory;
    private Connection connection;

    public RabbitMQManager() {
        factory = new ConnectionFactory();
        factory.setHost("localhost"); // Cambiar si RabbitMQ no está en localhost
    }

    public void conectar() throws IOException, TimeoutException {
        connection = factory.newConnection();
    }

    public void desconectar() throws IOException {
        if (connection != null) {
            connection.close();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}

    
    

