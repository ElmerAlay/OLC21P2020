package structs;

import java.io.FileWriter;
import java.io.PrintWriter;

/**
 *
 * @author junio
 */
public class Graficar {
    public static String Recorrido(AST root) throws Exception{
        String body = "";
        
        for(AST child : root.getChildren()){
            if(!child.getValue().equals("vacio")){
                body += "\"" + root.getIdNode() + "\"" + " [label=\"" + root.getLabel()+ "\"]\n";
                body += "\"" + child.getIdNode() + "\"" + " [label=\"" + child.getLabel() + "_" + child.getValue() + "\"]\n";
                body += "\"" + root.getIdNode() + "\" -> " + "\"" + child.getIdNode() + "\"\n";
                body += Recorrido(child);
            }else {
                body += "\"" + root.getIdNode() + "\"" + " [label=\"" + root.getLabel() + "\"]\n";
                body += "\"" + child.getIdNode() + "\"" + " [label=\"" + child.getLabel() + "\"]\n";
                body += "\"" + root.getIdNode() + "\" -> " + "\"" + child.getIdNode() + "\"\n";
                body += Recorrido(child);
            }
        }
        
        return body;
    }
    
    public static void graficar(String cadena, String cad){
        FileWriter fichero = null;
        PrintWriter pw = null;
        String nombre = cad;
        String archivo = nombre + ".dot";
        
        try{
            fichero = new FileWriter(archivo);
            pw = new PrintWriter(fichero);
            pw.println("digraph G { node[shape=oval, style=filled, collor=coral]; edge[color=chartreuse3]; rankdir=UD \n");
            pw.println(cadena);
            pw.println("}\n");
            fichero.close();
        }catch(Exception ex){
            System.out.println(ex);
        }
        
        try {
            String cmd =  "C:\\Program Files (x86)\\Graphviz2.38\\bin\\dot.exe -Tpng " + nombre + ".dot -o " + cad + ".png";              
            Runtime.getRuntime().exec(cmd);
        }catch(Exception ex1){
            System.out.println(ex1);
        }
    }
}
