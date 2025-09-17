# 📦 Mini ERP Backend

Este repositório contém o **backend de um sistema ERP simplificado**, desenvolvido em **Java 17** com **Javalin** e **PostgreSQL**.  
O objetivo é criar uma aplicação didática que simula funcionalidades essenciais de sistemas de gestão empresarial (ERP), servindo como **portfólio** e **base de estudos** em Java, SQL avançado e boas práticas de arquitetura.

---

## 🚀 Funcionalidades previstas

### ✅ Implementadas
- Estrutura inicial do projeto Maven.
- Configuração de dependências (Javalin, PostgreSQL, HikariCP, SLF4J).

### 🔜 Em andamento
- **Módulo de Clientes**  
  - CRUD completo (criar, listar, atualizar, excluir).
  - Pesquisa por nome.

- **Módulo de Produtos**  
  - Cadastro de produtos com preço e estoque.
  - Controle de estoque disponível.

- **Módulo de Pedidos**  
  - Criação de pedidos vinculados a clientes.
  - Inclusão de itens no pedido (com baixa de estoque automática via trigger).
  - Cancelamento de pedidos (com estorno de estoque).
  - Pagamento de pedidos.

- **Relatórios (Views em SQL)**  
  - Total de vendas por cliente.
  - Estoque atual.

### 🎯 Futuro / Evolução
- Autenticação de usuários com hash de senha (BCrypt).
- Transações e rollback para consistência de dados.
- Relatórios adicionais (ticket médio, vendas por produto, top clientes).
- Integração com frontend simples em HTML + JavaScript.
- Testes unitários em JUnit.

---

## 🛠️ Tecnologias utilizadas

- **Java 17**
- **Javalin 5** – microframework para APIs REST
- **PostgreSQL** – banco de dados relacional
- **JDBC + HikariCP** – acesso e pool de conexões
- **Triggers, Procedures e Views** – regras de negócio no banco
- **SLF4J Simple** – logging
- **JUnit 5** – testes unitários

---

## 📂 Estrutura do projeto

```
app-mini-erp-backend/
 └── src/
     ├── main/
     │   ├── java/br/com/minierp/   # código fonte Java
     │   └── resources/             # configs (ex: schema.sql)
     └── test/                      # testes unitários
```

---

## ▶️ Como executar localmente

1. **Clonar o repositório**
   ```bash
   git clone https://github.com/wesleywerikis/app-mini-erp-backend.git
   cd app-mini-erp-backend
   ```

2. **Configurar banco PostgreSQL**
   ```bash
   docker run --name pg -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres:16
   ```
   - Criar banco `erp`.
   - Executar `schema.sql` (a ser disponibilizado em breve).

3. **Rodar aplicação**
   ```bash
   mvn clean compile exec:java
   ```
   O servidor inicia em: `http://localhost:8080`

---

## 📌 Status do Projeto

> 🚧 Em desenvolvimento – este repositório está sendo atualizado continuamente.  
> O foco inicial é construir o **núcleo de um ERP** (clientes, produtos, pedidos, relatórios) para fins de aprendizado e portfólio.

---

## ✨ Autor

Desenvolvido por **Wesley Werikis** – estudando e aplicando Java, SQL e boas práticas de desenvolvimento backend.  
[LinkedIn](https://www.linkedin.com/in/wesleywerikis) | [GitHub](https://github.com/wesleywerikis)
