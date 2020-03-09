package instructions;

import abstracto.ASTNode;
import abstracto.TError;
import java.util.LinkedList;
import symbols.Environment;

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
        return this;
    }
    
    public ASTNode getExp(){
        return exp;
    }
}
