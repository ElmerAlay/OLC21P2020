package charts;

import abstracto.ASTNode;
import abstracto.TError;
import java.util.LinkedList;
import symbols.Environment;
import symbols.Vec;

/**
 *
 * @author junio
 */
public class Pie implements ASTNode{
    private LinkedList<ASTNode> params;
    private int row;
    private int column;

    public Pie(LinkedList<ASTNode> params, int row, int column) {
        super();
        this.params = params;
        this.row = row;
        this.column = column;
    }
    
    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        Object param1 = params.get(0).execute(environment, LError);
        Object param2 = params.get(1).execute(environment, LError);
        Object param3 = params.get(2).execute(environment, LError);
        if(param1 instanceof Vec){
            if(((Vec)param1).getValues()[0] instanceof Integer || ((Vec)param1).getValues()[0] instanceof Float){
                boolean flag = true;
                Object vals[] = ((Vec)param1).getValues();
                for(int i=0; i<vals.length; i++){
                    if(Float.parseFloat((vals[i]).toString()) < 0){
                        flag = false;
                        break;
                    }
                }
                if(flag){
                    if(param2 instanceof Vec && ((Vec)param2).getValues()[0] instanceof String){
                        Object labels[] = ((Vec)param2).getValues();
                        //Verifico que el título sea un vector de tamaño 1
                        if(param3 instanceof Vec && ((Vec)param3).getValues()[0] instanceof String){
                            String main = (((Vec)param3).getValues()[0]).toString();
                            //Verifico que los vectores sean del mismo tamaño
                            if(vals.length <= labels.length){
                                PieCharts pie = new PieCharts(vals, labels, main);
                                pie.start();

                                return null;
                            }else{
                                Object label[] = new Object[vals.length];
                                for(int i=0; i<labels.length; i++){
                                    label[i] = labels[i];
                                }
                                for(int i=labels.length; i<label.length; i++){
                                    label[i] = "Desconocido";
                                }
                                PieCharts pie = new PieCharts(vals, label, main);
                                pie.start();
                                TError error = new TError("Pie", "Semántico", "El vector de labels es menor a los datos", row, column);
                                LError.add(error);

                                return error;
                            }
                        }else{
                            PieCharts pie = new PieCharts(vals, labels, "Desconocido");
                            pie.start();

                            TError error = new TError("Pie", "Semántico", "El tercer argumento no es de tipo string", row, column);
                            LError.add(error);

                            return error;
                        }
                    }else{
                        TError error = new TError("Pie", "Semántico", "El segundo argumento no es de tipo string", row, column);
                        LError.add(error);

                        return error;
                    }
                }else{
                    TError error = new TError("Pie", "Semántico", "El vector de datos tiene numeros negativos", row, column);
                    LError.add(error);

                    return error;
                }
            }else{
                TError error = new TError("Pie", "Semántico", "El tipo del primer argumento debe ser numerico o integer", row, column);
                LError.add(error);

                return error;
            }
        }
        
        TError error = new TError("Pie", "Semántico", "El primer argumento debe ser un vector", row, column);
        LError.add(error);

        return error;
    }
}
