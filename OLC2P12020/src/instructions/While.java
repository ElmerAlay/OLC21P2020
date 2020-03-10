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
public class While implements ASTNode{
    private ASTNode condition;
    private LinkedList<ASTNode> linst;

    public While(ASTNode condition, LinkedList<ASTNode> linst) {
        super();
        this.condition = condition;
        this.linst = linst;
    }
    
    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        //Compruebo que la condición sea de tipo boolean
        boolean flag = true;
        do{
            Object cond = condition.execute(environment, LError);
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
                TError error = new TError("While", "Semántico", "La condición del no es de tipo booleano", 0, 0);
                LError.add(error);
                return error;
            }

            if(flag){
                Object result = null;
                for(ASTNode in: linst){
                    result = in.execute(environment, LError);
                    if(((String)result).equals("break")){
                        return null;
                    }else if(((String)result).equals("continue")){
                        break;
                    }
                }
            }
        }while(flag);
        
        return null;
    }
}
