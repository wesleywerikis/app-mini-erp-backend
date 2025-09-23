package br.com.minierp.domain;

import java.time.LocalDateTime;
import java.util.List;

public record Pedido(
        Integer id,
        Integer clienteId,
        Double total,
        String status,
        LocalDateTime criadoEm,
        List<ItemPedido> itens) {

}
