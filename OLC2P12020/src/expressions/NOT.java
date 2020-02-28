package expressions;

import abstracto.*;
import java.util.LinkedList;
import symbols.Environment;

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
         
        TError error = new TError("!", "Semántico", "no se puede comparar lógicamente esos 2 tipos de datos", 0, 0);
        LError.add(error);
        
        return error;
    }
}
