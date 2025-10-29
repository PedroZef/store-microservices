package br.com.storefront_cli.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Produto {

    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private int estoque;
}