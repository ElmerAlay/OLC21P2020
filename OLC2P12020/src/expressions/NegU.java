package expressions;

import abstracto.*;
import java.util.LinkedList;
import symbols.Environment;
import symbols.Mat;
import symbols.Vec;

/**
 *
 * @author junio
 */
public class NegU implements ASTNode{
    private ASTNode opu;
    private int row;
    private int column;
    
    public NegU(ASTNode opu, int row, int column) {
        super();
        this.opu = opu;
        this.row = row;
        this.column = column;
    }
    
    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        Object opu = this.opu.execute(environment, LError);
        
        //Primero verifico que el operador sea de tipo vector
        if(opu instanceof Vec){
            Object vec1[] = ((Vec)opu).getValues();
            
            //Verifico si el vector es de tamaño 1
            if (vec1.length==1) {
                if(vec1[0] instanceof Double){
                    Object result[] = { Double.parseDouble(vec1[0].toString()) * -1 };
                    return new Vec(result);
                }else if(vec1[0] instanceof Integer){
                    Object result[] = {Integer.parseInt(vec1[0].toString()) * -1 };
                    return new Vec(result);
                }else{
                    TError error = new TError("-", "Semántico", "no se puede aplicar negación unaria a ese tipo de dato", row, column);
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
                    if(vec1[i] instanceof Double){
                        result[i] = Double.parseDouble(vec1[i].toString()) * -1;
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
                    TError error = new TError("-", "Semántico", "no se puede aplicar negación unaria a ese tipo de dato", row, column);
                    LError.add(error);

                    return error;
                }
            }
        }
        else if(opu instanceof Mat){
            Mat mat1 = (Mat)opu;
            int con1=0;
            Object o1[] = new Object[mat1.row*mat1.col];
                
            for(int i=0;i<mat1.col;i++){
                for(int j=0;j<mat1.row;j++){
                    o1[con1] = mat1.getValues()[j][i];
                    con1++;
                }
            }
                
            Object res = new NegU(new Constant(new Vec(o1)), row, column).execute(environment, LError);
            Object result[][] = new Object[mat1.row][mat1.col];
            con1 = 0;
            if(res instanceof Vec){
                for(int i=0;i<mat1.col;i++){
                    for(int j=0;j<mat1.row;j++){
                        result[j][i] = ((Vec)res).getValues()[con1];
                        con1++;
                    }
                }
                return new Mat(result, mat1.row, mat1.col); 
            }else{
                TError error = new TError("-", "Semántico", "Error al aplicar negación unaria a la matriz", row, column);
                LError.add(error);

                return error;
            }
        }
        
        
        TError error = new TError("-", "Semántico", "no se puede aplicar negación unaria a ese tipo de dato", row, column);
        LError.add(error);
        
        return error;
    }
    
}
