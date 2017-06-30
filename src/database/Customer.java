package database;
import java.sql.*;
import java.util.*;

public class Customer {

	private int idCustomer;
	private String cpfCnpj;
	private String name;
	private boolean isActive;
	private float total;


	public Customer(int idCustomer,String cpfCnpj, String name, boolean isActive,float total){
		this(cpfCnpj,name,isActive,total);
		this.idCustomer=idCustomer;
	}

	public Customer(String cpfCnpj, String name, boolean isActive,float total){
		this.cpfCnpj=cpfCnpj;
		this.name=name;
		this.isActive=isActive;
		this.total=total;
		this.idCustomer=-1;
	}
	//GETTERS
	public int getId(){
		return this.idCustomer;
	}

	public String getCPF(){
		return this.cpfCnpj;
	}
	public String getName(){
		return this.name;
	}
	public boolean isActive(){
		return this.isActive;
	}
	public float getTotal(){
		return this.total;
	}

	/**
	 * Calcula a media do campo vl_total de acordo com as especificacoes do enunciado. Utiliza puramente SQL para o calculo
	 * 
	 * Inicialmente, pensou-se em fazer somente com SQL. Mas depois de pensar um pouco, talvez esse nao seria o objetivo do teste.
	 * Entao foram criadas outras versoes abaixo
	 *
	 * @param      conn           Objeto Connection para acessar o banco
	 * @param      customersList  Lista onde serao retornados os clientes que participaram do calculo. Os dados dessa lista sao inicialmente apagados para dar lugar aos novos.
	 *
	 * @return     MEdia.
	 *
	 * @throws     SQLException   {  }
	 */
	public static float calculateAvgCustomersSQL(Connection conn, List<Customer> customersList) throws SQLException{
		
		float avg=-1;
		//Query SQL para calcular a media
		String sqlAVG = "SELECT AVG(vl_total)"+
						" FROM "+DatabaseUtils.customerTableName+
						" WHERE"+
							" id_customer BETWEEN 1500 AND 2700"+
							" AND vl_total>560";

		//Query SQL para pegar os dados dos clientes que participaram do calculo
		String sqlCustomers = "SELECT id_customer, cpf_cnpj, nm_customer, is_active, vl_total"+
						" FROM "+DatabaseUtils.customerTableName+
						" WHERE"+
							" id_customer BETWEEN 1500 AND 2700"+
							" AND vl_total>560"+
						" ORDER BY vl_total DESC";

		PreparedStatement stm = conn.prepareStatement(sqlAVG);

		ResultSet resultSet =  stm.executeQuery();
		if(resultSet.next()){// Pega o resultado
			avg=resultSet.getFloat(1);
		}
		resultSet.close();
		
		stm = conn.prepareStatement(sqlCustomers);
		resultSet =  stm.executeQuery();
		while(resultSet.next()){ //percorre os clientes
			int id = resultSet.getInt("id_customer");
			String cpfCnpj = resultSet.getString("cpf_cnpj");
			String name = resultSet.getString("nm_customer");
			boolean isActive = resultSet.getBoolean("is_active");
			float total = resultSet.getFloat("vl_total");
			
			customersList.add(new Customer(id,cpfCnpj,name,isActive,total)); //cria objetos para armazena-los na memoria
		}
		resultSet.close();

		return avg;
	}

	/**
	 * Calcula a media, seguindo os criterios, sem utilizar SQL. SQL so eh utilizado para recuperar TODOS os clientes. 
	 * 
	 * Utilza SQL para recuperar todos os dados do banco, para depois utilizar o java para verificar as condicoes do enunciado 
	 * E ordenar.
	 *
	 * @param      conn           Objeto Connection para acessar o banco
	 * @param      customersList  Lista onde serao retornados os clientes que participaram do calculo. Os dados dessa lista sao inicialmente apagados para dar lugar aos novos.
	 *
	 * @return     Media
	 *
	 * @throws     SQLException   {  }
	 */

	public static float calculateAvgCustomers(Connection conn, List<Customer> customersList) throws SQLException{
		
		float avg=0;

		//SQL para pegar todos os dados do banco, sem filtrar nada.
		String sqlCustomers = "SELECT id_customer, cpf_cnpj, nm_customer, is_active, vl_total"+
						" FROM "+DatabaseUtils.customerTableName;

		PreparedStatement stm = conn.prepareStatement(sqlCustomers);
		ResultSet resultSet =  stm.executeQuery();
		
		customersList.clear(); //limpa a lista para evitar que dados externos influenciem
		
		while(resultSet.next()){ // percorre cada linha da tabela
			int id = resultSet.getInt("id_customer");
			String cpfCnpj = resultSet.getString("cpf_cnpj");
			String name = resultSet.getString("nm_customer");
			boolean isActive = resultSet.getBoolean("is_active");
			float total = resultSet.getFloat("vl_total");
			
			if(total>560 && id>=1500 && id<=2700){ // Condicoes impostas pelo exercicio
				customersList.add(new Customer(id,cpfCnpj,name,isActive,total)); // adiciona no vetor
				avg += total;
			}

		}
		resultSet.close();

		if(customersList.size() >0){ // verifica para evitar divisao por zero
			avg = avg/customersList.size();
		}

		//ORdena o vetor
		Collections.sort(customersList, new Comparator<Customer>(){
			//Utilizacao da comparacao com logica contraria, ou seja, de maneira que ordene de maneira decrescente
			public int compare(Customer o1, Customer o2){
				float o1Total = o1.getTotal();
				float o2Total = o2.getTotal();
				
				if(o1Total<o2Total){
					return 1;
				}else if(o1Total>o2Total){
					return -1;
				}else{
					return 0;
				}
			}

			public boolean equals(Object o){
				return false;
			}
		});

		return avg;
	}
	




	/**
	 * Insere o objeto atual no banco. NAO realiza autocommit, ou seja, commit deve ser chamado por fora
	 *
	 * @param      conn  The connection
	 *
	 * @return     { description_of_the_return_value }
	 */
	public boolean insert(Connection conn) throws SQLException{

		String sql = "";
		
		
		//Se nao possui um ID setado, entao utiliza-se a sequencia customerid_seq como id.
		if(this.idCustomer<0){
			sql = "INSERT INTO "+DatabaseUtils.customerTableName+ "(cpf_cnpj,nm_customer,is_active,vl_total,id_customer) VALUES(?,?,?,?,nextval('customerid_seq')) RETURNING id_customer";
		}else{//se no objeto ja esta setado o ID, utiliza o que ja existe
			sql = "INSERT INTO "+DatabaseUtils.customerTableName+ "(cpf_cnpj,nm_customer,is_active,vl_total,id_customer) VALUES(?,?,?,?,? )";
		}

		PreparedStatement stm = conn.prepareStatement(sql);

		stm.setString(1,this.cpfCnpj);
		stm.setString(2,this.name);
		stm.setBoolean(3,this.isActive);
		stm.setObject(4,new Float(this.total),java.sql.Types.NUMERIC);

		if(this.idCustomer<0){//se id vai ser gerado pela sequencia....
			ResultSet resultSet= stm.executeQuery();
			if(resultSet.next()){
				this.idCustomer= resultSet.getInt(1); //seta a proprieda idCustomer desse objeto com o id gerado no banco
				return true;
			}else{
				return false;	
			}
		}else{
			stm.setInt(5,this.idCustomer);
			return stm.execute();
		}
		
		
		

		
	}

	/**
	 * Para debug
	 *
	 * @return     String que representa o objeto
	 */
	public String toString(){
		return "id: "+this.idCustomer+" cpf: "+this.cpfCnpj+" nome: "+this.name+" Ativo: "+this.isActive+" total: "+this.total;
	}
}