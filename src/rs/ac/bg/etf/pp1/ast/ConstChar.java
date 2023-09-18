// generated with ast extension for cup
// version 0.8



package rs.ac.bg.etf.pp1.ast;

public class ConstChar extends ConstValue {

    private Character znakovnaConst;

    public ConstChar (Character znakovnaConst) {
        this.znakovnaConst=znakovnaConst;
    }

    public Character getZnakovnaConst() {
        return znakovnaConst;
    }

    public void setZnakovnaConst(Character znakovnaConst) {
        this.znakovnaConst=znakovnaConst;
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
        buffer.append("ConstChar(\n");

        buffer.append(" "+tab+znakovnaConst);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstChar]");
        return buffer.toString();
    }
}
