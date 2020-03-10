package structs;

import abstracto.ASTNode;
import abstracto.TError;
import expressions.*;
import instructions.*;
import java.util.LinkedList;
import symbols.*;

/**
 *
 * @author junio
 */
public class Recorrido {
    private Environment global; //= new Environment(null);
    private LinkedList<TError> LError; //= new LinkedList<TError>();
    private LinkedList<ASTNode> lInst;

    public Recorrido(Environment global, LinkedList<TError> LError, LinkedList<ASTNode> lInst) {
        this.global = global;
        this.LError = LError;
        this.lInst = lInst;
    }
    
    public void Resultado(AST root){
        for(ASTNode exp : getInstruccions(root, lInst)){
            exp.execute(global, LError);
        }
        
        //return global;
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
                else if(root.getLabel().equals("BREAK"))
                    return new Break();
                else if(root.getLabel().equals("CONTINUE"))
                    return new Continue();
                /*else if(root.getLabel().equals("RETURN"))
                    return new Return(exp);*/
                else if(root.getLabel().equals("Ref"))
                    return new VarRef(root.getValue());
            case 1:
                if(root.getLabel().equals("-")){
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
                }else if(root.getLabel().equals("ASIGN")){
                    ASTNode op1 = getExpression(root.getChildren().get(1));
                    return new VarAssig(root.getChildren().get(0).getValue(), op1);
                }else if(root.getLabel().equals("Struct")){
                    LinkedList<ASTNode> indexes = new LinkedList<>();
                    indexes = getCor(root.getChildren().get(1), indexes);
                    return new StructRef(root.getChildren().get(0).getValue(), indexes);
                }else if(root.getLabel().equals("Llam")){
                    LinkedList<ASTNode> lparam = new LinkedList<>();
                    lparam = getParam(root.getChildren().get(1), lparam);
                    return new CallFunc(root.getChildren().get(0).getValue(), lparam);
                }else if(root.getLabel().equals("MAT2")){
                    ASTNode exp1 = getExpression(root.getChildren().get(1));
                    return new MatRef2(root.getChildren().get(0).getValue(), exp1);
                }else if(root.getLabel().equals("MAT3")){
                    ASTNode exp1 = getExpression(root.getChildren().get(1));
                    return new MatRef3(root.getChildren().get(0).getValue(), exp1);
                }else if(root.getLabel().equals("MATASIGN") && root.getChildren().get(0).getLabel().equals("MAT1")){
                    String name = root.getChildren().get(0).getChildren().get(0).getValue();
                    ASTNode ind1 = getExpression(root.getChildren().get(0).getChildren().get(1));
                    ASTNode ind2 = getExpression(root.getChildren().get(0).getChildren().get(2));
                    ASTNode exp = getExpression(root.getChildren().get(1));
                    return new MatAssig1(name,ind1,ind2, exp);
                }else if(root.getLabel().equals("MATASIGN") && root.getChildren().get(0).getLabel().equals("MAT2")){
                    String name = root.getChildren().get(0).getChildren().get(0).getValue();
                    ASTNode ind1 = getExpression(root.getChildren().get(0).getChildren().get(1));
                    ASTNode exp = getExpression(root.getChildren().get(1));
                    return new MatAssig2(name,ind1, exp);
                }else if(root.getLabel().equals("MATASIGN") && root.getChildren().get(0).getLabel().equals("MAT3")){
                    String name = root.getChildren().get(0).getChildren().get(0).getValue();
                    ASTNode ind1 = getExpression(root.getChildren().get(0).getChildren().get(1));
                    ASTNode exp = getExpression(root.getChildren().get(1));
                    return new MatAssig3(name,ind1, exp);
                }else if(root.getLabel().equals("IF")){
                    ASTNode cond = getExpression(root.getChildren().get(0));
                    LinkedList<ASTNode> lexpt = new LinkedList<>();
                    lexpt = getInstruccions(root.getChildren().get(1), lexpt);
                    return new If(cond, lexpt);
                }else if(root.getLabel().equals("SWITCH")){
                    ASTNode exp = getExpression(root.getChildren().get(0));
                    LinkedList<CASE> lcase = new LinkedList<>();
                    lcase = getLCASE(exp, root.getChildren().get(1), lcase);
                    LinkedList<ASTNode> linstf = new LinkedList<>();
                    linstf = getInstruccions(root.getChildren().get(2), linstf);
                    return new SWITCH(exp, lcase, linstf);
                }else if(root.getLabel().equals("WHILE")){
                    ASTNode cond = getExpression(root.getChildren().get(0));
                    LinkedList<ASTNode> lexpt = new LinkedList<>();
                    lexpt = getInstruccions(root.getChildren().get(1), lexpt);
                    return new While(cond, lexpt);
                }else if(root.getLabel().equals("DO")){
                    ASTNode cond = getExpression(root.getChildren().get(1));
                    LinkedList<ASTNode> lexpt = new LinkedList<>();
                    lexpt = getInstruccions(root.getChildren().get(0), lexpt);
                    return new DoWhile(cond, lexpt);
                }
            case 3:
                if(root.getLabel().equals("ASIGN")){
                    ASTNode op1 = getExpression(root.getChildren().get(2));
                    LinkedList<ASTNode> indexes = new LinkedList<>();
                    indexes = getCor(root.getChildren().get(1), indexes);
                    return new StructAssig(root.getChildren().get(0).getValue(), op1, indexes);
                }else if(root.getLabel().equals("MAT1")){
                    ASTNode exp1 = getExpression(root.getChildren().get(1));
                    ASTNode exp2 = getExpression(root.getChildren().get(2));
                    return new MatRef(root.getChildren().get(0).getValue(), exp1, exp2);
                }else if(root.getLabel().equals("TERN")){
                    ASTNode cond = getExpression(root.getChildren().get(0));
                    ASTNode expT = getExpression(root.getChildren().get(1));
                    ASTNode expF = getExpression(root.getChildren().get(2));
                    return new Tern(cond, expT, expF);
                }else if(root.getLabel().equals("IF") && root.getChildren().get(2).getLabel().equals("LINST")){
                    ASTNode cond = getExpression(root.getChildren().get(0));
                    LinkedList<ASTNode> lexpt = new LinkedList<>();
                    LinkedList<ASTNode> lexpf = new LinkedList<>();
                    lexpt = getInstruccions(root.getChildren().get(1), lexpt);
                    lexpf = getInstruccions(root.getChildren().get(2), lexpf);
                    return new If(cond, lexpt, lexpf);
                }else if(root.getLabel().equals("IF") && root.getChildren().get(2).getLabel().equals("EI")){
                    ASTNode cond = getExpression(root.getChildren().get(0));
                    LinkedList<ASTNode> lexpt = new LinkedList<>();
                    LinkedList<ASTNode> lei = new LinkedList<>();
                    lexpt = getInstruccions(root.getChildren().get(1), lexpt);
                    lei = getLEI(root.getChildren().get(2), lei);
                    return new If(cond, lexpt, lei, null);
                }else if(root.getLabel().equals("SWITCH")){
                    ASTNode exp = getExpression(root.getChildren().get(0));
                    LinkedList<CASE> lcase = new LinkedList<>();
                    lcase = getLCASE(exp, root.getChildren().get(1), lcase);
                    LinkedList<ASTNode> linstf = new LinkedList<>();
                    linstf = getInstruccions(root.getChildren().get(2), linstf);
                    return new SWITCH(exp, lcase, linstf);
                }else if(root.getLabel().equals("FOR")){
                    ASTNode exp = getExpression(root.getChildren().get(1));
                    LinkedList<ASTNode> linst = new LinkedList<>();
                    linst = getInstruccions(root.getChildren().get(2), linst);
                    return new For(root.getChildren().get(0).getValue() ,exp, linst);
                }
            case 4:
                if(root.getLabel().equals("IF")){
                    ASTNode cond = getExpression(root.getChildren().get(0));
                    LinkedList<ASTNode> lexpt = new LinkedList<>();
                    LinkedList<ASTNode> lei = new LinkedList<>();
                    LinkedList<ASTNode> lexpf = new LinkedList<>();
                    lexpt = getInstruccions(root.getChildren().get(1), lexpt);
                    lei = getLEI(root.getChildren().get(2), lei);
                    lexpf = getInstruccions(root.getChildren().get(3), lexpf);
                    return new If(cond, lexpt, lei,lexpf);
                }
        }
        
        return null;
    }
    
    private LinkedList<ASTNode> getInstruccions(AST root, LinkedList<ASTNode> lInst){
        switch(root.getChildren().size()){
            case 1:
                if(root.getLabel().equals("INICIO")){
                    return getInstruccions(root.getChildren().get(0), lInst);
                }else if(root.getLabel().equals("LINST")){
                    lInst.add(getExpression(root.getChildren().get(0)));
                    return lInst;
                }
            case 2:
                if(root.getLabel().equals("LINST")){
                    lInst = getInstruccions(root.getChildren().get(0), lInst);
                    lInst.add(getExpression(root.getChildren().get(1)));
                    return lInst;
                }
        }
        return null;
    }
    
    private LinkedList<ASTNode> getCor(AST root, LinkedList<ASTNode> lCor){
        switch(root.getChildren().size()){
            case 1:
                if(root.getLabel().equals("LCOR")){
                    lCor.add(getExpression(root.getChildren().get(0)));
                    
                    return lCor;
                }else if(root.getLabel().equals("LCOR2")){
                    ASTNode op = getExpression(root.getChildren().get(0));
                    lCor.add(new Constant2(op));
                    
                    return lCor;
                }
            case 2:
                if(root.getLabel().equals("LCOR")){
                    LinkedList<ASTNode> lCor2 = new LinkedList<>();
                    
                    lCor = getCor(root.getChildren().get(0), lCor);
                    if(root.getChildren().get(1).getLabel().equals("LCOR2")){
                        lCor2 = getCor(root.getChildren().get(1), lCor2);
                    }else
                        lCor.add(getExpression(root.getChildren().get(1)));
                    
                    if(!lCor2.isEmpty()){
                        LinkedList<ASTNode> lista = new LinkedList<>();
                        lista.addAll(lCor);
                        lista.addAll(lCor2);
                        return lista;
                    }
                    
                    return lCor;
                }
        }
        return null;
    }
    
    private LinkedList<ASTNode> getParam(AST root, LinkedList<ASTNode> lparam){
        switch(root.getChildren().size()){
            case 1:
                if(root.getLabel().equals("LPARAM")){
                    lparam.add(getExpression(root.getChildren().get(0)));
                    
                    return lparam;
                }
            case 2:
                if(root.getLabel().equals("LPARAM")){
                    lparam = getParam(root.getChildren().get(0), lparam);
                    lparam.add(getExpression(root.getChildren().get(1)));
                    
                    return lparam;
                }
        }
        return null;
    }
    
    private LinkedList<ASTNode> getLEI(AST root, LinkedList<ASTNode> lei){
        switch(root.getChildren().size()){
            case 1:
                if(root.getLabel().equals("EI")){
                    lei.add(getExpression(root.getChildren().get(0)));
                    
                    return lei;
                }
            case 2:
                if(root.getLabel().equals("EI")){
                    lei = getLEI(root.getChildren().get(0), lei);
                    lei.add(getExpression(root.getChildren().get(1)));
                    
                    return lei;
                }
        }
        return null;
    }
    
    private LinkedList<CASE> getLCASE(ASTNode expT, AST root, LinkedList<CASE> lcase){
        switch(root.getChildren().size()){
            case 2:
                if(root.getLabel().equals("LCASE")){
                    LinkedList<ASTNode> linst = new LinkedList<>();
                    linst = getInstruccions(root.getChildren().get(1), linst);
                    ASTNode exp = getExpression(root.getChildren().get(0));
                    lcase.add(new CASE(expT, exp, linst));
                    
                    return lcase;
                }
            case 3:
                if(root.getLabel().equals("LCASE")){
                    lcase = getLCASE(expT, root.getChildren().get(0), lcase);
                    LinkedList<ASTNode> linst = new LinkedList<>();
                    linst = getInstruccions(root.getChildren().get(2), linst);
                    lcase.add(new CASE(expT, getExpression(root.getChildren().get(1)), linst));
                    
                    return lcase;
                }
        }
        return null;
    }
}
