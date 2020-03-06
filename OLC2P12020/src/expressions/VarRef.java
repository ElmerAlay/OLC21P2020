package expressions;

import abstracto.*;
import java.util.LinkedList;
import symbols.Environment;
import symbols.ListStruct;
import symbols.Mat;
import symbols.Vec;

/**
 *
 * @author junio
 */
public class VarRef implements ASTNode{
    private String name;

    public VarRef(String name) {
        super();
        this.name = name;
    }

    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        //Verifico que la variable exista en la tabla de símbolos
        if(environment.get(name) != null){
            //verifico que la variable sea de tipo vector
            if(environment.get(name).getValue() instanceof Vec){
                //retorno el vector 
                return new Vec(((Vec)environment.get(name).getValue()).getValues());   
            }else if(environment.get(name).getValue() instanceof ListStruct){  //Verifico que la variable sea de tipo lista
                return new ListStruct(((ListStruct)environment.get(name).getValue()).getValues());
            }else if(environment.get(name).getValue() instanceof Mat){  //Verifico que la variable sea de tipo matriz
                return (Mat)environment.get(name).getValue();
            }
        }
        
        TError error = new TError(name, "Semántico", "La variable no existe", 0, 0);
        LError.add(error);

        return error;
    }
    
}
