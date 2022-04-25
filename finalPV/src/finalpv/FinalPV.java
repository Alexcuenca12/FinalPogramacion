/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalpv;

import finalpv.Controller.controllerMenuPrincipal;
import finalpv.Vista.VistaMenu;

/**
 *
 * @author Usuario
 */
public class FinalPV {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        VistaMenu vista = new VistaMenu();
        controllerMenuPrincipal controller = new controllerMenuPrincipal(vista);
        controller.controlMenu();
    }

}
