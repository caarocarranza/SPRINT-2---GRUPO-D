package persistencia;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class InicializadorBD {
    private InicializadorBD() { }
    public static void inicializar() throws SQLException {
        String[] sentencias = {
            "CREATE TABLE IF NOT EXISTS cliente (dni INT PRIMARY KEY, nombre VARCHAR(60) NOT NULL, apellido VARCHAR(60) NOT NULL, telefono VARCHAR(30) NOT NULL, inasistencias INT NOT NULL DEFAULT 0)",
            "CREATE TABLE IF NOT EXISTS empleado (dni INT PRIMARY KEY, nombre VARCHAR(60) NOT NULL, apellido VARCHAR(60) NOT NULL, telefono VARCHAR(30) NOT NULL, rol VARCHAR(30) NOT NULL)",
            "CREATE TABLE IF NOT EXISTS tipo_cancha (tipo VARCHAR(20) PRIMARY KEY, precio DOUBLE NOT NULL)",
            "CREATE TABLE IF NOT EXISTS cancha (numero INT PRIMARY KEY, tipo VARCHAR(20) NOT NULL, disponibilidad BOOLEAN NOT NULL, FOREIGN KEY (tipo) REFERENCES tipo_cancha(tipo))",
            "CREATE TABLE IF NOT EXISTS reserva (numero INT PRIMARY KEY, cliente_dni INT NOT NULL, cancha_numero INT NOT NULL, fecha DATE NOT NULL, hora TIME NOT NULL, monto DOUBLE NOT NULL, estado VARCHAR(20) NOT NULL, medio_sena VARCHAR(30) NOT NULL, estado_sena VARCHAR(30) NOT NULL, medio_saldo VARCHAR(30), estado_saldo VARCHAR(30), FOREIGN KEY (cliente_dni) REFERENCES cliente(dni), FOREIGN KEY (cancha_numero) REFERENCES cancha(numero))"
        };
        try (Connection conexion = ConexionBD.conectar(); Statement st = conexion.createStatement()) {
            for (String sql : sentencias) st.executeUpdate(sql);
            st.executeUpdate("INSERT IGNORE INTO tipo_cancha(tipo, precio) VALUES ('PARED',35000),('BLINDEX',45000)");
        }
    }
}
