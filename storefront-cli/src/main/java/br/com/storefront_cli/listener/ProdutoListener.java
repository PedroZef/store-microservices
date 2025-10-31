package br.com.storefront_cli.listener;

import br.com.storefront_cli.model.Produto;
import br.com.storefront_cli.config.RabbitMqConfig;
import br.com.storefront_cli.service.ProdutoCacheService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate; // WebSocket
import org.springframework.stereotype.Component;

@Component
public class ProdutoListener {

    @Autowired
    private ProdutoCacheService cacheService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate; // WebSocket

    // Escuta SÓ 'produto.atualizado' e espera um 'Produto'
    @RabbitListener(queues = RabbitMqConfig.QUEUE_PRODUTO_ATUALIZADO)
    public void onProdutoAtualizado(Produto produto) {
        System.out.println("[STOREFRONT] Evento Recebido: produto.atualizado");
        cacheService.invalidarCacheProdutos();
        messagingTemplate.convertAndSend("/topic/produtos", produto);
    }

    // Escuta SÓ 'produto.deletado' e espera um 'Long'
    @RabbitListener(queues = RabbitMqConfig.QUEUE_PRODUTO_DELETADO)
    public void onProdutoDeletado(Long id) {
        System.out.println("[STOREFRONT] Evento Recebido: produto.deletado");
        cacheService.invalidarCacheProdutos();
        messagingTemplate.convertAndSend("/topic/produtos/deletado", id);
    }
}