// generated with ast extension for cup
// version 0.8



package rs.ac.bg.etf.pp1.ast;

public class ConstDeclListOne extends ConstDeclList {

    private ConstDeclList ConstDeclList;
    private ConstDeclValue ConstDeclValue;

    public ConstDeclListOne (ConstDeclList ConstDeclList, ConstDeclValue ConstDeclValue) {
        this.ConstDeclList=ConstDeclList;
        if(ConstDeclList!=null) ConstDeclList.setParent(this);
        this.ConstDeclValue=ConstDeclValue;
        if(ConstDeclValue!=null) ConstDeclValue.setParent(this);
    }

    public ConstDeclList getConstDeclList() {
        return ConstDeclList;
    }

    public void setConstDeclList(ConstDeclList ConstDeclList) {
        this.ConstDeclList=ConstDeclList;
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
        if(ConstDeclList!=null) ConstDeclList.accept(visitor);
        if(ConstDeclValue!=null) ConstDeclValue.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstDeclList!=null) ConstDeclList.traverseTopDown(visitor);
        if(ConstDeclValue!=null) ConstDeclValue.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstDeclList!=null) ConstDeclList.traverseBottomUp(visitor);
        if(ConstDeclValue!=null) ConstDeclValue.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstDeclListOne(\n");

        if(ConstDeclList!=null)
            buffer.append(ConstDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConstDeclValue!=null)
            buffer.append(ConstDeclValue.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstDeclListOne]");
        return buffer.toString();
    }
}
