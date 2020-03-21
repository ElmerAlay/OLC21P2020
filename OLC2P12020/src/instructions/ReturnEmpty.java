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
public class ReturnEmpty implements ASTNode{

    public ReturnEmpty() {
        super();
    }

    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        return "return";
    }
}
