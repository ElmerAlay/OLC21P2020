package expressions;

import abstracto.*;
import java.util.LinkedList;
import symbols.Environment;
import symbols.Vec;

/**
 *
 * @author junio
 */
public class NOT implements ASTNode{
    private ASTNode op;

    public NOT(ASTNode op) {
        super();
        this.op = op;
    }

    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        Object op = this.op.execute(environment, LError);
        
        if(op instanceof Boolean){
            return !Boolean.parseBoolean(op.toString());
        }
        
        
        //Primero verifico que el operador sea de tipo vector
        if(op instanceof Vec){
            Object vec1[] = ((Vec)op).getValues();
            boolean flag = true;
            Object result[] = new Object[vec1.length];
                
            //Recorrro los vectores y opero
            for(int i=0; i<vec1.length; i++){
                if(vec1[i] instanceof Boolean){
                    result[i] = !Boolean.parseBoolean(vec1[i].toString());
                }else {
                    flag = false;
                    break;
                }
            }
                
            //Verifico el valor de la variable flag
            if(flag){
                return new Vec(result);
            }else{
                TError error = new TError("!", "Semántico", "no se pueden comparar lógicamente esos 2 tipos de datos", 0, 0);
                LError.add(error);

                return error;
            }
        }
         
        TError error = new TError("!", "Semántico", "no se puede comparar lógicamente esos 2 tipos de datos", 0, 0);
        LError.add(error);
        
        return error;
    }
}
