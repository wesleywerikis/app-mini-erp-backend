package br.com.minierp;

import br.com.minierp.controller.ClienteController;
import br.com.minierp.infra.Database;
import io.javalin.Javalin;

public class App {

    public static void main(String[] args) {

        String dbUrl = System.getenv().getOrDefault("DB_URL", "jdbc:postgresql://localhost:5432/erp");
        String dbUser = System.getenv().getOrDefault("DB_USER", "postgres");
        String dbPass = System.getenv().getOrDefault("DB_PASS", "postgres");

        Database.init(dbUrl, dbUser, dbPass);

        Javalin app = Javalin.create(cfg -> {
            cfg.plugins.enableCors(cors -> cors.add(it -> it.anyHost())); // libera CORS p/ frontend
        }).start(8080);
        
        ClienteController.register(app);

        System.out.println("🚀 MiniERP rodando em http://localhost:8080");
        System.out.println("📦 Conectado ao banco: " + dbUrl);

        
    }
}
