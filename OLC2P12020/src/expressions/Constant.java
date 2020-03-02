package expressions;

import abstracto.*;
import java.util.LinkedList;
import symbols.Environment;
import symbols.Vec;

/**
 *
 * @author junio
 */
public class Constant implements ASTNode{
    private Object value;
    
    public Constant(Object value){
        super();
        this.value = value;
    }

    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        Object val[] = { value };
        return new Vec(val);
    }
}
