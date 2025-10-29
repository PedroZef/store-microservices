package br.com.storefront_cli.controller;

import br.com.storefront_cli.service.ProdutoCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import br.com.storefront_cli.model.Produto;
import br.com.storefront_cli.service.ProdutoApiService;

@Controller
public class ProdutoMvcController {

    @Autowired
    private ProdutoApiService apiService;

    @Autowired
    private ProdutoCacheService cacheService;

    @GetMapping("/")
    public String viewHomePage(Model model) {
        model.addAttribute("produtos", cacheService.getProdutos());
        return "index";
    }

    // Recebe o POST do formulário "Adicionar Novo Produto"
    @PostMapping("/criar")
    public String createProduto(@ModelAttribute Produto produto) {
        apiService.createProduto(produto); // Envia comando via RabbitMQ
        cacheService.invalidarCacheProdutos(); // Força a invalidação do cache
        return "redirect:/"; // Redireciona imediatamente
    }

    // Mostra a página de Edição (faz uma consulta HTTP)
    @GetMapping("/editar/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Produto produto = apiService.getProdutoById(id); // Query HTTP
        model.addAttribute("produto", produto);
        return "edit-produto";
    }

    // Recebe o POST do formulário "Atualizar"
    @PostMapping("/atualizar/{id}")
    public String updateProduto(@PathVariable Long id, @ModelAttribute Produto produto) {
        apiService.updateProduto(id, produto); // Envia comando via RabbitMQ
        cacheService.invalidarCacheProdutos(); // Força a invalidação do cache
        return "redirect:/";
    }

    // Recebe o POST do formulário "Deletar"
    @PostMapping("/deletar/{id}")
    public String deleteProduto(@PathVariable Long id) {
        apiService.deleteProduto(id); // Envia comando via RabbitMQ
        cacheService.invalidarCacheProdutos(); // Força a invalidação do cache
        return "redirect:/";
    }
}