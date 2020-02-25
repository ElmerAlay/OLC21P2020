package expressions;

import abstracto.ASTNode;

/**
 *
 * @author junio
 */
public class GT implements ASTNode{
    private ASTNode op1;
    private ASTNode op2;

    public GT(ASTNode op1, ASTNode op2) {
        super();
        this.op1 = op1;
        this.op2 = op2;
    }

    @Override
    public Object execute() {
        Object op1 = this.op1.execute();
        Object op2 = this.op2.execute();
        
        if(op1 instanceof String && op2 instanceof String){
            int result = op1.toString().compareTo(op2.toString());
            
            if(result > 0)
                return true;
            else
                return false;
        }else if((op1 instanceof Float || op1 instanceof Integer) && (op2 instanceof Float || op2 instanceof Integer)){
            int result = Float.compare(Float.parseFloat(op1.toString()), Float.parseFloat(op2.toString()));
            
            if (result > 0)
                return true;
            else
                return false;
        }
         
        return "Error semántico, no se pueden comparar esos 2 tipos de datos\n";
    }
}