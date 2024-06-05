package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Hello world!
 *
 */

class Employee{
    int id;
    String name;
    String position;
    Double Salary;

    public Employee(String name, String position, Double salary) {
        this.name = name;
        this.position = position;
        Salary = salary;
    }
}

public class App 
{
    public static void main( String[] args ) {
        Employee e1 = new Employee("B", "Intern", 10000.0);
        Employee e2 = new Employee("C", "Full Time", 20000.0);

        List<Employee> empList = Arrays.asList(e1,e2);
       /* empList.add(e1);
        empList.add(e2);
        */
        System.out.println( "Hello World!" );

        String url = "jdbc:mysql://localhost:3306/test_new_schema";
        String username = "root";
        String password = "password";

        try( Connection connection = DriverManager.getConnection(url, username, password)){

            // Creating Database Connection

            Class.forName(
                    "com.mysql.cj.jdbc.Driver");
            System.out.println("Connected to the database!");

            // Create Statement
            Statement stmt = connection.createStatement();
            createTable(stmt);

            //insert statement
            insertData(stmt, empList);

            List<Employee> empListOutput = selectData(stmt);
            System.out.println(empListOutput);

        } catch (Exception e) {
            System.err.println("Connection error: " + e.getMessage());
        }finally{

        }

    }

    public static void createTable(Statement stmt ){
        String tableSql = "CREATE TABLE IF NOT EXISTS test_employees"
                + "(emp_id int PRIMARY KEY AUTO_INCREMENT, name varchar(30),"
                + "position varchar(30), salary double)";
        try {
            stmt.execute(tableSql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void insertData(Statement stmt, List<Employee> empList){
      /*  for(Employee e: empList) {
            int result = 0;
            try {
                result = stmt.executeUpdate(
                        "insert into test_employees(emp_id,name,position,salary) values('x','" + e.name + "','" + e.position + "'" +
                                ", '" + e.Salary + "' )");
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
*/
            empList.forEach(e -> {
              int result = 0;
                        try {
                            result = stmt.executeUpdate(
                                    "insert into test_employees(name,position,salary) values('" + e.name + "','" + e.position + "'" +
                                            ", '" + e.Salary + "' )");
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                if (result > 0)
                    System.out.println("successfully inserted");
                else
                    System.out.println( "unsucessful insertion ");
                    });


            // if result is greater than 0, it means values
            // has been added
    }

    public static List<Employee> selectData(Statement stmt){
        String selectSql = "SELECT * FROM test_employees";

        List<Employee> empList1 = new ArrayList<>();
        Employee e = null;

        try (ResultSet rs = stmt.executeQuery(selectSql)) {
            while(rs.next()){
                //Display values
                int empId = rs.getInt("emp_id");
                String name = rs.getString("name");
                String position = rs.getString("position");
                Double salary = rs.getDouble("salary");

                System.out.print("Emp ID: " + rs.getInt("emp_id"));
                System.out.print(", Name: " + rs.getString("name"));
                System.out.print(", Position: " + rs.getString("position"));
                System.out.println(", salary: " + rs.getDouble("salary"));

                e = new Employee( name, position, salary);
                empList1.add(e);
            }
            // use resultSet here
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return empList1;
    }
}
