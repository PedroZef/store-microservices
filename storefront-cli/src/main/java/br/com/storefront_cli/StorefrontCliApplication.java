package br.com.storefront_cli;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@EnableCaching // Habilita o cache
@SpringBootApplication
public class StorefrontCliApplication {

	public static void main(String[] args) {
		SpringApplication.run(StorefrontCliApplication.class, args);
		System.out.println("Storefront_CLI iniciado com sucesso!");
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
