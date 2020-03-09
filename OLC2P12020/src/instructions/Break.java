package instructions;

import abstracto.ASTNode;
import abstracto.TError;
import java.util.LinkedList;
import symbols.Environment;

/**
 *
 * @author junio
 */
public class Break implements ASTNode{

    public Break() {
        super();
    }

    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        return "break";
    }
}
