/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalpv.Model.ModelProducto;

import java.awt.Image;
import java.io.FileInputStream;
import java.sql.Date;

/**
 *
 * @author Usuario
 */
public class Producto {

    private String codigoProducto;
    private String nomProd;
    private double precio;
    private double costo;
    private int stock;
    private String descripcion;
    private Date fechaingreso;
    private Image foto;
    private FileInputStream imagen;
    private int largo;

    public Producto() {
    }

    public Producto(String codigoProducto, String nomProd, double precio, double costo, int stock, String descripcion, Date fechaingreso, Image foto, FileInputStream imagen, int largo) {
        this.codigoProducto = codigoProducto;
        this.nomProd = nomProd;
        this.precio = precio;
        this.costo = costo;
        this.stock = stock;
        this.descripcion = descripcion;
        this.fechaingreso = fechaingreso;
        this.foto = foto;
        this.imagen = imagen;
        this.largo = largo;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public String getNomProd() {
        return nomProd;
    }

    public void setNomProd(String nomProd) {
        this.nomProd = nomProd;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaingreso() {
        return fechaingreso;
    }

    public void setFechaingreso(Date fechaingreso) {
        this.fechaingreso = fechaingreso;
    }

    public Image getFoto() {
        return foto;
    }

    public void setFoto(Image foto) {
        this.foto = foto;
    }

    public FileInputStream getImagen() {
        return imagen;
    }

    public void setImagen(FileInputStream imagen) {
        this.imagen = imagen;
    }

    public int getLargo() {
        return largo;
    }

    public void setLargo(int largo) {
        this.largo = largo;
    }

}
