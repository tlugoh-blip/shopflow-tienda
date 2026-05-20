package co.shopflow.tienda.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "pedidos-exchange";
    public static final String QUEUE_FACTURACION = "cola-facturacion";
    public static final String QUEUE_LOGISTICA = "cola-logistica";

    @Bean
    public FanoutExchange exchange() {
        return new FanoutExchange(EXCHANGE);
    }

    @Bean
    public Queue queueFacturacion() {
        return new Queue(QUEUE_FACTURACION, true);
    }

    @Bean
    public Queue queueLogistica() {
        return new Queue(QUEUE_LOGISTICA, true);
    }

    @Bean
    public Binding bindingFacturacion() {
        return BindingBuilder.bind(queueFacturacion()).to(exchange());
    }

    @Bean
    public Binding bindingLogistica() {
        return BindingBuilder.bind(queueLogistica()).to(exchange());
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}