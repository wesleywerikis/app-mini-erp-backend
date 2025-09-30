package br.com.minierp.controller;

import java.util.Map;

import br.com.minierp.dao.ClienteDao;
import br.com.minierp.domain.Cliente;
import io.javalin.Javalin;
import io.javalin.http.BadRequestResponse;

public class ClienteController {

    public static void register(Javalin app) {
        ClienteDao dao = new ClienteDao();

        app.get("/api/clientes", ctx -> {
            String q = ctx.queryParam("q");
            ctx.json(dao.listar(q));
        });

        app.post("/api/clientes", ctx -> {
            var body = ctx.bodyAsClass(Map.class);
            String nome = (String) body.get("nome");
            String doc = (String) body.getOrDefault("documento", "");
            String email = (String) body.getOrDefault("email", "");

            if (nome == null || nome.isBlank()) {
                throw new BadRequestResponse("Campo obrigatório: nome");
            }

            int id = dao.criar(new Cliente(null, nome, doc, email));
            ctx.status(201).json(Map.of("id", id));
        });
    }
}
