# Store-Microservices

O projeto Loja Store Micro-services possui uma arquitetura de microserviços reativa usando Java + Spring Boot, que se comunicam de forma síncrona (via HTTP) e assíncrona (via RabbitMQ).

## 🛠️ Tecnologias Utilizadas

-   Java 17 ou superior
-   Spring Boot 3.5
-   Spring AMQP
-   RabbitMQ
-   Erlang
-   Lombok
-   DevTools
-   H2 Database (para persistência)
-   Gradle

## 📋 Pré-requisitos

Antes de começar, você precisará instalar as seguintes ferramentas:

### 1. Erlang

O RabbitMQ depende do Erlang para funcionar.

-   **Download:** Acesse a [página oficial de download do Erlang](https://www.erlang.org/downloads).
-   **Instalação:** Baixe a versão recomendada para Windows (ex: OTP 26.x) e siga o assistente de instalação com as opções padrão.
-   **Configuração:** Adicione o diretório de instalação do Erlang à variável de ambiente `PATH` do seu sistema.

### 2. RabbitMQ

-   **Download:** Acesse a [página oficial de download do RabbitMQ para Windows](https://www.rabbitmq.com/install-windows.html).
-   **Instalação:** Siga as instruções de instalação.
-   **Gerenciar o Serviço:** Após a instalação, abra um terminal (PowerShell ou CMD como Administrador) e use os seguintes comandos para gerenciar o serviço do RabbitMQ:

    ```bash
    # Instalar o serviço
    rabbitmq-service install

    # Iniciar o serviço
    rabbitmq-service start

    # Verificar o status do serviço
    rabbitmq-service status

    # Parar o serviço (se necessário)
    rabbitmq-service stop
    ```

-   **Habilitar o Plugin de Gerenciamento:** Para usar a interface de monitoramento baseada na web, execute:
    ```bash
    rabbitmq-plugins enable rabbitmq_management
    ```

---

## 🚀 Como Executar a Aplicação

Certifique-se de que o RabbitMQ está em execução antes de iniciar os microserviços.

### 1. Inicie o Microserviço Warehouse (Backend)

Abra um novo terminal e execute os seguintes comandos:

```bash
cd warehouse-cli
./gradlew bootRun
```

### 2. Inicie o Microserviço Storefront (Frontend)

Abra um segundo terminal e execute os seguintes comandos:

```bash
cd storefront-cli
./gradlew bootRun
```

---

## Visão Geral dos Microserviços

O sistema é composto por dois microserviços:

### 1. `warehouse-cli/` (Porta 8084)

-   Este é o **"Armazém"** (backend).
-   Ele gerencia o estado dos produtos em um banco de dados em memória H2.
-   Ele expõe uma API REST para operações CRUD em produtos (`GET`, `POST`, `PUT`, `DELETE`) em `/api/produtos`.
-   Ele escuta (consome) "Comandos" do RabbitMQ (ex: `pedido.criado`, `produto.command.create`).
-   Ele publica "Eventos" no RabbitMQ (ex: `produto.atualizado`, `produto.deletado`).

### 2. `storefront-cli/` (Porta 8082)

-   Esta é a **"Vitrine"** (frontend).
-   Ela serve a UI do painel de administração, que é acessível em `http://localhost:8082`.
-   Ela publica "Comandos" no RabbitMQ quando o usuário realiza ações como "Adicionar", "Comprar", "Deletar" e "Editar".
-   Ela escuta (consome) "Eventos" do RabbitMQ para atualizar a UI em tempo real via WebSocket.

---

## 🌐 Endpoints Úteis

-   **Vitrine / Painel de Administração:** [http://localhost:8082/](http://localhost:8082/)

-   **Lista de Produtos do Armazém (JSON):** [http://localhost:8085/api/produtos](http://localhost:8085/api/produtos)
-   **Documentação da API do Armazém (Swagger):** [http://localhost:8085/swagger-ui/index.html](http://localhost:8085/swagger-ui/index.html)

-   **Gerenciamento do RabbitMQ:** [http://localhost:15672/](http://localhost:15672/) (usuário: `guest` / senha: `guest`)

### Endpoints Úteis

-   **Monitor do RabbitMQ:** `http://localhost:15672/` (user: guest / pass: guest)
-   **Vitrine / Painel Admin:** `http://localhost:8082/`
-   **Visulalização dos Produtos(JSON) do Warehouse:** `http://localhost:8084/api/produtos`
-   **API (Swagger) do Warehouse:** `http://localhost:8084/swagger-ui.html`
