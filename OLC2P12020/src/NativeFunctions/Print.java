package NativeFunctions;

import View.MainWindow;
import abstracto.*;
import java.util.LinkedList;
import symbols.Environment;
import symbols.ListStruct;
import symbols.Mat;
import symbols.Vec;

/**
 *
 * @author junio
 */
public class Print implements ASTNode{
    private ASTNode exp;

    public Print(ASTNode exp) {
        super();
        this.exp = exp;
    }
    
    public String printVec(Object vec[], String output){
        for(int i=0; i<vec.length; i++){
                output += vec[i].toString() + " ";
        }
        output += "\n";
        return output;
    }
    
    public String printList(LinkedList<Object> list, String output){
        for(int i=0; i<list.size(); i++){
            output += "[[" + (i+1) + "]]\n";
            if(list.get(i) instanceof Vec){
                Object vec[] = ((Vec)list.get(i)).getValues();
                output = printVec(vec, output);
            }else{
                LinkedList<Object> l = ((ListStruct)list.get(i)).getValues();
                output = printList(l, output);
            }
        }
        output += "\n";
        return output;
    }
    
    public String printMatriz(Mat matriz, String output){
        Object ma[][] = matriz.getValues();
        output += "\t";
        for(int i=0; i<matriz.col;i++){
            output += "[" + (i+1) + ",]\t";
        }
        output += "\n";
        for(int i=0; i<matriz.row;i++){
            output += "[" + (i+1) + ",]\t";
            for(int j=0; j<matriz.col;j++){
                output += ma[i][j].toString() + "\t";
            }
            output += "\n";
        }
        
        return output;
    }
    
    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        Object op = exp.execute(environment, LError);
        
        //verificamos si la expresión es un vector
        if(op instanceof Vec){
            String output = "";
            MainWindow.output += printVec(((Vec)op).getValues(), output);
            return output;
        }
        //verificamos si la expresión es una lista
        else if(op instanceof ListStruct){
            String output = "";
            MainWindow.output += printList(((ListStruct)op).getValues(), output);
            return output;
        }
        //verificamos si la expresión es una matriz
        else if(op instanceof Mat){
            String output = "";
            MainWindow.output += printMatriz(((Mat)op), output);
            return output;
        }
        
        TError error = new TError("print", "Semántico", "La expresión dentro de la función print no es válida", 0, 0);
        LError.add(error);

        return error;
    }
}
