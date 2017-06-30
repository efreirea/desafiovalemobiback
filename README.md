# Desafio Back
	
	Este projeto tem como objetivo cumprir o desafio proposto no processo seletivo da Valemobi. 

# Observações

* Usou-se a linguagem Java
* Foi utilizado PostgreSQL na versão 9.4. O arquivo .jar com o driver do PostgreSQL é necessário, mas não foi fornecido. Deve-se colocar esse arquivo na pasta /lib
* Os scripts assumem que existe um banco criado no Postgres com as exatas especificações presentes no arquivo [DatabaseCredentials.java](src/database/DatabaseCredentials.java). Para garantir isso, foi criado o script [dbcreation.sql](dbcreation.sql) que foi rodado manualmente à parte para criar o banco.
* No arquivo makefile existem scripts para compilação e execução do código
