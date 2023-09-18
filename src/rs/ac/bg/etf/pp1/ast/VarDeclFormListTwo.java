// generated with ast extension for cup
// version 0.8
// 26/7/2021 17:38:6


package rs.ac.bg.etf.pp1.ast;

public class VarDeclFormListTwo extends VarDeclFormList {

    private VarDeclForm VarDeclForm;

    public VarDeclFormListTwo (VarDeclForm VarDeclForm) {
        this.VarDeclForm=VarDeclForm;
        if(VarDeclForm!=null) VarDeclForm.setParent(this);
    }

    public VarDeclForm getVarDeclForm() {
        return VarDeclForm;
    }

    public void setVarDeclForm(VarDeclForm VarDeclForm) {
        this.VarDeclForm=VarDeclForm;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(VarDeclForm!=null) VarDeclForm.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarDeclForm!=null) VarDeclForm.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarDeclForm!=null) VarDeclForm.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDeclFormListTwo(\n");

        if(VarDeclForm!=null)
            buffer.append(VarDeclForm.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDeclFormListTwo]");
        return buffer.toString();
    }
}
