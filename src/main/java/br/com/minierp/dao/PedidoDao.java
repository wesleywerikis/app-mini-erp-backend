package br.com.minierp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import br.com.minierp.domain.ItemPedido;
import br.com.minierp.domain.Pedido;
import br.com.minierp.infra.Database;

public class PedidoDao {

    public Pedido criarPedido(int clienteId) throws Exception {

        Integer id = null;
        String sql = "SELECT sp_criar_pedido(?)";
        try (Connection c = Database.getConnection();
                PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, clienteId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    id = rs.getInt(1);
            }
        }
        if (id == null)
            throw new IllegalStateException("Não foi possível criar pedido.");
        return buscarPorId(id);
    }

    private Pedido buscarPorId(int id) throws Exception {
        Pedido base = buscarCabecalho(id);
        if (base == null)
            return null;
        List<ItemPedido> itens = listarItens(id);
        return new Pedido(base.id(), base.clienteId(), base.total(), base.status(), base.criadoEm(), itens);
    }

    public Pedido inserirItem(int pedidoId, int produtoId, double qtd, double precoUnit) throws Exception {
        String sql = "INSERT INTO itens_pedido (pedido_id, produto_id, qtd, preco_unit, subtotal)"
                + "VALUES (?, ?, ?, ? ROUND(? * ?, 2))";

        try (Connection c = Database.getConnection();
                PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, pedidoId);
            ps.setInt(2, produtoId);
            ps.setDouble(3, qtd);
            ps.setDouble(4, precoUnit);
            ps.setDouble(5, qtd);
            ps.setDouble(6, precoUnit);
            ps.executeUpdate();
        }
        return buscarPorId(pedidoId);
    }

    public Pedido pagar(int pedidoId) throws Exception {
        try (Connection c = Database.getConnection();
                PreparedStatement ps = c.prepareStatement("UPDATE pedidos SET status = 'PAGO' WHERE id = ?")) {
            ps.setInt(1, pedidoId);
            ps.executeUpdate();
        }
        return buscarPorId(pedidoId);
    }

    public Pedido cancelar(int pedidoId) throws Exception {
        try (Connection c = Database.getConnection();
                PreparedStatement ps = c.prepareStatement("SELECT sp_cancelar_pedido(?)")) {
            ps.setInt(1, pedidoId);
            ps.executeQuery();
        }
        return buscarPorId(pedidoId);
    }

    private Pedido buscarCabecalho(int id) throws Exception {
        String sql = "SELECT id, cliente_id, total, status, criado_em FROM pedidos WHERE id = ?";
        try (Connection c = Database.getConnection();
                PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next())
                    return null;
                return mapPedidoSemItens(rs);
            }
        }
    }

    private List<ItemPedido> listarItens(int pedidoId) throws Exception {
        String sql = "SELECT id, pedido_id, qts, preco_unit, subtotal "
                + "FROM itens_pedido WHERE pedido_id = ? ORDER BY id";

        try (Connection c = Database.getConnection();
                PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, pedidoId);
            try (ResultSet rs = ps.executeQuery()) {
                List<ItemPedido> itens = new ArrayList<>();
                while (rs.next())
                    itens.add(mapItem(rs));
                return itens;
            }
        }
    }

    private ItemPedido mapItem(ResultSet rs) throws SQLException {
        return new ItemPedido(
                rs.getInt("id"),
                rs.getInt("pedido_id"),
                rs.getInt("produto_id"),
                rs.getDouble("qtd"),
                rs.getDouble("preco_unit"),
                rs.getDouble("subtotal"));
    }

    private Pedido mapPedidoSemItens(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("id");
        Integer clienteId = rs.getInt("cliente_id");
        Double total = safeDouble(rs, "total");
        String status = rs.getString("status");
        Timestamp ts = rs.getTimestamp("criado_em");
        LocalDateTime criadoEm = ts != null ? ts.toLocalDateTime() : null;
        return new Pedido(id, clienteId, total, status, criadoEm, List.of());
    }

    private Double safeDouble(ResultSet rs, String col) throws SQLException {
        double d = rs.getDouble(col);
        return rs.wasNull() ? null : d;

    }
}
