package br.com.storefront_cli.controller;

import br.com.storefront_cli.service.PedidoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    // Recebe o POST do formulário "Comprar"
    @PostMapping("/pedir")
    public String fazerPedido(@RequestParam("produtoId") Long produtoId,
            @RequestParam("quantidade") int quantidade) {

        pedidoService.enviarPedido(produtoId, quantidade);

        // Redireciona imediatamente (Fire-and-forget)
        return "redirect:/";
    }
}