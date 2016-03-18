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
 Role varchar(20) not null,
 Mobile varchar(12),
 Location varchar(20) not null,
 Last_Login datetime null,
 Active boolean not null,
 CONSTRAINT chk_Mobile CHECK (Mobile NOT LIKE '%[^0-9]%')
 );
 insert into tblUsers values(null,'sushant','sushant',AES_ENCRYPT('mrrobot','rycbarm'),'Admin',9892189354,'Vikhroli',now(),true);
 _____________
 create table mtblCriminals
 (
 cid bigint AUTO_INCREMENT,
 image longblob,
 image_size int(11), 
 ad date,
 fname varchar(20),
 mname varchar(20),
 lname varchar(20),
 dob date,
 state varchar(20),
 city varchar(20),
 address varchar(100),
 gender varchar(1),
 mstatus varchar(10),
 color varchar(20),
 hair varchar(20),
 bg varchar(20),
 height double,
 weight double,
 eyes varchar(20),
 facility varchar(20),
 section varchar(20),
 cell varchar(20),
 ai varchar(500),
 primary key(cid)
 );
 */
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {

    static Connection con;
    PreparedStatement pst;
    static ResultSet rs;
    static Statement st;
    static int id = 0;
    static String name = null;

    Database() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("Jdbc:mysql://localhost:6666/crms?user=root&password=1234");
            st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);
        }
    }

    public static int getID() {
        return id;
    }

    public static Statement getStatement() {
        Statement st=null;
        try {
             st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return st;
    }

    public static Connection getConnection() {

        return con;
    }

    public static void closeStatement() {

        try {
            st.close();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String getName() throws SQLException {
        rs = st.executeQuery("select * from tblUsers where id=" + id);
        rs.next();
        return rs.getString(2);

    }

    public static String getRole() {
        String r = "";
        try {
            rs = st.executeQuery("select * from tblUsers where id=" + id);
            rs.next();
            r = rs.getString(5);
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return r;

    }

    public int checkLogin(String uname, String pwd) {
        try {
            pst = con.prepareStatement("select * from tblUsers where UserName=? and Password=AES_ENCRYPT(?,'rycbarm')");

            pst.setString(1, uname); //this replaces the 1st  "?" in the query for username
            pst.setString(2, pwd);    //this replaces the 2st  "?" in the query for password
            //executes the prepared statement
            rs = pst.executeQuery();
            if (rs.next()) {
                //TRUE iff the query founds any corresponding data
                id = rs.getInt(1);
                name = rs.getString(2);
                st.executeUpdate("update tblUsers set Last_Login=now() where id=" + id + ";");
                //st.executeUpdate("commit");
                return 1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("error while validating" + e);
            return 0;
        }
    }
}
