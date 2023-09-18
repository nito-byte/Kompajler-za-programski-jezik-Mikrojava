// generated with ast extension for cup
// version 0.8
// 26/7/2021 17:38:6


package rs.ac.bg.etf.pp1.ast;

public class ExprI extends Expr {

    private ExprIzraz ExprIzraz;

    public ExprI (ExprIzraz ExprIzraz) {
        this.ExprIzraz=ExprIzraz;
        if(ExprIzraz!=null) ExprIzraz.setParent(this);
    }

    public ExprIzraz getExprIzraz() {
        return ExprIzraz;
    }

    public void setExprIzraz(ExprIzraz ExprIzraz) {
        this.ExprIzraz=ExprIzraz;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ExprIzraz!=null) ExprIzraz.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ExprIzraz!=null) ExprIzraz.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ExprIzraz!=null) ExprIzraz.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ExprI(\n");

        if(ExprIzraz!=null)
            buffer.append(ExprIzraz.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ExprI]");
        return buffer.toString();
    }
}
