// generated with ast extension for cup
// version 0.8
// 26/7/2021 17:38:6


package rs.ac.bg.etf.pp1.ast;

public class FormParsTwo extends FormPars {

    private FormDecl FormDecl;

    public FormParsTwo (FormDecl FormDecl) {
        this.FormDecl=FormDecl;
        if(FormDecl!=null) FormDecl.setParent(this);
    }

    public FormDecl getFormDecl() {
        return FormDecl;
    }

    public void setFormDecl(FormDecl FormDecl) {
        this.FormDecl=FormDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(FormDecl!=null) FormDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FormDecl!=null) FormDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FormDecl!=null) FormDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FormParsTwo(\n");

        if(FormDecl!=null)
            buffer.append(FormDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FormParsTwo]");
        return buffer.toString();
    }
}
