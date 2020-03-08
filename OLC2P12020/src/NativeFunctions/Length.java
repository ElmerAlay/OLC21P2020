package NativeFunctions;

import abstracto.ASTNode;
import abstracto.TError;
import java.util.LinkedList;
import stadisticFunctions.SupportFunctions;
import symbols.Arr;
import symbols.Environment;
import symbols.ListStruct;
import symbols.Mat;
import symbols.Vec;

/**
 *
 * @author junio
 */
public class Length implements ASTNode{
    private ASTNode exp;

    public Length(ASTNode exp) {
        super();
        this.exp = exp;
    }
    
    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        Object op = exp.execute(environment, LError);
        Object result[] = new Object[1];
        
        if(op instanceof Vec){  //Verifico si la expresión es de tipo vector
            result[0] = ((Vec)op).getValues().length;
        }else if(op instanceof ListStruct){ //Verifico si la expresión es de tipo lista
            result[0] = ((ListStruct)op).getValues().size();
        }else if(op instanceof Mat){    //Verifico si la expresión es de tipo matriz
            result[0] = ((Mat)op).row * ((Mat)op).col;
        }else if(op instanceof Arr){ //Verifico si la expresión es de tipo arreglo
            int data[] = ((Arr)op).dim;
            int tam = ((Arr)op).row*((Arr)op).col;
            for(int i=0;i<data.length;i++){
                tam*= data[i];
            }
            result[0] = tam;
        }else{
            TError error = new TError("Length", "Semántico", "La expresión no es de ningún tipo de estructura", 0, 0);
            LError.add(error);

            return error;
        }
        
        return new Vec(result);
    }
}
