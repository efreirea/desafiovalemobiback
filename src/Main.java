import database.DatabaseUtils;
import database.Customer;

import java.sql.*;
import java.util.*;

/**
 * Classe principal do script. Utilizou-se PostgreSQL
 */
public class Main{
	
	private static void printVec(List<Customer> v){
		System.out.println("ID\tNOME\tVALOR");
		for (Customer c : v){
			System.out.println(c.getId()+"\t"+c.getName()+"\t"+c.getTotal());
		}
	}

	public static void main(String[] args){
		try{
			DatabaseUtils.resetDbAndPopulate(); //reseta o banco de dados e insere clientes
		}catch(Exception e){
			e.printStackTrace();
		}
		List<Customer> customers = new ArrayList<Customer>(); //array qye armazenara os clientes que participaram do calculo
		float avg=0;
		try{

			Connection conn = DatabaseUtils.getConn();
			avg = Customer.calculateAvgCustomers(conn,customers); //calcula a media e recupera os clientes
			conn.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		//printa saida
		System.out.println("Valor da Media: "+avg);
		Main.printVec(customers);
	}
}