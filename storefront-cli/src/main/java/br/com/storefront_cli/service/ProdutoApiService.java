package br.com.storefront_cli.service;

import br.com.storefront_cli.model.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProdutoApiService {

    @Autowired
    private RestTemplate restTemplate;

    private final String WAREHOUSE_CLI_API_URL = "http://localhost:8085/api/produtos";

    // --- QUERIES (Leitura via HTTP) ---
    public Produto[] getProdutos() {
        System.out.println("[STOREFRONT_CLI] Query (HTTP): Buscando todos os produtos");
        return restTemplate.getForObject(WAREHOUSE_CLI_API_URL, Produto[].class);
    }

    public Produto getProdutoById(Long id) {
        System.out.println("[STOREFRONT] Query (HTTP): Buscando produto " + id);
        return restTemplate.getForObject(WAREHOUSE_CLI_API_URL + "/" + id, Produto.class);
    }

    // --- COMMANDS (Escrita via HTTP) ---

    public void createProduto(Produto produto) {
        System.out.println("[STOREFRONT] Command (HTTP): Criando produto via REST");
        restTemplate.postForObject(WAREHOUSE_CLI_API_URL, produto, Produto.class);
    }

    public void updateProduto(Long id, Produto produto) {
        produto.setId(id); // Garante que o ID esteja no objeto
        System.out.println("[STOREFRONT] Command (HTTP): Atualizando produto via REST: " + id);
        restTemplate.put(WAREHOUSE_CLI_API_URL + "/" + id, produto);
    }

    public void deleteProduto(Long id) {
        System.out.println("[STOREFRONT] Command (HTTP): Deletando produto via REST: " + id);
        restTemplate.delete(WAREHOUSE_CLI_API_URL + "/" + id);
    }
}
