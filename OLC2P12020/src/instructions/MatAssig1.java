package instructions;

import abstracto.ASTNode;
import abstracto.TError;
import java.util.LinkedList;
import stadisticFunctions.SupportFunctions;
import symbols.Casteo;
import symbols.Environment;
import symbols.Mat;
import symbols.Vec;

/**
 *
 * @author junio
 */
public class MatAssig1 implements ASTNode{
    private String name;
    private ASTNode ind1;
    private ASTNode ind2;
    private ASTNode exp;

    public MatAssig1(String name, ASTNode ind1, ASTNode ind2,ASTNode exp) {
        super();
        this.name = name;
        this.ind1 = ind1;
        this.ind2 = ind2;
        this.exp = exp;
    }
    
    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        //Busco la variable y verifico que sea tipo matriz
        if(environment.get(name) != null && environment.get(name).getValue() instanceof Mat){
            Object op1 = ind1.execute(environment, LError);
            Object op2 = ind2.execute(environment, LError);
            Mat mat = (Mat)environment.get(name).getValue();
            
            //Verifico que los índices sean vectores de tipo integer de tamaño 1 y que no sobrepasen el tamaño de la matriz
            if(op1 instanceof Vec && op2 instanceof Vec &&
                    ((Vec)op1).getValues().length==1 && ((Vec)op2).getValues().length==1 &&
                        ((Vec)op1).getValues()[0] instanceof Integer && ((Vec)op2).getValues()[0] instanceof Integer && 
                            Integer.parseInt((((Vec)op1).getValues()[0]).toString())<=mat.row && 
                                Integer.parseInt((((Vec)op2).getValues()[0]).toString())<=mat.col && 
                                    Integer.parseInt((((Vec)op1).getValues()[0]).toString())>0 &&
                                        Integer.parseInt((((Vec)op2).getValues()[0]).toString())>0){
                //Verifico que la expresión sea un vector de tamaño 1
                Object op = exp.execute(environment, LError);
                if(op instanceof Vec && ((Vec)op).getValues().length==1){
                    int row = Integer.parseInt((((Vec)op1).getValues()[0]).toString());
                    int col = Integer.parseInt((((Vec)op2).getValues()[0]).toString());
                    Object vals[] = new Object[mat.row*mat.col];
                    int cont = 0;
                    for(int i=0; i<mat.col; i++){
                        for(int j=0; j<mat.row; j++){
                            vals[cont] = mat.getValues()[j][i];
                            cont++;
                        }
                    }
                    
                    int index = SupportFunctions.mapLexiMat(row-1, col-1, mat.row);
                    
                    Object values[] = Casteo.llenar(vals, ((Vec)op).getValues()[0], index+1);
                    
                    Object result[][] = new Object[mat.row][mat.col];
                    cont = 0;
                    for(int i=0; i<mat.col; i++){
                        for(int j=0; j<mat.row; j++){
                            result[j][i] = values[cont++];
                        }
                    }
                    ((Mat)environment.get(name).getValue()).setValues(result);
                    return "Asignación correcta";
                }else{
                    TError error = new TError(name, "Semántico", "Se está intentando agregar un vector de más de un elemento a una posición de la matriz", 0, 0);
                    LError.add(error);

                    return error;
                }
            }else{
                TError error = new TError(name, "Semántico", "Error de índices de la matríz", 0, 0);
                LError.add(error);

                return error;
            }
        }
        
        TError error = new TError(name, "Semántico", "La variable no existe o no es de tipo matriz", 0, 0);
        LError.add(error);

        return error;
    }
}
