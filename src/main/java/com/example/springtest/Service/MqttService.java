package com.example.springtest.Service;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

// import javax.annotation.PostConstruct;
import jakarta.annotation.PostConstruct;
import java.util.concurrent.CompletableFuture;

@Component
public class MqttService {

    @Value("${mqtttest.username}")
    private String username;
    @Value("${mqtttest.password}")
    private String password;

    @Value("${mqtttest.url}")
    private String brokerUrl;

    @Value("${mqtt.client.id:spring_client}")
    private String clientId;

    private MqttClient mqttClient;
    private static final Logger logger = LoggerFactory.getLogger(MqttService.class);
    private boolean connected = false;

    @PostConstruct
    public void connect() {
        // 异步连接以避免阻塞应用程序启动
        CompletableFuture.runAsync(this::establishConnection);
    }

    private void establishConnection() {
        try {
            logger.info("Initializing MQTT client with broker: {}, clientId: {}", brokerUrl, clientId);
            mqttClient = new MqttClient(brokerUrl, clientId);
            MqttConnectOptions options = new MqttConnectOptions();
            System.out.println("username:" + username +"\r\n"+ "password:" + password.toString());
            options.setUserName(username);
            options.setPassword(password.toCharArray());
            // options.setAutomaticReconnect(true);
            // options.setCleanSession(true);
            // options.setConnectionTimeout(10);

            mqttClient.connect(options);
            mqttClient.subscribe("#", (topic, message) -> {
                logger.info("Received message on topic {}: {}", topic, new String(message.getPayload()));
            });

            connected = true;
            logger.info("Successfully connected to MQTT broker");
        } catch (MqttException e) {
            logger.error("Failed to initialize MQTT client: {}", e.getMessage(), e);
            connected = false;
        }
    }

    public void sendMessage(String topic, String message) throws MqttException {
        if (!connected || !mqttClient.isConnected()) {
            logger.warn("MQTT client is not connected. Cannot send message to topic: {}", topic);
            return;
        }

        MqttMessage mqttMessage = new MqttMessage(message.getBytes());
        mqttMessage.setQos(1);
        mqttClient.publish(topic, mqttMessage);
        logger.info("Message sent to topic {}: {}", topic, message);
    }

    public boolean isConnected() {
        return connected && mqttClient != null && mqttClient.isConnected();
    }

    public void disconnect() {
        try {
            if (mqttClient != null && mqttClient.isConnected()) {
                mqttClient.disconnect();
                mqttClient.close();
                connected = false;
                logger.info("Disconnected from MQTT broker");
            }
        } catch (MqttException e) {
            logger.error("Error disconnecting from MQTT broker: {}", e.getMessage(), e);
        }
    }
}