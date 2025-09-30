package br.com.minierp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import br.com.minierp.domain.Cliente;
import br.com.minierp.infra.Database;

public class ClienteDao {

    public List<Cliente> listar(String q) throws Exception {
        String base = "SELECT id, nome, documento, email FROM clientes";
        String where = (q != null && !q.isBlank()) ? " WHERE nome ILIKE ? " : " ";
        String order = " ORDER BY id DESC LIMIT 100";
        String sql = base + where + order;

        try (Connection c = Database.getConnection();
        PreparedStatement ps = c.prepareStatement(sql)) {
            
            if(q != null && !q.isBlank()){
                ps.setString(1, "%" + q + "%");
            }

            try (ResultSet rs = ps.executeQuery()) {
                List<Cliente> out = new ArrayList<>();
                while (rs.next()) {
                    out.add(new Cliente(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("documento"),
                        rs.getString("email")
                    ));
             }
             return out;
            }
        }
    }

    public int criar(Cliente cli) throws Exception {
        String sql = "INSERT INTO clientes (nome, documento, email) VALUES (?,?,?) RETURNING id";
        try (Connection c = Database.getConnection();
                PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, cli.nome());
            ps.setString(2, cli.documento());
            ps.setString(3, cli.email());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }
}
