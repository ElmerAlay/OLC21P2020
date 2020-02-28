package expressions;

import abstracto.*;
import java.util.LinkedList;
import symbols.Environment;
import symbols.Symbol;
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
            Object index1 = indexes.remove(0).execute(environment, LError);  //Guardo el primer índice de la lista para comparar su valor con el del tamaño de la variable
            
            Symbol symbol = environment.get(name);
            
            if(symbol.getValue()instanceof Vec){ //Verifico que la variable sea de tipo vector
                int tDimension = ((Vec)symbol.getValue()).getValues().length;  //Obtengo su tamaño para validar que el primer indice sea correcto
                
                if(index1 instanceof Integer){ //Valido que la primera posición sea integer
                    //Valido que el primer índice no sea mayor al tamaño de la variable
                    if((Integer.parseInt(index1.toString())) <= tDimension){
                        boolean flag = true;   //Me servirá para saber si todos los índices son correctos

                        //Ejecuto los demás índices
                        for(ASTNode exp : indexes){
                            Object indexn = exp.execute(environment, LError);
                            
                            //Verifico que sea de tipo integer y que sea igual a 1
                            if(indexn instanceof Integer && Integer.parseInt(indexn.toString())==1){
                            }else {
                                flag = false;
                                break;
                            }
                        }
                        
                        //Verifico el valor de la variable flag
                        if(flag){
                           return ((Vec)symbol.getValue()).getValues()[Integer.parseInt(index1.toString())-1];
                        }else{
                            TError error = new TError(name, "Semántico", "Todos los  subíndices deben ser de tipo integer e igual a 1", 0, 0);
                            LError.add(error);

                            return error;
                        }
                    }else{
                        TError error = new TError(name, "Semántico", "El índice sobrepasa el tamaño del vector", 0, 0);
                        LError.add(error);

                        return error; 
                    }
                }
            }
        }
        
        TError error = new TError(name, "Semántico", "La variable no existe", 0, 0);
        LError.add(error);

        return error;
    }
}
