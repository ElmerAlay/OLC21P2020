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

    public Mean(LinkedList<ASTNode> lexp) {
        super();
        this.lexp = lexp;
    }
    
    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        //Verificamos el número de expresiones obtenidas es igual a 1
        if(lexp.size() == 1){
            Object exp = lexp.get(0).execute(environment, LError);
            //Verificamos que sea un vector
            if(exp instanceof Vec){
                Object values[] = ((Vec)exp).getValues();
                float mean = Float.parseFloat("0.0");
                boolean flag = true;
                
                for (int i=0; i < values.length; i++) {
                    if(values[i] instanceof Integer || values[i] instanceof Float)
                        mean = mean + Float.parseFloat(values[i].toString());
                    else{
                        flag = false;
                        break;
                    }
                }
                
                if(flag){
                    Object result[] = {mean / values.length};
                    return new Vec(result);
                }else{
                    TError error = new TError("Mean", "Semántico", "El vector no es de tipo integer o numérico", 0, 0);
                    LError.add(error);

                    return error; 
                }
            }else{
                TError error = new TError("Mean", "Semántico", "La expresión dentro de la función no es vector", 0, 0);
                LError.add(error);

                return error;
            }
        }else if(lexp.size() == 2){
            Object exp = lexp.get(0).execute(environment, LError);
            Object exp2 = lexp.get(1).execute(environment, LError);
            
            //Verificamos que sean vectores
            if(exp instanceof Vec && exp instanceof Vec){
                Object trim[] = ((Vec)exp2).getValues();
                
                //Verificar que trim sea de tamaño 1
                if(trim.length==1){
                    float mean = Float.parseFloat("0.0");
                    Object values[] = ((Vec)exp).getValues();
                    int cont = 0;
                    boolean flag = true;

                    for (int i=0; i < values.length; i++) {
                        if(values[i] instanceof Integer || values[i] instanceof Float){
                            if(Float.parseFloat(values[i].toString())>= Float.parseFloat(trim[0].toString())){
                                mean = mean + Float.parseFloat(values[i].toString());
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
                        TError error = new TError("Mean", "Semántico", "El primer argumento no es de tipo integer o numérico", 0, 0);
                        LError.add(error);

                        return error; 
                    }
                }else{
                    TError error = new TError("Mean", "Semántico", "El 2do argumento debe contener sólo un valor y ser de tipo integer o numérico", 0, 0);
                    LError.add(error);

                    return error; 
                }
            }else{
                TError error = new TError("Mean", "Semántico", "Alguna expresión dentro de la función no es vector", 0, 0);
                LError.add(error);

                return error;
            }
        }
            
        TError error = new TError("Mean", "Semántico", "La función sólo puede traer 2 argumentos", 0, 0);
        LError.add(error);

        return error;
    }
}
