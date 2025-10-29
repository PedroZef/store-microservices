package br.com.warehouse_cli.listener;

import br.com.warehouse_cli.dto.PedidoDTO;
import br.com.warehouse_cli.Service.PedidoService;
import br.com.warehouse_cli.config.RabbitMqConfig;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PedidoListener {

    @Autowired
    private PedidoService pedidoService;

    // Escuta a fila de pedidos
    @RabbitListener(queues = RabbitMqConfig.QUEUE_PEDIDO)
    public void onPedidoRecebido(PedidoDTO pedido) {
        System.out.println("[WAREHOUSE_CLI] Comando Recebido: pedido.criado");
        pedidoService.processarPedido(pedido);
    }
}