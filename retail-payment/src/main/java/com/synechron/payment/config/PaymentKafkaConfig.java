/**
 * 
 */
package com.synechron.payment.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.synechron.constants.Constants;
import com.synechron.event.OrderEvent;
import com.synechron.event.PaymentEvent;

/**
 * Payment Event Kafka Configuration
 * Order Event Consumer
 * Payment Event Producer
 * @author darshan
 *
 */
@Configuration
@EnableKafka
public class PaymentKafkaConfig {
	
	@Value("${kafka.url}")
	private String kafkaUrl;

	/**
	 * Order Event Consumer Configuration
	 * @return
	 */
	@Bean
	public ConsumerFactory<String, OrderEvent> orderPaymentConsumerFactory() {
		Map<String, Object> config = new HashMap<>();
		config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaUrl);
		config.put(ConsumerConfig.GROUP_ID_CONFIG, Constants.ORDER_PAYMENT_GROUP);
		config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		return new DefaultKafkaConsumerFactory<String, OrderEvent>(config, new StringDeserializer(), new JsonDeserializer<OrderEvent>(OrderEvent.class));
	}
	
	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, OrderEvent> orderPaymentListenerContainerFactory(){
		ConcurrentKafkaListenerContainerFactory<String, OrderEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(orderPaymentConsumerFactory());
		return factory;
	}
	
	/**
	 * Payment Event Producer Configuration
	 * @return
	 */
	@Bean
	public ProducerFactory<String, PaymentEvent> paymentProducerFactory() {
		Map<String, Object> config = new HashMap<>();
		config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaUrl);
		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		return new DefaultKafkaProducerFactory<>(config);
	}
	
	@Bean
	public KafkaTemplate<String, PaymentEvent> kafkaPaymentTemplate(){
		return new KafkaTemplate<>(paymentProducerFactory());
	}
}
