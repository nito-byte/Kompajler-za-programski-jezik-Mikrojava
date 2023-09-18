// generated with ast extension for cup
// version 0.8



package rs.ac.bg.etf.pp1.ast;

public class ConstNumber extends ConstValue {

    private Integer numerickaConst;

    public ConstNumber (Integer numerickaConst) {
        this.numerickaConst=numerickaConst;
    }

    public Integer getNumerickaConst() {
        return numerickaConst;
    }

    public void setNumerickaConst(Integer numerickaConst) {
        this.numerickaConst=numerickaConst;
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
        buffer.append("ConstNumber(\n");

        buffer.append(" "+tab+numerickaConst);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstNumber]");
        return buffer.toString();
    }
}
