package br.com.minierp.domain;

public record ItemPedido(
    Integer id,
    Integer pedidoId,
    Integer produtoId,
    Double qtd,
    Double precoUnit,
    Double subtotal
) {

}
