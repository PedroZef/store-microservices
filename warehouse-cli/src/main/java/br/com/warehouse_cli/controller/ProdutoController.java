package br.com.warehouse_cli.controller;

import br.com.warehouse_cli.Service.ProdutoService;
import br.com.warehouse_cli.model.Produto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/produtos")
@Tag(name = "API de Produtos", description = "Endpoints para consulta e manipulação de Produtos.")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @Operation(summary = "Lista todos os produtos (usado pelo Storefront na carga inicial)")
    @GetMapping
    public List<Produto> getAllProdutos() {
        return produtoService.findAll();
    }

    @Operation(summary = "Busca um produto por ID (usado pelo Storefront para editar)")
    @GetMapping("/{id}")
    public ResponseEntity<Produto> getProdutoById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(produtoService.findById(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Cria um novo produto")
    @PostMapping
    public Produto createProduto(@RequestBody Produto produto) {
        return produtoService.saveAndNotify(produto);
    }

    @Operation(summary = "Atualiza um produto existente")
    @PutMapping("/{id}")
    public ResponseEntity<Produto> updateProduto(@PathVariable Long id, @RequestBody Produto produtoDetails) {
        try {
            Produto produto = produtoService.findById(id);
            produto.setNome(produtoDetails.getNome());
            produto.setDescricao(produtoDetails.getDescricao());
            produto.setPreco(produtoDetails.getPreco());
            produto.setEstoque(produtoDetails.getEstoque());
            final Produto updatedProduto = produtoService.saveAndNotify(produto);
            return ResponseEntity.ok(updatedProduto);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Deleta um produto")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduto(@PathVariable Long id) {
        try {
            produtoService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}