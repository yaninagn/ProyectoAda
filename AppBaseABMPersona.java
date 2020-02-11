package baseDatos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;

public class AppBaseABMPersona {

	public static void main(String[] args) {

		Connection conexion = null;
		try {
			conexion = AdminBD.obtenerConexion();
			Scanner sc = new Scanner(System.in);

			int opcion = mostrarMenu(sc);
			while (opcion != 0) {

				switch (opcion) {
				case 1:
					alta(conexion, sc);
					break;
				case 2:
					modificacion(conexion, sc);
					break;
				case 3:
					baja(conexion, sc);
					break;
				case 4:
					listado(conexion);
					break;
				case 0:
					
					break;

				default:
					System.out.println("Por favor, seleccione una opción correcta.");
					break;
				}
				opcion = mostrarMenu(sc);
			}

			conexion.close();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	private static void listado(Connection conexion) {
		System.out.println();
		System.out.println("LISTADO--------------------");
		System.out.println("ID--NOMBRE----EDAD---F.NACIM---");
		Statement stmt;
		try {
			stmt = conexion.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM PERSONA");
			while (rs.next()) {
				System.out.println(rs.getInt(1) + " " + rs.getString(2) + " " + rs.getInt(3) + " " + rs.getDate(4));
			}
			System.out.println("FIN LISTADO------------");
			System.out.println();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private static void baja(Connection conexion, Scanner sc) {
		System.out.println("BAJA DE PERSONA");
		System.out.println("Ingrese Nro de ID del registro a eliminar");
		String ID = sc.next();

		Statement stmt;
		try {
			stmt = conexion.createStatement();

			ResultSet rs = stmt.executeQuery("SELECT * FROM PERSONA WHERE ID =" + ID);
			rs.next();
			System.out.println("¿Desea Eliminar el registro Nro: " + ID + " ?" + " " + rs.getString(2) + " "
					+ rs.getInt(3) + " " + rs.getDate(4));
			System.out.println("1) SI   2) NO");

			int opcion = sc.nextInt();
			if (opcion != 2) {

				try {
					stmt = conexion.createStatement();
					String DELETE = "DELETE FROM PERSONA WHERE ID =" + ID;
					stmt.executeUpdate(DELETE);

					System.out.println("El registro Nro. " + ID + " ha sido eliminado con éxito.");

				} catch (SQLException e) {
					e.printStackTrace();

				}
			} else {
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void modificacion(Connection conexion, Scanner sc) {
		System.out.println("MODIFICACION DE DATOS");
		System.out.println("Ingrese Nro de ID del registro a MODIFICAR");
		String ID = sc.next();
		System.out.println("Por favor, seleccione el dato a modificar: 1) NOMBRE 2)FECHA DE NACIMIENTO");
		int opcion = sc.nextInt();
		try {
			Statement stmt;
			stmt = conexion.createStatement();
			while (opcion != 0) {

				switch (opcion) {
				case 1:
					System.out.println("Ingrese nuevo Nombre");
					String nombreNew = sc.next();
					String update = "UPDATE PERSONA SET NOMBRE='" + nombreNew + "' WHERE ID=" + ID + ";";
					stmt.executeUpdate(update);
					System.out.println("El nombre del registro " + ID + " ha sido modificado con éxito");

					break;
				case 2:

					stmt = conexion.createStatement();
					System.out.println("Ingrese nuevA FECHA DE NACIMIENTO (AAAA/MM/DD)");
					String fNacNew = sc.next();

					String update2 = "UPDATE PERSONA SET FECHA_NACIMIENTO='" + fNacNew + "' WHERE ID=" + ID + ";";
					stmt.executeUpdate(update2);
					System.out.println("La fecha de Nacimiento del registro " + ID + " ha sido modificada con éxito");
					break;
				default:
					break;
				}
				opcion = mostrarMenu(sc);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private static void alta(Connection conexion, Scanner sc) {

		System.out.println("ALTA DE PERSONA");
		System.out.println("Ingrese nombre:");
		String nombre = sc.next();
		System.out.println("Ingrese fecha nacimiento (aaaa/mm/dd):");
		String fNac = sc.next();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		int edad = 0;
		try {
			Date fechaNac = sdf.parse(fNac);
			edad = calcularEdad(fechaNac);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		Statement stmt;
		try {
			stmt = conexion.createStatement();
			String insert = "INSERT INTO PERSONA (nombre, edad, fecha_nacimiento) VALUES ('" + nombre + "', " + edad
					+ " , '" + fNac + "')";
			stmt.executeUpdate(insert);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private static int calcularEdad(Date fechaNac) {
		GregorianCalendar gc = new GregorianCalendar();
		GregorianCalendar hoy = new GregorianCalendar();
		gc.setTime(fechaNac);
		int anioActual = hoy.get(Calendar.YEAR);
		int anioNacim = gc.get(Calendar.YEAR);

		int mesActual = hoy.get(Calendar.MONTH);
		int mesNacim = gc.get(Calendar.MONTH);

		int diaActual = hoy.get(Calendar.DATE);
		int diaNacim = gc.get(Calendar.DATE);

		int dif = anioActual - anioNacim;

		if (mesActual < mesNacim) {
			dif = dif - 1;
		} else {
			if (mesActual == mesNacim && diaActual < diaNacim) {
				dif = dif - 1;
			}
		}

		return dif;
	}

	private static int mostrarMenu(Scanner sc) {

		System.out.println("SISTEMA DE PERSONAS (ABM)");
		System.out.println("=========================");
		System.out.println("MENU OPCIONES: ");
		System.out.println("1: ALTA ");
		System.out.println("2: MODIFICACION ");
		System.out.println("3: BAJA");
		System.out.println("4: LISTADO");
		System.out.println("0: SALIR");
		int opcion = 0;
		opcion = sc.nextInt();
		return opcion;
	}
}
