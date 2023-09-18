// generated with ast extension for cup
// version 0.8
// 26/7/2021 17:38:6


package rs.ac.bg.etf.pp1.ast;

public class VarDeclaration extends VarDecl {

    private Type Type;
    private VarDeclFormList VarDeclFormList;

    public VarDeclaration (Type Type, VarDeclFormList VarDeclFormList) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.VarDeclFormList=VarDeclFormList;
        if(VarDeclFormList!=null) VarDeclFormList.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public VarDeclFormList getVarDeclFormList() {
        return VarDeclFormList;
    }

    public void setVarDeclFormList(VarDeclFormList VarDeclFormList) {
        this.VarDeclFormList=VarDeclFormList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Type!=null) Type.accept(visitor);
        if(VarDeclFormList!=null) VarDeclFormList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(VarDeclFormList!=null) VarDeclFormList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(VarDeclFormList!=null) VarDeclFormList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDeclaration(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclFormList!=null)
            buffer.append(VarDeclFormList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDeclaration]");
        return buffer.toString();
    }
}
