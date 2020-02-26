package expressions;

import abstracto.*;
import java.util.LinkedList;
import symbols.Environment;

/**
 *
 * @author junio
 */
public class NegU implements ASTNode{
    private ASTNode opu;

    public NegU(ASTNode opu) {
        super();
        this.opu = opu;
    }
    
    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        Object opu = this.opu.execute(environment, LError);
        
        if(opu instanceof Float){
            return Float.parseFloat(opu.toString()) * -1;
        }else if(opu instanceof Integer){
            return Integer.parseInt(opu.toString()) * -1;
        }
        
        TError error = new TError("+", "Semántico", "no se puede aplicar negación unaria ese tipo de dato", 0, 0);
        LError.add(error);
        
        return error;
    }
    
}
