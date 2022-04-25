/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalpv.Model.ModelProducto;

import finalpv.Model.ConectionPg;
import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

/**
 *
 * @author Usuario
 */
public class ModeloProducto extends Producto {

    ConectionPg cpg = new ConectionPg();
    int r;
    int ret;

    public ModeloProducto() {
    }

    public ModeloProducto(String codigoProducto, String nomProd, double precio, double costo, int stock, String descripcion, Date fechaingreso, Image foto, FileInputStream imagen, int largo) {
        super(codigoProducto, nomProd, precio, costo, stock, descripcion, fechaingreso, foto, imagen, largo);
    }

    public List<Producto> listProducto() {
        ArrayList<Producto> listaProducto = new ArrayList<>();
        byte[] bytea;
        try {
            //Sentencia
            String sql = "Select * from Producto";
            //Consulta a la base
            ResultSet rsP = cpg.consulta(sql);
            //Creacion del objeto producto
            while (rsP.next()) {
                Producto produc = new Producto();
                produc.setCodigoProducto(rsP.getString("codigoproducto"));
                produc.setNomProd(rsP.getString("nomprod"));
                produc.setPrecio(rsP.getDouble("precio"));
                produc.setStock(rsP.getInt("stock"));
                produc.setDescripcion(rsP.getString("descripcion"));
                produc.setCosto(rsP.getDouble("costo"));
                produc.setFechaingreso(rsP.getDate("fechaingresp"));
                //Conversion 
                bytea = rsP.getBytes("imagen");
                //Decodificacion 
                if (bytea != null) {
                    try {
                        produc.setFoto(obtenerImagen(bytea));
                    } catch (IOException ex) {
                        Logger.getLogger(ModeloProducto.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                listaProducto.add(produc);
            }
            rsP.close();
            return listaProducto;
        } catch (SQLException ex) {
            Logger.getLogger(ModeloProducto.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public List<Producto> buscarproducto(String criterio) {
        byte[] bytea;
        try {
            ArrayList<Producto> listaper = new ArrayList<Producto>();
            String sql = "SELECT * FROM producto WHERE (nomprod) ilike UPPER ('%" + criterio + "%')OR UPPER(codigoproducto) ilike UPPER ('%" + criterio + "%')";
            ResultSet rs = cpg.consulta(sql);

            while (rs.next()) {
                Producto productobus = new Producto();
                productobus.setCodigoProducto(rs.getString("codigoproducto"));
                productobus.setNomProd(rs.getString("nomprod"));
                productobus.setPrecio(rs.getDouble("precio"));
                productobus.setStock(rs.getInt("stock"));
                productobus.setDescripcion(rs.getString("descripcion"));
                productobus.setCosto(rs.getDouble("costo"));
                productobus.setFechaingreso(rs.getDate("fechaingresp"));

                //Conversion 
                bytea = rs.getBytes("imagen");
                //Decodificacion 
                if (bytea != null) {
                    try {
                        productobus.setFoto(obtenerImagen(bytea));
                    } catch (IOException ex) {
                        Logger.getLogger(ModeloProducto.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                listaper.add(productobus);
            }
            rs.close();
            return listaper;
        } catch (SQLException ex) {
            Logger.getLogger(ModeloProducto.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public boolean crearProductoByte() {
        try {
            String sql;
            sql = "INSERT INTO Producto (codigoproducto,nomprod,precio,stock,imagen,descripcion,costo,fechaingresp)";
            sql += "VALUES(?,?,?,?,?,?,?,?)";
            PreparedStatement ps = cpg.getCon().prepareStatement(sql);
            ps.setString(1, getCodigoProducto());
            ps.setString(2, getNomProd());
            ps.setDouble(3, getPrecio());
            ps.setInt(4, getStock());
            ps.setBinaryStream(5, getImagen(), getLargo());
            ps.setString(6, getDescripcion());
            ps.setDouble(7, getCosto());
            ps.setDate(8, getFechaingreso());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ModeloProducto.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean ActualizarProductoByte() {
        String sql;
        sql = "update producto set nomprod=?, precio=?,costo=? ,fechaingresp=?,descripcion=?,stock=?"
                + "where codigoproducto='" + getCodigoProducto() + "'";
        try {
            PreparedStatement ps = cpg.getCon().prepareStatement(sql);
            ps.setString(1, getNomProd());
            ps.setDouble(2, getPrecio());
            ps.setDouble(3, getCosto());
            ps.setDate(4, getFechaingreso());
            ps.setString(5, getDescripcion());
            ps.setInt(6, getStock());
            // ps.setBinaryStream(6, getImagen(), getLargo());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ModeloProducto.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean eliminarProducto(String id) {
        String sql = "DELETE FROM Producto WHERE codigoproducto='" + id + "'";
        return cpg.accion(sql);
    }

    private Image obtenerImagen(byte[] bytes) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        Iterator it = ImageIO.getImageReadersByFormatName("jpeg");
        Object source = bis;
        ImageReader reader = (ImageReader) it.next();
        ImageInputStream iis = ImageIO.createImageInputStream(source);
        reader.setInput(iis, true);
        //Dimensiones 
        ImageReadParam param = reader.getDefaultReadParam();
        param.setSourceSubsampling(1, 1, 0, 0);
        return reader.read(0, param);
    }

}
