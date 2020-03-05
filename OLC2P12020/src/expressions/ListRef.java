package expressions;

import abstracto.ASTNode;
import abstracto.TError;
import java.util.LinkedList;
import symbols.Environment;
import symbols.ListStruct;
import symbols.Vec;
import symbols.Vec2;

/**
 *
 * @author junio
 */
public class ListRef implements ASTNode{
    String name;
    private LinkedList<Object> list;
    private LinkedList<ASTNode> indexes;

    public ListRef(String name, LinkedList<Object> list, LinkedList<ASTNode> indexes) {
        super();
        this.name = name;
        this.list = list;
        this.indexes = indexes;
    }
    
    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        //Obtengo el primer índice para obtener los valores en esa posición
        Object index = indexes.remove(0).execute(environment, LError);
        
        //Verifico que el index sea de tipo Vector y tenga sólo un elemento
        if(index instanceof Vec && ((Vec)index).getValues().length==1){
            //Verifico que su elemento sea de tipo integer
            if(((Vec)index).getValues()[0] instanceof Integer){
                //Verifico que el indice esté dentro del tamaño de la lista
                if(Integer.parseInt((((Vec)index).getValues()[0]).toString()) <= list.size() && Integer.parseInt((((Vec)index).getValues()[0]).toString()) > 0){
                    //Creo una nueva lista para retornar
                    LinkedList<Object> value= new LinkedList<>();
                    if(list.get(Integer.parseInt((((Vec)index).getValues()[0]).toString())-1) instanceof LinkedList)
                        value = (LinkedList)list.get(Integer.parseInt((((Vec)index).getValues()[0]).toString())-1);
                    else
                        value.add(list.get(Integer.parseInt((((Vec)index).getValues()[0]).toString())-1));

                    //Verifico si la lista de índices está vacía
                    if(indexes.isEmpty()){
                        //Retorno una nueva lista con el valor solicitado
                        return new ListStruct(value);
                    }
                    //De lo contrario vuelvo a llamar a este mismo método pero con el primer índice ya eliminado
                    else{
                        Object o = new ListRef(name, value, indexes).execute(environment, LError);
                        if(o instanceof ListStruct){
                            return (ListStruct)o;
                        }else if(o instanceof Vec){
                            return (Vec)o;
                        }
                    }
                }else{
                    TError error = new TError(name, "Semántico", "El índice es mayor al tamaño de la lista", 0, 0);
                    LError.add(error);
                    return error;
                }
            }else{
                TError error = new TError(name, "Semántico", "El índice no es de tipo integer", 0, 0);
                LError.add(error);
                return error;
            }
        }
        //Verifico que el index sea de tipo Vector2 y tenga sólo un elemento
        else if(index instanceof Vec2 && ((Vec2)index).getValues().length==1){
            //Verifico que su elemento sea de tipo integer
            if(((Vec2)index).getValues()[0] instanceof Integer){
                //Verifico que el indice esté dentro del tamaño del vector
                if(Integer.parseInt((((Vec2)index).getValues()[0]).toString()) <= list.size() && Integer.parseInt((((Vec2)index).getValues()[0]).toString()) > 0){
                    //Creo un nuevo vector para retornar
                    if(list.get(Integer.parseInt((((Vec2)index).getValues()[0]).toString())-1) instanceof Vec){
                        Object value[]= ((Vec)list.get(Integer.parseInt((((Vec2)index).getValues()[0]).toString())-1)).getValues();
                         
                        //Verifico si la lista de índices está vacía
                        if(indexes.isEmpty()){
                            //Retorno un nuevo vector con el valor solicitado
                            return new Vec(value);
                        }
                        //De lo contrario vuelvo llamo a vecRef pero con el primer índice ya eliminado
                        else{
                            Object o = new VecRef(name, value, indexes).execute(environment, LError);
                            if(o instanceof Vec)
                                return (Vec)o;
                        }
                    }
                    else if(list.get(Integer.parseInt((((Vec2)index).getValues()[0]).toString())-1) instanceof ListStruct){
                        //Verifico si la lista de índices está vacía
                        if(indexes.isEmpty()){
                            //Retorno un nuevo vector con el valor solicitado
                            return (ListStruct)list.get(Integer.parseInt((((Vec2)index).getValues()[0]).toString())-1);
                        }
                        //De lo contrario vuelvo llamo a ListRef pero con el primer índice ya eliminado
                        else{
                            LinkedList<Object> a = ((ListStruct)list.get(Integer.parseInt((((Vec2)index).getValues()[0]).toString())-1)).getValues();
                            Object o = new ListRef(name, a, indexes).execute(environment, LError);
                            if(o instanceof ListStruct){
                                return (ListStruct)o;
                            }else if(o instanceof Vec){
                                return (Vec)o;
                            }
                        }
                    }
                    else{
                        TError error = new TError(name, "Semántico", "El contenido del índice específicado no es de tipo primitivo o vector", 0, 0);
                        LError.add(error);
                        return error;
                    }
                }else{
                    TError error = new TError(name, "Semántico", "El índice es mayor al tamaño del vector", 0, 0);
                    LError.add(error);
                    return error;
                }
            }else{
                TError error = new TError(name, "Semántico", "El índice no es de tipo integer", 0, 0);
                LError.add(error);
                return error;
            }
        }
        
        TError error = new TError(name, "Semántico", "El índice debe ser un vector de sólo un elemento", 0, 0);
        LError.add(error);
        return error;
    }
}
