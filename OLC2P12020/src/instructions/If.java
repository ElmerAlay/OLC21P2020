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
public class If implements ASTNode{
    private ASTNode condition;
    private LinkedList<ASTNode> lexpT;
    private LinkedList<ASTNode> lexpEI;
    private LinkedList<ASTNode> lexpE;
    private int row;
    private int column;

    public If(ASTNode condition, LinkedList<ASTNode> lexpT, int row, int column) {
        super();
        this.condition = condition;
        this.lexpT = lexpT;
        this.row = row;
        this.column = column;
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
            TError error = new TError("If", "Semántico", "La condición del no es de tipo booleano", row, column);
            LError.add(error);
            return error;
        }
        
        if(flag){
            Environment local = new Environment(environment, "local_if");
            
            for(ASTNode in: lexpT){
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
                }
                else if(in instanceof ReturnEmpty)
                    return null;
                in.execute(local, LError);
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
                Environment local = new Environment(environment, "local_else");
                for(ASTNode in: lexpE){
                    if(in instanceof Break)
                        return null;
                    else if(in instanceof Continue)
                        continue;
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
                    }
                    else if(in instanceof ReturnEmpty)
                        return null;
                    in.execute(local, LError);
                }            
            }
        }
        
        return false;
    }
}
