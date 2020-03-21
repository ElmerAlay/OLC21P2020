package NativeFunctions;

import abstracto.ASTNode;
import abstracto.TError;
import java.util.LinkedList;
import stadisticFunctions.SupportFunctions;
import symbols.Arr;
import symbols.Environment;
import symbols.ListStruct;
import symbols.Mat;
import symbols.Type;
import symbols.Vec;

/**
 *
 * @author junio
 */
public class TypeOf implements ASTNode{
    private ASTNode exp;
    private int row;
    private int column;

    public TypeOf(ASTNode exp, int row, int column) {
        super();
        this.exp = exp;
        this.row = row;
        this.column = column;
    }
    
    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        Object op = exp.execute(environment, LError);
        Object result[] = new Object[1];
        
        if(op instanceof Vec){  //Verifico si la expresión es de tipo vector
            result[0] = SupportFunctions.type(((Vec)op).getValues()[0]);
        }else if(op instanceof ListStruct){ //Verifico si la expresión es de tipo lista
            result[0] = "Lista";
        }else if(op instanceof Mat){    //Verifico si la expresión es de tipo mat
            result[0] = SupportFunctions.type(((Mat)op).getValues()[0][0]);
        }else if(op instanceof Arr){    //Verifico si la expresión es de tipo arreglo
            LinkedList<Object> data = ((Arr)op).getData();
            if(data.get(0) instanceof Vec){
                result[0] = SupportFunctions.type(((Vec)data.get(0)).getValues()[0]);
            }else if(data.get(0) instanceof ListStruct){
                result[0] = "Lista";
            }else{
                TError error = new TError("TypeOf", "Semántico", "La expresión no es de tipo lista o vector", row, column);
                LError.add(error);

                return error;
            }
        }else{
            TError error = new TError("TypeOf", "Semántico", "La expresión no es de ningún tipo de estructura", row, column);
            LError.add(error);

            return error;
        }
        
        return new Vec(result);
    }
}
