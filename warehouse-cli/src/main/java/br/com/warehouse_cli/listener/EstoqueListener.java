package br.com.warehouse_cli.listener;

import br.com.warehouse_cli.config.RabbitMqConfig;
import br.com.warehouse_cli.model.Produto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class EstoqueListener {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * Este listener recebe atualizações de produtos (incluindo de estoque)
     * e as retransmite para o front-end (a página do warehouse) via WebSocket.
     */
    @RabbitListener(queues = RabbitMqConfig.QUEUE_ESTOQUE_UI)
    public void onEstoqueAtualizado(Produto produto) {
        System.out.println(
                "[WAREHOUSE_CLI] Evento de produto recebido para retransmitir via WebSocket: " + produto.getNome());

        // Envia o produto atualizado para o tópico WebSocket
        // O front-end estará ouvindo neste tópico.
        messagingTemplate.convertAndSend("/topic/estoque", produto);

        System.out.println("[WAREHOUSE-CLI] Mensagem enviada para o WebSocket /topic/estoque");
    }
}
