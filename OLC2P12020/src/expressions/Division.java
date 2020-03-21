package expressions;

import abstracto.*;
import java.util.LinkedList;
import symbols.Casteo;
import symbols.Environment;
import symbols.Mat;
import symbols.Vec;

/**
 *
 * @author junio
 */
public class Division implements ASTNode{
    private ASTNode op1;
    private ASTNode op2;
    private int row;
    private int column;

    public Division(ASTNode op1, ASTNode op2, int row, int column) {
        super();
        this.op1 = op1;
        this.op2 = op2;
        this.row = row;
        this.column = column;
    }
    
    @Override
    public Object execute(Environment environment, LinkedList<TError> LError) {
        Object op1 = this.op1.execute(environment,LError);
        Object op2 = this.op2.execute(environment,LError);
        
        
        //Primero verifico que los 2 operadores sean de tipo vector
        if(op1 instanceof Vec && op2 instanceof Vec){
            Object vec1[] = ((Vec)op1).getValues();
            Object vec2[] = ((Vec)op2).getValues();
            
            //Verifico si el 1er vector es de tamaño 1 y el 2do también
            if (vec1.length==1 && vec2.length==1) {
                if(!vec2[0].toString().equals("0")){
                    if((vec1[0] instanceof Double && (vec2[0] instanceof Double || vec2[0] instanceof Integer)) || 
                        (vec2[0] instanceof Double && (vec1[0] instanceof Double || vec1[0] instanceof Integer))){
                        Object result[] = { Double.parseDouble(vec1[0].toString()) / Double.parseDouble(vec2[0].toString()) };
                        return new Vec(result);
                    }else if((vec1[0] instanceof Integer && vec2[0] instanceof Integer)){
                        Object result[] = {Integer.parseInt(vec1[0].toString()) / Integer.parseInt(vec2[0].toString())};
                        return new Vec(result);
                    }else{
                        TError error = new TError("/", "Semántico", "no se puede dividir esos 2 tipos de datos", row, column);
                        LError.add(error);

                        return error;
                    }
                }else{
                    TError error = new TError("/", "Semántico", "no se puede dividir dentro de 0", row, column);
                    LError.add(error);

                    return error;
                }
            }
            //Verifico que los 2 vectores sean del mismo tamaño pero que no sean 1
            else if(vec1.length==vec2.length){
                boolean flag = true;
                Object result[] = new Object[vec1.length];
                
                //Recorrro los vectores y opero
                for(int i=0; i<vec1.length; i++){
                    if(!vec2[i].toString().equals("0")){
                        if((vec1[i] instanceof Double && (vec2[i] instanceof Double || vec2[i] instanceof Integer)) || 
                            (vec2[i] instanceof Double && (vec1[i] instanceof Double || vec1[i] instanceof Integer))){
                            result[i] = Double.parseDouble(vec1[i].toString()) / Double.parseDouble(vec2[i].toString());
                        }else if((vec1[i] instanceof Integer && vec2[i] instanceof Integer)){
                            result[i] = Integer.parseInt(vec1[i].toString()) / Integer.parseInt(vec2[i].toString());
                        }else{
                            flag = false;
                            break;
                        }
                    }else{
                        flag = false;
                        break;
                    }
                }
                
                //Verifico el valor de la variable flag
                if(flag){
                    return new Vec(result);
                }else{
                    TError error = new TError("/", "Semántico", "no se puede dividir esos 2 tipos de datos o no se puede dividir dentro de 0", row, column);
                    LError.add(error);

                    return error;
                }
            }
            //verifico que el 1er vector sea 1 y el segundo sea de mayor tamaño
            else if(vec1.length==1 && vec2.length>1){
                boolean flag = true;
                Object result[] = new Object[vec2.length];
                
                //Recorrro los vectores y opero
                for(int i=0; i<vec2.length; i++){
                    if(!vec2[i].toString().equals("0")){
                        if((vec1[0] instanceof Double && (vec2[i] instanceof Double || vec2[i] instanceof Integer)) || 
                            (vec2[i] instanceof Double && (vec1[0] instanceof Double || vec1[0] instanceof Integer))){
                            result[i] = Double.parseDouble(vec1[0].toString()) / Double.parseDouble(vec2[i].toString());
                        }else if((vec1[i] instanceof Integer && vec2[i] instanceof Integer)){
                            result[i] = Integer.parseInt(vec1[0].toString()) / Integer.parseInt(vec2[i].toString());
                        }else{
                            flag = false;
                            break;
                        }
                    }else{
                        flag = false;
                        break;
                    }
                }
                
                //Verifico el valor de la variable flag
                if(flag){
                    return new Vec(result);
                }else{
                    TError error = new TError("/", "Semántico", "no se puede dividir esos 2 tipos de datos o no se puede dividir dentro de 0", row, column);
                    LError.add(error);

                    return error;
                }
            }
            //Verifico que el primer vector sea de tamaño n y el vector 2 sea tamaño 1
            else if(vec1.length>1 && vec2.length==1){
                boolean flag = true;
                Object result[] = new Object[vec1.length];
                
                //Recorrro los vectores y opero
                for(int i=0; i<vec1.length; i++){
                    if(!vec2[0].toString().equals("0")){
                        if((vec1[i] instanceof Double && (vec2[0] instanceof Double || vec2[0] instanceof Integer)) || 
                            (vec2[0] instanceof Double && (vec1[i] instanceof Double || vec1[i] instanceof Integer))){
                            result[i] = Double.parseDouble(vec1[i].toString()) / Double.parseDouble(vec2[0].toString());
                        }else if((vec1[i] instanceof Integer && vec2[i] instanceof Integer)){
                            result[i] = Integer.parseInt(vec1[i].toString()) / Integer.parseInt(vec2[0].toString());
                        }else{
                            flag = false;
                            break;
                        }
                    }else{
                        flag = false;
                        break;
                    }
                }
                
                //Verifico el valor de la variable flag
                if(flag){
                    return new Vec(result);
                }else{
                    TError error = new TError("/", "Semántico", "no se puede dividir esos 2 tipos de datos", row, column);
                    LError.add(error);

                    return error;
                }
            }
            //Por último es el caso en el que los vectores son de distinto tamaño
            else{
                TError error = new TError("/", "Semántico", "no se puede dividir 2 vectores de distinto tamaño", row, column);
                LError.add(error);

                return error;
            }
        }
        else if(op1 instanceof Mat && op2 instanceof Mat){
            Mat mat1 = (Mat)op1;
            Mat mat2 = (Mat)op2;
            
            //Comparo que sean del mismo tamaño
            if(mat1.row==mat2.row && mat1.col==mat2.col){
                int con1=0, con2=0;
                Object o1[] = new Object[mat1.row*mat1.col], o2[] = new Object[mat2.row*mat2.col];
                for(int i=0;i<mat1.col;i++){
                    for(int j=0;j<mat1.row;j++){
                        o1[con1] = mat1.getValues()[j][i];
                        con1++;
                    }
                }
                for(int i=0;i<mat2.col;i++){
                    for(int j=0;j<mat2.row;j++){
                        o2[con2] = mat2.getValues()[j][i];
                        con2++;
                    }
                }
                
                if(o1[0] instanceof Integer || o1[0] instanceof Double){
                    o1 = Casteo.convertDouble(o1);
                }
                
                Object res = new Division(new Constant(new Vec(o1)), new Constant(new Vec(o2)), row, column).execute(environment, LError);
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
                    TError error = new TError("/", "Semántico", "Error al dividir las matrices", row, column);
                    LError.add(error);

                    return error;
                }
            }else{
                TError error = new TError("/", "Semántico", "no se puede dividir las matrices porque no tienen las mismas dimensiones", row, column);
                LError.add(error);

                return error;
            }
        }
        else if(op1 instanceof Mat && op2 instanceof Vec){
            Mat mat1 = (Mat)op1;
            Object vec[] = ((Vec)op2).getValues();
            
            //Comparo que sea igual a 1 el vector
            if(vec.length==1){
                int con1=0;
                Object o1[] = new Object[mat1.row*mat1.col];
                for(int i=0;i<mat1.col;i++){
                    for(int j=0;j<mat1.row;j++){
                        o1[con1] = mat1.getValues()[j][i];
                        con1++;
                    }
                }
                
                if(o1[0] instanceof Integer || o1[0] instanceof Double){
                    o1 = Casteo.convertDouble(o1);
                }
                
                Object res = new Division(new Constant(new Vec(o1)), new Constant((Vec)op2), row, column).execute(environment, LError);
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
                    TError error = new TError("/", "Semántico", "Error al Dividir la matriz con el vector", row, column);
                    LError.add(error);

                    return error;
                }
            }else{
                TError error = new TError("/", "Semántico", "no se puede dividir una matriz y un vector de más de un valor", row, column);
                LError.add(error);

                return error;
            }
        }
        else if(op1 instanceof Vec && op2 instanceof Mat){
            Mat mat1 = (Mat)op2;
            Object vec[] = ((Vec)op1).getValues();
            
            //Comparo que sea igual a 1 el vector
            if(vec.length==1){
                int con1=0;
                Object o1[] = new Object[mat1.row*mat1.col];
                for(int i=0;i<mat1.col;i++){
                    for(int j=0;j<mat1.row;j++){
                        o1[con1] = mat1.getValues()[j][i];
                        con1++;
                    }
                }
                
                if(o1[0] instanceof Integer || o1[0] instanceof Double){
                    o1 = Casteo.convertDouble(o1);
                }
                    
                Object res = new Division(new Constant((Vec)op1), new Constant(new Vec(o1)), row, column).execute(environment, LError);
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
                    TError error = new TError("/", "Semántico", "Error al dividir la matriz con el vector", row, column);
                    LError.add(error);

                    return error;
                }
            }else{
                TError error = new TError("/", "Semántico", "no se puede dividir una matriz y un vector de más de un valor", row, column);
                LError.add(error);

                return error;
            }
        }
        
        TError error = new TError("/", "Semántico", "no se puede dividir esos 2 tipos de datos", row, column);
        LError.add(error);
        
        return error;
    }
    
}
