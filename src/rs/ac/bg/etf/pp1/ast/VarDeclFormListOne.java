// generated with ast extension for cup
// version 0.8
// 26/7/2021 17:38:6


package rs.ac.bg.etf.pp1.ast;

public class VarDeclFormListOne extends VarDeclFormList {

    private VarDeclFormList VarDeclFormList;
    private VarDeclForm VarDeclForm;

    public VarDeclFormListOne (VarDeclFormList VarDeclFormList, VarDeclForm VarDeclForm) {
        this.VarDeclFormList=VarDeclFormList;
        if(VarDeclFormList!=null) VarDeclFormList.setParent(this);
        this.VarDeclForm=VarDeclForm;
        if(VarDeclForm!=null) VarDeclForm.setParent(this);
    }

    public VarDeclFormList getVarDeclFormList() {
        return VarDeclFormList;
    }

    public void setVarDeclFormList(VarDeclFormList VarDeclFormList) {
        this.VarDeclFormList=VarDeclFormList;
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
        if(VarDeclFormList!=null) VarDeclFormList.accept(visitor);
        if(VarDeclForm!=null) VarDeclForm.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarDeclFormList!=null) VarDeclFormList.traverseTopDown(visitor);
        if(VarDeclForm!=null) VarDeclForm.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarDeclFormList!=null) VarDeclFormList.traverseBottomUp(visitor);
        if(VarDeclForm!=null) VarDeclForm.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDeclFormListOne(\n");

        if(VarDeclFormList!=null)
            buffer.append(VarDeclFormList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclForm!=null)
            buffer.append(VarDeclForm.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDeclFormListOne]");
        return buffer.toString();
    }
}
