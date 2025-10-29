package br.com.warehouse_cli.Service;

import br.com.warehouse_cli.model.Produto;
import br.com.warehouse_cli.dto.PedidoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PedidoService {

    @Autowired
    private ProdutoService produtoService; // Usa o outro serviço

    // Esta é a Regra de Negócio principal
    @Transactional
    public void processarPedido(PedidoDTO pedido) {
        System.out.println("[WAREHOUSE_CLI] Processando pedido para " + pedido.getQuantidade() + "x Produto ID "
                + pedido.getProdutoId());

        try {
            Produto produto = produtoService.findById(pedido.getProdutoId());

            if (produto.getEstoque() >= pedido.getQuantidade()) {
                produto.setEstoque(produto.getEstoque() - pedido.getQuantidade());
                // Salva E Notifica (o saveAndNotify envia o evento)
                produtoService.saveAndNotify(produto);
                System.out.println("[WAREHOUSE_CLI] Pedido APROVADO. Novo estoque: " + produto.getEstoque());
            } else {
                System.err.println("[WAREHOUSE_CLI] Pedido REJEITADO. Estoque insuficiente.");
            }
        } catch (Exception e) {
            System.err.println("[WAREHOUSE_CLI] Erro ao processar pedido: " + e.getMessage());
        }
    }
}