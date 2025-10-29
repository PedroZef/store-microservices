# store-microservices

-   Loja Store Micro-services é um projeto com arquitetura de microserviços reativa usando Java + Spring Boot que se comunicam síncrona(via http) e assíncrona(via RabbitMQ).

## Arquitetura

O sistema é composto por dois microserviços:

1.  **`warehouse/` (Porta 8084)**

    -   O "Armazém" (backend).
    -   [http://localhost:8084/swagger-ui/index.html]
    -   Gerencia o estado dos produtos (no banco H2).
    -   Expõe uma API REST para CRUD dos produtos( `GET` `POST` `PUT` `DELETE`) (`/api/produtos`).
    -   Escuta (Consome) "Comandos" do RabbitMQ (ex: `pedido.criado`, `produto.command.create`).
    -   Publica "Eventos" no RabbitMQ (ex: `produto.atualizado`, `produto.deletado`).

2.  **`storefront/` (Porta 8082)**
    -   A "Vitrine" (frontend).
    -   Serve a UI (painel de admin) em `http://localhost:8082`.
    -   Publica "Comandos" no RabbitMQ (ex: quando o usuário clica em "Adicionar", "Comprar", "Deletar" e "Editar").
    -   Escuta (Consome) "Eventos" do RabbitMQ para atualizar a UI em tempo real via WebSocket.

### Como Rodar

**Pré-requisitos:**

-   Java 17+
-   RabbitMQ via Local (rodando em `localhost:5672`)

1.  **Terminal 1 (Warehouse):**

    ```bash
    cd warehouse
    ./gradlew bootRun
    ```

2.  **Terminal 2 (Storefront):**
    ```bash
    cd storefront
    ./gradlew bootRun
    ```

### Endpoints Úteis

-   **Monitor do RabbitMQ:** `http://localhost:15672/` (user: guest / pass: guest)
-   **Vitrine / Painel Admin:** `http://localhost:8082/`
-   **Visulalização dos Produtos(JSON) do Warehouse:** `http://localhost:8084/api/produtos`
-   **API (Swagger) do Warehouse:** `http://localhost:8084/swagger-ui.html`
