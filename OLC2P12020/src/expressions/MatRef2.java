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
public class MatRef2 implements ASTNode{
    private String name;
    private ASTNode exp1;
    private int row;
    private int column;
    
    public MatRef2(String name, ASTNode exp1, int row, int column) {
        this.name = name;
        this.exp1 = exp1;
        this.row = row;
        this.column = column;
    }
    
    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        //Verificamos si la variable existe y que es una matriz
        if(environment.get(name) != null && environment.get(name).getValue() instanceof Mat){
            Object op1 = exp1.execute(environment, LError);
            Mat mat = ((Mat)environment.get(name).getValue());
                
            //Verifico que la fila sea vector de tipo integer de un solo valor
            if((op1 instanceof Vec)&&
                        (((Vec)op1).getValues().length==1) &&
                            (((Vec)op1).getValues()[0] instanceof Integer)){
                    int row = Integer.parseInt((((Vec)op1).getValues()[0]).toString());
                    
                    //Verificar que la fila no sobrepasa el tamaño de la matriz
                    if(row<=mat.row && row>0){
                        Object result[] = new Object[mat.col];
                        for(int i=0; i<mat.col; i++){
                            result[i] = mat.getValues()[row-1][i];
                        }
                        return new Vec(result);
                    }else{
                        TError error = new TError(name, "Semántico", "El índice sobrepasa el tamaño del vector", this.row, column);
                        LError.add(error);
                        return error;
                    }
            }else{
                TError error = new TError(name, "Semántico", "El índice no es un vector de un solo valor de tipo integer", row, column);
                LError.add(error);
                return error;
            }
        }
    
        TError error = new TError(name, "Semántico", "La variable no existe o no es de tipo matriz", row, column);
        LError.add(error);
        return error;
    }
}
