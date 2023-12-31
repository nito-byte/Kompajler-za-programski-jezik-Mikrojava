// generated with ast extension for cup
// version 0.8



package rs.ac.bg.etf.pp1.ast;

public class ConstBool extends ConstValue {

    private Integer boolConst;

    public ConstBool (Integer boolConst) {
        this.boolConst=boolConst;
    }

    public Integer getBoolConst() {
        return boolConst;
    }

    public void setBoolConst(Integer boolConst) {
        this.boolConst=boolConst;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstBool(\n");

        buffer.append(" "+tab+boolConst);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstBool]");
        return buffer.toString();
    }
}
