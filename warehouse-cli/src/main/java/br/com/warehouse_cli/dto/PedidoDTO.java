package br.com.warehouse_cli.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {

    private Long produtoId;
    private int quantidade;
    // (Poderia ter userId, etc.)
}
