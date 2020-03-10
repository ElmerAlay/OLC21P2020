package instructions;

import abstracto.ASTNode;
import abstracto.TError;
import static com.oracle.nio.BufferSecrets.instance;
import expressions.Constant;
import java.util.LinkedList;
import symbols.Arr;
import symbols.Environment;
import symbols.ListStruct;
import symbols.Mat;
import symbols.Symbol;
import symbols.Type;
import symbols.Vec;

/**
 *
 * @author junio
 */
public class For implements ASTNode{
    private String id;
    private ASTNode exp;
    private LinkedList<ASTNode> linst;

    public For(String id, ASTNode exp, LinkedList<ASTNode> linst) {
        super();
        this.id = id;
        this.exp = exp;
        this.linst = linst;
    }
    
    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        //Verificar de qué tipo es la expresión
        Object op = exp.execute(environment, LError);
        Object result = null;
        if(op instanceof Vec || op instanceof Mat || op instanceof ListStruct || op instanceof Arr){
            if(op instanceof Vec){
                for(int i=0;i<((Vec)op).getValues().length; i++){
                    Constant c = new Constant(((Vec)op).getValues()[i]);
                    new VarAssig(id, c).execute(environment, LError);
                    for(ASTNode ins: linst){
                        result = ins.execute(environment, LError);
                        if(((String)result).equals("break")){
                            return null;
                        }else if(((String)result).equals("continue")){
                            break;
                        }
                    }
                }
            }else if(op instanceof ListStruct){
                for(int i=0;i<((ListStruct)op).getValues().size(); i++){
                    Constant c = new Constant(((ListStruct)op).getValues().get(i));
                    new VarAssig(id, c).execute(environment, LError);
                    for(ASTNode ins: linst){
                        result = ins.execute(environment, LError);
                        if(((String)result).equals("break")){
                            return null;
                        }else if(((String)result).equals("continue")){
                            break;
                        }
                    }
                }
            }else if(op instanceof Mat){
                for(int i=0;i<((Mat)op).row; i++){
                    for(int j=0;j<((Mat)op).col; j++){
                        Constant c = new Constant(((Mat)op).getValues()[i][j]);
                        new VarAssig(id, c).execute(environment, LError);
                        for(ASTNode ins: linst){
                            result = ins.execute(environment, LError);
                            if(((String)result).equals("break")){
                                return null;
                            }else if(((String)result).equals("continue")){
                                break;
                            }
                        }
                    }
                }
            }else if(op instanceof Arr){
                for(int i=0;i<((Arr)op).getData().size(); i++){
                    if(((Arr)op).getData().get(i) instanceof Vec){
                        Constant c = new Constant(((Vec)((Arr)op).getData().get(i)).getValues()[0]);
                        new VarAssig(id, c).execute(environment, LError);
                    }else if(((Arr)op).getData().get(i) instanceof ListStruct){
                        Constant c = new Constant(((ListStruct)((Arr)op).getData().get(i)).getValues().get(0));
                        new VarAssig(id, c).execute(environment, LError);
                    }
                    for(ASTNode ins: linst){
                        result = ins.execute(environment, LError);
                        if(((String)result).equals("break")){
                            return null;
                        }else if(((String)result).equals("continue")){
                            break;
                        }
                    }
                }
            }
            
            return null;
        }else{
            TError error = new TError("For", "Semántico", "La expresión debe ser de tipo de alguna estructura", 0, 0);
            LError.add(error);
            return error;
        }
    }
}
