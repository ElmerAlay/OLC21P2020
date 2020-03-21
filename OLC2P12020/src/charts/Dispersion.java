package charts;

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
public class Dispersion implements ASTNode{
    private LinkedList<ASTNode> largs;
    private int row;
    private int column;

    public Dispersion(LinkedList<ASTNode> largs, int row, int column) {
        super();
        this.largs = largs;
        this.row = row;
        this.column = column;
    }
    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        Object arg1 = largs.get(0).execute(environment, LError);
        Object arg2 = largs.get(1).execute(environment, LError);
        Object arg3 = largs.get(2).execute(environment, LError);
        Object arg4 = largs.get(3).execute(environment, LError);
        Object arg5 = largs.get(4).execute(environment, LError);
        //Verifico que los argumentos sean vectores
        if(arg2 instanceof Vec && arg3 instanceof Vec && arg4 instanceof Vec && arg5 instanceof Vec){
            //Verifico que los argumentos 2,3,4 sean de tipo String
            Object xlab[] = ((Vec)arg2).getValues();
            Object ylab[] = ((Vec)arg3).getValues();
            Object main[] = ((Vec)arg4).getValues();
            if(xlab[0] instanceof String && ylab[0] instanceof String && main[0] instanceof String){
                //Verifico que si los valores son vectores
                if(arg1 instanceof Vec){
                    //Verifico que sea de tipo float o integer
                    Object values[] = ((Vec)arg1).getValues();
                    if(values[0] instanceof Float || values[0] instanceof Integer){
                        //Verifico que los valores sean positivos
                        boolean flag = true;
                        for(int i=0; i<values.length; i++){
                            if(Float.parseFloat((values[i]).toString()) < 0){
                                flag = false;
                                break;
                            }
                        }
                        if(flag){
                            //Verifico que el último argumento sea un vector de tamaño 2 de tipo numerico
                            Object ylim[] = ((Vec)arg5).getValues();
                            if((ylim[0] instanceof Integer || ylim[0] instanceof Float) && ylim.length==2){
                                float inf = Float.parseFloat(ylim[0].toString());
                                float sup = Float.parseFloat(ylim[1].toString());
                                //hago la comparción para saber si el limite inf es menor que el superior
                                int cont = 0;
                                if(inf<=sup){
                                    for(int i=0; i<values.length; i++){
                                        if(Float.parseFloat((values[i]).toString()) <= sup && Float.parseFloat((values[i]).toString())>=inf){
                                            cont++;
                                        }
                                    }
                                    Object val[] = new Object[cont];
                                    cont=0;
                                    for(int i=0; i<values.length; i++){
                                        if(Float.parseFloat((values[i]).toString()) <= sup && Float.parseFloat((values[i]).toString())>=inf){
                                            val[cont] = values[i];
                                            cont++;
                                        }
                                    }
                                    
                                    LineCharts line = new LineCharts(val, "p", xlab[0].toString(), ylab[0].toString(), main[0].toString());
                                    line.start();
                                    return null;
                                }else{
                                    LineCharts line = new LineCharts(values, "p", xlab[0].toString(), ylab[0].toString(), main[0].toString());
                                    line.start();
                                    TError error = new TError("plot", "Semántico", "El mínimo y el máximo están incorrectos", row, column);
                                    LError.add(error);

                                    return error; 
                                }
                            }
                        }else{
                            TError error = new TError("plot", "Semántico", "Algunos valores son negativos", row, column);
                            LError.add(error);

                            return error; 
                        }
                    }else{
                        TError error = new TError("plot", "Semántico", "Los valores no son de tipo numerico o integer", row, column);
                        LError.add(error);

                        return error; 
                    }
                }
                //Verifico si los valores es una matriz
                else if(arg1 instanceof Mat){
                    Object vals[][] = ((Mat)arg1).getValues();
                    Object values[] = new Object[((Mat)arg1).col * ((Mat)arg1).row];
                    int cont = 0;
                    for(int i=0; i<((Mat)arg1).col; i++){
                        for(int j=0; j<((Mat)arg1).row; j++){
                            values[cont] = vals[j][i];
                            cont++;
                        }
                    }
                    
                    //Verifico que sea de tipo float o integer
                    if(values[0] instanceof Float || values[0] instanceof Integer){
                        //Verifico que los valores sean positivos
                        boolean flag = true;
                        for(int i=0; i<values.length; i++){
                            if(Float.parseFloat((values[i]).toString()) < 0){
                                flag = false;
                                break;
                            }
                        }
                        if(flag){
                            //Verifico que el último argumento sea un vector de tamaño 2 de tipo numerico
                            Object ylim[] = ((Vec)arg5).getValues();
                            if((ylim[0] instanceof Integer || ylim[0] instanceof Float) && ylim.length==2){
                                float inf = Float.parseFloat(ylim[0].toString());
                                float sup = Float.parseFloat(ylim[1].toString());
                                //hago la comparción para saber si el limite inf es menor que el superior
                                cont  = 0;
                                if(inf<=sup){
                                    for(int i=0; i<values.length; i++){
                                        if(Float.parseFloat((values[i]).toString()) <= sup && Float.parseFloat((values[i]).toString())>=inf){
                                            cont++;
                                        }
                                    }
                                    Object val[] = new Object[cont];
                                    cont=0;
                                    for(int i=0; i<values.length; i++){
                                        if(Float.parseFloat((values[i]).toString()) <= sup && Float.parseFloat((values[i]).toString())>=inf){
                                            val[cont] = values[i];
                                            cont++;
                                        }
                                    }
                                    
                                    LineCharts line = new LineCharts(val, "p", xlab[0].toString(), ylab[0].toString(), main[0].toString());
                                    line.start();
                                    return null;
                                }else{
                                    LineCharts line = new LineCharts(values, "p", xlab[0].toString(), ylab[0].toString(), main[0].toString());
                                    line.start();
                                    TError error = new TError("plot", "Semántico", "El mínimo y el máximo están incorrectos", row, column);
                                    LError.add(error);

                                    return error; 
                                }
                            }
                        }else{
                            TError error = new TError("plot", "Semántico", "Algunos valores son negativos", row, column);
                            LError.add(error);

                            return error; 
                        }
                    }else{
                        TError error = new TError("plot", "Semántico", "Los valores no son de tipo numerico o integer", row, column);
                        LError.add(error);

                        return error; 
                    }
                }else{
                    TError error = new TError("plot", "Semántico", "Los valores no son vector", row, column);
                    LError.add(error);

                    return error; 
                }
            }else{
                TError error = new TError("plot", "Semántico", "Los últimos tres argumentos deben ser de tipo string", row, column);
                LError.add(error);

                return error;
            }
        }
        TError error = new TError("plot", "Semántico", "Algún argumento no es de tipo vector", row, column);
        LError.add(error);

        return error;
    }
}
