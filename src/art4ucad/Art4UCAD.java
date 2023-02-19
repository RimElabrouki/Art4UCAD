/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package art4ucad;

import art4upojo.Ciudad;
import art4upojo.ExcepcionArt4UBD;
import art4upojo.Usuario;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

/**
 *
 * @author Rim El abrouki
 * @version 1.0
 * @since AaD 1.0
 */
public class Art4UCAD {

    Connection conexion;

    /**
     * Método que cargará el driver para poderse conectar a la BD
     *
     * @throws ExcepcionArt4UBD se lanzará cuando se produzca algun problema al
     * cargar el driver jdbc
     *
     */
    public Art4UCAD() throws ExcepcionArt4UBD {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException ex) {
            ExcepcionArt4UBD e = new ExcepcionArt4UBD();
            e.setMensajeErrorAdministrador(ex.getMessage());
            e.setMensajeErrorUsuario("Error general en el sistema. Consulta con el administrador.");
            throw e;
        }
    }

    /**
     *
     * Este metodo permite conectar Netbeans con la BD
     *
     */
    public void conectarBD() {
        try {
            conexion = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe", "ART4U", "kk");
        } catch (SQLException ex) {
            ExcepcionArt4UBD e = new ExcepcionArt4UBD();
            e.setMensajeErrorAdministrador(ex.getMessage());
            e.setCodigoError(ex.getErrorCode());
            e.setSentanciaSQL(null);
            e.setMensajeErrorUsuario("Error general del sistema. Consulte con el administrador");

        }
    }

    /**
     * Este metodo permite desconectar la BD
     *
     */
    private void desconectarBD() {
        try {
            conexion.close();
        } catch (SQLException ex) {
            ExcepcionArt4UBD e = new ExcepcionArt4UBD();
            e.setMensajeErrorAdministrador(ex.getMessage());
            e.setCodigoError(ex.getErrorCode());
            e.setSentanciaSQL(null);
            e.setMensajeErrorUsuario("Error general del sistema. Consulte con el administrador");

        }

    }

    //******************** LOS METODOS DEL USUARIO(insertar Usuario,actualizar Usuario,Eliminar Usuario,Leer Usuario,Leer Usuarios )***************************
    /**
     *
     * Este método permite insertar 1 Registro en la tabla Usuario
     *
     * @param usuario es el objeto de la clase Usuario que contiene todos los
     * datos del usuario a insertar
     *
     * @return la cantidad de registros insertados
     * @throws ExcepcionArt4UBD si se produce cualquier error en la insertacion
     * de la usuario en la base de datos.
     * @author Rim El abrouki
     * @version 1.2
     * @since 0.0
     *
     */
    public int insertarUsuario(Usuario usuario) throws ExcepcionArt4UBD {
        int registrosAfectados = 0;
        String dml = "insert into USUARIO(USUARIO_ID,NOMBRE,APELLIDO,GENERO,FECHA_NACIO,EMAIL,TELEFONO,NUMEROTARJETA,CIUDAD_ID)"
                + "VALUES (SECUENIA_USUARIOS.nextval,?,?,?,?,?,?,?,?)";
        try {
            conectarBD();
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dml);
            sentenciaPreparada.setString(1, usuario.getNombre());
            sentenciaPreparada.setString(2, usuario.getApellido());
            sentenciaPreparada.setString(3, usuario.getGenero());
            sentenciaPreparada.setObject(4, usuario.getFechaNacio(), Types.DATE);
            sentenciaPreparada.setString(5, usuario.getEmail());
            sentenciaPreparada.setString(6, usuario.getTelefono());
            sentenciaPreparada.setString(7, usuario.getNumeroTarjeta());
            sentenciaPreparada.setObject(8, usuario.getCiudad().getCiudadId(), Types.INTEGER);
            sentenciaPreparada.executeUpdate();
            sentenciaPreparada.close();
            desconectarBD();
        } catch (SQLException ex) {
            ExcepcionArt4UBD e = new ExcepcionArt4UBD();
            e.setMensajeErrorAdministrador(ex.getMessage());
            e.setCodigoError(ex.getErrorCode());
            e.setSentanciaSQL(dml);
            e.setMensajeErrorUsuario("Error general del sistema. Consulte con el administrador");
            switch (ex.getErrorCode()) {
                case 1://Unique 
                    e.setMensajeErrorUsuario("\n\tError: los siguientes campos: Email, Telefono y Numero de Tarjeta ya existen y no se pueden repetir. \n\tEn caso de que el error persista consulta con el administrador. ");
                    break;
                case 2291://Forienkey
                    e.setMensajeErrorUsuario("\n\tError: la Ciudad introduca no existe");
                    break;
                case 2290:
                    //check Constraint
                    e.setMensajeErrorUsuario("\n\tError: Para El Email @ y . son campos obligatorios. " + "\n\tError: El genero Accepta una de las letras 'V','M','O', como respuesta. " + "\n\tError: El Numero de Tarjeta debe tener 16 digitos. ");
                    break;
                case 1400://Not null 
                    e.setMensajeErrorUsuario("\n\tError: Nombre,Apellido,Genero,Fecha de nacionamiento, Email, Telefono Son campos obligatorios. ");
                    break;
                case 12899: //check constrainr de Numero de tarjeta
                    e.setMensajeErrorUsuario("Error: \n\tError: El Numero de Tarjeta debe tener 16 digitos. ");
                    break;
                default:
                    e.setMensajeErrorUsuario("\n\tError general en el sistema. Consulta con el administrador.");
                    break;

            }
            throw e;

        }
        return registrosAfectados;
    }

    /**
     * Este método nos permite actualizar 1 Usuario de la tabla USUARIO
     *
     * @param usuarioId es el id del USUARIO que actulizaremos.
     * @param usuario es el objeto de la clase USUARIO que contiene los datos
     * del USUARIO a actualizar
     *
     * @return devuelve la cantidad de registros actualizados
     * @throws ExcepcionArt4UBD si se produce cualquier error en la
     * actualizacion de la usuario en la base de datos.
     *
     * @author Rim El abrouki
     * @version 1.2
     * @since 0.0
     */
    public int actualizarUsuario(Integer usuarioId, Usuario usuario) throws ExcepcionArt4UBD {
        conectarBD();
        int registrosAfectados = 0;
        String dml = "update USUARIO SET nombre=?, APELLIDO=?, GENERO=?, FECHA_NACIO=?, EMAIL=?, TELEFONO=?, NUMEROTARJETA=?, CIUDAD_ID=? where USUARIO_ID=" + usuarioId;
        try {
            conectarBD();
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dml);
            sentenciaPreparada.setString(1, usuario.getNombre());
            sentenciaPreparada.setString(2, usuario.getApellido());
            sentenciaPreparada.setString(3, usuario.getGenero());
            sentenciaPreparada.setObject(4, usuario.getFechaNacio(), Types.DATE);
            sentenciaPreparada.setString(5, usuario.getEmail());
            sentenciaPreparada.setString(6, usuario.getTelefono());
            sentenciaPreparada.setString(7, usuario.getNumeroTarjeta());
            sentenciaPreparada.setObject(8, usuario.getCiudad().getCiudadId(), Types.INTEGER);
            registrosAfectados = sentenciaPreparada.executeUpdate();
            sentenciaPreparada.close();
            desconectarBD();
        } catch (SQLException ex) {
            ExcepcionArt4UBD e = new ExcepcionArt4UBD();
            e.setMensajeErrorAdministrador(ex.getMessage());
            e.setCodigoError(ex.getErrorCode());
            e.setSentanciaSQL(dml);
            e.setMensajeErrorUsuario("Error general del sistema. Consulte con el administrador");
            switch (ex.getErrorCode()) {
                case 1://Unique 
                    e.setMensajeErrorUsuario("\n\tError: los siguientes campos: Email, Telefono y Numero de Tarjeta ya existen y no se pueden repetir.");
                    break;
                case 2291://Forienkey
                    e.setMensajeErrorUsuario("\n\tError: la Ciudad introduca no existe");
                    break;
                case 2290:
                    //check Constraint
                    e.setMensajeErrorUsuario("\n\tError: Para El Email @ y . son campos obligatorios. " + "\n\tError: El genero Acepta una de las letras 'V','M','O', como respuesta. " + "\n\tError: El Numero de Tarjeta debe tener 16 digitos. ");
                    break;
                case 1400://Not null 
                    e.setMensajeErrorUsuario("\n\tError: Nombre,Apellido,Genero,Fecha de nacionamiento, Email, Telefono,no pueden estar vacíos.");
                    break;
                case 1407:
                    //campos obligatorios
                    e.setMensajeErrorUsuario("\n\tError: Nombre,Apellido,Genero,Fecha de nacionamiento, Email, Telefono Son campos obligatorios.");
                    break;
                case 12899: //check constrainr de Numero de tarjeta
                    e.setMensajeErrorUsuario("Error: \n\tError: El Numero de Tarjeta debe tener 16 digitos. ");
                    break;
                default:
                    e.setMensajeErrorUsuario("\n\tError general en el sistema. Consulta con el administrador.");
                    break;

            }
            throw e;
        }
        return registrosAfectados;
    }

    /**
     * Este método permite eliminar 1 USUARIO desde su id de la tabla USUARIO
     *
     * @param usuarioId contiene el identificador del USUARIO a eliminar
     * @return cantidad de Registros Afectados
     * @throws ExcepcionArt4UBD si se produce cualquier error en la insertacion
     * de la usuario en la base de datos.
     * @author Rim El abrouki
     * @version 1.2
     * @since 0.0
     *
     */
    public int eliminarUsuario(Integer usuarioId) throws ExcepcionArt4UBD {

        conectarBD();
        String dml;
        int registrosAfectados = 0;
        dml = "delete from USUARIO where USUARIO_ID =" + usuarioId;
        try {
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dml);
            registrosAfectados = sentenciaPreparada.executeUpdate();
            sentenciaPreparada.close();
            desconectarBD();
        } catch (SQLException ex) {
            ExcepcionArt4UBD e = new ExcepcionArt4UBD();
            e.setMensajeErrorAdministrador(ex.getMessage());
            e.setCodigoError(ex.getErrorCode());
            e.setSentanciaSQL(dml);
            e.setMensajeErrorUsuario("Error general del sistema. Consulte con el administrador");
            switch (ex.getErrorCode()) {
                case 2292:
                    e.setMensajeErrorUsuario("Imposible eliminar Usuario porque tiene Ciudad asociada");
                    break;
                default:
                    e.setMensajeErrorUsuario("Error general del sistema. Consulte con el administrador");
            }
            throw e;
        }
        return registrosAfectados;

    }

    /**
     *
     * Este método permite LEER 1 Usuario de la tabla Usuario (Un Usuario puede
     * tener la misma Ciudad de otro Usuario)
     *
     * @param usuarioId contiene el identificador del USUARIO a leer
     *
     * @return devuelve una instancia de USUARIO con los datos leídos
     *
     * @throws ExcepcionArt4UBD ExcepcionArt4UBD se lanzará en caso de ERROR al
     * leer 1 USUARIO la tabla USUARIO
     * @author Rim El abrouki
     * @version 1.2
     * @since 0.0
     *
     */
    public Usuario leerUsuario(Integer usuarioId) throws ExcepcionArt4UBD {
        conectarBD();
        Usuario u = new Usuario();

        String dql = "select * from CIUDAD C,USUARIO U " + "where U.CIUDAD_ID = C.CIUDAD_ID and U.USUARIO_ID = " + usuarioId;
        try {
            Statement sentencia = conexion.createStatement();
            ResultSet resultado = sentencia.executeQuery(dql);
            if (resultado.next()) {
                u.setUsuarioId(resultado.getInt("USUARIO_ID"));
                u.setNombre(resultado.getString("NOMBRE"));
                u.setApellido(resultado.getString("APELLIDO"));
                u.setGenero(resultado.getString("GENERO"));
                u.setFechaNacio(resultado.getDate("FECHA_NACIO"));
                u.setEmail(resultado.getString("EMAIL"));
                u.setTelefono(resultado.getString("TELEFONO"));
                u.setNumeroTarjeta(resultado.getString("NUMEROTARJETA"));
                //creamos un objeto ciudad de tipo ciudad
                Ciudad ciudad = new Ciudad();
                ciudad.setCiudadId(resultado.getInt("CIUDAD_ID"));
                ciudad.setNombre(resultado.getString("NOMBRE_CIUDAD"));
                u.setCiudad(ciudad);
            }
            resultado.close();
            sentencia.close();
            desconectarBD();
        } catch (SQLException ex) {
            ExcepcionArt4UBD e = new ExcepcionArt4UBD();
            e.setMensajeErrorAdministrador(ex.getMessage());
            e.setCodigoError(ex.getErrorCode());
            e.setSentanciaSQL(dql);
            e.setMensajeErrorUsuario("Error general del sistema. Consulte con el administrador");
        }
        return u;
    }

    /**
     *
     * Este método permite leer todos los USUARIOS de la tabla USUARIO.
     *
     * @return un ArrayList de USUARIO, el cual contiene los USUARIOS leidos
     * @throws ExcepcionArt4UBD ExcepcionArt4UBD se lanzará en caso de ERROR al
     * listar todos los USUARIOS de la tabla USUARIO
     * @author Rim El abrouki
     * @version 1.2
     * @since 0.0
     *
     */
    public ArrayList<Usuario> leerUsuarios() throws ExcepcionArt4UBD {
        String dql = "select * from CIUDAD C,USUARIO U " + "where U.CIUDAD_ID = C.CIUDAD_ID ORDER BY USUARIO_ID ";
        conectarBD();
        ArrayList<Usuario> listaUsuarios = new ArrayList();

        try {
            Statement sentencia = conexion.createStatement();
            ResultSet resultado = sentencia.executeQuery(dql);
            while (resultado.next()) {
                Usuario u = new Usuario();
                u.setUsuarioId(resultado.getInt("USUARIO_ID"));
                u.setNombre(resultado.getString("NOMBRE"));
                u.setApellido(resultado.getString("APELLIDO"));
                u.setGenero(resultado.getString("GENERO"));
                u.setFechaNacio(resultado.getDate("FECHA_NACIO"));
                u.setEmail(resultado.getString("EMAIL"));
                u.setTelefono(resultado.getString("TELEFONO"));
                u.setNumeroTarjeta(resultado.getString("NUMEROTARJETA"));
                Ciudad c = new Ciudad();
                c.setCiudadId(resultado.getInt("CIUDAD_ID"));
                c.setNombre(resultado.getString("NOMBRE_CIUDAD"));
                u.setCiudad(c);
                listaUsuarios.add(u);
            }
            resultado.close();
            sentencia.close();
            desconectarBD();

        } catch (SQLException ex) {

            ExcepcionArt4UBD e = new ExcepcionArt4UBD();
            e.setMensajeErrorAdministrador(ex.getMessage());
            e.setCodigoError(ex.getErrorCode());
            e.setSentanciaSQL(dql);
            e.setMensajeErrorUsuario("Error general del sistema. Consulte con el administrador");
            throw e;
        }

        return listaUsuarios;
    }

    //******************** LOS METODOS DEL CIUDAD(insertar Ciudad,actualizar Ciudad,Eliminar Ciudad,Leer Ciudad,Leer Ciudades )***************************
    /**
     * Este método nos permite insertar 1 ciudad a la tabla Ciudad
     *
     * @param ciudad es el objeto de la clase Ciudad que contiene todos los
     * datos del Ciudad a insertar
     *
     * @return cantidad de Ciudades insertados
     *
     * @throws ExcepcionArt4UBD si se produce cualquier error en la insertacion
     * de la ciudad en la base de datos.
     * @author Rim El abrouki
     * @version 1.2
     * @since 0.0
     *
     */
    public int insertarCiudad(Ciudad ciudad) throws ExcepcionArt4UBD {
        int registrosAfectados = 0;
        String dml = "insert into CIUDAD(CIUDAD_ID,NOMBRE_CIUDAD)"
                + "VALUES (SECUENCIA_CIUDADES.nextval,?)";
        try {
            conectarBD();
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dml);
            sentenciaPreparada.setString(1, ciudad.getNombre());
            sentenciaPreparada.executeUpdate();
            sentenciaPreparada.close();
            desconectarBD();
        } catch (SQLException ex) {
            ExcepcionArt4UBD e = new ExcepcionArt4UBD();
            e.setMensajeErrorAdministrador(ex.getMessage());
            e.setCodigoError(ex.getErrorCode());
            e.setSentanciaSQL(dml);
            e.setMensajeErrorUsuario("Error general del sistema. Consulte con el administrador");

            switch (ex.getErrorCode()) {
                case 1://Unique 
                    e.setMensajeErrorUsuario("\n\tError: El Nombre de la Ciudad ya existe y no se pueden repetir. \n\tEn caso de que el error persista consulta con el administrador. ");
                    break;
                case 1400://Not null 
                    e.setMensajeErrorUsuario("\n\tError: El Nombre de la Ciudad es un campo Obligatorioio. ");
                    break;
                default:
                    e.setMensajeErrorUsuario("\n\tError general en el sistema. Consulta con el administrador.");
                    break;

            }
            throw e;

        }
        return registrosAfectados;
    }

    /**
     * Este método nos permite actualizar 1 Ciudad de la tabla Ciudad
     *
     * @param ciudadId es el id del Ciudad que actulizaremos.
     * @param ciudad es el objeto de la clase Ciudad que contiene los datos del
     * Ciudad a actualizar
     *
     * @return devuelve la cantidad de ciudades actualizados
     *
     * @throws ExcepcionArt4UBD si se produce cualquier error en la
     * actualizacion de la usuario en la base de datos.
     *
     * @author Rim El abrouki
     * @version 1.2
     * @since 0.0
     *
     */
    public int actualizarciudad(Integer ciudadId, Ciudad ciudad) throws ExcepcionArt4UBD {
        conectarBD();
        int registrosAfectados = 0;
        String dml = "update Ciudad SET NOMBRE_CIUDAD=? where CIUDAD_ID=" + ciudadId;
        try {
            conectarBD();
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dml);
            sentenciaPreparada.setString(1, ciudad.getNombre());
            registrosAfectados = sentenciaPreparada.executeUpdate();
            sentenciaPreparada.close();
            desconectarBD();
        } catch (SQLException ex) {
            ExcepcionArt4UBD e = new ExcepcionArt4UBD();
            e.setMensajeErrorAdministrador(ex.getMessage());
            e.setCodigoError(ex.getErrorCode());
            e.setSentanciaSQL(dml);
            e.setMensajeErrorUsuario("Error general del sistema. Consulte con el administrador");
            switch (ex.getErrorCode()) {
                case 1://Unique 
                    e.setMensajeErrorUsuario("\n\tError: El Nombre de la Ciudad ya existe y no se pueden repetir.");
                    break;
                case 1400://Not null 
                    e.setMensajeErrorUsuario("\n\tError: El Nombre de la Ciudad no puede estar vacío. ");
                    break;
                case 1407:
                    //campos obligatorios
                    e.setMensajeErrorUsuario("Error:\n\tEl nombre de la Ciudad es un campo obligatorio");
                    break;

                default:
                    e.setMensajeErrorUsuario("\n\tError general en el sistema. Consulta con el administrador.");
                    break;

            }
            throw e;
        }
        return registrosAfectados;
    }

    /**
     * Este método permite eliminar 1 CIUDAD desde su id de la tabla CIUDAD
     *
     * @param ciudadId contiene el identificador del CIUDAD a eliminar
     * @return cantidad de Ciudades Afectados
     *
     * @throws ExcepcionArt4UBD si se produce cualquier error en la insertacion
     * de la usuario en la base de datos.
     *
     * @author Rim El abrouki
     * @version 1.2
     * @since 0.0
     */
    public int eliminarCiudad(Integer ciudadId) throws ExcepcionArt4UBD {
        int registrosAfectados = 0;
        String dml = "delete from CIUDAD where CIUDAD_ID=" + ciudadId;

        try {
            conectarBD();
            PreparedStatement sentencia = conexion.prepareStatement(dml);
            registrosAfectados = sentencia.executeUpdate(dml);
            desconectarBD();
        } catch (SQLException ex) {
            ExcepcionArt4UBD e = new ExcepcionArt4UBD();
            e.setMensajeErrorAdministrador(ex.getMessage());
            e.setCodigoError(ex.getErrorCode());
            e.setSentanciaSQL(dml);
            e.setMensajeErrorUsuario("Error general del sistema. Consulte con el administrador");
            switch (ex.getErrorCode()) {
                case 2292:
                    e.setMensajeErrorUsuario("No se puede eliminar La Ciudad porque tiene Usuarios asociados");
                    break;
                default:
                    e.setMensajeErrorUsuario("Error general del sistema. Consulte con el administrador");
                    break;
            }
            throw e;
        }
        return registrosAfectados;

    }

    /**
     * Este método permite LEER 1 Ciudad de la tabla Ciudad
     *
     * @param ciudadId contiene el identificador del CIUDAD a leer
     * @return devuelve una instancia de CIUDAD con los datos leídos
     * @throws ExcepcionArt4UBD ExcepcionArt4UBD se lanzará en caso de ERROR al
     * leer 1 USUARIO la tabla USUARIO
     * @author Rim El abrouki
     * @version 1.2
     * @since 0.0
     */
    public Ciudad leerCiudad(Integer ciudadId) throws ExcepcionArt4UBD {

        Ciudad c = new Ciudad();
        String dql = "select * from CIUDAD where CIUDAD_ID  = " + ciudadId;
        try {
            conectarBD();
            Statement sentencia = conexion.createStatement();
            ResultSet resultado = sentencia.executeQuery(dql);
            if (resultado.next()) {
                c.setCiudadId(resultado.getInt("CIUDAD_ID"));
                c.setNombre(resultado.getString("NOMBRE_CIUDAD"));
            }
            resultado.close();
            sentencia.close();
            desconectarBD();

        } catch (SQLException ex) {
            ExcepcionArt4UBD e = new ExcepcionArt4UBD();
            e.setMensajeErrorAdministrador(ex.getMessage());
            e.setCodigoError(ex.getErrorCode());
            e.setSentanciaSQL(dql);
            e.setMensajeErrorUsuario("Error general del sistema. Consulte con el administrador");
        }
        return c;

    }

    /**
     * Este método permite leer todos los CIUDADES de la tabla CIUDAD.
     *
     * @return un ArrayList de CIUDAD, el cual contiene los CIUDADES leidos
     * @throws ExcepcionArt4UBD ExcepcionArt4UBD se lanzará en caso de ERROR al
     * leer 1 USUARIO la tabla USUARIO
     * @author Rim El abrouki
     * @version 1.2
     * @since 0.0
     */
    public ArrayList<Ciudad> leerCiudades() throws ExcepcionArt4UBD {
        String dql = "select * from Ciudad order by CIUDAD_ID";
        ArrayList<Ciudad> listaCiudades = new ArrayList();
        try {
            conectarBD();
            Statement sentencia = conexion.createStatement();
            ResultSet resultado = sentencia.executeQuery(dql);
            while (resultado.next()) {
                Ciudad c = new Ciudad();
                c.setCiudadId(resultado.getInt("CIUDAD_ID"));
                c.setNombre(resultado.getString("NOMBRE_CIUDAD"));
                listaCiudades.add(c);
            }
            resultado.close();
            sentencia.close();
            desconectarBD();
        } catch (SQLException ex) {
            ExcepcionArt4UBD e = new ExcepcionArt4UBD();
            e.setMensajeErrorAdministrador(ex.getMessage());
            e.setCodigoError(ex.getErrorCode());
            e.setSentanciaSQL(dql);
            e.setMensajeErrorUsuario("Error general del sistema. Consulte con el administrador");
            throw e;
        }
        return listaCiudades;
    }

}
