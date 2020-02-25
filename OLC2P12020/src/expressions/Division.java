package expressions;

import abstracto.ASTNode;

/**
 *
 * @author junio
 */
public class Division implements ASTNode{
    private ASTNode op1;
    private ASTNode op2;

    public Division(ASTNode op1, ASTNode op2) {
        super();
        this.op1 = op1;
        this.op2 = op2;
    }
    
    @Override
    public Object execute() {
        Object op1 = this.op1.execute();
        Object op2 = this.op2.execute();
        
        if(!op2.toString().equals("0"))
            if((op1 instanceof Float && (op2 instanceof Float || op2 instanceof Integer)) || 
               (op2 instanceof Float && (op1 instanceof Float || op1 instanceof Integer))){
                return Float.parseFloat(op1.toString()) / Float.parseFloat(op2.toString());
            }else if((op1 instanceof Integer && op2 instanceof Integer)){
                return Integer.parseInt(op1.toString()) / Integer.parseInt(op2.toString());
            }
        else {
            return "Error semántico, no se puede dividir dentro de 0";
        }
        
        
        return "Error semántico, no se pueden dividir esos 2 tipos de datos\n";
    }
    
}
