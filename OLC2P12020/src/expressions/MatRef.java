package expressions;

import abstracto.ASTNode;
import abstracto.TError;
import java.util.LinkedList;
import stadisticFunctions.SupportFunctions;
import symbols.Environment;
import symbols.Mat;
import symbols.Vec;

/**
 *
 * @author junio
 */
public class MatRef implements ASTNode{
    private String name;
    private ASTNode exp1;
    private ASTNode exp2;
    private int row;
    private int column;

    public MatRef(String name, ASTNode exp1, ASTNode exp2, int row, int column) {
        this.name = name;
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.row = row;
        this.column = column;
    }
    
    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        //Verificamos si la variable existe y que es una matriz
        if(environment.get(name) != null && environment.get(name).getValue() instanceof Mat){
            Object op1 = exp1.execute(environment, LError);
            Object op2 = exp2.execute(environment, LError);
            Mat mat = ((Mat)environment.get(name).getValue());
                
            //Verifico que las filas y columnas son vectores de tipo integer de un solo valor
            if((op1 instanceof Vec && op2 instanceof Vec)&&
                (((Vec)op1).getValues().length==1 &&((Vec)op2).getValues().length==1) &&
                    (((Vec)op1).getValues()[0] instanceof Integer && ((Vec)op2).getValues()[0] instanceof Integer)){
                int row = Integer.parseInt((((Vec)op1).getValues()[0]).toString());
                int col = Integer.parseInt((((Vec)op2).getValues()[0]).toString());
                    
                //Verificar que la fila y la columna no sobrepasen el tamaño de la matriz
                if(row<=mat.row && col<=mat.col && row>0 && col>0){
                    int ind = SupportFunctions.mapLexiMat(row-1, col-1, row*col);
                    //Object result[] = {mat.getValues()[ind]};
                    Object result[] = { mat.getValues()[row-1][col-1] };
                    return new Vec(result);
                }else{
                    TError error = new TError(name, "Semántico", "Los índices sobrepasan el tamaño de la matriz", row, column);
                    LError.add(error);
                    return error;
                }
            }else{
                TError error = new TError(name, "Semántico", "Los índices no son vectores de un solo valor de tipo integer", row, column);
                LError.add(error);
                return error;
            }
        }
    
        TError error = new TError(name, "Semántico", "La variable no existe o no es de tipo matriz", row, column);
        LError.add(error);
        return error;
    }
}
