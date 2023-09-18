// generated with ast extension for cup
// version 0.8
// 26/7/2021 17:38:6


package rs.ac.bg.etf.pp1.ast;

public class FactorDesignTwo extends Factor {

    private Designator Designator;
    private DesignStmActPars DesignStmActPars;

    public FactorDesignTwo (Designator Designator, DesignStmActPars DesignStmActPars) {
        this.Designator=Designator;
        if(Designator!=null) Designator.setParent(this);
        this.DesignStmActPars=DesignStmActPars;
        if(DesignStmActPars!=null) DesignStmActPars.setParent(this);
    }

    public Designator getDesignator() {
        return Designator;
    }

    public void setDesignator(Designator Designator) {
        this.Designator=Designator;
    }

    public DesignStmActPars getDesignStmActPars() {
        return DesignStmActPars;
    }

    public void setDesignStmActPars(DesignStmActPars DesignStmActPars) {
        this.DesignStmActPars=DesignStmActPars;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Designator!=null) Designator.accept(visitor);
        if(DesignStmActPars!=null) DesignStmActPars.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Designator!=null) Designator.traverseTopDown(visitor);
        if(DesignStmActPars!=null) DesignStmActPars.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Designator!=null) Designator.traverseBottomUp(visitor);
        if(DesignStmActPars!=null) DesignStmActPars.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FactorDesignTwo(\n");

        if(Designator!=null)
            buffer.append(Designator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DesignStmActPars!=null)
            buffer.append(DesignStmActPars.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FactorDesignTwo]");
        return buffer.toString();
    }
}
