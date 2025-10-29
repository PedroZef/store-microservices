package br.com.storefront_cli.listener;

import br.com.storefront_cli.config.RabbitMqConfig;
import br.com.storefront_cli.model.Produto;
import br.com.storefront_cli.service.ProdutoApiService;
import br.com.storefront_cli.service.ProdutoCacheService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate; // WebSocket
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = RabbitMqConfig.QUEUE_PRODUTO)
public class ProdutoListener {

    @Autowired
    private ProdutoCacheService cacheService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate; // WebSocket
    @Autowired
    private ProdutoApiService apiService; // Para buscar o produto atualizado

    @RabbitHandler
    public void onProdutoAtualizado(Produto produto) { // Alterado para receber o objeto Produto
        System.out.println(
                "\n[STOREFRONT_CLI] 1. Mensagem RabbitMQ Recebida (Atualização para o Produto): " + produto.getNome());
        try {
            // O produto já foi recebido na mensagem, não é necessário buscar via API.
            System.out.println("[STOREFRONT_CLI] 2. Produto recebido via RabbitMQ: " + produto.getNome());

            // Envia o PRODUTO INTEIRO para o tópico de atualização
            messagingTemplate.convertAndSend("/topic/produtos", produto);
            System.out.println("[STOREFRONT_CLI] 4. Mensagem WebSocket enviada para /topic/produtos.");

        } catch (Exception e) {
            System.err.println("[STOREFRONT_CLI] ERRO ao processar mensagem de atualização: " + e.getMessage());
        }
    }

    @RabbitHandler
    public void onProdutoDeletado(Long id) {
        System.out.println("\n[STOREFRONT_CLI] 1. Mensagem RabbitMQ Recebida (Deleção): ID " + id);
        try {
            cacheService.invalidarCacheProdutos();
            System.out.println("[STOREFRONT_CLI] 2. Cache de produtos invalidado.");

            // Envia apenas o ID para o tópico de deleção
            messagingTemplate.convertAndSend("/topic/produtos/deletado", id);
            System.out.println("[STOREFRONT_CLI] 3. Mensagem WebSocket enviada para /topic/produtos/deletado.");

        } catch (Exception e) {
            System.err.println("[STOREFRONT_CLI] ERRO ao processar mensagem de deleção: " + e.getMessage());
        }
    }

}
