package br.com.storefront_cli.controller;

import br.com.storefront_cli.config.RabbitMqConfig;
import br.com.storefront_cli.model.Produto;
import br.com.storefront_cli.service.ProdutoCacheService;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class HomeController {

    @Autowired
    private ProdutoCacheService cacheService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // Escuta por ATUALIZAÇÕES/CRIAÇÕES
    @RabbitListener(queues = RabbitMqConfig.QUEUE_PRODUTO)
    public void onProdutoAtualizado(@Payload Produto produto) {
        System.out.println("[STOREFRONT_CLI] Evento Recebido: produto.atualizado");
        cacheService.invalidarCacheProdutos();
        // Envia o PRODUTO INTEIRO para o WebSocket
        messagingTemplate.convertAndSend("/topic/produtos", produto);
    }

    // Escuta por DELEÇÕES
    @RabbitListener(queues = RabbitMqConfig.QUEUE_PRODUTO)
    public void onProdutoDeletado(@Payload Long id) {
        System.out.println("[STOREFRONT_CLI] Evento Recebido: produto.deletado");
        cacheService.invalidarCacheProdutos();
        // Envia apenas o ID para o WebSocket
        messagingTemplate.convertAndSend("/topic/produtos/deletado", id);
    }

}
