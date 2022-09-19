package com.nipi.blacklog.config;

import lombok.Getter;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@EnableRabbit
@Configuration
public class RabbitMQConfig {

	@Value("${rabbitmq.excel.exchange}")
	private String excelExchange;

	@Value("${rabbitmq.excel.queue.parsing}")
	private String excelParsingQueue;

	@Value("${rabbitmq.excel.routing_key.parsing}")
	private String excelParsingRoutingKey;

	@Bean
	public DirectExchange excelExchange() {
		return new DirectExchange(excelExchange);
	}

	@Bean
	public Queue parsingQueue() {
		return new Queue(excelParsingQueue);
	}

	@Bean
	public Binding excelParsingBinding() {
		return BindingBuilder
				.bind(parsingQueue())
				.to(excelExchange())
				.with(excelParsingRoutingKey);
	}

	@Bean
	public MessageConverter messageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	public AmqpTemplate template(ConnectionFactory connectionFactory) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(messageConverter());
		return rabbitTemplate;
	}
}
