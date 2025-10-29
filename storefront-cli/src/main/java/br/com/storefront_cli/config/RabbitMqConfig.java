package br.com.storefront_cli.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMqConfig {

    // === FLUXO 1: WAREHOUSE -> STOREFRONT (Consumidor) ===
    public static final String EXCHANGE_PRODUTO = "produto-exchange";
    public static final String QUEUE_PRODUTO = "storefront-produto-queue";
    public static final String ROUTING_KEY_PRODUTO_ATUALIZADO = "produto.atualizado";
    public static final String ROUTING_KEY_PRODUTO_DELETADO = "produto.deletado";

    @Bean
    TopicExchange produtoExchange() {
        return new TopicExchange(EXCHANGE_PRODUTO);
    }

    @Bean
    Queue produtoQueue() {
        return new Queue(QUEUE_PRODUTO, true);
    }

    @Bean
    Binding produtoAtualizadoBinding(Queue produtoQueue, TopicExchange produtoExchange) {
        return BindingBuilder.bind(produtoQueue).to(produtoExchange).with(ROUTING_KEY_PRODUTO_ATUALIZADO);
    }

    @Bean
    Binding produtoDeletadoBinding(Queue produtoQueue, TopicExchange produtoExchange) {
        return BindingBuilder.bind(produtoQueue).to(produtoExchange).with(ROUTING_KEY_PRODUTO_DELETADO);
    }

    // === FLUXO 2: STOREFRONT -> WAREHOUSE (Produtor) ===
    public static final String EXCHANGE_PEDIDO = "pedido-exchange";
    public static final String QUEUE_PEDIDO = "warehouse-pedido-queue";
    public static final String ROUTING_KEY_PEDIDO = "pedido.criado";

    @Bean
    TopicExchange pedidoExchange() {
        return new TopicExchange(EXCHANGE_PEDIDO);
    }

    // (Declaramos a fila do warehouse para garantir que ela exista)
    @Bean
    Queue pedidoQueue() {
        return new Queue(QUEUE_PEDIDO, true);
    }

    @Bean
    Binding pedidoBinding(Queue pedidoQueue, TopicExchange pedidoExchange) {
        return BindingBuilder.bind(pedidoQueue).to(pedidoExchange).with(ROUTING_KEY_PEDIDO);
    }

    // === CONFIGURAÇÃO UNIVERSAL (JSON) ===
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}