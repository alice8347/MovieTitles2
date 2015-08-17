import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

/**
 * Simple Java Program to connect Oracle database by using Oracle JDBC thin
 * driver Make sure you have Oracle JDBC thin driver in your classpath before
 * running this program
 * 
 * @author
 */
public class MovieTitles2 {

	public static void main(String args[]) throws SQLException {
		// URL of Oracle database server
		String url = "jdbc:oracle:thin:testuser/password@localhost";

		// properties for creating connection to Oracle database
		Properties props = new Properties();
		props.setProperty("user", "testdb");
		props.setProperty("password", "password");

		// creating connection to Oracle database using JDBC
		Connection conn = DriverManager.getConnection(url, props);

		System.out.println("Myxyllplyk's Random Movie Title Generator\n");

		String adjDBCount = "SELECT Count(*) FROM adjectives";
		// creating PreparedStatement object to execute query
		PreparedStatement preStatement = conn.prepareStatement(adjDBCount);
		ResultSet result = preStatement.executeQuery();

		int adjCount = 0;
		while (result.next()) {
			adjCount = result.getInt(1);
			System.out.print("Choosing randomly from " + adjCount
					+ " adjectives ");
		}

		String nounDBCount = "SELECT Count(*) FROM nouns";
		preStatement = conn.prepareStatement(nounDBCount);
		result = preStatement.executeQuery();

		int nounCount = 0;
		while (result.next()) {
			nounCount = result.getInt(1);
			System.out.println("and " + nounCount + " nouns ("
					+ (adjCount * nounCount) + " combinations).");
		}

		String getAdj = "SELECT adj FROM (SELECT * FROM movieTitleGenerator ORDER BY DBMS_RANDOM.RANDOM) WHERE rownum = 1";
		PreparedStatement adjStatement = conn.prepareStatement(getAdj);
		ResultSet adjResult = adjStatement.executeQuery();

		String getNoun = "SELECT noun FROM (SELECT * FROM movieTitleGenerator ORDER BY DBMS_RANDOM.RANDOM) WHERE rownum = 1";
		PreparedStatement nounStatement = conn.prepareStatement(getNoun);
		ResultSet nounResult = nounStatement.executeQuery();

		String adj = "", noun = "";
		while (adjResult.next() && nounResult.next()) {
			adj = adjResult.getString(1);
			noun = nounResult.getString(1);
		}

		String movieTitle = adj + " " + noun;
		System.out.println("Your movie title is: " + movieTitle);
		System.out.print("Please enter the description: ");
		Scanner sc = new Scanner(System.in);
		String description = sc.nextLine();

		String movieIdSql = "SELECT COUNT(*) FROM movies";
		PreparedStatement movieIdStatement = conn.prepareStatement(movieIdSql);
		ResultSet movieIdResultSet = movieIdStatement.executeQuery();
		int movieId = 0;
		while (movieIdResultSet.next()) {
			movieId = movieIdResultSet.getInt(1) + 1;
		}
		
		String update = "INSERT INTO movies VALUES (" + movieId + ", '" + movieTitle + "', '" + description + "')";
		preStatement = conn.prepareStatement(update);
		preStatement.executeQuery();
		
		System.out.println("Movie information updated.");
	}
}