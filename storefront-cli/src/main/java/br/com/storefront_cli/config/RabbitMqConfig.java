package br.com.storefront_cli.config;

import br.com.storefront_cli.model.Produto;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMqConfig {

    // === FLUXO 1: WAREHOUSE -> STOREFRONT (Consumidor) ===
    public static final String EXCHANGE_PRODUTO = "produto-exchange";
    public static final String QUEUE_PRODUTO_ATUALIZADO = "storefront-produto-atualizado-queue";
    public static final String QUEUE_PRODUTO_DELETADO = "storefront-produto-deletado-queue";
    public static final String ROUTING_KEY_PRODUTO_ATUALIZADO = "produto.atualizado";
    public static final String ROUTING_KEY_PRODUTO_DELETADO = "produto.deletado";

    @Bean
    TopicExchange produtoExchange() {
        return new TopicExchange(EXCHANGE_PRODUTO);
    }

    @Bean
    Queue produtoAtualizadoQueue() {
        return new Queue(QUEUE_PRODUTO_ATUALIZADO, true);
    }

    @Bean
    Queue produtoDeletadoQueue() {
        return new Queue(QUEUE_PRODUTO_DELETADO, true);
    }

    @Bean
    Binding produtoAtualizadoBinding(Queue produtoAtualizadoQueue, TopicExchange produtoExchange) {
        return BindingBuilder.bind(produtoAtualizadoQueue).to(produtoExchange).with(ROUTING_KEY_PRODUTO_ATUALIZADO);
    }

    @Bean
    Binding produtoDeletadoBinding(Queue produtoDeletadoQueue, TopicExchange produtoExchange) {
        return BindingBuilder.bind(produtoDeletadoQueue).to(produtoExchange).with(ROUTING_KEY_PRODUTO_DELETADO);
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
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        DefaultClassMapper classMapper = new DefaultClassMapper();
        classMapper.setTrustedPackages("*");

        Map<String, Class<?>> idClassMapping = new HashMap<>();
        // Quando a String "br.com.warehouse_cli.model.Produto" chegar...
        // ...use a classe local "br.com.storefront_cli.model.Produto.class"
        idClassMapping.put("br.com.warehouse_cli.model.Produto", Produto.class);

        classMapper.setIdClassMapping(idClassMapping);
        converter.setClassMapper(classMapper);

        return converter;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}