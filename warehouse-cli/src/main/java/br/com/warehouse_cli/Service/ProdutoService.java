package br.com.warehouse_cli.Service;

import br.com.warehouse_cli.config.RabbitMqConfig;
import br.com.warehouse_cli.model.Produto;
import br.com.warehouse_cli.repository.ProdutoRepository;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public Produto findById(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Produto não encontrado: " + id));
    }

    public List<Produto> findAll() {
        return produtoRepository.findAll();
    }

    // Método para salvar E notificar (usado pelos listeners)
    @Transactional
    public Produto saveAndNotify(Produto produto) {
        Produto produtoSalvo = produtoRepository.save(produto);

        System.out.println("[WAREHOUSE_CLI] Evento: Enviando produto.atualizado para ID " + produtoSalvo.getId());
        // Envia o EVENTO de atualização
        rabbitTemplate.convertAndSend(
                RabbitMqConfig.EXCHANGE_PRODUTO,
                RabbitMqConfig.ROUTING_KEY_PRODUTO_ATUALIZADO,
                produtoSalvo);
        return produtoSalvo;
    }

    // Método para deletar E notificar (usado pelos listeners)
    @Transactional
    public void deleteById(Long id) {
        Produto produto = findById(id); // Garante que existe
        produtoRepository.delete(produto);

        System.out.println("[WAREHOUSE_CLI] Evento: Enviando produto.deletado para ID " + id);
        // Envia o EVENTO de deleção
        rabbitTemplate.convertAndSend(
                RabbitMqConfig.EXCHANGE_PRODUTO,
                RabbitMqConfig.ROUTING_KEY_PRODUTO_DELETADO,
                id);
    }
}