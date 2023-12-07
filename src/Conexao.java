import org.neo4j.driver.*;

public class Conexao {
    private static final String URI = "neo4j+s://7ef295a0.databases.neo4j.io";
    private static final String USER = "neo4j";
    private static final String PASSWORD = "WbGHKqK8kU_jSjseNK_duax3PSKtP--PCbUgbIz44eU";

    public static Driver createDriver() {
        return GraphDatabase.driver(URI, AuthTokens.basic(USER, PASSWORD));
    }

    public static boolean testarConexao() {
        try (Driver driver = createDriver()) {
            // Tenta criar uma sessão e executa uma consulta de teste\
            try (Session session = driver.session()) {
                Result result = session.run("RETURN 1");
                return result.single().get(0).asInt() == 1;
            }
        } catch (Exception e) {
            // Se houver qualquer exceção, a conexão falhou
            return false;
        }

    }
}