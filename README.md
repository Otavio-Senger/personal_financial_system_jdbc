# SISTEMA FINANCEIRO PESSOAL
Programa criado para controle de receitas e despesas via terminal. Etapa 1 de um projeto de aprendizado maior divido em outras 4 etapas. Começando com banco de dados local e java puro e, posteriormente, será introduzido API REST (Spring boot) e, por fim, AWS.

## ÍNDICE
 - Técnologias Utilizadas
 - Funcionalidades
 - Estrutura do Projeto
 - Como Configurar o Projeto
 - Preview do Menu
 - Próximos Passos

## TÉCNOLOGIAS UTILIZADAS
- **Java**
- **JDBC**
- **MySQL**
- **IntelliJ**

## FUNCIONALIDADES
- Cadastrar transações registrando o valor da transação, descrição, data e a categoria (EX: alimentação, lazer, salário);
- Editar uma transação, podendo alterar qualquer campo da mesma;
- Listar transações, mostrando todas as transações e suas informações;
- Deletar transação, excluindo uma transação do seu desejo;
- Filtrar transações, sendo possível listar transações filtradas pelo tipo, categoria ou data;
- Cadastrar uma nova categoria registrando o nome e o tipo (despesas e receitas);
- Listar categorias, mostrando todas as categorias e suas informações;
- Deletar categoria, excluindo uma categoria do seu desejo;
- Relatório de entradas e saídas, mostrando o valor total das entradas e das saídas.

## ESTRUTURA DO PROJETO
- **Connection:** Responsável por fazer a conexão com o banco de dados;
- **Model:** Entidades, onde ficam as tabelas do banco de dados;
- **DAO:** Responsável pelo acesso e manipulação dos dados no BD, centralizando as operações SQL realizadas via JDBC;
- **Main:** Ponto de entrada da aplicação, responsável por iniciar e executar o fluxo principal do sistema.

## COMO CONFIGURAR E RODAR O PROJETO
- **Criar banco de dados:**

```sql
CREATE DATABASE personal_financial_system;
USE personal_financial_system;

CREATE TABLE CATEGORY (
id INT AUTO_INCREMENT PRIMARY KEY,
name VARCHAR(50) NOT NULL,
type ENUM ('REVENUE', 'EXPENSES') NOT NULL
);

CREATE TABLE TRANSACTIONS (
id INT AUTO_INCREMENT PRIMARY KEY,
descriptions VARCHAR(100) NOT NULL,
amount DECIMAL(10, 2) NOT NULL,
dates DATE NOT NULL,
category_id INT NOT NULL,
FOREIGN KEY (category_id) REFERENCES CATEGORY (id)
);

INSERT INTO CATEGORY (name, type) VALUES
('FOOD', 'EXPENSES'),
('FUEL', 'EXPENSES'),
('LEISURE', 'EXPENSES'),
('BILLS', 'EXPENSES'),
('WAGE', 'REVENUE'),
('FREELANCE', 'REVENUE');
```                                                 

- Após a criação do banco de dados, deve-se ir no projeto e inserir seu usuário e senha do banco de dados no caminho abaixo (Não vem no repositório por questão de segurança !!!):
  **SISTEMA_FINANCEIRO_PESSOAL/JAVA+JDBC/sistema_financeiro_pessoal_jdbc/src/main/resources/db.properties**

## PREVIEW DO MENU
```
 ==== SISTEMA FINANCEIRO PESSOAL ====
1 - Register Transaction 
2 - List Transactions 
3 - Register Category 
4 - List Categories 
5 - Delete Transaction 
6 - Delete Category 
7 - Filter Transactions 
8 - Inflow and Outflow Report 
9 - Edit Transaction 
0 - Exit
Enter choice: 
```
## PRÓXIMOS PASSOS
Este projeto faz parte de um aprendizado maior, dividido em 4 etapas evolutivas:

- **Etapa 2:** Migração para Spring Boot com API REST, introduzindo comunicações via HTTP/JSON entre o cliente e servidor. Também é nessa etapa que a autenticação (login) será implementada;
- **Etapa 3:** Criação de uma interface web (HTML, CSS, JavaScript), consumindo a mesma API REST construída na etapa 2;
- **Etapa 4:** Deploy em nuvem utilizando AWS, com banco de dados migrado para Amazon RDS (MySQL).

