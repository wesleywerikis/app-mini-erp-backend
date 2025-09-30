-- Dispara a cada item inserido: baixa estoque e valida
CREATE OR REPLACE FUNCTION trg_baixa_estoque()
RETURNS TRIGGER AS $$
BEGIN
  UPDATE produtos
     SET estoque = estoque - NEW.qtd
   WHERE id = NEW.produto_id;

  IF (SELECT estoque FROM produtos WHERE id = NEW.produto_id) < 0 THEN
    RAISE EXCEPTION 'Estoque insuficiente para o produto %', NEW.produto_id;
  END IF;

  -- Atualiza subtotal e total do pedido
  NEW.subtotal := ROUND(NEW.qtd * NEW.preco_unit, 2);

  UPDATE pedidos
     SET total = (
       SELECT COALESCE(SUM(subtotal),0) FROM itens_pedido WHERE pedido_id = NEW.pedido_id
     )
   WHERE id = NEW.pedido_id;

  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS tg_baixa_estoque ON itens_pedido;
CREATE TRIGGER tg_baixa_estoque
AFTER INSERT ON itens_pedido
FOR EACH ROW
EXECUTE FUNCTION trg_baixa_estoque();
