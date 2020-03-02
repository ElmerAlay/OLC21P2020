package expressions;

import abstracto.*;
import java.util.LinkedList;
import symbols.Environment;
import symbols.Vec;

/**
 *
 * @author junio
 */
public class NegU implements ASTNode{
    private ASTNode opu;

    public NegU(ASTNode opu) {
        super();
        this.opu = opu;
    }
    
    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        Object opu = this.opu.execute(environment, LError);
        
        if(opu instanceof Float){
            return Float.parseFloat(opu.toString()) * -1;
        }else if(opu instanceof Integer){
            return Integer.parseInt(opu.toString()) * -1;
        }
        
        //Primero verifico que el operador sea de tipo vector
        if(opu instanceof Vec){
            Object vec1[] = ((Vec)opu).getValues();
            
            //Verifico si el vector es de tamaño 1
            if (vec1.length==1) {
                if(vec1[0] instanceof Float){
                    Object result[] = { Float.parseFloat(vec1[0].toString()) * -1 };
                    return new Vec(result);
                }else if(vec1[0] instanceof Integer){
                    Object result[] = {Integer.parseInt(vec1[0].toString()) * -1 };
                    return new Vec(result);
                }else{
                    TError error = new TError("-", "Semántico", "no se puede aplicar negación unaria a ese tipo de dato", 0, 0);
                    LError.add(error);

                    return error;
                }
            }
            //Verifico que el vector sea mayor a 1
            else if(vec1.length>1){
                boolean flag = true;
                Object result[] = new Object[vec1.length];
                
                //Recorrro los vectores y opero
                for(int i=0; i<vec1.length; i++){
                    if(vec1[i] instanceof Float){
                        result[i] = Float.parseFloat(vec1[i].toString()) * -1;
                    }else if(vec1[i] instanceof Integer){
                        result[i] = Integer.parseInt(vec1[i].toString()) * -1;
                    }else {
                        flag = false;
                        break;
                    }
                }
                
                //Verifico el valor de la variable flag
                if(flag){
                    return new Vec(result);
                }else{
                    TError error = new TError("-", "Semántico", "no se puede aplicar negación unaria a ese tipo de dato", 0, 0);
                    LError.add(error);

                    return error;
                }
            }
        }
        
        TError error = new TError("-", "Semántico", "no se puede aplicar negación unaria a ese tipo de dato", 0, 0);
        LError.add(error);
        
        return error;
    }
    
}
