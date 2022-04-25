/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalpv.Controller;

import finalpv.Model.ModelProducto.ModeloProducto;
import finalpv.Vista.VistaMenu;
import finalpv.Vista.vistaProductos;
import javax.swing.JLabel;

/**
 *
 * @author Usuario
 */
public class controllerMenuPrincipal {

    VistaMenu vistaMenu;

    public controllerMenuPrincipal(VistaMenu vistaMenu) {
        this.vistaMenu = vistaMenu;
        vistaMenu.setVisible(true);
        this.vistaMenu.setLocationRelativeTo(null);
    }

    public void controlMenu() {

        vistaMenu.getMenuI_crear2().addActionListener(l -> crudProductos());
        vistaMenu.getBtnProductos().addActionListener(l -> crudProductos());
    }

    public void crudProductos() {
        //Instanciar clases del Modelo y la Vista
        ModeloProducto modeloProducto = new ModeloProducto();
        vistaProductos vistaProducto = new vistaProductos();
        //Agregar vista Productos al DeskopPane
        vistaMenu.getDpPrincipal().add(vistaProducto);
        controladorProductos controladorProducto = new controladorProductos(modeloProducto, vistaProducto);
        controladorProducto.controlProducto();

    }

}
