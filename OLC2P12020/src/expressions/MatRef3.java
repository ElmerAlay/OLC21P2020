package expressions;

import abstracto.ASTNode;
import abstracto.TError;
import java.util.LinkedList;
import symbols.Environment;
import symbols.Mat;
import symbols.Vec;

/**
 *
 * @author junio
 */
public class MatRef3 implements ASTNode{
    private String name;
    private ASTNode exp1;

    public MatRef3(String name, ASTNode exp1) {
        this.name = name;
        this.exp1 = exp1;
    }
    
    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        //Verificamos si la variable existe y que es una matriz
        if(environment.get(name) != null && environment.get(name).getValue() instanceof Mat){
            Object op1 = exp1.execute(environment, LError);
            Mat mat = ((Mat)environment.get(name).getValue());
                
            //Verifico que la columna sea vector de tipo integer de un solo valor
            if((op1 instanceof Vec)&&
                        (((Vec)op1).getValues().length==1) &&
                            (((Vec)op1).getValues()[0] instanceof Integer)){
                    int col = Integer.parseInt((((Vec)op1).getValues()[0]).toString());
                    
                    //Verificar que la fila no sobrepasa el tamaño de la matriz
                    if(col<=mat.col && col>0){
                        Object result[] = new Object[mat.row];
                        for(int i=0; i<mat.row; i++){
                            result[i] = mat.getValues()[i][col-1];
                        }
                        return new Vec(result);
                    }else{
                        TError error = new TError(name, "Semántico", "El índice sobrepasa el tamaño del vector", 0, 0);
                        LError.add(error);
                        return error;
                    }
            }else{
                TError error = new TError(name, "Semántico", "El índice no es un vector de un solo valor de tipo integer", 0, 0);
                LError.add(error);
                return error;
            }
        }
    
        TError error = new TError(name, "Semántico", "La variable no existe o no es de tipo matriz", 0, 0);
        LError.add(error);
        return error;
    }
}
