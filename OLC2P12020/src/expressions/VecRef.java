package expressions;

import abstracto.ASTNode;
import abstracto.TError;
import java.util.LinkedList;
import symbols.Environment;
import symbols.Vec;
import symbols.Vec2;

/**
 *
 * @author junio
 */
public class VecRef implements ASTNode{
    String name;
    private Object vec[];
    private LinkedList<ASTNode> indexes;
    private int row;
    private int column;

    public VecRef(String name, Object[] vec, LinkedList<ASTNode> indexes, int row, int column) {
        super();
        this.name = name;
        this.vec = vec;
        this.indexes = indexes;
        this.row = row;
        this.column = column;
    }
    
    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        //Recorro toda la lista de índices para verificar que sean válidos para los vectores
        for(ASTNode index : indexes){
            Object o = index.execute(environment, LError);
            if(o instanceof Vec2){
                TError error = new TError(name, "Semántico", "El tipo de acceso [[]] no se puede usar en los vectores", row, column);
                LError.add(error);
                return error;
            }
        }
        
        //Obtengo el primer índice para obtener los valores en esa posición
        Object index = indexes.remove(0).execute(environment, LError);
        
        //Verifico que el index sea de tipo Vector y tenga sólo un elemento
        if(index instanceof Vec && ((Vec)index).getValues().length==1){
            //Verifico que su elemento sea de tipo integer
            if(((Vec)index).getValues()[0] instanceof Integer){
                //Verifico que el indice esté dentro del tamaño del vector
                if(Integer.parseInt((((Vec)index).getValues()[0]).toString()) <= vec.length && Integer.parseInt((((Vec)index).getValues()[0]).toString()) > 0){
                    //Creo un nuevo vector para retornar
                    Object value[]= {vec[Integer.parseInt((((Vec)index).getValues()[0]).toString())-1]};

                    //Verifico si la lista de índices está vacía
                    if(indexes.isEmpty()){
                        //Retorno un nuevo vector con el valor solicitado
                        return new Vec(value);
                    }
                    //De lo contrario vuelvo a llamar a este mismo método pero con el primer índice ya eliminado
                    else{
                        Object o = new VecRef(name, value, indexes, row, column).execute(environment, LError);
                        if(o instanceof Vec)
                            return (Vec)o;
                        else
                            return o;
                    }
                }else{
                    TError error = new TError(name, "Semántico", "El índice es mayor al tamaño del vector o es negativo", row, column);
                    LError.add(error);
                    return error;
                }
            }else{
                TError error = new TError(name, "Semántico", "El índice no es de tipo integer", row, column);
                LError.add(error);
                return error;
            }
        }
        
        TError error = new TError(name, "Semántico", "El índice debe ser un vector de sólo un elemento", row, column);
        LError.add(error);
        return error;
    }
}
