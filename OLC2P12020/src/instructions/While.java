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
public class While implements ASTNode{
    private ASTNode condition;
    private LinkedList<ASTNode> linst;
    private int row;
    private int column;

    public While(ASTNode condition, LinkedList<ASTNode> linst, int row, int column) {
        super();
        this.condition = condition;
        this.linst = linst;
        this.row = row;
        this.column = column;
    }
    
    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        //Compruebo que la condición sea de tipo boolean
        boolean flag = false;
        Environment local = new Environment(environment, "local_while");
        do{
            Object cond = condition.execute(environment, LError);
            if(cond instanceof Vec && ((Vec)cond).getValues()[0] instanceof Boolean){
                //Verificamos si todo el vector es true
                Object v[] = ((Vec)cond).getValues();
                for(int i=0; i<v.length; i++){
                    if(v[i] instanceof Boolean && Boolean.parseBoolean(v[i].toString())){
                        flag = true;
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
                TError error = new TError("While", "Semántico", "La condición no es de tipo booleano", row, column);
                LError.add(error);
                return error;
            }

            if(flag){
                for(ASTNode in: linst){
                    if(in instanceof Break)
                        return null;
                    else if(in instanceof Continue)
                        break;
                    else if(in instanceof Return){
                        Object op = ((Return)in).getExp().execute(local, LError);
                        if(op instanceof Vec)
                            return (Vec)op;
                        else if(op instanceof ListStruct)
                            return (ListStruct)op;
                        else if(op instanceof Mat)
                            return (Mat)op;
                        else if(op instanceof Arr)
                            return (Arr)op;

                        TError error = new TError("return", "Semántico", "La expresión no es correcta", row, column);
                        LError.add(error);

                        return error;
                    }else if(in instanceof ReturnEmpty)
                        return null;
                    in.execute(local, LError);
                }
            }
        }while(flag);
        
        return null;
    }
}
