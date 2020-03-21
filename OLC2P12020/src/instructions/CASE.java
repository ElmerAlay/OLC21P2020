package instructions;

import abstracto.ASTNode;
import abstracto.TError;
import java.util.LinkedList;
import symbols.Environment;
import symbols.Vec;

/**
 *
 * @author junio
 */
public class CASE implements ASTNode{
    private ASTNode exp;
    private ASTNode exp1;
    private LinkedList<ASTNode> linst;
    private int row;
    private int column;

    public CASE(ASTNode exp, ASTNode exp1, LinkedList<ASTNode> linst, int row, int column) {
        super();
        this.exp = exp;
        this.exp1 = exp1;
        this.linst = linst;
        this.row = row;
        this.column = column;
    }

    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        //Compruebo que la condición sea de tipo string o integer y que tenga un sólo valor
        Object op = exp.execute(environment, LError);
        if(op instanceof Vec && 
                (((Vec)op).getValues()[0] instanceof Integer || ((Vec)op).getValues()[0] instanceof String) && 
                    ((Vec)op).getValues().length==1){
            boolean flag = false;
            Object op2 = exp1.execute(environment, LError);
            if(((Vec)op).getValues()[0] instanceof Integer && op2 instanceof Vec && 
                        ((Vec)op2).getValues().length==1 && ((Vec)op2).getValues()[0] instanceof Integer){
                flag = Integer.parseInt((((Vec)op).getValues()[0]).toString())==Integer.parseInt((((Vec)op2).getValues()[0]).toString());
            }
            else if(((Vec)op).getValues()[0] instanceof String && op2 instanceof Vec  && 
                    ((Vec)op2).getValues().length==1 && ((Vec)op2).getValues()[0] instanceof String){
                flag = (((Vec)op).getValues()[0]).toString().compareTo((((Vec)op2).getValues()[0]).toString())==0;
            }else {
                TError error = new TError("CASE", "Semántico", "La expresión del case no es del mismo tipo de la del switch", row, column);
                LError.add(error);
                return error;
            }
            
            boolean bandera = false;
            if(flag){
                for(ASTNode in: linst){
                    if(in instanceof Break){
                        bandera = true;
                        return bandera;
                    }
                    in.execute(environment, LError);
                }
                return bandera;
            }else{
                return false;
            }
        }
            
        TError error = new TError("SWITCH", "Semántico", "La expresión debe ser integer o string", row, column);
        LError.add(error);
        return error;
    }
}
