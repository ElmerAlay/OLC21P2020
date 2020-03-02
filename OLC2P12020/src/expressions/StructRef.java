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
            
            if(symbol.getValue() instanceof Vec){ //Verifico que la variable sea de tipo vector
                int tDimension = ((Vec)symbol.getValue()).getValues().length;  //Obtengo su tamaño para validar que el primer indice sea correcto
                
                //Verifico si el index1 es de tipo vector
                if(index1 instanceof Vec){
                    Object ind1[] = ((Vec)index1).getValues();
                    //Verifico que sea de tamaño 1
                    if(ind1.length==1){
                        if(ind1[0] instanceof Integer){ //Valido que la primera posición sea integer
                            //Valido que el primer índice no sea mayor al tamaño de la variable
                            if((Integer.parseInt(ind1[0].toString())) <= tDimension){
                                boolean flag = true;   //Me servirá para saber si todos los índices son correctos

                                //Ejecuto los demás índices
                                for(ASTNode exp : indexes){
                                    Object indexn = exp.execute(environment, LError);

                                    //Verifico que sea vector
                                    if(indexn instanceof Vec){
                                        Object indn[] = ((Vec)indexn).getValues();
                                        //Que sea de tamaño 1
                                        if(indn.length==1){
                                            //Verifico que sea de tipo integer y que sea igual a 1
                                            if(indn[0] instanceof Integer && Integer.parseInt(indn[0].toString())==1){
                                            }else {
                                                flag = false;
                                                break;
                                            }
                                        }else{
                                            TError error = new TError(name, "Semántico", "El índice es de tipo vector pero tiene más de un elemento", 0, 0);
                                            LError.add(error);

                                            return error;
                                        }
                                    }
                                }

                                //Verifico el valor de la variable flag
                                if(flag){
                                    Object result[] = {((Vec)symbol.getValue()).getValues()[Integer.parseInt(ind1[0].toString())-1]};
                                    return new Vec(result);
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
                        }else{
                            TError error = new TError(name, "Semántico", "El 1er índice no es de tipo integer", 0, 0);
                            LError.add(error);

                            return error;
                        }
                    }else{
                        TError error = new TError(name, "Semántico", "El 1er índice es de tipo vector pero tiene más de un elemento", 0, 0);
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
