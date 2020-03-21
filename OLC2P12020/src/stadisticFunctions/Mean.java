package stadisticFunctions;

import abstracto.*;
import java.util.LinkedList;
import symbols.Environment;
import symbols.Vec;

/**
 *
 * @author junio
 */
public class Mean implements ASTNode{
    private LinkedList<ASTNode> lexp;
    private int row;
    private int column;

    public Mean(LinkedList<ASTNode> lexp, int row, int column) {
        super();
        this.lexp = lexp;
        this.row = row;
        this.column = column;
    }
    
    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        //Verificamos el número de expresiones obtenidas es igual a 1
        if(lexp.size() == 1){
            Object exp = lexp.get(0).execute(environment, LError);
            //Verificamos que sea un vector
            if(exp instanceof Vec){
                Object values[] = ((Vec)exp).getValues();
                double mean = Double.parseDouble("0.0");
                boolean flag = true;
                
                for (int i=0; i < values.length; i++) {
                    if(values[i] instanceof Integer || values[i] instanceof Double)
                        mean = mean + Double.parseDouble(values[i].toString());
                    else{
                        flag = false;
                        break;
                    }
                }
                
                if(flag){
                    Object result[] = {mean / values.length};
                    return new Vec(result);
                }else{
                    TError error = new TError("Mean", "Semántico", "El vector no es de tipo integer o numérico", row, column);
                    LError.add(error);

                    return error; 
                }
            }else{
                TError error = new TError("Mean", "Semántico", "La expresión dentro de la función no es vector", row, column);
                LError.add(error);

                return error;
            }
        }else if(lexp.size() == 2){
            Object exp = lexp.get(0).execute(environment, LError);
            Object exp2 = lexp.get(1).execute(environment, LError);
            
            //Verificamos que sean vectores
            if(exp instanceof Vec && exp2 instanceof Vec){
                Object trim[] = ((Vec)exp2).getValues();
                
                //Verificar que trim sea de tamaño 1
                if(trim.length==1 && (trim[0] instanceof Integer || trim[0] instanceof Double)){
                    boolean f = false;
                    Object values[] = ((Vec)exp).getValues();
                    for(int i=0; i<values.length; i++){
                        if(Double.parseDouble(values[i].toString())>=Double.parseDouble(trim[0].toString())){
                            f = true;
                            break;
                        }
                    }
                    
                    if(f){
                        double mean = Double.parseDouble("0.0");
                        int cont = 0;
                        boolean flag = true;

                        for (int i=0; i < values.length; i++) {
                            if(values[i] instanceof Integer || values[i] instanceof Double){
                                if(Double.parseDouble(values[i].toString()) >= Double.parseDouble(trim[0].toString())){
                                    mean = mean + Double.parseDouble(values[i].toString());
                                    cont++;
                                }
                            }else{
                                flag = false;
                                break;
                            }
                        }

                        if(flag){
                            Object result[] = {mean / cont};
                            return new Vec(result);
                        }else{
                            TError error = new TError("Mean", "Semántico", "El primer argumento no es de tipo integer o numérico", row, column);
                            LError.add(error);

                            return error; 
                        }
                    }else{
                        double mean = Double.parseDouble("0.0");
                        boolean flag = true;

                        for (int i=0; i < values.length; i++) {
                            if(values[i] instanceof Integer || values[i] instanceof Double)
                                mean = mean + Double.parseDouble(values[i].toString());
                            else{
                                flag = false;
                                break;
                            }
                        }

                        if(flag){
                            Object result[] = {mean / values.length};
                            return new Vec(result);
                        }else{
                            TError error = new TError("Mean", "Semántico", "El vector no es de tipo integer o numérico", row, column);
                            LError.add(error);

                            return error; 
                        }
                    }
                }else{
                    TError error = new TError("Mean", "Semántico", "El 2do argumento debe contener sólo un valor y ser de tipo integer o numérico", row, column);
                    LError.add(error);

                    return error; 
                }
            }else{
                TError error = new TError("Mean", "Semántico", "Alguna expresión dentro de la función no es vector", row, column);
                LError.add(error);

                return error;
            }
        }
            
        TError error = new TError("Mean", "Semántico", "La función sólo puede traer 2 argumentos", row, column);
        LError.add(error);

        return error;
    }
}
