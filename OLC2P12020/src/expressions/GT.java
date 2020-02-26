package expressions;

import abstracto.*;
import java.util.LinkedList;
import symbols.Environment;

/**
 *
 * @author junio
 */
public class GT implements ASTNode{
    private ASTNode op1;
    private ASTNode op2;

    public GT(ASTNode op1, ASTNode op2) {
        super();
        this.op1 = op1;
        this.op2 = op2;
    }

    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        Object op1 = this.op1.execute(environment, LError);
        Object op2 = this.op2.execute(environment, LError);
        
        if(op1 instanceof String && op2 instanceof String){
            int result = op1.toString().compareTo(op2.toString());
            
            if(result > 0)
                return true;
            else
                return false;
        }else if((op1 instanceof Float || op1 instanceof Integer) && (op2 instanceof Float || op2 instanceof Integer)){
            int result = Float.compare(Float.parseFloat(op1.toString()), Float.parseFloat(op2.toString()));
            
            if (result > 0)
                return true;
            else
                return false;
        }
         
        TError error = new TError("+", "Semántico", "no se puede comparar esos 2 tipos de datos", 0, 0);
        LError.add(error);
        
        return error;
    }
}