package rs.ac.bg.etf.pp1;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;

public class CodeGenerator extends VisitorAdaptor {
	
	
	public enum CONS {
		IZVAN_IF(0), UNUTAR_IF(1),;
		private final int value;

		CONS(final int newValue) {value = newValue;}
		public int getValue() {return value;}
	}

	PozicijaLista pozicijaLista = new PozicijaLista();

	{
		int vr = CONS.IZVAN_IF.getValue();
		pozicijaLista.ubaci(vr);	
	}
	
	List<Integer> listaZaAND = new LinkedList<Integer>();
	List<Integer> listaZaOR  = new LinkedList<Integer>();
	
	IFLista  ifLista = new IFLista();
	ElseLista elseLista = new ElseLista();
	
	//*****************************************************************//
	
	@Override
	public void visit(VarDeclFormItem VarDeclFormItem){
		
		Obj var = VarDeclFormItem.obj;
		if (var.getLevel() == 0)
			var.setAdr(Code.dataSize++);
	}
	
	@Override
	public void visit(VarDeclFormArray VarDeclFormArray){
		
		Obj var = VarDeclFormArray.obj;
		if (var.getLevel() == 0)
			var.setAdr(Code.dataSize++);
	}
	
	//*****************************************************************//

	@Override
	public void visit(StatementPrintOne StatementPrintOne) {
		
		int len = 1;
		if (StatementPrintOne.getExpr().struct == Tab2.intType || StatementPrintOne.getExpr().struct == Tab2.boolType)
			len = 5; 
		Code.loadConst(len);
		Code.put(StatementPrintOne.getExpr().struct == Tab2.charType ? Code.bprint : Code.print);
	}

	@Override
	public void visit(StatementPrintTwo StatementPrintTwo) {
		int len = StatementPrintTwo.getC2().intValue();
		Code.loadConst(len);
		Code.put(StatementPrintTwo.getExpr().struct == Tab.charType ? Code.bprint : Code.print);
	}

	@Override
	public void visit(StatementRead StatementRead) {
		
		Code.put(StatementRead.getDesignator().obj.getType() == Tab.charType ? Code.bread : Code.read);
		Code.store(StatementRead.getDesignator().obj);
	}
	
	//********************************************************************//

	@Override
	public void visit(FactorConstNum FactorConstNum) {
		
		int val = FactorConstNum.getC1().intValue();
		Code.loadConst(val);
	}

	@Override
	public void visit(FactorConstChar FactorConstChar) {
		
		int val = FactorConstChar.getC1().charValue();
		Code.loadConst(val);
	}

	@Override
	public void visit(FactorConstBool FactorConstBool) {
		
		int val = FactorConstBool.getC1().intValue();
		Code.loadConst(val);
	}

	@Override
	public void visit(FactTypeTwo FactTypeTwo) {
		
		Code.put(Code.newarray);
		int val = FactTypeTwo.getType().struct == Tab.charType ? 0 : 1;
		Code.put(val);
	}
		
	@Override
	public void visit(FactorParen FactorParen) {
	}
	
	
	@Override 
	public void visit(FactorDesignOne FactorDesignOne){
		
		Obj d = FactorDesignOne.getDesignator().obj;
		Code.load(d);
	}
	
	//********************************************************************//

	@Override
	public void visit(TermMulopFactor TermMulopFactor) {

		int opcode = TermMulopFactor.getMulop().obj.getKind();
		Code.put(opcode);
	}

	@Override
	public void visit(ExprAddopTerm ExprAddopTerm) {

		int opcode = ExprAddopTerm.getAddop().obj.getKind();
		Code.put(opcode);
	}

	@Override
	public void visit(NegExprTerm NegExprTerm) {

		Code.put(Code.neg);
	}
	
	//*****************************************************************//
	
	@Override
	public void visit (DesignArrayName DesignArrayName){

		Code.load(DesignArrayName.obj);	
	}

	@Override
	public void visit(DesignatorStatement_Assign DesignatorStatement_Assign) {

		Code.store(DesignatorStatement_Assign.getDesignator().obj);
	}
	
	@Override
	public void visit (DesignatorStatement_Dec DesignatorStatement_Dec){

		Obj d = DesignatorStatement_Dec.getDesignator().obj;
		if (d.getKind() == Obj.Elem) {
			Code.put(Code.dup2);
		}
		Code.load(d);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.store(d);
	}
	
	@Override
	public void visit(DesignatorStatement_Inc DesignatorStatement_Inc){
		Obj d = DesignatorStatement_Inc.getDesignator().obj;
		if (d.getKind() == Obj.Elem) {
			Code.put(Code.dup2);
		}
		Code.load(d);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(d);
	}
	
	
	//*******************************************************************//
	//Metod
	
	@Override
	public void visit (MethDeclNoPars MethDeclNoPars){

		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	@Override
	public void visit(MethDeclWithPars MethDeclWithPars){

		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	
	//*******************************************************************//
	

	@Override
	public void visit(MethodTypeAndName MethodTypeAndName) {
		MethodTypeAndName.obj.setAdr(Code.pc);
		if (MethodTypeAndName.obj.getName().equals("main")) {
			Code.mainPc = Code.pc;
		}

		Code.put(Code.enter);
		int fp = 0;
		for (Obj o : MethodTypeAndName.obj.getLocalSymbols()) {
			if (o.getFpPos() > 0)
				fp++;
		}
		int lv = MethodTypeAndName.obj.getLocalSymbols().size();
		Code.put(fp);
		Code.put(lv);
	}



	@Override
	public void visit(FactorDesignTwo FactorDesignTwo) {
		int offset = FactorDesignTwo.getDesignator().obj.getAdr() - Code.pc;
		Code.put(Code.call);
		Code.put2(offset);
	}

	@Override
	public void visit(DesignatorStatement_Func DesignatorStatement_Func) {
		int offset = DesignatorStatement_Func.getDesignator().obj.getAdr() - Code.pc;
		Code.put(Code.call);
		Code.put2(offset);
		if (DesignatorStatement_Func.getDesignator().obj.getType() != Tab.noType) {
			Code.put(Code.pop);
		}

	}
	
	//**********************************************************//

	

	public void visit(If i) {
		int vr = CONS.UNUTAR_IF.getValue();
		pozicijaLista.ubaci(vr);
		
	}

	public void visit(Else els) {
		int opCode = Code.jmp;
		Code.put(opCode);
		Code.put2(0);
		
		elseLista.ubaci(Code.pc - 2);
		Code.fixup(ifLista.skini());
	}

	public void visit(UnmatchedIf unmatchedIf) {
		int vr = ifLista.skini();
		Code.fixup(vr);
	}

	public void visit(UnmatchedElse unmatchedElse) {
		int vr = elseLista.skini();
		Code.fixup(vr);
	}

	public void visit(StatementIf stmIf) {
		int vr = elseLista.skini();
		Code.fixup(vr);
	}

	public void visit(ConditionUslov condTernary) {

		int vr = CONS.UNUTAR_IF.getValue();
		int pozicija = pozicijaLista.topElement();
		if (pozicija == vr) {
			Code.put(Code.jmp);
			Code.put2(0);
			ifLista.ubaci(Code.pc-2);

			Iterator<Integer> iterator = listaZaOR.iterator();
			while (iterator.hasNext()) {
				Code.fixup(iterator.next()); //then
			}
		}

		listaZaOR.clear();
		//int vr = CONS.UNUTAR_IF.getValue();
		pozicija = pozicijaLista.topElement();
		if (pozicija == vr)pozicijaLista.skini();
	}

	

	
	public void visit(ConditionOneOR ConditionOneOR) {
		int opCode = Code.jmp;
		Code.put(opCode);
		Code.put2(0); //  na then
		
		int tekuciPC = Code.pc;
		int adr = tekuciPC - 2;
		listaZaOR.add(adr);
		
		Iterator<Integer> iterator = listaZaAND.iterator();
		while (iterator.hasNext()) {
			Code.fixup(iterator.next());
		}
		
		listaZaAND.clear();
	}

	public void visit(ConditionTwoOR ConditionTwoOR) {
		int opCode = Code.jmp;
		Code.put(opCode);
		Code.put2(0); // skakanje na then
		
		
		int tekuciPC = Code.pc;
		int adr = tekuciPC - 2;
		listaZaOR.add(adr);
		
		Iterator<Integer> iterator = listaZaAND.iterator();
		while (iterator.hasNext()) {
			Code.fixup(iterator.next());
		}
		
		listaZaAND.clear();
	}


	

	public void visit(CondTermOneAND CondTermOneAND) {
		int trenutniPC = Code.pc;
		int adresa = trenutniPC - 2;
		listaZaAND.add(adresa);
	}
	
	public void visit(CondTermTwoAND CondTermTwoAND) {
		int trenutniPC = Code.pc;
		int adresa = trenutniPC - 2;
		listaZaAND.add(adresa);
	}
	
	public void visit(CondFactTwoExpr CondFactTwoExpr) {
		postaviRelop(CondFactTwoExpr.getRelop());

	}

	public void postaviRelop(SyntaxNode node) {
		String methodIndex = "";
		methodIndex += (node instanceof RelopEqEq) ? "1" : "0";
		methodIndex += (node instanceof RelopNotEq) ? "1" : "0";
		methodIndex += (node instanceof RelopLsEq) ? "1" : "0";
		methodIndex += (node instanceof RelopLs) ? "1" : "0";
		methodIndex += (node instanceof RelopGr) ? "1" : "0";
		methodIndex += (node instanceof RelopGrEq) ? "1" : "0";

		switch (methodIndex) {
		case "100000":
			Code.putFalseJump(Code.eq, 0);
			break;
		case "010000":
			Code.putFalseJump(Code.ne, 0);
			break;
		case "001000":
			Code.putFalseJump(Code.le, 0);
			break;
		case "000100":
			Code.putFalseJump(Code.lt, 0);
			break;
		case "000010":
			Code.putFalseJump(Code.gt, 0);
			break;
		case "000001":
			Code.putFalseJump(Code.ge, 0);
			break;
		default:
			System.out.println("No action defined");
		}
	}

	public void visit(CondFactOneExpr CondFactOneExpr) {
		int konstanta = Code.const_1;
		int opCodeEqual = Code.eq;
		Code.put(konstanta);
		Code.putFalseJump(opCodeEqual, 0);
	}

	
	
	
	

}
