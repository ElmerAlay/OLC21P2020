package NativeFunctions;

import abstracto.ASTNode;
import abstracto.TError;
import java.util.LinkedList;
import symbols.Arr;
import symbols.Environment;
import symbols.ListStruct;
import symbols.Vec;

/**
 *
 * @author junio
 */
public class ArrayFunc implements ASTNode{
    private LinkedList<ASTNode> lparam;

    public ArrayFunc(LinkedList<ASTNode> lparam) {
        super();
        this.lparam = lparam;
    }
    
    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        Object exp = lparam.remove(0).execute(environment, LError);
        Object indexes = lparam.remove(0).execute(environment, LError);
        
        //Verifico que el vector de verdad sea un vector de tipo integer
        if(indexes instanceof Vec && ((Vec)indexes).getValues()[0] instanceof Integer){
            Object v[] = ((Vec)indexes).getValues();
            //Verifico que el tamaño del vector sea al menos de 3
            if(v.length >= 3){
                //Obtengo los datos
                int row = Integer.parseInt((v[0]).toString());
                int col = Integer.parseInt((v[1]).toString());
                int dim[] = new int[v.length-2];
                for(int i=2; i<v.length; i++){
                    dim[i-2] = Integer.parseInt((v[i]).toString());
                }
                LinkedList<Object> data = new LinkedList<>();
               
                int tam = row*col;
                for(int i=0; i<dim.length; i++){
                    tam *= dim[i];
                }
                
                //Verifico que la exp sea de tipo vec
                if(exp instanceof Vec){
                    Object d[] = ((Vec)exp).getValues();
                    
                    int cont = 0;
                    for(int i=0; i<tam; i++){
                        if(cont<d.length){
                            Object[] d1={d[cont]};
                            data.add(new Vec(d1));
                            cont++;
                        }else{
                            cont=0;
                            Object[] d1={d[cont]};
                            data.add(new Vec(d1));
                            cont++;
                        }
                    }
                    
                    return new Arr(data, row, col, dim);
                }
                //Verifico que la exp sea de tipo list
                else if(exp instanceof ListStruct){
                    LinkedList<Object> d = ((ListStruct)exp).getValues();
                    boolean flag = true;
                    for(int i=0; i<d.size(); i++){
                        if(d.get(i) instanceof Vec && ((Vec)d.get(i)).getValues().length==1){
                        }else{
                            flag = false;
                            break;
                        }
                    }
                    
                    if(flag){
                        int cont = 0;
                        for(int i=0; i<tam; i++){
                            if(cont<d.size()){
                                LinkedList<Object> d1 = new LinkedList<>();
                                d1.add(d.get(cont));
                                data.add(new ListStruct(d1));
                                cont++;
                            }else{
                                LinkedList<Object> d1 = new LinkedList<>();
                                cont=0;
                                d1.add(d.get(cont));
                                data.add(new ListStruct(d1));
                                cont++;
                            }
                        }
                        return new Arr(data, row, col, dim);
                    }else{
                        TError error = new TError("array", "Semántico", "No se puede insertar una lista que no contiene datos primitivos y de tamaño 1", 0, 0);
                        LError.add(error);

                        return error;
                    }
                }
                
                TError error = new TError("array", "Semántico", "Los datos que se intentan insertar no son de tipo primitivo o lista", 0, 0);
                LError.add(error);

                return error;
            }
            
            TError error = new TError("array", "Semántico", "Las dimensiones deben ser al menos 3", 0, 0);
            LError.add(error);

            return error;
        }
        
        TError error = new TError("array", "Semántico", "El segundo argumento debe ser un vector de tipo integer", 0, 0);
        LError.add(error);

        return error;
    }
}
