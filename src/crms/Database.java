/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crms;

/**
 *
 * @author Orlon
 */
/*
CREATE TABLE tblUsers (
`id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
Name varchar(50) not null,
UserName varchar(50) not null unique,
Password BLOB not null,
Role int not null,
Mobile varchar(12),
Location varchar(20) not null,
Last_Login datetime null,
Active boolean not null,
FOREIGN KEY(Role) REFERENCES mtblRoles(RID),
CONSTRAINT chk_Mobile CHECK (Mobile NOT LIKE '%[^0-9]%')
);
insert into tblUsers values(null,'sushant','sushant',AES_ENCRYPT('mrrobot','rycbarm'),1,9892189354,'Vikhroli',now(),true);
*/

import java.sql.*;
public class Database 
{
    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    Statement st;
    static int id=0;
    static String name=null;
    Database()
    {
        try{
             
            //MAKE SURE YOU KEEP THE mysql_connector.jar file in java/lib folder
            //ALSO SET THE CLASSPATH
            Class.forName("com.mysql.jdbc.Driver");
            con=DriverManager.getConnection("Jdbc:mysql://localhost:6666/crms?user=root&password=1234");
            st=con.createStatement();
                        
           }
        catch (ClassNotFoundException | SQLException e) 
        {
            System.out.println(e);
        }
    }
    public static int getID()
    {   return id;
    }
    public Statement getStatement()
    {
        return st;
    }
    public Connection getConnection()
    {
        return con;
    }
    
    public static String getName()
    {
        return name;
    }
    public int checkLogin(String uname,String pwd)
    {
        try {
            pst=con.prepareStatement("select * from tblUsers where UserName=? and Password=AES_ENCRYPT(?,'rycbarm')");
            
            pst.setString(1, uname); //this replaces the 1st  "?" in the query for username
            pst.setString(2, pwd);    //this replaces the 2st  "?" in the query for password
            //executes the prepared statement
            rs=pst.executeQuery();
            if(rs.next())
            {
                //TRUE iff the query founds any corresponding data
               id=rs.getInt(1);
               name=rs.getString(2);
               st.executeUpdate("update tblUsers set Last_Login=now() where id="+id+";");
               //st.executeUpdate("commit");
               return 1;
            }
            else
            {
                return 0;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("error while validating"+e);
            return 0;
        }
    }
}
