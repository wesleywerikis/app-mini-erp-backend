-- Cria pedido (cabeçalho) e devolve id
CREATE OR REPLACE FUNCTION sp_criar_pedido(p_cliente_id INT)
RETURNS INT AS $$
DECLARE
  v_pedido_id INT;
BEGIN
  INSERT INTO pedidos (cliente_id, total, status)
  VALUES (p_cliente_id, 0, 'ABERTO')
  RETURNING id INTO v_pedido_id;

  RETURN v_pedido_id;
END;
$$ LANGUAGE plpgsql;

-- Cancela pedido e estorna estoque
CREATE OR REPLACE FUNCTION sp_cancelar_pedido(p_pedido_id INT)
RETURNS VOID AS $$
DECLARE
  r RECORD;
BEGIN
  -- só cancela se não estiver cancelado
  UPDATE pedidos SET status = 'CANCELADO' WHERE id = p_pedido_id AND status <> 'CANCELADO';

  -- estorna estoque
  FOR r IN SELECT produto_id, qtd FROM itens_pedido WHERE pedido_id = p_pedido_id LOOP
    UPDATE produtos SET estoque = estoque + r.qtd WHERE id = r.produto_id;
  END LOOP;

  -- zera total (opcional, pode manter histórico)
  UPDATE pedidos SET total = 0 WHERE id = p_pedido_id;
END;
$$ LANGUAGE plpgsql;
