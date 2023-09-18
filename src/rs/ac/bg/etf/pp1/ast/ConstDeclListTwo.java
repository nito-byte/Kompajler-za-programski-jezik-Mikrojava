// generated with ast extension for cup
// version 0.8



package rs.ac.bg.etf.pp1.ast;

public class ConstDeclListTwo extends ConstDeclList {

    private ConstDeclValue ConstDeclValue;

    public ConstDeclListTwo (ConstDeclValue ConstDeclValue) {
        this.ConstDeclValue=ConstDeclValue;
        if(ConstDeclValue!=null) ConstDeclValue.setParent(this);
    }

    public ConstDeclValue getConstDeclValue() {
        return ConstDeclValue;
    }

    public void setConstDeclValue(ConstDeclValue ConstDeclValue) {
        this.ConstDeclValue=ConstDeclValue;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConstDeclValue!=null) ConstDeclValue.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstDeclValue!=null) ConstDeclValue.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstDeclValue!=null) ConstDeclValue.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstDeclListTwo(\n");

        if(ConstDeclValue!=null)
            buffer.append(ConstDeclValue.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstDeclListTwo]");
        return buffer.toString();
    }
}
