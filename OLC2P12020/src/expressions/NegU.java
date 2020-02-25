package expressions;

import abstracto.ASTNode;

/**
 *
 * @author junio
 */
public class NegU implements ASTNode{
    private ASTNode opu;

    public NegU(ASTNode opu) {
        super();
        this.opu = opu;
    }
    
    @Override
    public Object execute() {
        Object opu = this.opu.execute();
        
        if(opu instanceof Float){
            return Float.parseFloat(opu.toString()) * -1;
        }else if(opu instanceof Integer){
            return Integer.parseInt(opu.toString()) * -1;
        }
        
        return "Error semántico, no se puede aplicar negación unaria ese tipo de dato\n";
    }
    
}
