import controlador.GestorReserva;
import modelo.Empleado;
import modelo.RolEmpleado;
import persistencia.InicializadorBD;
import vista.Menu;
import java.sql.SQLException;

public class Principal {
    public static void main(String[] args) {
        try {
            InicializadorBD.inicializar();
            GestorReserva sistema = new GestorReserva();
            Empleado encargado = sistema.registrarEmpleado("Juan", "Flores", "3536560965", 43809890, RolEmpleado.ENCARGADO);
            Menu menu = new Menu(sistema, encargado);
            menu.iniciar();
        } catch (SQLException e) {
            System.out.println("No se pudo iniciar el sistema.");
            System.out.println("Detalle: " + e.getMessage());
        }
    }
}
