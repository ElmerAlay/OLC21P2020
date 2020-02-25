package expressions;

import abstracto.ASTNode;

/**
 *
 * @author junio
 */
public class NOT implements ASTNode{
    private ASTNode op;

    public NOT(ASTNode op) {
        super();
        this.op = op;
    }

    @Override
    public Object execute() {
        Object op = this.op.execute();
        
        if(op instanceof Boolean){
            return !Boolean.parseBoolean(op.toString());
        }
         
        return "Error semántico, no se puede comparar lógicamente esos 2 tipos de datos\n";
    }
}
