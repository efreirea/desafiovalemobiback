package database;

/**
 * Interface para guardar informacoes de acesso a banco. Ests sendo utilizado PostgreSQL versao 9.4.
 */
public interface DatabaseCredentials{
	public static final String dbName= "valemobidb";
	public static final String url = "jdbc:postgresql://localhost/"+dbName;
	public static final String user= "valemobi";
	public static final String passwd= "1234";
}