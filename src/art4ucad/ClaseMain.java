package art4ucad;

import art4upojo.Ciudad;
import art4upojo.ExcepcionArt4UBD;
import art4upojo.Usuario;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Rim El abrouki
 * @version 1.0
 * @since AaD 1.0
 */
public class ClaseMain {

    /**
     * Método principal de la clase
     *
     * @param args No recibe parámetros de entrada
     */
    public static void main(String[] args) {
        int registrosAfectados = 0;
        try {
            Art4UCAD cad = new Art4UCAD();

//            //*************** INSERTAR USUARIO *******************
//            try {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//            java.util.Date fechaUtil = sdf.parse("1992-07-18");
//            java.sql.Date fechaSql = new java.sql.Date(fechaUtil.getTime());
//                Usuario u = new Usuario(null, "edwardo", "gabriel", "V", fechaSql, "edwardo@gmial.com", "23433816", "4734547440123456", new Ciudad(2));
//                registrosAfectados = cad.insertarUsuario(u);
//            } catch (ParseException ex) {
//                System.out.println("ERROR DE FECHA" + ex.getMessage());
//            }
//// ******************** ACTUALIZAR USUARIO****************************************
//            try {
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                java.util.Date fechaUtil = sdf.parse("1392-02-19");
//                java.sql.Date fechaSql = new java.sql.Date(fechaUtil.getTime());
//                Ciudad c1 = new Ciudad();
//                c1.setCiudadId(6);
//                Usuario u = new Usuario(null, "Suzan", "sus", "V", fechaSql, "suzana@gmail.aa", "11838ee762", "1311167891123456", c1);
//                System.out.println(cad.actualizarUsuario(77, u));
//            } catch (ParseException ex) {
//                System.out.println("ERROR DE FECHA" + ex.getMessage());
//            }

//-----------------------LEER USUARIOS---------------------
//         ArrayList<Usuario> listaUsuarios = cad.leerUsuarios();
//            for (Usuario u : listaUsuarios) {
//                System.out.println(u.toString());}    
// //****************ELIMINAR USUARIOS***************
//        registrosAfectados = cad.eliminarUsuario(4);
//         System.out.println("Registros afectados: " + registrosAfectados);
////************************** LEER USUARIO ********************************
//            Usuario usario = cad.leerUsuario(1);
//            System.out.println(usario.toString());
////////////********** INSERTAR CIUDAD *****************
//        Ciudad ciud = new Ciudad(null, "kk");
//        registrosAfectados = cad.insertarCiudad(ciud);
////            ****************ELIMINAR CIUDAD************************
//            registrosAfectados = cad.eliminarCiudad(4);
//            System.out.println("Registros afectados: " + registrosAfectados);
////***************LEER CIUDAD**********************
//            Ciudad c = cad.leerCiudad(1);
//            System.out.println(c.toString());
////---------------------MOSTRAR TODAS LAS CIUDADES--------------------
//       ArrayList<Ciudad> listaCiudades = cad.leerCiudades();
//             Iterator<Ciudad> itrCategorias = listaCiudades.iterator();
//             while (itrCategorias.hasNext()) {
//                 Ciudad ciudadsiguiente = itrCategorias.next();
//                 System.out.println(ciudadsiguiente.toString());
//             }
////////-------------ACTUALIZAR CIUDAD-------------------
//             Ciudad cat = new Ciudad(null,"kk1");
//////              el primer parametro es el id de la ciudad que queros cambiar 
//             registrosAfectados = cad.actualizarciudad(3, cat);
//      System.out.println("Registros afectados: " + registrosAfectados);
//---------------
        } catch (ExcepcionArt4UBD ex) {
            System.out.println(ex);
        }
    }
}
