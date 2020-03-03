package NativeFunctions;

import View.MainWindow;
import abstracto.*;
import java.util.LinkedList;
import symbols.Environment;
import symbols.Vec;

/**
 *
 * @author junio
 */
public class Print implements ASTNode{
    private ASTNode exp;
    private Environment environment;
    private LinkedList<TError> LError;

    public Print(ASTNode exp) {
        this.exp = exp;
    }
    
    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        Object op = exp.execute(environment, LError);
        
        //verificamos si la expresión es un vector
        if(op instanceof Vec){
            Object values[] = ((Vec)op).getValues();
            String output = "";
            for(int i=0; i<values.length; i++){
                output += values[i].toString() + " ";
            }
            output += "\n";
            
            MainWindow.output += output;
            return output;
        }
        
        TError error = new TError("print", "Semántico", "La expresión dentro de la función print no es válida", 0, 0);
        LError.add(error);

        return error;
    }
}
