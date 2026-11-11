package persistencia;
import modelo.*;
import java.sql.*;
import java.util.ArrayList;

public class RepositorioReservas {
    public void guardar(Reserva r) throws SQLException {
        String sql="INSERT INTO reserva(numero,cliente_dni,cancha_numero,fecha,hora,monto,estado,medio_sena,estado_sena,medio_saldo,estado_saldo) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        try(Connection cn=ConexionBD.conectar(); PreparedStatement ps=cn.prepareStatement(sql)) {
            ps.setInt(1,r.getNumero()); ps.setInt(2,r.getCliente().getDni()); ps.setInt(3,r.getCancha().getNumero());
            ps.setDate(4,Date.valueOf(r.getFecha())); ps.setTime(5,Time.valueOf(r.getHora())); ps.setDouble(6,r.getMonto());
            ps.setString(7,r.getEstado()); ps.setString(8,r.getSena().getMedio().name()); ps.setString(9,r.getSena().getEstado());
            if(r.getSaldo()==null) { ps.setNull(10,Types.VARCHAR); ps.setNull(11,Types.VARCHAR); }
            else { ps.setString(10,r.getSaldo().getMedio().name()); ps.setString(11,r.getSaldo().getEstado()); }
            ps.executeUpdate();
        }
    }
    public void actualizar(Reserva r) throws SQLException {
        String sql="UPDATE reserva SET estado=?,estado_sena=?,medio_saldo=?,estado_saldo=? WHERE numero=?";
        try(Connection cn=ConexionBD.conectar(); PreparedStatement ps=cn.prepareStatement(sql)) {
            ps.setString(1,r.getEstado()); ps.setString(2,r.getSena().getEstado());
            if(r.getSaldo()==null) { ps.setNull(3,Types.VARCHAR); ps.setNull(4,Types.VARCHAR); }
            else { ps.setString(3,r.getSaldo().getMedio().name()); ps.setString(4,r.getSaldo().getEstado()); }
            ps.setInt(5,r.getNumero()); ps.executeUpdate();
        }
    }
    public ArrayList<Reserva> leerTodas(ArrayList<Cliente> clientes, ArrayList<Cancha> canchas) throws SQLException {
        ArrayList<Reserva> lista=new ArrayList<>();
        String sql="SELECT * FROM reserva ORDER BY numero";
        try(Connection cn=ConexionBD.conectar(); Statement st=cn.createStatement(); ResultSet rs=st.executeQuery(sql)) {
            while(rs.next()) {
                Cliente cliente=null; Cancha cancha=null;
                for(Cliente c:clientes) if(c.getDni()==rs.getInt("cliente_dni")) cliente=c;
                for(Cancha c:canchas) if(c.getNumero()==rs.getInt("cancha_numero")) cancha=c;
                if(cliente!=null && cancha!=null) {
                    String medioSaldo=rs.getString("medio_saldo");
                    lista.add(new Reserva(rs.getInt("numero"),cliente,cancha,rs.getDate("fecha").toLocalDate(),rs.getTime("hora").toLocalTime(),rs.getDouble("monto"),rs.getString("estado"),MedioPago.valueOf(rs.getString("medio_sena")),rs.getString("estado_sena"),medioSaldo==null?null:MedioPago.valueOf(medioSaldo),rs.getString("estado_saldo")));
                }
            }
        } return lista;
    }
}
