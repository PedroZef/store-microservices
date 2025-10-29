# store-microservices

-   Loja Store Micro-services é um projeto com arquitetura de microserviços reativa usando Java + Spring Boot que se comunicam síncrona(via http) e assíncrona(via RabbitMQ).

## 🛠️ Tecnologias Utilizadas

-   Java 17 ou maior
-   Spring Boot 3.1
-   Maven
-   RabbitMQ + Erlang
-   Spring AMQP
-   Lombok
-   DevTools
-   H2 Database (opcional para persistência)

## **Pré-requisitos:**

### 🐇 Instalação do RabbitMQ no Windows 11

### 1. Instalar RabbitMQ

-   Baixe em: https://www.rabbitmq.com/install-windows.html
-   Após instalar, execute no terminal:

```bash
rabbitmq-service install
rabbitmq-service start
rabbitmq-service status
rabbitmq-service stop

```

### 2. Instalar Erlang

-   Baixe em: https://www.erlang.org/downloads
-   Instale e adicione ao PATH do sistema

-   RabbitMQ depende do Erlang para funcionar.

-   Baixe a versão recomendada para Windows (ex: OTP 26.x)
-   Instale com as opções padrão

**Como RabbitMQ rodando**:

-   Após instalar, abra o terminal (PowerShell ou CMD) e execute:

-   (` rabbitmq-service stsrt` )

    **Terminal 1 (Warehouse):**

### O sistema é composto por dois microserviços:

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

    ```bash
    cd warehouse
    ./gradlew bootRun
    ```

3.  **Terminal 2 (Storefront):**
    ```bash
    cd storefront
    ./gradlew bootRun
    ```

### Endpoints Úteis

-   **Monitor do RabbitMQ:** `http://localhost:15672/` (user: guest / pass: guest)
-   **Vitrine / Painel Admin:** `http://localhost:8082/`
-   **Visulalização dos Produtos(JSON) do Warehouse:** `http://localhost:8084/api/produtos`
-   **API (Swagger) do Warehouse:** `http://localhost:8084/swagger-ui.html`
