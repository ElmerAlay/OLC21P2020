package expressions;

import abstracto.*;
import java.util.LinkedList;
import symbols.Environment;

/**
 *
 * @author junio
 */
public class OR implements ASTNode{
    private ASTNode op1;
    private ASTNode op2;

    public OR(ASTNode op1, ASTNode op2) {
        super();
        this.op1 = op1;
        this.op2 = op2;
    }

    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        Object op1 = this.op1.execute(environment, LError);
        Object op2 = this.op2.execute(environment, LError);
        
        if(op1 instanceof Boolean && op2 instanceof Boolean){
            return Boolean.parseBoolean(op1.toString()) || Boolean.parseBoolean(op2.toString());
        }
         
        TError error = new TError("+", "Semántico", "no se puede comparar lógicamente esos 2 tipos de datos", 0, 0);
        LError.add(error);
        
        return error;
    }
}
