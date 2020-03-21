package expressions;

/**
 *
 * @author junio
 */

import abstracto.ASTNode;
import abstracto.TError;
import java.util.LinkedList;
import symbols.Environment;

public class Default implements ASTNode{

    public Default() {
        super();
    }
    
    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        return null;
    }
}
