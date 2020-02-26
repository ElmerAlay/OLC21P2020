package expressions;

import abstracto.*;
import java.util.LinkedList;
import symbols.Environment;

/**
 *
 * @author junio
 */
public class Addition implements ASTNode{
     
    private ASTNode op1;
    private ASTNode op2;
    
    public Addition(ASTNode op1, ASTNode op2){
        super();
        this.op1 = op1;
        this.op2 = op2;
    }
    
    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        Object op1 = this.op1.execute(environment, LError);
        Object op2 = this.op2.execute(environment, LError);
        
        if(op1 instanceof String || op2 instanceof String){
            return "" + op1 + op2;
        }else if((op1 instanceof Float && (op2 instanceof Float || op2 instanceof Integer)) || 
                 (op2 instanceof Float && (op1 instanceof Float || op1 instanceof Integer))){
            return Float.parseFloat(op1.toString()) + Float.parseFloat(op2.toString());
        }else if((op1 instanceof Integer && op2 instanceof Integer)){
            return Integer.parseInt(op1.toString()) + Integer.parseInt(op2.toString());
        }
     
        TError error = new TError("+", "Semántico", "no se puede sumar esos 2 tipos de datos", 0, 0);
        LError.add(error);
        
        return error;
    }
    
}
