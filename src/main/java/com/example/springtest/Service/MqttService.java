    package com.example.springtest.Service;
    
    import org.eclipse.paho.client.mqttv3.MqttClient;
    import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
    import org.eclipse.paho.client.mqttv3.MqttException;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.stereotype.Component;
    
    @Component
    public class MqttService {
        
        @Value("${mqtttest.username}")
        private String username;
        @Value("${mqtttest.password}")
        private String password;
        
        private MqttClient mqttClient;
        private static final Logger logger = LoggerFactory.getLogger(MqttService.class);
        
        public MqttService(@Value("${mqtttest.url}") String brokerUrl,
                           @Value("${mqtt.client.id:spring_client}") String clientId) {
            try {
                logger.info("Initializing MQTT client with broker: {}, clientId: {}", brokerUrl, clientId);
                mqttClient = new MqttClient(brokerUrl, clientId);
                MqttConnectOptions options = new MqttConnectOptions();
                options.setUserName(username);
                options.setPassword(password.toCharArray());
                mqttClient.connect(options);
                mqttClient.subscribe("#");
            } catch (MqttException e) {
                logger.error("Failed to initialize MQTT client: {}", e.getMessage(), e);
                // 可以选择不抛出异常，而是标记连接失败状态
                throw new RuntimeException("Failed to initialize MQTT client", e);
            }
        }
    }