package instructions;

import abstracto.ASTNode;
import abstracto.TError;
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
public class Return implements ASTNode{
    private ASTNode exp;
    
    public Return(ASTNode exp) {
        super();
        this.exp = exp;
    }

    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        return null;
    }
    
    public ASTNode getExp(){
        return this.exp;
    }
}
