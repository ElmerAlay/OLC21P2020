package structs;

import symbols.Environment;
import abstracto.ASTNode;
import java.util.ArrayList;
import javafx.scene.Group;
import javafx.scene.control.TextArea;

/**
 *
 * @author junio
 */
public class Arbol {
    private ArrayList<ASTNode> instrucciones;
    private TextArea consola;
    private Environment global;
    private Group grupo;
    
    public Arbol(ArrayList<ASTNode> instrucciones) {
        this.instrucciones = instrucciones;
    }

    public ArrayList<ASTNode> getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(ArrayList<ASTNode> instrucciones) {
        this.instrucciones = instrucciones;
    }

    public TextArea getConsola() {
        return consola;
    }

    public void setConsola(TextArea consola) {
        this.consola = consola;
    }

    public Environment getGlobal() {
        return global;
    }

    public void setGlobal(Environment global) {
        this.global = global;
    }

    public Group getGrupo() {
        return grupo;
    }

    public void setGrupo(Group grupo) {
        this.grupo = grupo;
    }
}
