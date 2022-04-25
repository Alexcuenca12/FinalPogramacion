/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalpv.Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
public class ConectionPg {

    Connection con;
    String url = "jdbc:postgresql://localhost:5432/baseFinal";
    String user = "postgres";
    String pass = "1234";

    public ConectionPg() {
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("OK");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConectionPg.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            con = DriverManager.getConnection(url, user, pass);
        } catch (SQLException ex) {
            Logger.getLogger(ConectionPg.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ResultSet consulta(String sql) {
        try {
            Statement st = con.createStatement();
            return st.executeQuery(sql);
        } catch (SQLException ex) {
            Logger.getLogger(ConectionPg.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public boolean accion(String sql) {

        boolean resultado;
        try {
            Statement st = con.createStatement();
            st.execute(sql);
            st.close();
            resultado = true;

        } catch (SQLException ex) {
            Logger.getLogger(ConectionPg.class.getName()).log(Level.SEVERE, null, ex);
            resultado = false;
        }
        return resultado;
    }

    public Connection getCon() {
        return con;
    }

}
