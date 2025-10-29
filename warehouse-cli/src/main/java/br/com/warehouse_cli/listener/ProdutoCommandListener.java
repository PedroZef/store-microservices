package br.com.warehouse_cli.listener;

import br.com.warehouse_cli.config.RabbitMqConfig;
import br.com.warehouse_cli.Service.ProdutoService;
import br.com.warehouse_cli.model.Produto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProdutoCommandListener {

    @Autowired
    private ProdutoService produtoService;

    // Escuta a fila específica de CRIAR
    @RabbitListener(queues = RabbitMqConfig.QUEUE_PRODUTO_CREATE)
    public void onProdutoCriar(Produto produto) {
        System.out.println("[WAREHOUSE_CLI] Comando Recebido: produto.command.create");
        produtoService.saveAndNotify(produto);
    }

    // Escuta a fila específica de ATUALIZAR
    @RabbitListener(queues = RabbitMqConfig.QUEUE_PRODUTO_UPDATE)
    public void onProdutoAtualizar(Produto produto) {
        System.out.println("[WAREHOUSE_CLI] Comando Recebido: produto.command.update");
        // O saveAndNotify já lida com criação e atualização, então pode ser
        // reutilizado.
        produtoService.saveAndNotify(produto);
    }

    // Escuta a fila específica de DELETAR
    @RabbitListener(queues = RabbitMqConfig.QUEUE_PRODUTO_DELETE)
    public void onProdutoDeletar(Long id) {
        System.out.println("[WAREHOUSE_CLI] Comando Recebido: produto.command.delete com ID: " + id);
        try {
            produtoService.deleteById(id);
        } catch (Exception e) {
            System.err.println("[WAREHOUSE_CLI] Erro ao deletar produto com ID " + id + ": " + e.getMessage());
        }
    }
}