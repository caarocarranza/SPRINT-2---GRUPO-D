package controlador;
import modelo.*;
import persistencia.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class GestorReserva {
    private ArrayList<Cliente> clientes;
    private ArrayList<Empleado> empleados = new ArrayList<>();
    private ArrayList<Cancha> canchas;
    private ArrayList<Reserva> reservas;
    private int numeroReserva = 1;
    private final RepositorioClientes repoClientes = new RepositorioClientes();
    private final RepositorioReservas repoReservas = new RepositorioReservas();
    private final RepositorioCanchas repoCanchas = new RepositorioCanchas();

    public GestorReserva() throws SQLException {
        repoCanchas.cargarPrecios();
        clientes=repoClientes.leerTodos(); canchas=repoCanchas.leerTodas();
        if(canchas.isEmpty()) {
            registrarCanchaPorDefecto(1,TipoCancha.PARED); registrarCanchaPorDefecto(2,TipoCancha.PARED);
            registrarCanchaPorDefecto(3,TipoCancha.BLINDEX); registrarCanchaPorDefecto(4,TipoCancha.BLINDEX);
        }
        reservas=repoReservas.leerTodas(clientes,canchas);
        for(Reserva r:reservas) if(r.getNumero()>=numeroReserva) numeroReserva=r.getNumero()+1;
    }
    public ArrayList<Cliente> getClientes(){return clientes;}
    public ArrayList<Cancha> getCanchas(){return canchas;}
    public Cliente buscarClientePorDni(int dni){for(Cliente c:clientes) if(c.getDni()==dni)return c; return null;}
    public Cliente registrarCliente(String n,String a,String t,int dni){
        if(buscarClientePorDni(dni)!=null){System.out.println("Error: Ya existe un cliente con ese DNI.");return null;}
        Cliente c=new Cliente(n,a,t,dni); try{repoClientes.guardar(c);clientes.add(c);return c;}catch(SQLException e){System.out.println("Error al guardar cliente: "+e.getMessage());return null;}
    }
    public Empleado registrarEmpleado(String n,String a,String t,int dni,RolEmpleado rol){
        Empleado e=new Empleado(n,a,t,dni,rol); empleados.add(e); return e;
    }
    public Cancha buscarCanchaPorNumero(int n){for(Cancha c:canchas)if(c.getNumero()==n)return c;return null;}
    public Cancha registrarCancha(int n,TipoCancha t,boolean disponible){
        if(buscarCanchaPorNumero(n)!=null){System.out.println("Error: Ya existe esa cancha.");return null;}
        Cancha c=new Cancha(n,t,disponible); try{repoCanchas.guardar(c);canchas.add(c);return c;}catch(SQLException e){System.out.println("Error al guardar cancha: "+e.getMessage());return null;}
    }
    public ArrayList<Cancha> consultarDisponibilidad(LocalDate fecha,LocalTime hora){
        ArrayList<Cancha> libres=new ArrayList<>();
        for(Cancha c:canchas) {
            if(!c.getDisponibilidad()) continue;
            boolean libre=true;
            for(Reserva r:reservas) {
                if(r.getCancha().getNumero()==c.getNumero() && r.seSuperpone(fecha,hora)) { libre=false; break; }
            }
            if(libre) libres.add(c);
        }
        return libres;
    }
    public Reserva registrarReserva(Cliente cliente,Cancha cancha,LocalDate fecha,LocalTime hora,MedioPago medio){
        if(fecha.isBefore(LocalDate.now())){System.out.println("Error: No se pueden realizar reservas en fechas pasadas.");return null;}
        if(!consultarDisponibilidad(fecha,hora).contains(cancha)){System.out.println("Error: La cancha no está disponible en ese horario.");return null;}
        Reserva r=new Reserva(numeroReserva,cliente,cancha,fecha,hora,medio);
        try{repoReservas.guardar(r);reservas.add(r);numeroReserva++;return r;}catch(SQLException e){System.out.println("Error al guardar reserva: "+e.getMessage());return null;}
    }
    public Reserva buscarReservaPorNumero(int n){for(Reserva r:reservas)if(r.getNumero()==n)return r;return null;}
    public void cancelarReserva(Reserva r,LocalDateTime ahora){r.cancelar(ahora);guardarCambioReserva(r);}
    public void registrarAsistencia(Reserva r,boolean asistio,MedioPago medio){r.registrarAsistencia(asistio,medio);guardarCambioReserva(r);if(!asistio)try{repoClientes.actualizar(r.getCliente());}catch(SQLException e){System.out.println("Error al actualizar inasistencias: "+e.getMessage());}}
    private void guardarCambioReserva(Reserva r){try{repoReservas.actualizar(r);}catch(SQLException e){System.out.println("Error al actualizar reserva: "+e.getMessage());}}
    public void consultarReservas(Cliente c){boolean encontro=false;for(Reserva r:reservas)if(r.getCliente().getDni()==c.getDni()){System.out.println(r);encontro=true;}if(!encontro)System.out.println("El cliente no tiene reservas.");}
    public void modificarPrecio(Empleado e,TipoCancha t,double precio){if(e!=null&&e.getRol()==RolEmpleado.ENCARGADO){t.setPrecio(precio);try{repoCanchas.guardarPrecios();}catch(SQLException ex){System.out.println("Error al guardar precio: "+ex.getMessage());}}else System.out.println("Solo el encargado puede modificar los precios.");}
    private void registrarCanchaPorDefecto(int n,TipoCancha t)throws SQLException{Cancha c=new Cancha(n,t,true);repoCanchas.guardar(c);canchas.add(c);}
}
