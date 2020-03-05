package NativeFunctions;

import abstracto.ASTNode;
import abstracto.TError;
import java.util.LinkedList;
import symbols.Environment;
import symbols.ListStruct;
import symbols.Vec;

/**
 *
 * @author junio
 */
public class ListFunc implements ASTNode{
    private LinkedList<ASTNode> lexp;

    public ListFunc(LinkedList<ASTNode> lexp) {
        super();
        this.lexp = lexp;
    }
    
    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        boolean flag = true; //Me servirá para saber que todas las expresiones son vectores o listas
        LinkedList<Object> values = new LinkedList<>();
        
        for (ASTNode exp : lexp) {
            Object op = exp.execute(environment, LError);
            if(op instanceof Vec || op instanceof ListStruct){
                values.add(op);
            }else {
                flag = true;
            }
        }
        
        if(flag){
            return new ListStruct(values);
        }
        
        TError error = new TError("List", "Semántico", "En esta función sólo pueden venir vectores u otras listas", 0, 0);
        LError.add(error);

        return error;
    }
}
