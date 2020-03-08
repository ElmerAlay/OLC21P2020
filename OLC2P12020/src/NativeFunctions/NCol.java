package NativeFunctions;

import abstracto.ASTNode;
import abstracto.TError;
import java.util.LinkedList;
import symbols.Environment;
import symbols.Mat;
import symbols.Vec;

/**
 *
 * @author junio
 */
public class NCol implements ASTNode{
    private ASTNode exp;

    public NCol(ASTNode exp) {
        super();
        this.exp = exp;
    }
    
    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        Object op = exp.execute(environment, LError);
        Object result[] = new Object[1];
        
        if(op instanceof Mat){    //Verifico si la expresión es de tipo matriz
            result[0] = ((Mat)op).col;
            return new Vec(result);
        }
        
        TError error = new TError("NCOL", "Semántico", "El argumento no es de tipo matriz", 0, 0);
        LError.add(error);

        return error;
    }
}
