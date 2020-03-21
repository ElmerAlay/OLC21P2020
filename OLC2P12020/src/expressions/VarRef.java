package expressions;

import abstracto.*;
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
public class VarRef implements ASTNode{
    private String name;
    private int row;
    private int column;

    public VarRef(String name, int row, int column) {
        super();
        this.name = name;
        this.row = row;
        this.column = column;
    }

    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        //Verifico que la variable exista en la tabla de símbolos
        if(environment.get(name) != null){
            //verifico que la variable sea de tipo vector
            if(environment.get(name).getValue() instanceof Vec){
                //retorno el vector 
                return (Vec)environment.get(name).getValue();   
            }else if(environment.get(name).getValue() instanceof ListStruct){  //Verifico que la variable sea de tipo lista
                return (ListStruct)environment.get(name).getValue();
            }else if(environment.get(name).getValue() instanceof Mat){  //Verifico que la variable sea de tipo matriz
                return (Mat)environment.get(name).getValue();
            }else if(environment.get(name).getValue() instanceof Arr){  //Verifico que la variable sea de tipo arreglo
                return (Arr)environment.get(name).getValue();
            }else{
                TError error = new TError(name, "Semántico", "Error de contenido de la variable", row, column);
                LError.add(error);

                return error;
            }
        }
        
        TError error = new TError(name, "Semántico", "La variable no existe", row, column);
        LError.add(error);

        return error;
    }
    
}
