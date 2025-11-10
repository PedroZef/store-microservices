package br.com.warehouse_cli.config;

import br.com.warehouse_cli.dto.PedidoDTO;
import br.com.warehouse_cli.model.Produto;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMqConfig {

    // === VIA 1: Eventos (Warehouse -> Storefront) ===
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
    Binding produtoAtualizadoBinding(@Qualifier("produtoQueue") Queue produtoQueue, TopicExchange produtoExchange) {
        return BindingBuilder.bind(produtoQueue).to(produtoExchange).with(ROUTING_KEY_PRODUTO_ATUALIZADO);
    }

    @Bean
    Binding produtoDeletadoBinding(Queue produtoQueue, TopicExchange produtoExchange) {
        return BindingBuilder.bind(produtoQueue).to(produtoExchange).with(ROUTING_KEY_PRODUTO_DELETADO);
    }

    // === VIA 2: Comandos de Pedido (Storefront -> Warehouse) ===
    public static final String EXCHANGE_PEDIDO = "pedido-exchange";
    public static final String QUEUE_PEDIDO = "warehouse-pedido-queue";
    public static final String ROUTING_KEY_PEDIDO = "pedido.criado";

    @Bean
    TopicExchange pedidoExchange() {
        return new TopicExchange(EXCHANGE_PEDIDO);
    }

    @Bean
    Queue pedidoQueue() {
        return new Queue(QUEUE_PEDIDO, true);
    }

    @Bean
    Binding pedidoBinding(Queue pedidoQueue, TopicExchange pedidoExchange) {
        return BindingBuilder.bind(pedidoQueue).to(pedidoExchange).with(ROUTING_KEY_PEDIDO);
    }

    // === VIA 3: Comandos de CRUD de Produto (Externo -> Warehouse) ===
    public static final String EXCHANGE_PRODUTO_COMMAND = "produto-command-exchange";

    // Filas e chaves para CRIAR
    public static final String QUEUE_PRODUTO_CREATE = "produto-command-create-queue";
    public static final String ROUTING_KEY_PRODUTO_CREATE = "produto.command.create";

    // Filas e chaves para ATUALIZAR
    public static final String QUEUE_PRODUTO_UPDATE = "produto-command-update-queue";
    public static final String ROUTING_KEY_PRODUTO_UPDATE = "produto.command.update";

    // Filas e chaves para DELETAR
    public static final String QUEUE_PRODUTO_DELETE = "produto-command-delete-queue";
    public static final String ROUTING_KEY_PRODUTO_DELETE = "produto.command.delete";

    @Bean
    TopicExchange produtoCommandExchange() {
        return new TopicExchange(EXCHANGE_PRODUTO_COMMAND);
    }

    // Beans para CRIAR
    @Bean
    Queue produtoCreateQueue() {
        return new Queue(QUEUE_PRODUTO_CREATE, true);
    }

    @Bean
    Binding produtoCreateBinding(Queue produtoCreateQueue, TopicExchange produtoCommandExchange) {
        return BindingBuilder.bind(produtoCreateQueue).to(produtoCommandExchange).with(ROUTING_KEY_PRODUTO_CREATE);
    }

    // Beans para ATUALIZAR
    @Bean
    Queue produtoUpdateQueue() {
        return new Queue(QUEUE_PRODUTO_UPDATE, true);
    }

    @Bean
    Binding produtoUpdateBinding(Queue produtoUpdateQueue, TopicExchange produtoCommandExchange) {
        return BindingBuilder.bind(produtoUpdateQueue).to(produtoCommandExchange).with(ROUTING_KEY_PRODUTO_UPDATE);
    }

    // Beans para DELETAR
    @Bean
    Queue produtoDeleteQueue() {
        return new Queue(QUEUE_PRODUTO_DELETE, true);
    }

    @Bean
    Binding produtoDeleteBinding(Queue produtoDeleteQueue, TopicExchange produtoCommandExchange) {
        return BindingBuilder.bind(produtoDeleteQueue).to(produtoCommandExchange).with(ROUTING_KEY_PRODUTO_DELETE);
    }

    // === FLUXO 3: WAREHOUSE -> WAREHOUSE UI (Consumidor de Eventos para UI) ===
    public static final String QUEUE_ESTOQUE_UI = "warehouse-estoque-ui-queue";

    @Bean
    Queue estoqueUiQueue() {
        return new Queue(QUEUE_ESTOQUE_UI, true);
    }

    @Bean
    Binding estoqueUiBinding(Queue estoqueUiQueue, TopicExchange produtoExchange) {
        return BindingBuilder.bind(estoqueUiQueue).to(produtoExchange).with(ROUTING_KEY_PRODUTO_ATUALIZADO);
    }

    // === Configuração Universal de JSON ===
    @Bean
    public MessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        DefaultClassMapper classMapper = new DefaultClassMapper();
        classMapper.setTrustedPackages("*");

        Map<String, Class<?>> idClassMapping = new HashMap<>();

        idClassMapping.put("br.com.storefront_cli.dto.PedidoDTO", PedidoDTO.class);

        // Quando o TypeID do storefront chegar, converta para a classe local do
        // warehouse
        idClassMapping.put("br.com.storefront_cli.model.Produto", Produto.class);

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