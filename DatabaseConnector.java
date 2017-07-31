import java.sql.*;
import java.util.*;

public class DatabaseConnector {

	private static Statement statement;
	private static PreparedStatement pstatement;
	private static Connection connection;

	private String driver = "com.mysql.jdbc.Driver";
	private String url = "jdbc:mysql://localhost:3306/phonebook";
	private String login = "";
	private String password = "";

	public DatabaseConnector() {
		try{
			try {
				Class.forName(driver);	
				connection = DriverManager.getConnection(url, login, password);
			}
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		catch (SQLException e) {
			e.printStackTrace();			
		}		
	}

	public  void add(String firstName, String lastName, String number) throws SQLException {
		pstatement = connection.prepareStatement("INSERT INTO phoneBook VALUES(?, ?, ?)");
		pstatement.setString(1, firstName);
		pstatement.setString(2, lastName);
		pstatement.setString(3, number);		
		pstatement.executeUpdate();
		pstatement.close();
		}
	
	public  void delete(String number) throws SQLException {
		pstatement = connection.prepareStatement("DELETE FROM phoneBook WHERE number = ?");
		pstatement.setString(1, number);
		pstatement.executeUpdate();
		pstatement.close();
	}

	public  ArrayList<Person> getAll() throws SQLException {
		ArrayList<Person> personsList = new ArrayList<>();
		statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT * FROM phoneBook");
		while(resultSet.next()) {
			String firstName = resultSet.getString("firstName");
			String lastName = resultSet.getString("secondName");
			String number = resultSet.getString("number");
			Person person = new Person(firstName, lastName, number);
			personsList.add(person);
		}
		return personsList;
	}

	public void closeConnection() throws SQLException {
		connection.close();
	}
}
