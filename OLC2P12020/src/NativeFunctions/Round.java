package NativeFunctions;

import abstracto.*;
import java.util.LinkedList;
import symbols.Environment;
import symbols.Vec;

/**
 *
 * @author junio
 */
public class Round implements ASTNode{
    private ASTNode exp;

    public Round(ASTNode exp) {
        this.exp = exp;
    }
    
    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        Object op = exp.execute(environment, LError);
        //verificamos si la expresión es un vector
        if(op instanceof Vec){
            Object values[] = ((Vec)op).getValues();
            //Verificamos que sea de tamaño 1
            if(values.length==1){
                //Verificar que sea de tipo float
                if(values[0] instanceof Float){
                    Object tam[] = { Math.round(Float.parseFloat(values[0].toString())) };
                    return new Vec(tam);
                }else{
                    TError error = new TError("Round", "Semántico", "El vector dentro de la función no es de tipo numérico", 0, 0);
                    LError.add(error);

                    return error;
                }
            }else{
                TError error = new TError("Round", "Semántico", "El vector dentro de la función no es de tamaño 1", 0, 0);
                LError.add(error);

                return error;
            }
        }
        
        TError error = new TError("Round", "Semántico", "La expresión dentro de la función no es válida", 0, 0);
        LError.add(error);

        return error;
    }
    
}
