package project;

/**
 * 
 * @author Kavitha Muthu, Swathi Prasad
 *
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 
 * Class is used to get JDBC connection
 *
 */
public class ConnectDB {

	public Connection getConnection() {
		try {
			// Load driver class
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		//username and password for the JDBC conection
		String url = "jdbc:mysql://localhost:3306/SoSafeProject";
		String name = "root";
		String password = "root";

		Connection con = null;
		try {
			// Get JDBC connection
			con = DriverManager.getConnection(url, name, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	}
}