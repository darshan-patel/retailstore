/**
 * 
 */
package com.synechron.order.config;

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

import com.synechron.event.InventoryEvent;
import com.synechron.event.OrderEvent;
import com.synechron.event.PaymentEvent;
import static com.synechron.constants.Constants.*;

/**
 * Order Event Kafka Configuration
 * @author darshan
 *
 */
@Configuration
@EnableKafka
public class OrderKafkaConfig {

	@Value("${kafka.url}")
	private String kafkaUrl;

	/**
	 * Order Event Producer Configuration
	 * 
	 * @return
	 */
	@Bean
	public ProducerFactory<String, OrderEvent> orderProducerFactory() {
		Map<String, Object> config = new HashMap<>();
		config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaUrl);
		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		return new DefaultKafkaProducerFactory<>(config);
	}

	@Bean
	public KafkaTemplate<String, OrderEvent> kafkaOrderTemplate() {
		return new KafkaTemplate<>(orderProducerFactory());
	}

	/**
	 * Inventory Event Consumer configuration
	 * @return
	 */
	@Bean
	public ConsumerFactory<String, InventoryEvent> inventoryConsumerFactory() {
		Map<String, Object> config = new HashMap<>();
		config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaUrl);
		config.put(ConsumerConfig.GROUP_ID_CONFIG, INVENTORY_GROUP);
		config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		return new DefaultKafkaConsumerFactory<String, InventoryEvent>(config, new StringDeserializer(),
				new JsonDeserializer<InventoryEvent>(InventoryEvent.class));
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, InventoryEvent> inventoryListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, InventoryEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(inventoryConsumerFactory());
		return factory;
	}

	/**
	 * Payment Event Consumer configuration
	 * @return
	 */
	@Bean
	public ConsumerFactory<String, PaymentEvent> paymentConsumerFactory() {
		Map<String, Object> config = new HashMap<>();
		config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaUrl);
		config.put(ConsumerConfig.GROUP_ID_CONFIG, PAYMENT_GROUP);
		config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		return new DefaultKafkaConsumerFactory<String, PaymentEvent>(config, new StringDeserializer(),
				new JsonDeserializer<PaymentEvent>(PaymentEvent.class));
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, PaymentEvent> paymentListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, PaymentEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(paymentConsumerFactory());
		return factory;
	}
}