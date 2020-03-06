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
public class MatAssig2 implements ASTNode{
    private String name;
    private ASTNode ind1;
    private ASTNode exp;

    public MatAssig2(String name, ASTNode ind1,ASTNode exp) {
        super();
        this.name = name;
        this.ind1 = ind1;
        this.exp = exp;
    }
    
    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        //Busco la variable y verifico que sea tipo matriz
        if(environment.get(name) != null && environment.get(name).getValue() instanceof Mat){
            Object op1 = ind1.execute(environment, LError);
            Mat mat = (Mat)environment.get(name).getValue();
            
            //Verifico que los índices sean vectores de tipo integer de tamaño 1 y que no sobrepasen el tamaño de la matriz
            if(op1 instanceof Vec && ((Vec)op1).getValues().length==1 &&
                    ((Vec)op1).getValues()[0] instanceof Integer && Integer.parseInt((((Vec)op1).getValues()[0]).toString())<=mat.row
                        && Integer.parseInt((((Vec)op1).getValues()[0]).toString())>0){
                //Verifico que la expresión sea un vector de tamaño 1
                Object op = exp.execute(environment, LError);
                if(op instanceof Vec && ((Vec)op).getValues().length==1){
                    int row = Integer.parseInt((((Vec)op1).getValues()[0]).toString());
                    Object vals[] = new Object[mat.row*mat.col];
                    int cont = 0;
                    for(int i=0; i<mat.col; i++){
                        for(int j=0; j<mat.row; j++){
                            vals[cont] = mat.getValues()[j][i];
                            cont++;
                        }
                    }
                    
                    Object values[] = new Object[mat.row*mat.col];
                    for(int i=0; i<mat.col; i++){
                        int index = SupportFunctions.mapLexiMat(row-1, i, mat.row);
                        values = Casteo.llenar(vals, ((Vec)op).getValues()[0], index+1);
                    }
                    
                    Object result[][] = new Object[mat.row][mat.col];
                    cont = 0;
                    for(int i=0; i<mat.col; i++){
                        for(int j=0; j<mat.row; j++){
                            result[j][i] = values[cont++];
                        }
                    }
                    ((Mat)environment.get(name).getValue()).setValues(result);
                    return "Asignación correcta";
                }
                else if(op instanceof Vec && ((Vec)op).getValues().length==mat.col){
                    int row = Integer.parseInt((((Vec)op1).getValues()[0]).toString());
                    Object vals[] = new Object[mat.row*mat.col];
                    int cont = 0;
                    for(int i=0; i<mat.col; i++){
                        for(int j=0; j<mat.row; j++){
                            vals[cont] = mat.getValues()[j][i];
                            cont++;
                        }
                    }
                    
                    Object values[] = new Object[mat.row*mat.col];
                    for(int i=0; i<mat.col; i++){
                        int index = SupportFunctions.mapLexiMat(row-1, i, mat.row);
                        values = Casteo.llenar(vals, ((Vec)op).getValues()[i], index+1);
                    }
                    
                    Object result[][] = new Object[mat.row][mat.col];
                    cont = 0;
                    for(int i=0; i<mat.col; i++){
                        for(int j=0; j<mat.row; j++){
                            result[j][i] = values[cont++];
                        }
                    }
                    ((Mat)environment.get(name).getValue()).setValues(result);
                    return "Asignación correcta";
                }
                else{
                    TError error = new TError(name, "Semántico", "El tamaño de la expresión sobrepasa el tamaño del vector", 0, 0);
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
