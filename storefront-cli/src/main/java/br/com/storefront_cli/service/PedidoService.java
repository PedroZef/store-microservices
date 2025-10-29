package br.com.storefront_cli.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.storefront_cli.config.RabbitMqConfig;
import br.com.storefront_cli.dto.PedidoDTO;

@Service
public class PedidoService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void enviarPedido(Long produtoId, int quantidade) {
        PedidoDTO pedido = new PedidoDTO(produtoId, quantidade);

        System.out.println("[STOREFRONT_CLI] Command (AMQP): Enviando pedido.criado");

        rabbitTemplate.convertAndSend(
                RabbitMqConfig.EXCHANGE_PEDIDO,
                RabbitMqConfig.ROUTING_KEY_PEDIDO,
                pedido);
    }
}