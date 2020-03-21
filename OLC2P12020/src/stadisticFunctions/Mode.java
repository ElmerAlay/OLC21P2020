package stadisticFunctions;

import abstracto.ASTNode;
import abstracto.TError;
import java.util.LinkedList;
import symbols.Environment;
import symbols.Vec;

/**
 *
 * @author junio
 */
public class Mode implements ASTNode{
    private LinkedList<ASTNode> lexp;
    private int row;
    private int column;

    public Mode(LinkedList<ASTNode> lexp, int row, int column) {
        super();
        this.lexp = lexp;
        this.row = row;
        this.column = column;
    }
    
    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        //Verificamos el número de expresiones obtenidas es igual a 1
        if(lexp.size() == 1){
            Object exp = lexp.get(0).execute(environment, LError);
            //Verificamos que sea un vector
            if(exp instanceof Vec){
                Object values[] = SupportFunctions.burbuja(((Vec)exp).getValues(),0);
                int frecTemp, frecModa = 0;
                double moda1 = -1; 
                
                if(values[0] instanceof Integer || values[0] instanceof Double){
                    for ( int i = 0; i < values.length; i++ ) {
                        frecTemp = 1; 
                        for ( int j = i + 1; j < values.length; j++ ) {
                            if ( Double.parseDouble(values[i].toString()) == Double.parseDouble(values[j].toString()) )
                                frecTemp++;
                        }
                        
                        if ( frecTemp>frecModa ) {
                            frecModa = frecTemp;
                            moda1 = Double.parseDouble(values[i].toString());
                        }
                    }
                    
                    Object result[] = {moda1};
                    return new Vec(result);
                }else{
                    TError error = new TError("Mode", "Semántico", "El vector no es de tipo integer o numérico", row, column);
                    LError.add(error);

                    return error; 
                }
            }else{
                TError error = new TError("Mode", "Semántico", "La expresión dentro de la función no es vector", row, column);
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
                if(trim.length==1 && (trim[0] instanceof Integer || trim[0] instanceof Double)){
                    boolean f = false;
                    Object values[] = SupportFunctions.burbuja(((Vec)exp).getValues(),0);
                    for(int i=0; i<values.length; i++){
                        if(Double.parseDouble(values[i].toString())>=Double.parseDouble(trim[0].toString())){
                            f = true;
                            break;
                        }
                    }
                    
                    if(f){
                        int frecTemp, frecModa = 0;
                        double moda1 = -1; 
                        int cont = 0;

                        if(values[0] instanceof Integer || values[0] instanceof Double){
                            for(int i=0; i<values.length; i++){
                                if(Double.parseDouble(values[i].toString())<Double.parseDouble(trim[0].toString()))
                                    cont++;
                            }

                            Object vals[] = new Object[values.length - cont];
                            for(int i=cont; i<values.length; i++){
                                vals[i-cont] = Double.parseDouble(values[i].toString());
                            }

                            for ( int i = 0; i < vals.length; i++ ) {
                                frecTemp = 1; 
                                for ( int j = i + 1; j < vals.length; j++ ) {
                                    if ( Double.parseDouble(vals[i].toString()) == Double.parseDouble(vals[j].toString()) )
                                        frecTemp++;
                                }

                                if ( frecTemp>frecModa ) {
                                    frecModa = frecTemp;
                                    moda1 = Double.parseDouble(vals[i].toString());
                                }
                            }

                            Object result[] = {moda1};
                            return new Vec(result);
                        }else{
                            TError error = new TError("Mode", "Semántico", "El primer argumento no es de tipo integer o numérico", row, column);
                            LError.add(error);

                            return error; 
                        }
                    }else {
                        int frecTemp, frecModa = 0;
                        double moda1 = -1; 

                        if(values[0] instanceof Integer || values[0] instanceof Double){
                            for ( int i = 0; i < values.length; i++ ) {
                                frecTemp = 1; 
                                for ( int j = i + 1; j < values.length; j++ ) {
                                    if ( Double.parseDouble(values[i].toString()) == Double.parseDouble(values[j].toString()) )
                                        frecTemp++;
                                }

                                if ( frecTemp>frecModa ) {
                                    frecModa = frecTemp;
                                    moda1 = Double.parseDouble(values[i].toString());
                                }
                            }

                            Object result[] = {moda1};
                            return new Vec(result);
                        }else{
                            TError error = new TError("Mode", "Semántico", "El vector no es de tipo integer o numérico", row, column);
                            LError.add(error);

                            return error; 
                        }
                    }
                }else{
                    TError error = new TError("Mode", "Semántico", "El 2do argumento debe contener sólo un valor y ser de tipo integer o numérico", row, column);
                    LError.add(error);

                    return error; 
                }
            }else{
                TError error = new TError("Mode", "Semántico", "Alguna expresión dentro de la función no es vector", row, column);
                LError.add(error);

                return error;
            }
        }
            
        TError error = new TError("Mode", "Semántico", "La función sólo puede traer 2 argumentos", row, column);
        LError.add(error);

        return error;
    }
}