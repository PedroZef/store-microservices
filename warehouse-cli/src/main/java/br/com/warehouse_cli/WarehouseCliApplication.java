package br.com.warehouse_cli;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import br.com.warehouse_cli.model.Produto;
import br.com.warehouse_cli.repository.ProdutoRepository;

@SpringBootApplication
public class WarehouseCliApplication {

	public static void main(String[] args) {
		SpringApplication.run(WarehouseCliApplication.class, args);
	}

	@Bean
	CommandLineRunner initDatabase(ProdutoRepository produtoRepository) {
		return args -> {
			System.out.println("\n[WAREHOUSE_CLI] Populando banco de dados com produtos de exemplo...");

			Produto p1 = new Produto();
			p1.setNome("Notebook Gamer");
			p1.setDescricao("Notebook de alta performance para jogos.");
			p1.setPreco(new BigDecimal("7500.00"));
			p1.setEstoque(10);

			Produto p2 = new Produto();
			p2.setNome("Mouse sem Fio");
			p2.setDescricao("Mouse ergonômico com conexão Bluetooth.");
			p2.setPreco(new BigDecimal("150.00"));
			p2.setEstoque(50);

			Produto p3 = new Produto();
			p3.setNome("Teclado Mecânico");
			p3.setDescricao("Teclado com switches mecânicos e RGB.");
			p3.setPreco(new BigDecimal("450.00"));
			p3.setEstoque(25);

			produtoRepository.save(p1);
			produtoRepository.save(p2);
			produtoRepository.save(p3);

			System.out.println("[WAREHOUSE_CLI] Produtos salvos no banco de dados.");
		};
	}
}
