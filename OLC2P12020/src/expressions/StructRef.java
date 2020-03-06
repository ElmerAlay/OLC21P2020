package expressions;

import abstracto.ASTNode;
import abstracto.TError;
import java.util.LinkedList;
import symbols.Environment;
import symbols.ListStruct;
import symbols.Mat;
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
            //Verifico si el símbolo es una matriz
            else if(environment.get(name).getValue() instanceof Mat){
                //Verifico que la lista de índices sea de tamaño 1
                if(indexes.size()==1){
                    Object o = indexes.remove(0).execute(environment, LError);
                    //Verifico que o sea un vector de tipo integer de un sólo valor
                    if(o instanceof Vec && ((Vec)o).getValues().length==1 && ((Vec)o).getValues()[0] instanceof Integer){
                        int index = Integer.parseInt((((Vec)o).getValues()[0]).toString());
                        //Verifico que el index esté dentro del tamaño de la matriz
                        Mat mat = (Mat)environment.get(name).getValue();
                        if(index<=mat.col*mat.row && index>0){
                            int cont = 0;
                            for(int i=0; i<mat.col; i++){
                                for(int j=0; j<mat.row; j++){
                                    if(cont==(index-1)){
                                        Object result[] = {mat.getValues()[j][i]};
                                        return new Vec(result);
                                    }
                                    cont++;
                                }
                            }
                        }else{
                            TError error = new TError(name, "Semántico", "El índice sobrepasa el tamaño del vector o es negativo", 0, 0);
                            LError.add(error);
                            return error;
                        }
                    }else{
                        TError error = new TError(name, "Semántico", "El índice debe ser un vector de tipo integer de tamaño 1", 0, 0);
                        LError.add(error);
                        return error;
                    } 
                }else{
                    TError error = new TError(name, "Semántico", "La variable es de tipo matriz, pero tiene más de un índice para acceder", 0, 0);
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
