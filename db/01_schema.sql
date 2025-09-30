-- schema.sql
CREATE TABLE usuarios (
  id SERIAL PRIMARY KEY,
  nome        VARCHAR(100) NOT NULL,
  email       VARCHAR(120) UNIQUE NOT NULL,
  senha_hash  VARCHAR(255) NOT NULL,
  criado_em   TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE clientes (
  id SERIAL PRIMARY KEY,
  nome        VARCHAR(120) NOT NULL,
  documento   VARCHAR(20),
  email       VARCHAR(120),
  criado_em   TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE produtos (
  id SERIAL PRIMARY KEY,
  nome         VARCHAR(120) NOT NULL,
  sku          VARCHAR(50) UNIQUE,
  preco        NUMERIC(12,2) NOT NULL DEFAULT 0,
  estoque      NUMERIC(12,3) NOT NULL DEFAULT 0, -- permite unidade fracionada
  ativo        BOOLEAN NOT NULL DEFAULT TRUE,
  criado_em    TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE pedidos (
  id SERIAL PRIMARY KEY,
  cliente_id   INT NOT NULL REFERENCES clientes(id),
  total        NUMERIC(12,2) NOT NULL DEFAULT 0,
  status       VARCHAR(20) NOT NULL DEFAULT 'ABERTO', -- ABERTO | PAGO | CANCELADO
  criado_em    TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE itens_pedido (
  id SERIAL PRIMARY KEY,
  pedido_id    INT NOT NULL REFERENCES pedidos(id) ON DELETE CASCADE,
  produto_id   INT NOT NULL REFERENCES produtos(id),
  qtd          NUMERIC(12,3) NOT NULL CHECK (qtd > 0),
  preco_unit   NUMERIC(12,2) NOT NULL CHECK (preco_unit >= 0),
  subtotal     NUMERIC(12,2) NOT NULL
);

-- Índices úteis
CREATE INDEX idx_pedidos_cliente ON pedidos(cliente_id);
CREATE INDEX idx_itens_produto   ON itens_pedido(produto_id);
CREATE INDEX idx_produtos_nome   ON produtos(nome);
