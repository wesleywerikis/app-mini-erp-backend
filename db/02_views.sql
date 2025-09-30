-- Total de vendas por cliente
CREATE OR REPLACE VIEW vw_total_vendas_por_cliente AS
SELECT
  c.id AS cliente_id,
  c.nome AS cliente_nome,
  SUM(p.total) AS total_vendido
FROM clientes c
JOIN pedidos p ON p.cliente_id = c.id AND p.status <> 'CANCELADO'
GROUP BY c.id, c.nome;

-- Estoque atual (simples)
CREATE OR REPLACE VIEW vw_estoque_atual AS
SELECT id AS produto_id, nome, estoque
FROM produtos
WHERE ativo = TRUE;
