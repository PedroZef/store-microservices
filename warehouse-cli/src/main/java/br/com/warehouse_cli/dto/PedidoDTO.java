package br.com.warehouse_cli.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Objeto simples para receber a mensagem do pedido
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {

    private Long produtoId;
    private int quantidade;
    // (Poderia ter userId, etc.)
}
