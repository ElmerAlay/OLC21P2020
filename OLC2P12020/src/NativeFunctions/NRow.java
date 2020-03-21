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
public class NRow implements ASTNode{
    private ASTNode exp;
    private int row;
    private int column;

    public NRow(ASTNode exp, int row, int column) {
        super();
        this.exp = exp;
        this.row = row;
        this.column = column;
    }
    
    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        Object op = exp.execute(environment, LError);
        Object result[] = new Object[1];
        
        if(op instanceof Mat){    //Verifico si la expresión es de tipo matriz
            result[0] = ((Mat)op).row;
            return new Vec(result);
        }
        
        TError error = new TError("NRow", "Semántico", "El argumento no es de tipo matriz", row, column);
        LError.add(error);

        return error;
    }
}
