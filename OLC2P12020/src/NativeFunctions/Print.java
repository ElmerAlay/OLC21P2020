package NativeFunctions;

import View.MainWindow;
import abstracto.*;
import java.util.LinkedList;
import symbols.Arr;
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
            output += "[," + (i+1) + "]\t";
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
    
    public String printArr(Arr arr, String output){
        LinkedList<Object> l = arr.getData();
        int tam = 0;
        for(int i=0; i<arr.dim.length;i++){
            tam += arr.dim[i];
        }
        
        int in=0;
        for(int k=0; k<tam;k++){
            output += ",";
            for(int i=0; i<arr.dim.length;i++){
                output += ",";
            }
            output += (k+1)+"\n\t\t";
            for(int i=0; i<arr.col;i++){
                output += "[,"+(i+1)+"]\t\t";
            }
            output += "\n";
            for(int i=0; i<arr.row;i++){
                output += "["+(i+1)+",]\t\t";
                for(int j=0; j<arr.col;j++){
                    if(l.get(in) instanceof Vec)
                        output += ((Vec)l.get(in)).getValues()[0]+"\t\t";
                    else
                        output += ((Vec)((ListStruct)l.get(in)).getValues().get(0)).getValues()[0]+"\t\t";
                    in++;
                }
                output += "\n";
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
        //verificamos si la expresión es un arreglo
        else if(op instanceof Arr){
            String output = "";
            MainWindow.output += printArr(((Arr)op), output);
            return output;
        }
        
        TError error = new TError("print", "Semántico", "La expresión dentro de la función print no es válida", 0, 0);
        LError.add(error);

        return error;
    }
}
