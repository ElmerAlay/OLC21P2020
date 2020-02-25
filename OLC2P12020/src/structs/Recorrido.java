package structs;

import abstracto.ASTNode;
import expressions.*;

/**
 *
 * @author junio
 */
public class Recorrido {
    public Object Resultado(AST root){
        return getExpression(root).execute();
        
    }
    
    private ASTNode getExpression(AST root){
        switch(root.getChildren().size()){
            case 0:
                if(root.getLabel().equals("Integer"))
                    return new Constant(Integer.parseInt(root.getValue()));
                else if(root.getLabel().equals("Numerico"))
                    return new Constant(Float.parseFloat(root.getValue()));
                else if(root.getLabel().equals("String"))
                    return new Constant(root.getValue());
                else if(root.getLabel().equals("True"))
                    return new Constant(true);
                else if(root.getLabel().equals("False"))
                    return new Constant(false);
            case 1:
                if(root.getLabel().equals("INICIO"))
                    return getExpression(root.getChildren().get(0));
                else if(root.getLabel().equals("-")){
                    ASTNode opu = getExpression(root.getChildren().get(0));
                    return new NegU(opu);
                }else if(root.getLabel().equals("!")){
                    ASTNode opu = getExpression(root.getChildren().get(0));
                    return new NOT(opu);
                }else if(root.getLabel().equals("( )")){
                    return getExpression(root.getChildren().get(0));
                }
            case 2:
                if(root.getLabel().equals("+")){
                    ASTNode op1 = getExpression(root.getChildren().get(0));
                    ASTNode op2 = getExpression(root.getChildren().get(1));
                    return new Addition(op1, op2);
                }else if(root.getLabel().equals("-")){
                    ASTNode op1 = getExpression(root.getChildren().get(0));
                    ASTNode op2 = getExpression(root.getChildren().get(1));
                    return new Subtraction(op1, op2);
                }else if(root.getLabel().equals("*")){
                    ASTNode op1 = getExpression(root.getChildren().get(0));
                    ASTNode op2 = getExpression(root.getChildren().get(1));
                    return new Multiplication(op1, op2);
                }else if(root.getLabel().equals("/")){
                    ASTNode op1 = getExpression(root.getChildren().get(0));
                    ASTNode op2 = getExpression(root.getChildren().get(1));
                    return new Division(op1, op2);
                }else if(root.getLabel().equals("^")){
                    ASTNode op1 = getExpression(root.getChildren().get(0));
                    ASTNode op2 = getExpression(root.getChildren().get(1));
                    return new Pot(op1, op2);
                }else if(root.getLabel().equals("%%")){
                    ASTNode op1 = getExpression(root.getChildren().get(0));
                    ASTNode op2 = getExpression(root.getChildren().get(1));
                    return new Module(op1, op2);
                }else if(root.getLabel().equals("==")){
                    ASTNode op1 = getExpression(root.getChildren().get(0));
                    ASTNode op2 = getExpression(root.getChildren().get(1));
                    return new Equals(op1, op2);
                }else if(root.getLabel().equals("!=")){
                    ASTNode op1 = getExpression(root.getChildren().get(0));
                    ASTNode op2 = getExpression(root.getChildren().get(1));
                    return new NotEquals(op1, op2);
                }else if(root.getLabel().equals(">")){
                    ASTNode op1 = getExpression(root.getChildren().get(0));
                    ASTNode op2 = getExpression(root.getChildren().get(1));
                    return new GT(op1, op2);
                }else if(root.getLabel().equals("<")){
                    ASTNode op1 = getExpression(root.getChildren().get(0));
                    ASTNode op2 = getExpression(root.getChildren().get(1));
                    return new LT(op1, op2);
                }else if(root.getLabel().equals(">=")){
                    ASTNode op1 = getExpression(root.getChildren().get(0));
                    ASTNode op2 = getExpression(root.getChildren().get(1));
                    return new GTE(op1, op2);
                }else if(root.getLabel().equals("<=")){
                    ASTNode op1 = getExpression(root.getChildren().get(0));
                    ASTNode op2 = getExpression(root.getChildren().get(1));
                    return new LTE(op1, op2);
                }else if(root.getLabel().equals("|")){
                    ASTNode op1 = getExpression(root.getChildren().get(0));
                    ASTNode op2 = getExpression(root.getChildren().get(1));
                    return new OR(op1, op2);
                }else if(root.getLabel().equals("&")){
                    ASTNode op1 = getExpression(root.getChildren().get(0));
                    ASTNode op2 = getExpression(root.getChildren().get(1));
                    return new AND(op1, op2);
                }
        }
        
        return null;
    }
}
