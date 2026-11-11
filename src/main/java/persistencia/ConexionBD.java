package persistencia;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    private static final String URL = "jdbc:mysql://localhost:3306/reservas_padel?serverTimezone=America/Argentina/Cordoba";
    private static final String USUARIO = "root";
    private static final String CLAVE = "";
    private ConexionBD() { }
    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CLAVE);
    }
}
