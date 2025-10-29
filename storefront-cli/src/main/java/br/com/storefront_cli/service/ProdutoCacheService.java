package br.com.storefront_cli.service;

import br.com.storefront_cli.model.Produto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;

@Service
public class ProdutoCacheService {

    @Autowired
    private ProdutoApiService produtoApiService;

    @Cacheable("produtos")
    public List<Produto> getProdutos() {
        System.out.println("[STOREFRONT] Cache MISS. Buscando na API Warehouse (HTTP)...");
        try {
            // Chama o serviço de API
            Produto[] produtos = produtoApiService.getProdutos();
            return produtos != null ? Arrays.asList(produtos) : List.of();
        } catch (Exception e) {
            System.err.println("[STOREFRONT] Erro ao buscar produtos: " + e.getMessage());
        }
        return List.of();
    }

    @CacheEvict(value = "produtos", allEntries = true)
    public void invalidarCacheProdutos() {
        System.out.println("[STOREFRONT] Cache 'produtos' invalidado.");
    }

}