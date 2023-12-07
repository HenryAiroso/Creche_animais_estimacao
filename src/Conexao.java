import org.neo4j.driver.*;

public class Conexao {
    private static final String URI = "bolt://localhost:7687";
    private static final String USER = "neo4j";
    private static final String PASSWORD = "12345678";

    public static Driver createDriver() {
        return GraphDatabase.driver(URI, AuthTokens.basic(USER, PASSWORD));
    }


}