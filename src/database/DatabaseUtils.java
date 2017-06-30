package database;
import java.sql.*;
import java.util.*;

/**
 * Classe que fornece o objeto Connection para acesso ao banco. Contem alguns scripts de criacao, remocao e povoamento do banco.
 */
public class DatabaseUtils implements DatabaseCredentials{
	
	static {
		//Verifica se o Driver do Postgresql pode ser carregado (se esta no classpath)
		try{
			Class.forName("org.postgresql.Driver");
		}catch(ClassNotFoundException e){
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public static final String customerTableName = "tb_customer_account";


	/**
	 * Cria tabela e a SEQUENCE. Lanca excecao se a sequencia ja existe
	 * 
	 *  A versao 9.4 do PostgreSQL nao suporta a funcionalidade IF NOT EXISTS para sequencias. Entao, se ela ja existir, 
	 *  uma excecao sera lancada.
	 *
	 * @throws     SQLException  {  }
	 */
	public static void createTables() throws SQLException{
		
		String sql = "CREATE TABLE IF NOT EXISTS tb_customer_account (id_customer bigint,cpf_cnpj VARCHAR(20),nm_customer VARCHAR(50),is_active bool,vl_total NUMERIC(100,2),PRIMARY KEY(id_customer));";

		Connection conn = DatabaseUtils.getConn();

		conn.prepareStatement(sql).execute();

		sql= "CREATE SEQUENCE customerid_seq";
		conn.prepareStatement(sql).execute();

		conn.close();

	}

	/**
	 * Remove a tabela de consumidor e a SEQUENCE 'customerid_seq'. 
	 *
	 * @throws     SQLException  { exception_description }
	 */
	public static void dropTables() throws SQLException{
		Connection conn = DatabaseUtils.getConn();

		String sql = "DROP TABLE  IF EXISTS tb_customer_account;";
		conn.prepareStatement(sql).execute();

		sql= "DROP SEQUENCE IF EXISTS customerid_seq";
		conn.prepareStatement(sql).execute();

		conn.close();
	}

	public static Connection getConn() throws SQLException{
		return DriverManager.getConnection(DatabaseUtils.url,DatabaseUtils.user,DatabaseUtils.passwd);
	}

	/**
	 * Simplesmente chama outras funcoes dessa classe para realizar as seguitnes tarefas: remover as tableas e sequencias, as cria novamente
	 * e insere uma quantidade de linhas na tabela.
	 *
	 * @throws     SQLException  { }
	 */
	public static void resetDbAndPopulate() throws SQLException{
		DatabaseUtils.dropTables();
		DatabaseUtils.createTables();
		DatabaseUtils.populateCustomer();
	}

	/**
	 * Insere 12 clientes arbitrarios para teste. 
	 *
	 * @throws     SQLException  {  }
	 */
	public static void populateCustomer() throws SQLException{
		Connection conn = DatabaseUtils.getConn();

		conn.setAutoCommit(false);

		List<Customer> newCustomers = new ArrayList<Customer>();

		newCustomers.add(new Customer(1,"1.1.1.1","Cliente 1",false,400f));
		newCustomers.add(new Customer(2500,"2.2.2.2","Cliente 2",false,560f));
		newCustomers.add(new Customer(3,"3.3.3.3","Cliente 3",false,560.1f));
		newCustomers.add(new Customer(1800,"4.4.4.4","Cliente 4",false,750f));
		newCustomers.add(new Customer(5,"5.5.5.5","Cliente 5",true,400f));
		newCustomers.add(new Customer(1700,"6.6.6.6","Cliente 6",false,4000f));
		newCustomers.add(new Customer(7,"7.7.7.7","Cliente 7",true,400f));
		newCustomers.add(new Customer(8,"8.8.8.8","Cliente 8",false,400f));
		newCustomers.add(new Customer(2700,"9.9.9.9","Cliente 9",false,560.1f));
		newCustomers.add(new Customer(10,"10.10.10.10","Cliente 10",false,400f));
		newCustomers.add(new Customer(1500,"11.11.11.11","Cliente 11",true,560.1f));
		newCustomers.add(new Customer(9000,"12.12.12.12","Cliente 12",true,790f));


		for (Customer c : newCustomers){
			c.insert(conn);
		}



		conn.commit();
		conn.close();
	}	
}