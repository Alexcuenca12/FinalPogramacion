/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalpv.Controller;

import com.toedter.calendar.JDateChooser;
import finalpv.Model.ConectionPg;
import finalpv.Model.ModelProducto.ModeloProducto;
import finalpv.Model.ModelProducto.Producto;
import finalpv.Vista.vistaProductos;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.xml.ws.Holder;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Usuario
 */
public class controladorProductos {

    private ModeloProducto modeloProducto;
    private vistaProductos vistaProducto;
    private JFileChooser jfc;

    public controladorProductos(ModeloProducto modeloProducto, vistaProductos vistaProducto) {
        this.modeloProducto = modeloProducto;
        this.vistaProducto = vistaProducto;
        cargarProducto();
        vistaProducto.setVisible(true);
    }

    public void controlProducto() {
        //Estar a la escucha de todos los eventos
        //Btn Actualizar

        vistaProducto.getBtn_crear2().addActionListener(l -> abrirDialogo(1));
        vistaProducto.getBtn_edit2().addActionListener(l -> abrirDialogo(2));
        //Listeners del Dialogo
        vistaProducto.getBtnAceptar().addActionListener(l -> crear_actualizarProducto());
        vistaProducto.getBtn_delet2().addActionListener(l -> eliminarProducto());
        vistaProducto.getBtnCancelar().addActionListener(l -> cancelar());
        vistaProducto.getBtnExaminar().addActionListener(l -> examinaFoto());
        vistaProducto.getBtn_print2().addActionListener(l -> imprimirReporte());
        vistaProducto.getTxtBuscar().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                cargarProducto();
            }
        });
    }

    private void abrirDialogo(int c) {
        String title;
        if (c == 1) {
            title = "CREAR PRODUCTO";
            vistaProducto.getDlgProducto().setName("crear");
        } else {
            title = "EDITAR PRODUCTO";
            vistaProducto.getDlgProducto().setName("editar");
            modificar();
        }
        vistaProducto.getDlgProducto().setLocationRelativeTo(vistaProducto);
        vistaProducto.getDlgProducto().setSize(850, 450);
        vistaProducto.getDlgProducto().setTitle(title);
        vistaProducto.getDlgProducto().setVisible(true);
    }

    private void crear_actualizarProducto() {
        if ("crear".equals(vistaProducto.getDlgProducto().getName())) {
            //Insertar 
            String idProducto = (vistaProducto.getTxtID().getText());
            String nombre = vistaProducto.getTxtNombreP().getText();
            double precio = Double.parseDouble(vistaProducto.getTxtPrecio().getText());
            double costo = Double.parseDouble(vistaProducto.getTxtCosto().getText());
            int Stock = (int) vistaProducto.getSpinStock().getValue();
            String fechaIngreso = getFecha(vistaProducto.getDc_fecha());
            String descripcion = vistaProducto.getTxtDescripcion().getText();
            //Instancia del Modelo
            ModeloProducto producto = new ModeloProducto();
            //Set
            producto.setCodigoProducto(idProducto);
            producto.setNomProd(nombre);
            producto.setPrecio(precio);
            producto.setCosto(costo);
            producto.setStock(Stock);
            producto.setFechaingreso(Date.valueOf(fechaIngreso));
            producto.setDescripcion(descripcion);
            //Set en el metodo
            try {
                //Foto
                FileInputStream img = new FileInputStream(jfc.getSelectedFile());
                int largo = (int) jfc.getSelectedFile().length();
                producto.setImagen(img);
                producto.setLargo(largo);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(controladorProductos.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (producto.crearProductoByte()) {
                vistaProducto.getDlgProducto().setVisible(false);
                JOptionPane.showMessageDialog(vistaProducto, "Producto Creado Satisfactoriamente");
                cargarProducto();
            } else {
                JOptionPane.showMessageDialog(vistaProducto, "No se pudo crear el producto");
            }

        } else if ("editar".equals(vistaProducto.getDlgProducto().getName())) {
            //Editar
            String idProducto = (vistaProducto.getTxtID().getText());
            String nombre = vistaProducto.getTxtNombreP().getText();
            double precio = Double.parseDouble(vistaProducto.getTxtPrecio().getText());
            double costo = Double.parseDouble(vistaProducto.getTxtCosto().getText());
            int Stock = (int) vistaProducto.getSpinStock().getValue();
            String fechaIngreso = getFecha(vistaProducto.getDc_fecha());
            String descripcion = vistaProducto.getTxtDescripcion().getText();
            //Instancia del Modelo
            ModeloProducto producto = new ModeloProducto();
            //Set
            producto.setCodigoProducto(idProducto);
            producto.setNomProd(nombre);
            producto.setPrecio(precio);
            producto.setCosto(costo);
            producto.setStock(Stock);
            producto.setFechaingreso(Date.valueOf(fechaIngreso));
            producto.setDescripcion(descripcion);

            if (producto.ActualizarProductoByte()) {
                vistaProducto.getDlgProducto().setVisible(false);
                JOptionPane.showMessageDialog(vistaProducto, "Producto actualizado Satisfactoriamente");
                cargarProducto();
            } else {
                JOptionPane.showMessageDialog(vistaProducto, "No se pudo actualizado el producto");
            }
        }
    }

    public void cargarProducto() {
        vistaProducto.getTblProductos().setDefaultRenderer(Object.class, new ImagenTabla());
        vistaProducto.getTblProductos().setRowHeight(100);
        //Enlazar el modelo de la tabla con el controlador
        DefaultTableModel tblModeloProducto;
        tblModeloProducto = (DefaultTableModel) vistaProducto.getTblProductos().getModel();
        tblModeloProducto.setNumRows(0);//Limpia filas tabla
        String criterio = vistaProducto.getTxtBuscar().getText();
        List<Producto> listaProducto = modeloProducto.buscarproducto(criterio);//Enlazo al Modelo y obtengo los datos
        Holder<Integer> i = new Holder<>(0);
        listaProducto.stream().forEach(pr -> {
            tblModeloProducto.addRow(new Object[6]);//Creo una fila vacia/
            vistaProducto.getTblProductos().setValueAt(pr.getCodigoProducto(), i.value, 0);
            vistaProducto.getTblProductos().setValueAt(pr.getNomProd(), i.value, 1);
            vistaProducto.getTblProductos().setValueAt(pr.getDescripcion(), i.value, 2);
            vistaProducto.getTblProductos().setValueAt(pr.getCosto(), i.value, 3);
            vistaProducto.getTblProductos().setValueAt(pr.getPrecio(), i.value, 4);
            vistaProducto.getTblProductos().setValueAt(pr.getStock(), i.value, 5);
            vistaProducto.getTblProductos().setValueAt(pr.getFechaingreso(), i.value, 6);
            Image foto = pr.getFoto();
            if (foto != null) {

                Image nimg = foto.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                ImageIcon icono = new ImageIcon(nimg);
                DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
                renderer.setIcon(icono);
                vistaProducto.getTblProductos().setValueAt(new JLabel(icono), i.value, 7);

            } else {
                vistaProducto.getTblProductos().setValueAt(null, i.value, 7);
            }

            i.value++;

        });

    }
    SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");

    public String getFecha(JDateChooser jd) {
        if (jd.getDate() != null) {
            return formato.format(jd.getDate());
        } else {
            return null;
        }
    }

    public void modificar() {
        int seleccionado = vistaProducto.getTblProductos().getSelectedRow();
        if (seleccionado != -1) {
            String ver = vistaProducto.getTblProductos().getValueAt(seleccionado, 0).toString();
            List<Producto> tblProducto = modeloProducto.listProducto();
            for (int i = 0; i < tblProducto.size(); i++) {
                if (tblProducto.get(i).getCodigoProducto().equals(ver)) {
                    vistaProducto.getTxtID().setText(String.valueOf(tblProducto.get(i).getCodigoProducto()));
                    vistaProducto.getTxtNombreP().setText(tblProducto.get(i).getNomProd());
                    vistaProducto.getTxtPrecio().setText(String.valueOf(tblProducto.get(i).getPrecio()));
                    vistaProducto.getSpinStock().setValue(tblProducto.get(i).getStock());
                    vistaProducto.getTxtCosto().setText("" + tblProducto.get(i).getCosto());
                    vistaProducto.getTxtDescripcion().setText(tblProducto.get(i).getDescripcion());
                    vistaProducto.getDc_fecha().setDate((tblProducto.get(i).getFechaingreso()));

                    if (tblProducto.get(i).getFoto() == null) {
                        vistaProducto.getLabelFoto().setIcon(null);
                    } else {
                        Image in = tblProducto.get(i).getFoto();
                        Image img = in.getScaledInstance(133, 147, Image.SCALE_SMOOTH);
                        Icon icono = new ImageIcon(img);
                        vistaProducto.getLabelFoto().setIcon(icono);
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(vistaProducto, "No a seleccionado a ningun producto");
        }
    }

    public void limpiar() {
        vistaProducto.getTxtID().setText("");
        vistaProducto.getTxtNombreP().setText("");
        vistaProducto.getTxtPrecio().setText("");
        vistaProducto.getSpinStock().setValue(0);
        vistaProducto.getTxtDescripcion().setText("");

    }

    private void eliminarProducto() {
        ModeloProducto eliProducto = new ModeloProducto();
        int fila = vistaProducto.getTblProductos().getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(vistaProducto, "Por favor seleccione una fila");
        } else {
            String idproducto = vistaProducto.getTblProductos().getValueAt(fila, 0).toString();
            eliProducto.eliminarProducto(idproducto);
            cargarProducto();
            JOptionPane.showMessageDialog(vistaProducto, "Producto Eliminado");
        }
    }

    public void cancelar() {
        vistaProducto.setVisible(true);
        vistaProducto.getDlgProducto().setVisible(false);
    }

    private void examinaFoto() {
        jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int estado = jfc.showOpenDialog(vistaProducto);
        if (estado == JFileChooser.APPROVE_OPTION) {
            try {
                Image imagen = ImageIO.read(jfc.getSelectedFile()).getScaledInstance(
                        vistaProducto.getLabelFoto().getWidth(),
                        vistaProducto.getLabelFoto().getHeight(),
                        Image.SCALE_DEFAULT);

                Icon icono = new ImageIcon(imagen);
                vistaProducto.getLabelFoto().setIcon(icono);
                vistaProducto.getLabelFoto().updateUI();
            } catch (IOException ex) {
                Logger.getLogger(controladorProductos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private void imprimirReporte() {
        ConectionPg conexion = new ConectionPg();
        try {
            JasperReport jr = (JasperReport) JRLoader.loadObject(getClass().getResource("/Vista/Reportes/reportePrueba.jasper"));

            JasperPrint jp = JasperFillManager.fillReport(jr, null, conexion.getCon()); //cargado el reporte con los datos de la bd
            JasperViewer jv = new JasperViewer(jp);
            jv.setVisible(true);
        } catch (JRException ex) {
            Logger.getLogger(controladorProductos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
