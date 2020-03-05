package expressions;

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
public class StructRef implements ASTNode{
    private String name;
    private LinkedList<ASTNode> indexes;

    public StructRef(String name, LinkedList<ASTNode> indexes) {
        super();
        this.name = name;
        this.indexes = indexes;
    }
    
    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        if(environment.get(name) != null){  //Primero verifico que la variable existe 
            //Verifico si el símbolo es un vector
            if(environment.get(name).getValue() instanceof Vec){
                Object o = new VecRef(name, ((Vec)environment.get(name).getValue()).getValues(), indexes).execute(environment, LError);
                if(o instanceof Vec){
                    return (Vec)o;
                }else{
                    TError error = new TError(name, "Semántico", "Error de índices en el vector", 0, 0);
                    LError.add(error);
                    return error;
                }
            }
            //Verifico si el símbolo es una lista
            else if(environment.get(name).getValue() instanceof ListStruct){
                Object o = new ListRef(name, ((ListStruct)environment.get(name).getValue()).getValues(), indexes).execute(environment, LError);
                if(o instanceof ListStruct){
                    return (ListStruct)o;
                }else if (o instanceof Vec){
                    return (Vec)o;
                }
                else{
                    TError error = new TError(name, "Semántico", "Error de índices en la lista", 0, 0);
                    LError.add(error);
                    return error;
                }
            }
        }
        
        TError error = new TError(name, "Semántico", "La variable no existe", 0, 0);
        LError.add(error);
        return error;
    }
}
