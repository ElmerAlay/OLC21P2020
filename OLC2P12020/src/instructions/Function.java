package instructions;

import abstracto.ASTNode;
import abstracto.TError;
import java.util.LinkedList;
import symbols.Environment;

/**
 *
 * @author junio
 */
public class Function{
    private String name;
    private LinkedList<ASTNode> lparam;
    private LinkedList<ASTNode> linst;

    public Function(String name, LinkedList<ASTNode> lparam, LinkedList<ASTNode> linst) {
        this.name = name;
        this.lparam = lparam;
        this.linst = linst;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LinkedList<ASTNode> getLparam() {
        return lparam;
    }

    public void setLparam(LinkedList<ASTNode> lparam) {
        this.lparam = lparam;
    }

    public LinkedList<ASTNode> getLinst() {
        return linst;
    }

    public void setLinst(LinkedList<ASTNode> linst) {
        this.linst = linst;
    }
    
    public String getNameParam(int i){
        //if(lparam.get(i) instanceof IdParam)
            return ((IdParam)lparam.get(i)).getName();
        /*else{
            return null;
        }*/
    }
}
