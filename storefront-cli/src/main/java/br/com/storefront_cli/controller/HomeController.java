package br.com.storefront_cli.controller;

import br.com.storefront_cli.service.ProdutoCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private ProdutoCacheService produtoCacheService;

    // A ÚNICA RESPONSABILIDADE DESTE ARQUIVO É A PÁGINA

    @GetMapping("/")
    public String getHomePage(Model model) {
        model.addAttribute("produtos", produtoCacheService.getProdutos());
        return "index";
    }

}
