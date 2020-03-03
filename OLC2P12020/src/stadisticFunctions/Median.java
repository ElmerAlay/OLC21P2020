package stadisticFunctions;

import abstracto.*;
import java.util.LinkedList;
import symbols.Environment;
import symbols.Vec;

/**
 *
 * @author junio
 */
public class Median implements ASTNode{
    private LinkedList<ASTNode> lexp;

    public Median(LinkedList<ASTNode> lexp) {
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
                Object values[] = SupportFunctions.burbuja(((Vec)exp).getValues(),0);
                int pos;
                float temp0 = 0;
                
                if(values[0] instanceof Integer || values[0] instanceof Float){
                    if(values.length%2 == 0){
                        pos = (int)(values.length/2)-1;
                        temp0 = (Float.parseFloat(values[pos].toString())+Float.parseFloat(values[pos+1].toString()))/2;
                    }else if(values.length%2 != 0){
                        pos = (int)(values.length/2 + 0.5)-1;
                        temp0 = Float.parseFloat(values[pos].toString());
                    }
                    
                    Object result[] = {temp0};
                    return new Vec(result);
                }else{
                    TError error = new TError("Median", "Semántico", "El vector no es de tipo integer o numérico", 0, 0);
                    LError.add(error);

                    return error; 
                }
            }else{
                TError error = new TError("Median", "Semántico", "La expresión dentro de la función no es vector", 0, 0);
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
                    Object values[] = SupportFunctions.burbuja(((Vec)exp).getValues(),0);
                    int cont = 0;
                    
                    if(values[0] instanceof Integer || values[0] instanceof Float){
                        for(int i=0; i<values.length; i++){
                            if(Float.parseFloat(values[i].toString())<Float.parseFloat(trim[0].toString()))
                                cont++;
                        }
                        
                        Object vals[] = new Object[values.length - cont];
                        for(int i=cont; i<values.length; i++){
                            vals[i-cont] = Float.parseFloat(values[i].toString());
                        }
                        
                        int pos;
                        float temp0 = 0;
                        
                        if(vals.length%2 == 0){
                            pos = (int)(vals.length/2)-1;
                            temp0 = (Float.parseFloat(vals[pos].toString())+Float.parseFloat(vals[pos+1].toString()))/2;
                        }else if(vals.length%2 != 0){
                            pos = (int)(vals.length/2 + 0.5)-1;
                            temp0 = Float.parseFloat(vals[pos].toString());
                        }

                        Object result[] = {temp0};
                        return new Vec(result);
                    }else{
                        TError error = new TError("Median", "Semántico", "El primer argumento no es de tipo integer o numérico", 0, 0);
                        LError.add(error);

                        return error; 
                    }
                }else{
                    TError error = new TError("Median", "Semántico", "El 2do argumento debe contener sólo un valor y ser de tipo integer o numérico", 0, 0);
                    LError.add(error);

                    return error; 
                }
            }else{
                TError error = new TError("Median", "Semántico", "Alguna expresión dentro de la función no es vector", 0, 0);
                LError.add(error);

                return error;
            }
        }
            
        TError error = new TError("Median", "Semántico", "La función sólo puede traer 2 argumentos", 0, 0);
        LError.add(error);

        return error;
    }
}
