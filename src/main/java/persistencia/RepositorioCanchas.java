package persistencia;
import modelo.Cancha;
import modelo.TipoCancha;
import java.sql.*;
import java.util.ArrayList;

public class RepositorioCanchas {
    public void guardar(Cancha c) throws SQLException {
        try(Connection cn=ConexionBD.conectar(); PreparedStatement ps=cn.prepareStatement("INSERT INTO cancha(numero,tipo,disponibilidad) VALUES(?,?,?)")) {
            ps.setInt(1,c.getNumero()); ps.setString(2,c.getTipo().name()); ps.setBoolean(3,c.getDisponibilidad()); ps.executeUpdate();
        }
    }
    public ArrayList<Cancha> leerTodas() throws SQLException {
        ArrayList<Cancha> lista=new ArrayList<>();
        try(Connection cn=ConexionBD.conectar(); Statement st=cn.createStatement(); ResultSet rs=st.executeQuery("SELECT numero,tipo,disponibilidad FROM cancha ORDER BY numero")) {
            while(rs.next()) lista.add(new Cancha(rs.getInt("numero"),TipoCancha.valueOf(rs.getString("tipo")),rs.getBoolean("disponibilidad")));
        } return lista;
    }
    public void guardarPrecios() throws SQLException {
        try(Connection cn=ConexionBD.conectar(); PreparedStatement ps=cn.prepareStatement("UPDATE tipo_cancha SET precio=? WHERE tipo=?")) {
            for(TipoCancha t:TipoCancha.values()) { ps.setDouble(1,t.getPrecio()); ps.setString(2,t.name()); ps.addBatch(); } ps.executeBatch();
        }
    }
    public void cargarPrecios() throws SQLException {
        try(Connection cn=ConexionBD.conectar(); Statement st=cn.createStatement(); ResultSet rs=st.executeQuery("SELECT tipo,precio FROM tipo_cancha")) {
            while(rs.next()) TipoCancha.valueOf(rs.getString("tipo")).setPrecio(rs.getDouble("precio"));
        }
    }
}
