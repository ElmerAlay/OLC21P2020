package structs;

import java.util.ArrayList;

/**
 *
 * @author Elmer
 */
public class AST {
    private String label; //Es lo que se visualiza en el nodo a la hora de graficarlo
    private ArrayList<AST> children = new ArrayList<>(); //Arreglo de hijos que tendran los nodos
    private String value = "vacio";    //Representa el valor del nodo
    private int idNode;      //Este id servirá para que tengamos problema al graficar
    private int row;
    private int column;
    
    /**
     * Permite agregar un nodo como hijo al AST
     * @param child representa el nodo hijo
     */
    public void addChildren(AST child){
        children.add(child);
    }

    /**
     * 
     * @return los hijos que pertenecen al nodo 
     */
    public ArrayList<AST> getChildren(){
        return children;
    }
    
    /**
     * 
     * @return la etiqueta del nodo en tipo String
     */
    public String getLabel() {
        return label;
    }

    /**
     * Modifica el texto de la etiqueta del nodo
     * @param label representa el nuevo texto
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     *
     * @return El valor del nodo
     */
    public String getValue() {
        return value;
    }

    /**
     * Nos permite modificar el valor del nodo
     * @param value El nuevo valor que tomará el nodo
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * 
     * @return el id del nodo 
     */
    public int getIdNode() {
        return idNode;
    }

    /**
     * Modifica el id del nodo
     * @param idNode Número que indica el id del nodo
     */
    public void setIdNode(int idNode) {
        this.idNode = idNode;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }
}
