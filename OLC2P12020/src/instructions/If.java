package instructions;

import abstracto.ASTNode;
import abstracto.TError;
import java.util.LinkedList;
import symbols.Environment;
import symbols.Mat;
import symbols.Vec;

/**
 *
 * @author junio
 */
public class If implements ASTNode{
    private ASTNode condition;
    private LinkedList<ASTNode> lexpT;
    private LinkedList<ASTNode> lexpEI;
    private LinkedList<ASTNode> lexpE;

    public If(ASTNode condition, LinkedList<ASTNode> lexpT) {
        super();
        this.condition = condition;
        this.lexpT = lexpT;
    }

    public If(ASTNode condition, LinkedList<ASTNode> lexpT, LinkedList<ASTNode> lexpE) {
        super();
        this.condition = condition;
        this.lexpT = lexpT;
        this.lexpE = lexpE;
    }

    public If(ASTNode condition, LinkedList<ASTNode> lexpT, LinkedList<ASTNode> lexpEI, LinkedList<ASTNode> lexpE) {
        super();
        this.condition = condition;
        this.lexpT = lexpT;
        this.lexpEI = lexpEI;
        this.lexpE = lexpE;
    }
    
    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        //Compruebo que la condición sea de tipo boolean
        Object cond = condition.execute(environment, LError);
        boolean flag = true;
        if(cond instanceof Vec && ((Vec)cond).getValues()[0] instanceof Boolean){
            //Verificamos si todo el vector es true
            Object v[] = ((Vec)cond).getValues();
            for(int i=0; i<v.length; i++){
                if(Boolean.parseBoolean(v[i].toString())){
                }else{
                    flag = false;
                    break;
                }
            }
        }else if(cond instanceof Mat && ((Mat)cond).getValues()[0][0] instanceof Boolean){
            Object v[][] = ((Mat)cond).getValues();
            for(int i=0; i<((Mat)cond).col; i++){
                for(int j=0; j<((Mat)cond).row; j++){
                    if(Boolean.parseBoolean(v[j][i].toString())){
                    }else{
                        flag = false;
                        break;
                    }
                }
                if (!flag)
                    break;
            }
        }else {
            TError error = new TError("If", "Semántico", "La condición del no es de tipo booleano", 0, 0);
            LError.add(error);
            return error;
        }
        
        if(flag){
            for(ASTNode in: lexpT){
                in.execute(environment, LError);
            }
            return true;
        }else{
            boolean bandera = false;
            if(lexpEI != null){
                for(ASTNode in: lexpEI){
                    if((boolean)in.execute(environment, LError)){
                        bandera = true;
                        break;
                    }
                }
            }
            if(lexpE!=null && !bandera){
                for(ASTNode in: lexpE){
                    in.execute(environment, LError);
                }            
            }
        }
        
        return false;
    }
}
