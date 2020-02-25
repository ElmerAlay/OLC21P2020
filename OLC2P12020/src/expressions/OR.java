package expressions;

import abstracto.ASTNode;

/**
 *
 * @author junio
 */
public class OR implements ASTNode{
    private ASTNode op1;
    private ASTNode op2;

    public OR(ASTNode op1, ASTNode op2) {
        super();
        this.op1 = op1;
        this.op2 = op2;
    }

    @Override
    public Object execute() {
        Object op1 = this.op1.execute();
        Object op2 = this.op2.execute();
        
        if(op1 instanceof Boolean && op2 instanceof Boolean){
            return Boolean.parseBoolean(op1.toString()) || Boolean.parseBoolean(op2.toString());
        }
         
        return "Error semántico, no se puede comparar lógicamente esos 2 tipos de datos\n";
    }
}
