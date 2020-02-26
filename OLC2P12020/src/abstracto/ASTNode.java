package abstracto;

import java.util.LinkedList;
import symbols.Environment;

/**
 *
 * @author junio
 */
public interface ASTNode {
    public Object execute(Environment environment, LinkedList<TError> LError);
}
