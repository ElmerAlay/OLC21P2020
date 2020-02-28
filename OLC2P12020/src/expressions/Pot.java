package expressions;

import abstracto.*;
import java.util.LinkedList;
import symbols.Environment;

/**
 *
 * @author junio
 */
public class Pot implements ASTNode{
    private ASTNode op1;
    private ASTNode op2;

    public Pot(ASTNode op1, ASTNode op2) {
        super();
        this.op1 = op1;
        this.op2 = op2;
    }

    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        Object ope1 = this.op1.execute(environment, LError);
        Object ope2 = this.op2.execute(environment, LError);
        
        if((ope1 instanceof Float || ope1 instanceof Integer) && (ope2 instanceof Float || ope2 instanceof Integer)){
            Double result = Math.pow(Float.parseFloat(ope1.toString()), Float.parseFloat(ope2.toString()));
            return Float.parseFloat(result.toString());
        }
        
        TError error = new TError("^", "Semántico", "no se puede realizar potencia a esos 2 tipos de datos", 0, 0);
        LError.add(error);
        
        return error;
    }
}
