package expressions;

import abstracto.*;
import java.util.LinkedList;
import symbols.Environment;

/**
 *
 * @author junio
 */
public class VarRef implements ASTNode{
    private String name;

    public VarRef(String name) {
        super();
        this.name = name;
    }

    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        if(environment.get(name) == null){
            TError error = new TError(name, "Semántico", "La variable no existe", 0, 0);
            LError.add(error);

            return error;
        }else
            return environment.get(name).getValue();
        
    }
    
}
