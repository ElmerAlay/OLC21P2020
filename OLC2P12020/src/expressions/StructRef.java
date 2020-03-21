package expressions;

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
public class StructRef implements ASTNode{
    private String name;
    private LinkedList<ASTNode> indexes;
    private int row;
    private int column;

    public StructRef(String name, LinkedList<ASTNode> indexes, int row, int column) {
        super();
        this.name = name;
        this.indexes = indexes;
        this.row = row;
        this.column = column;
    }
    
    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        if(environment.get(name) != null){  //Primero verifico que la variable existe 
            //Verifico si el símbolo es un vector
            if(environment.get(name).getValue() instanceof Vec){
                Object o = new VecRef(name, ((Vec)environment.get(name).getValue()).getValues(), indexes, row, column).execute(environment, LError);
                if(o instanceof Vec){
                    return (Vec)o;
                }else{
                    TError error = new TError(name, "Semántico", "Error de índices en el vector", row, column);
                    LError.add(error);
                    return error;
                }
            }
            //Verifico si el símbolo es una lista
            else if(environment.get(name).getValue() instanceof ListStruct){
                Object o = new ListRef(name, ((ListStruct)environment.get(name).getValue()).getValues(), indexes, row, column).execute(environment, LError);
                if(o instanceof ListStruct){
                    return (ListStruct)o;
                }else if (o instanceof Vec){
                    return (Vec)o;
                }
                else{
                    TError error = new TError(name, "Semántico", "Error de índices en la lista", row, column);
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
                            TError error = new TError(name, "Semántico", "El índice sobrepasa el tamaño del vector o es negativo", row, column);
                            LError.add(error);
                            return error;
                        }
                    }else{
                        TError error = new TError(name, "Semántico", "El índice debe ser un vector de tipo integer de tamaño 1", row, column);
                        LError.add(error);
                        return error;
                    } 
                }else{
                    TError error = new TError(name, "Semántico", "La variable es de tipo matriz, pero tiene más de un índice para acceder", row, column);
                    LError.add(error);
                    return error;
                }
            }
             //Verifico si el símbolo es un arreglo
            else if(environment.get(name).getValue() instanceof Arr){
                Arr arr = ((Arr)environment.get(name).getValue());
                int tam[] = new int[arr.dim.length+2];
                tam[0] = arr.row;
                tam[1] = arr.col;
                for(int i=2; i<tam.length;i++){
                    tam[i] = arr.dim[i-2];
                }
                int in = 0;
                Object result = null;
                LinkedList<Object> index = new LinkedList<>();
                
                //Verifico que se accesa a todas las dimensiones
                if(indexes.size() == (arr.dim.length+2)){
                    //Verifico que cada indice sea de tipo integer y de tamaño uno
                    boolean flag = true;
                    for(ASTNode ind: indexes){
                        index.add(ind.execute(environment, LError));
                        if(index.getLast() instanceof Vec && ((Vec)index.getLast()).getValues().length==1 && ((Vec)index.getLast()).getValues()[0] instanceof Integer){
                        }else{
                            flag = false;
                            break;
                        }
                    }
                    
                    if(flag){
                        boolean f = true;
                        if(index.size() == 3){
                            int i1 = Integer.parseInt((((Vec)index.get(0)).getValues()[0]).toString());
                            int i2 = Integer.parseInt((((Vec)index.get(1)).getValues()[0]).toString());
                            int i3 = Integer.parseInt((((Vec)index.get(2)).getValues()[0]).toString());
                            //Verificamos que no sobrepasen el arreglo
                            if(i1<=arr.row && i2<=arr.col && i3<=arr.dim[0] && i1>0 && i2>0 && i3>0){
                                int inds[] = {i1-1,i2-1,i3-1};
                                in = SupportFunctions.mapLexArr(inds, tam, tam.length);
                            }else{
                                f = false;
                            }
                        }else if(index.size() == 4){
                            int i1 = Integer.parseInt((((Vec)index.get(0)).getValues()[0]).toString());
                            int i2 = Integer.parseInt((((Vec)index.get(1)).getValues()[0]).toString());
                            int i3 = Integer.parseInt((((Vec)index.get(2)).getValues()[0]).toString());
                            int i4 = Integer.parseInt((((Vec)index.get(3)).getValues()[0]).toString());
                            //Verificamos que no sobrepasen el arreglo
                            if(i1<=arr.row && i2<=arr.col && i3<=arr.dim[0] && i4<=arr.dim[1] && i1>0 && i2>0 && i3>0 && i4>0){
                                int inds[] = {i1-1,i2-1,i3-1,i4-1};
                                in = SupportFunctions.mapLexArr(inds, tam, tam.length);
                            }else{
                                f = false;
                            }
                        }else if(index.size() == 5){
                            int i1 = Integer.parseInt((((Vec)index.get(0)).getValues()[0]).toString());
                            int i2 = Integer.parseInt((((Vec)index.get(1)).getValues()[0]).toString());
                            int i3 = Integer.parseInt((((Vec)index.get(2)).getValues()[0]).toString());
                            int i4 = Integer.parseInt((((Vec)index.get(3)).getValues()[0]).toString());
                            int i5 = Integer.parseInt((((Vec)index.get(4)).getValues()[0]).toString());
                            //Verificamos que no sobrepasen el arreglo
                            if(i1<=arr.row && i2<=arr.col && i3<=arr.dim[0] && i4<=arr.dim[1] && i5<=arr.dim[1] && i1>0 && i2>0 && i3>0 && i4>0 && i5>0){
                                int inds[] = {i1-1,i2-1,i3-1,i4-1,i5-1};
                                in = SupportFunctions.mapLexArr(inds, tam, tam.length);
                            }else{
                                f = false;
                            }
                        }
                        
                        if(f){
                            result = arr.getData().get(in);
                            if(result instanceof Vec)
                                return (Vec)result;
                            else
                                return (ListStruct)result;
                        }else{
                            TError error = new TError(name, "Semántico", "El índice sobrepasa el tamaño del arreglo", row, column);
                            LError.add(error);
                            return error;
                        }
                    }else{
                        TError error = new TError(name, "Semántico", "Los índices deben ser vectores de tipo integer y tamaño 1", row, column);
                        LError.add(error);
                        return error;
                    }
                }else{
                    TError error = new TError(name, "Semántico", "La variable es de tipo arreglo y el número de índices no es igual al de sus dimensiones", row, column);
                    LError.add(error);
                    return error;
                }
            }else{
                TError error = new TError(name, "Semántico", "La referencia no representa ningúna estructura", row, column);
                LError.add(error);
                return error;
            }
        }
        
        TError error = new TError(name, "Semántico", "La variable no existe", row, column);
        LError.add(error);
        return error;
    }
}
