import opcoes.Opcoes;
import org.neo4j.driver.Driver;

//Projeto Henrique de Macedo Airoso da Silva & Ana Clara Queiroz Machado

public class Principal {
    public static void main(String[] args) {
        try (Driver driver = Conexao.createDriver()) {
            Opcoes.showMenu(driver);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}