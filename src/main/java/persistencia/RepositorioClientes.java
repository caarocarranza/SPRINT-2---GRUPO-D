package persistencia;
import modelo.Cliente;
import java.sql.*;
import java.util.ArrayList;

public class RepositorioClientes {
    public void guardar(Cliente c) throws SQLException {
        String sql = "INSERT INTO cliente(dni,nombre,apellido,telefono,inasistencias) VALUES(?,?,?,?,?)";
        try (Connection cn=ConexionBD.conectar(); PreparedStatement ps=cn.prepareStatement(sql)) {
            ps.setInt(1,c.getDni()); ps.setString(2,c.getNombre()); ps.setString(3,c.getApellido());
            ps.setString(4,c.getTelefono()); ps.setInt(5,c.getInasistencias()); ps.executeUpdate();
        }
    }
    public ArrayList<Cliente> leerTodos() throws SQLException {
        ArrayList<Cliente> lista=new ArrayList<>();
        try(Connection cn=ConexionBD.conectar(); Statement st=cn.createStatement(); ResultSet rs=st.executeQuery("SELECT * FROM cliente ORDER BY apellido,nombre")) {
            while(rs.next()) { Cliente c=new Cliente(rs.getString("nombre"),rs.getString("apellido"),rs.getString("telefono"),rs.getInt("dni"));
                for(int i=0;i<rs.getInt("inasistencias");i++) c.registrarInasistencia(); lista.add(c); }
        } return lista;
    }
    public void actualizar(Cliente c) throws SQLException {
        try(Connection cn=ConexionBD.conectar(); PreparedStatement ps=cn.prepareStatement("UPDATE cliente SET inasistencias=? WHERE dni=?")) {
            ps.setInt(1,c.getInasistencias()); ps.setInt(2,c.getDni()); ps.executeUpdate();
        }
    }
}
