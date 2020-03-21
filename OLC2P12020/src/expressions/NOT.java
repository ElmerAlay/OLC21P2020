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
public class NOT implements ASTNode{
    private ASTNode op;
    private int row;
    private int column;

    public NOT(ASTNode op, int row, int column) {
        super();
        this.op = op;
        this.row = row;
        this.column = column;
    }

    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        Object op = this.op.execute(environment, LError);
        
        if(op instanceof Boolean){
            return !Boolean.parseBoolean(op.toString());
        }
        
        
        //Primero verifico que el operador sea de tipo vector
        if(op instanceof Vec){
            Object vec1[] = ((Vec)op).getValues();
            boolean flag = true;
            Object result[] = new Object[vec1.length];
                
            //Recorrro los vectores y opero
            for(int i=0; i<vec1.length; i++){
                if(vec1[i] instanceof Boolean){
                    result[i] = !Boolean.parseBoolean(vec1[i].toString());
                }else {
                    flag = false;
                    break;
                }
            }
                
            //Verifico el valor de la variable flag
            if(flag){
                return new Vec(result);
            }else{
                TError error = new TError("!", "Semántico", "no se pueden comparar lógicamente esos 2 tipos de datos", row, column);
                LError.add(error);

                return error;
            }
        }
        else if(op instanceof Mat){
            Mat mat1 = (Mat)op;
            int con1=0;
            Object o1[] = new Object[mat1.row*mat1.col];
                
            for(int i=0;i<mat1.col;i++){
                for(int j=0;j<mat1.row;j++){
                    o1[con1] = mat1.getValues()[j][i];
                    con1++;
                }
            }
                
            Object res = new NOT(new Constant(new Vec(o1)), row, column).execute(environment, LError);
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
                TError error = new TError("!", "Semántico", "Error al aplicar not a la matriz", row, column);
                LError.add(error);

                return error;
            }
        } 
        
        TError error = new TError("!", "Semántico", "no se puede comparar lógicamente esos 2 tipos de datos", row, column);
        LError.add(error);
        
        return error;
    }
}
