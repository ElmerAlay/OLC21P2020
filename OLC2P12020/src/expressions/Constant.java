package expressions;

import abstracto.*;
import java.util.LinkedList;
import symbols.Arr;
import symbols.Environment;
import symbols.ListStruct;
import symbols.Mat;
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
        if(value instanceof Vec)
            return (Vec)value;
        else if(value instanceof ListStruct)
            return (ListStruct)value;
        else if(value instanceof Mat)
            return (Mat)value;
        else if(value instanceof Arr)
            return (Arr)value;
        
        Object val[] = { value };
        return new Vec(val);
    }
}
