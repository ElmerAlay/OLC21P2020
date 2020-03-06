package NativeFunctions;

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
public class Matrix implements ASTNode{
    private LinkedList<ASTNode> lexp;

    public Matrix(LinkedList<ASTNode> lexp) {
        super();
        this.lexp = lexp;
    }
    
    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        //Obtenemos los datos
        Object data = lexp.remove(0).execute(environment, LError);
        Object nRow = lexp.remove(0).execute(environment, LError);
        Object nCol = lexp.remove(0).execute(environment, LError);
        
        //Comprobar que la fila y columna deben ser vectores de tipo integer de 1 valor
        if((nRow instanceof Vec && nCol instanceof Vec) && (((Vec)nRow).getValues().length==1 && ((Vec)nCol).getValues().length==1)
                && (((Vec)nRow).getValues()[0] instanceof Integer && ((Vec)nCol).getValues()[0] instanceof Integer)){
            //Almaceno en variables la fila y columna
            int row = Integer.parseInt((((Vec)nRow).getValues()[0]).toString());
            int col = Integer.parseInt((((Vec)nCol).getValues()[0]).toString());
            
            //Compruebo los datos sean vectores
            if(data instanceof Vec){
                //Compruebo que sean múltiplo y submultiplo del tamaño de la matriz
                if(SupportFunctions.esMultiplo(row*col,((Vec)data).getValues().length) || SupportFunctions.esMultiplo(((Vec)data).getValues().length, row*col)){
                    Object d[] = ((Vec)data).getValues();
                    int cont = 0;
                    Object result[][] = new Object[row][col];
                   
                    if(d.length>=row*col){
                        for(int i=0; i<col; i++){
                            for(int j=0; j<row; j++){
                                result[j][i] = d[cont];
                                cont++;
                            }
                        }
                    }else if(d.length<row*col){
                        for(int i=0; i<col; i++){
                            for(int j=0; j<row; j++){
                                if(cont<d.length){
                                    result[j][i] = d[cont];
                                    cont++;
                                }else{
                                    cont=0;
                                    result[j][i] = d[cont];
                                    cont++;
                                }
                            }
                        }
                    }
                    return new Mat(result,row,col);
                }else {
                    TError error = new TError("matrix", "Semántico", "La cantidad de datos no es múltiplo o submúltiplo de la matriz", 0, 0);
                    LError.add(error);

                    return error;
                }
            }else{
                TError error = new TError("matrix", "Semántico", "Los datos no son de tipo vector", 0, 0);
                LError.add(error);

                return error;
            }
        }
        
        TError error = new TError("matrix", "Semántico", "La fila y columna deben ser vectores enteros de 1 solo valor", 0, 0);
        LError.add(error);

        return error;
    }
}
