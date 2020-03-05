package expressions;

import abstracto.*;
import java.util.LinkedList;
import symbols.Environment;
import symbols.Vec;
import symbols.Vec2;

/**
 *
 * @author junio
 */
public class Constant2 implements ASTNode{
    private ASTNode value;
    
    public Constant2(ASTNode value){
        super();
        this.value = value;
    }

    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        Object val = value.execute(environment, LError);
        
        return new Vec2(((Vec)val).getValues());
    }
}