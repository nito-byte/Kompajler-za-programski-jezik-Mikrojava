package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class SemanticCheck extends VisitorAdaptor {

	
	Struct currentType = null;
	Obj currentMethod = null;
	boolean returnFound = false;
	boolean errorDetected = false;
	int nVars;
	
	
	int constDeclarationCount = 0;
	int varDeclarationCount = 0;
	int locDeclarationCount = 0;
	
	Logger log = Logger.getLogger(getClass());
	public List<String> listaSemantickihGresaka = new ArrayList<String>();
	
	public List<String> getListaSemantickihGresaka() {
		return listaSemantickihGresaka;
	}
	
	public void setListaSemantickihGresaka(List<String> listaSemantickihGresaka) {
		this.listaSemantickihGresaka = listaSemantickihGresaka;
	}
	//********************************************************************//
	
	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0)
			msg.append(" na liniji ").append(line);
		log.error(msg.toString());
		
		//ovde cu sada da ubacim logovanje errora u moj niz
		listaSemantickihGresaka.add(msg.toString());
	}


	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0)
			msg.append(" na liniji ").append(line);
		log.info(msg.toString());
	}

	public boolean passed(){
		return !errorDetected;
	}
	
// ***********************************************************************************//
	
	
	@Override
	public void visit(ProgramName programName) {
		// kreiramo inicijalni opseg sa tipovima
		// kreiramo objektni cvor
		// necemo otvarati novi scope jer je on vec otvoren
		String name = programName.getName();
		Obj pom;

		pom = Tab2.insert(Obj.Prog, name, Tab2.noType);
		programName.obj = pom;

		Tab2.openScope();
	}

	@Override
	public void visit(Program program) {
		nVars = Tab.currentScope.getnVars();
		// treba na kraju da ulancam u onaj obj cvor
		// treba da zatvorim scope
		Obj objektniCvor = program.getProgramName().obj;
		Tab2.chainLocalSymbols(objektniCvor);
		Tab2.closeScope();
	}

	// ***************************************************************************************//

	@Override
	public void visit(TypeIdent TypeIdent) {
		// treba da dohvatim naziv tipa
		// treba da proverim da li taj tip vec postoji u init opsegu
		// i onda treba da proverim ako postoji da li je to stvarno tip
		currentType = Tab2.noType; // da bi pregazili prethodnu vrednost
		String typeName = TypeIdent.getI1();
		Obj typeObj = Tab2.find(typeName);
		if (typeObj == Tab2.noObj) {
			report_error("Greska: nije definisa tip tog naziva -> " + typeName, TypeIdent);
		} else {
			if (typeObj.getKind() != Obj.Type) {
				report_error("Greska: " + typeName + " nije tip!", TypeIdent);
			} else {
				currentType = typeObj.getType();
			}
		}
		TypeIdent.struct = currentType;
		// int x;
		// alksdlaksd x;
		// main x;
	}

	// ***************************************************************************************//

	@Override
	public void visit(VarDeclFormItem VarDeclFormItem) {
		// provera da li je vec u scope-u
		// ako nije dodajem, ako jeste error
		VarDeclFormItem.obj = Tab.noObj;
		String name = VarDeclFormItem.getI1();
		if (Tab2.currentScope().findSymbol(name) != null) {
			report_error("Greska: vec je deklarisana promenljiva " + name, VarDeclFormItem);
		} else {
			VarDeclFormItem.obj = Tab2.insert(Obj.Var, name, currentType);
		}
		
		if(VarDeclFormItem.obj.getLevel() ==0)
		varDeclarationCount++;
		else
			locDeclarationCount++;
	}

	@Override
	public void visit(VarDeclFormArray VarDeclFormArray) {
		// proverim da li je deklarisana
		// ako jeste greska, ako nije insert u TS

		VarDeclFormArray.obj = Tab.noObj;
		String name = VarDeclFormArray.getI1();
		if (Tab2.currentScope().findSymbol(name) != null) {
			report_error("Greska: " + name + " je vec definisano", VarDeclFormArray);
		} else {
			VarDeclFormArray.obj = Tab2.insert(Obj.Var, name, new Struct(Struct.Array, currentType));
		}
		
		if(VarDeclFormArray.obj.getLevel() ==0)
			varDeclarationCount++;
		else
			locDeclarationCount++;
		// int a, b, c[], d[];
		// ...
		// a = b;
		// a = c; !
		// c = d; !
	}

	// ***********************************************************************************//

	@Override
	public void visit(ConstNumber ConstNumber) {
		// treba da dohvatim vrednost konstante i da je ubacim u
		// objektni cvor na tom nivou, pa cu vrednost preneti u cvor navise
		int adr = ConstNumber.getNumerickaConst();
		ConstNumber.obj = new Obj(Obj.NO_VALUE, "", Tab2.intType);
		ConstNumber.obj.setAdr(adr);
		ConstNumber.obj.setLevel(0);
	}

	@Override
	public void visit(ConstChar ConstChar) {
		// dohvatim vrednost konstante
		// kreiram novi obj cvor i tu sacuvam vrednost koju cu prenositi
		int adr = ConstChar.getZnakovnaConst();
		ConstChar.obj = new Obj(Obj.NO_VALUE, "", Tab2.charType);
		ConstChar.obj.setAdr(adr);
		ConstChar.obj.setLevel(0);
	}

	@Override
	public void visit(ConstBool ConstBool) {
		// treba da sacuvam bool vrednost u obj ispod, a onda da ga
		// prenesem u obj jedan nivo iznad
		int adr = ConstBool.getBoolConst();
		ConstBool.obj = new Obj(Obj.NO_VALUE, "", Tab2.boolType);
		ConstBool.obj.setAdr(adr);
		ConstBool.obj.setLevel(0);
	}

	@Override
	public void visit(ConstDeclValue ConstDeclValue) {
		// proverim da li se konstanta vec nalazi u TS i ako da greska, ako
		// ne provera tipova da li su ekvivalentni na kraju sacuvam u .obj
		String name = ConstDeclValue.getI1();
		int adr = ConstDeclValue.getConstValue().obj.getAdr();

		if (ConstDeclValue.getConstValue().obj.getType() != currentType) {
			report_error("Greska: Vrednost konstante " + name + " nije dobrog tipa", ConstDeclValue);
		} 

		if (Tab2.currentScope().findSymbol(name) != null) {
			report_error("Greska: konstanta " + name + " je vec definisana!", ConstDeclValue);
		} else {
			Obj o = Tab2.insert(Obj.Con, name, currentType);
			o.setAdr(adr);
		}
	}

	@Override
	public void visit(ConstDeclaration ConstDeclaration) {
		constDeclarationCount++;
	}
	
	// const int c = 'a';
	
	// ***********************************************************************************//

	@Override
	public void visit(MethodTypeAndName MethodTypeAndName) {
		String name = MethodTypeAndName.getImeMetode();
		if (Tab2.find(name) != Tab2.noObj) {
			report_error("Greska: Vec definisano ime -> " + name, MethodTypeAndName);
		} else {
			MethodTypeAndName.obj = currentMethod = Tab2.insert(Obj.Meth, name, currentType);
			Tab2.openScope();
		}
		returnFound = false;
	}
		
	@Override
	public void visit(MethodTypeVOID MethodTypeVOID) {
		//moram da postavim currentType, jer se tu ne obidje Type
		currentType = Tab2.noType;
	}
	
	
	//moramo kad izadjemo iz metode da uzmemo i da ulancamo sve lok. promenljive
	@Override
	public void visit(MethDeclWithPars MethDeclWithPars) {
		Tab2.chainLocalSymbols(currentMethod);
		Tab2.closeScope();
		if (currentMethod.getType() != Tab.noType) {
			if (!returnFound) {
				report_error("Greska: Metodi nedostaje return iskaz", MethDeclWithPars);
			}
		}
	}

	@Override
	public void visit(MethDeclNoPars MethDeclNoPars) {
		//ulancam lokalne simbole na  obj cvor metode
		//zatvorim scope
		//proverim da li postoji naredba return
		Tab2.chainLocalSymbols(currentMethod);
		Tab2.closeScope();
		if (currentMethod.getType() != Tab.noType) {
			if (!returnFound) {
				report_error("Greska: Metodi nedostaje return iskaz", MethDeclNoPars);
			}
		}
	}
	
	// ***********************************************************************************//
	
	@Override
	public void visit(FormParsItem FormParsItem) {
		//dohvatim naziv formalnog parametra proverim da li vec postoji u 
		//trenutnom scope-u, ako ne postoji onda ga dodajem u TS
		String name = FormParsItem.getI2();
		FormParsItem.obj = Tab.noObj;
		if (Tab2.currentScope().findSymbol(name) != null) {
			report_error("Greska: Vec je definisano ime -> " + name, FormParsItem);
		}
		else {
			FormParsItem.obj = Tab2.insert(Obj.Var, name, currentType);
		}
	}
	
	
	@Override
	public void visit(FormParsArray FormParsArray) {
		String name = FormParsArray.getI2();
		FormParsArray.obj = Tab.noObj;
		if (Tab2.currentScope().findSymbol(name) != null) {
			report_error("Greska: Vec je definisano ime -> " + name, FormParsArray);
		}
		else {
			FormParsArray.obj = Tab2.insert(Obj.Var, name, new Struct(Struct.Array, currentType));
		}
	}

	
	// ***********************************************************************************//
	
	@Override
	public void visit(ActParsFirst ActParsFirst) {
		//ovde ce se nalaziti prvi element u listi. Ono sto je jako bitno je da ce stablo 
		//da se gradi od vrha ka dnu, ali da ce da se obilazni od dna ka vrhu. Znaci 
		//poslednji dodati element ce biti prvi element i on ce da se nalazi skroz na dnu.
		//
		ActParsFirst.list = new ArrayList<Struct>();
		ActParsFirst.list.add(ActParsFirst.getExpr().struct);
	}
	
	//Sada treba da uzmem i da dodam ostatak, sve ostale expr u listu.
	//Najpre treba da uzmem i da preuzmem listu koju sam napravila za prvi. 
	@Override
	public void visit(ActParsUmpth ActParsUmpth) {
		ActParsUmpth.list = ActParsUmpth.getActPars().list;
		ActParsUmpth.list.add(ActParsUmpth.getExpr().struct);
	}
	
	//ovde nece biti nikakvih parametara, zato treba da je tu prazna lista
	@Override
	public void visit(DesignStmActParsNone DesignStmActParsNone) {
		DesignStmActParsNone.list = new ArrayList<Struct>();
	}
	
	@Override
	public void visit(DesignStmActParsSome DesignStmActParsSome) {
		DesignStmActParsSome.list = DesignStmActParsSome.getActPars().list;
	}
	
	//************************************************************************//
	//OVAJ DEO PROVERIIIIIIIII!!!!!!!!!!!!!!!!!!!!!!!!!!!
	
	@Override
	public void visit(FactorDesignTwo FactorDesignTwo) {
		FactorDesignTwo.struct = FactorDesignTwo.getDesignator().obj.getType();
		
		Obj designator = FactorDesignTwo.getDesignator().obj;
		if (designator.getKind() != Obj.Meth) {
			report_error("Greska"+designator.getName() + " nije metod", FactorDesignTwo);
		} else {
			List<Struct> actPars = FactorDesignTwo.getDesignStmActPars().list;
			
			for(Obj o: designator.getLocalSymbols()) {
				if (o.getFpPos() > 0) {
					if (actPars.isEmpty()) {
						report_error("Greska: PRemalp", FactorDesignTwo);
						break;
					}
					if (!o.getType().equals(actPars.remove(0))) {
						report_error("Greska: u " + o.getFpPos() + ". parametru, ne slazu se tipovi", FactorDesignTwo);
					}
					//report_info("Poziv funkcije koja ima parametar " + o.getName(), DesignatorStatement_Func);
				}
			}
			if (!actPars.isEmpty()) {
				report_error("Greska: PRevise", FactorDesignTwo);
			}
		}
	}
	
	@Override
	public void visit(DesignatorStatement_Func DesignatorStatement_Func) {
		Obj designator = DesignatorStatement_Func.getDesignator().obj;
		if (designator.getKind() != Obj.Meth) {
			report_error("Greska: "+designator.getName() + " nije metod", DesignatorStatement_Func);
		} else {
			List<Struct> actPars = DesignatorStatement_Func.getDesignStmActPars().list;
			
			for(Obj o: designator.getLocalSymbols()) {
				if (o.getFpPos() > 0) {
					if (actPars.isEmpty()) {
						report_error("Greska: Premalp", DesignatorStatement_Func);
						break;
					}
					if (!o.getType().equals(actPars.remove(0))) {
						report_error("Greska: u " + o.getFpPos() + ". parametru, ne slazu se tipovi", DesignatorStatement_Func);
					}
					//report_info("Poziv funkcije koja ima parametar " + o.getName(), DesignatorStatement_Func);
				}
			}
			if (!actPars.isEmpty()) {
				report_error("Greska: Previse", DesignatorStatement_Func);
			}
		}
	}
	
	

	@Override
	public void visit(FactorParen FactorParen) {
		FactorParen.struct = FactorParen.getExpr().struct;
	}

	@Override
	public void visit(FormParsTwo FormParsTwo) {
		FormParsTwo.obj = FormParsTwo.getFormDecl().obj;
		FormParsTwo.getFormDecl().obj.setFpPos(1);
	}

	@Override
	public void visit(FormParsOne FormParsOne) {
		int fpPos = FormParsOne.getFormPars().obj.getFpPos();
		FormParsOne.obj = FormParsOne.getFormDecl().obj;
		FormParsOne.obj.setFpPos(fpPos+1);
	}
	
	
	// ***********************************************************************************//
	// PROVERE


	

	@Override
	public void visit(DesignIdentifikator DesignIdentifikator) {
		String name = DesignIdentifikator.getI1();
		DesignIdentifikator.obj = Tab.find(name);
		if (DesignIdentifikator.obj == Tab.noObj) {
			report_error("Greska: promenljiva "+name+" nije definisana!", DesignIdentifikator);
		}
	}


	@Override
	public void visit(DesignArrayName DesignArrayName) {
		String name = DesignArrayName.getI1();
		DesignArrayName.obj = Tab.find(name);
		if (DesignArrayName.obj == Tab.noObj) {
			//report_error("Nije definisano " + name, DesignatorArrayNameDerived1);
			report_error("Greska: promenljiva " + name + " tipa niz nije dekarisana, ", DesignArrayName);
		}
	}
		
	
	//DesignatorStatement = Designator Assignop Expr ";"
	//- Designator mora oznacavati promenljivu, element niza ili polje unutar objekta.
	//- Tip neterminala Expr mora biti kompatibilan pri dodeli sa tipom neterminala Designator.
	@Override
	public void visit(DesignatorStatement_Assign DesignatorStatement_Assign) {
		Obj designator = DesignatorStatement_Assign.getDesignator().obj;
		Struct expr = DesignatorStatement_Assign.getExpr().struct;
		if (designator.getKind() != Obj.Var && designator.getKind() != Obj.Elem) {
			report_error("Greska: " + designator.getName() + " nije ni var ni elem niza!", DesignatorStatement_Assign);
		} else {
			
			if (!expr.compatibleWith(designator.getType())) {
				//report_info("Tip prvi  je: "+expr.toString(), DesignatorStatement_Assign);
				//report_info("Tip drugi je: "+designator.getType().toString(), DesignatorStatement_Assign);
				report_error("Greska: u assign naredbi nekompatibilni tipovi", DesignatorStatement_Assign);
			}			
		}	
	}
	

	//Designator = Designator "[" Expr "]".
	//- Tip neterminala Designator mora biti niz.
	//- Tip neterminala Expr mora biti int.
	@Override
	public void visit(DesignNiz DesignNiz){
		DesignNiz.obj = Tab.noObj;
		
		Obj designaor = DesignNiz.getDesignatorArrayName().obj;
		Struct expr = DesignNiz.getExpr().struct;
		
		if (designaor.getType().getKind() != Struct.Array) {
			report_error("Greska: promenljiva nije tipa niz, ", DesignNiz);
		} else {
			if (expr != Tab.intType) {
				report_error("Greska: indeks niza nije int tipa!", DesignNiz);
			} else {
				DesignNiz.obj = new Obj(Obj.Elem, designaor.getName(), designaor.getType().getElemType());
			}
		}
	}
	
	
	//DesignatorStatement = Designator ("++" | "--") ":"
	//- Designator mora oznacavati promenljivu, element niza ili polje objekta unutrasnje klase.
	//- Designator mora biti tipa int.
	//  pr: a++, a--, a[1]++, a[1]--; npr
	@Override
	public void visit(DesignatorStatement_Inc DesignatorStatement_Inc) {
		Obj designator = DesignatorStatement_Inc.getDesignator().obj;
		if (designator.getKind() != Obj.Var && designator.getKind() != Obj.Elem) {
			report_error("Greska: kod inkrementiranja " + designator.getName() + " nije ni promenljiva ni element niza!",
					DesignatorStatement_Inc);
		}
		if (designator.getType() != Tab.intType) {
			report_error("Greska: kod inkrementiranja " + designator.getName() + " nije promenljiva ni element niza tipa int!",
					DesignatorStatement_Inc);
		}
	}
	
	
	@Override
	public void visit(DesignatorStatement_Dec DesignatorStatement_Dec) {
		Obj designator = DesignatorStatement_Dec.getDesignator().obj;
		if (designator.getKind() != Obj.Var && designator.getKind() != Obj.Elem) {
			report_error("Greska: kod dekrementiranja " + designator.getName() + " nije ni var ni elem niza!",
					DesignatorStatement_Dec);
		}
		if (designator.getType() != Tab.intType) {
			report_error("Greska: kod dekrementiranja " + designator.getName() + " nije tipa int!",
					DesignatorStatement_Dec);
		}
	}

	@Override
	public void visit(DesignEnum DesignEnum) {
		// uradi jednom (nakon sto dodas ENUM-e)
	}

	// ***********************************************************************************//
	

	 // Expr mora biti tipa int, char ili bool.
	@Override
	public void visit(StatementPrintTwo StatementPrintTwo) {
		Struct expr = StatementPrintTwo.getExpr().struct;
		if (expr != Tab.intType && expr != Tab.charType && expr != Tab2.boolType) {
			report_error("Greska: nedozvoljen tip izraza u print naredbi!", StatementPrintTwo);
		}
	}

	// Expr mora biti tipa int, char ili bool.
	@Override
	public void visit(StatementPrintOne StatementPrintOne) {
		Struct expr = StatementPrintOne.getExpr().struct;
		if (expr != Tab.intType && expr != Tab.charType && expr != Tab2.boolType) {
			report_error("Greska: nedozvoljen tip izraza u print naredbi!", StatementPrintOne);
		}
	}

	//Statement = "read" "(" Designator ")" ";".
	// -Designator mora oznacavati promenljivu, element niza ili polje unutar objekta.
	// -Designator mora biti tipa int, char ili bool.
	@Override
	public void visit(StatementRead StatementRead) {
		Obj designator = StatementRead.getDesignator().obj;
		if (designator.getKind() != Obj.Var && designator.getKind() != Obj.Elem) {
			report_error("Greska u naredbi read: nije promenljiva ni elem niza!", StatementRead);
		} else {
			if (designator.getType() != Tab.intType && designator.getType() != Tab.charType && designator.getType() != Tab2.boolType) {
				report_error("Greska u naredbi read, tip promenljive nije int/char/bool!", StatementRead);
			}
		}
	}

	//Statement = "return" [Expr] .
	// - Tip neterminala Expr mora biti ekvivalentan povratnom tipu tekuce metode/ globalne funkcije.
	// - Ako neterminal Expr nedostaje, tekuca metoda mora biti deklarisana kao void.
	// - Ne sme postojati izvan tela (statickih) metoda, odnosno globalnih funkcija.
	@Override
	public void visit(StatementRetTwo StatementRetTwo) {
		//u currentType je ili noType ili neki tip
		if (!currentMethod.getType().equals(Tab.noType)) {
			report_error("Greska: Los povratni tip u naredbi return!", StatementRetTwo);
		}
		returnFound = true;
	}
	
	
	@Override
	public void visit(StatementRetOne StatementRetOne) {
		Struct expr = StatementRetOne.getExpr().struct;
		if (!currentMethod.getType().equals(expr)) {
			report_error("Greska: Los povratni tip u naredbi return!", StatementRetOne);
		}
		returnFound = true;
	}
	
	
	// ***********************************************************************************//
	// factor-i 
	
	//moram imati informaciju o tome kog su tipa, jer je Expr->Term->Fact
	//pa posle kad budem proveravala tipove u Expr-u ce mi trebati to
	@Override
	public void visit(FactorConstNum FactorConstNum) {
		FactorConstNum.struct = Tab.intType;
	}
	
	@Override
	public void visit(FactorConstChar FactorConstChar) {
		FactorConstChar.struct = Tab2.charType;
	}
	
	@Override
	public void visit(FactorConstBool FactorConstBool) {
		FactorConstBool.struct = Tab2.boolType;
	}
	
	@Override
	public void visit(FactorDesignOne FactorDesignOne) {
		//ovde se takodje samo pamti tip, jer je ovo samo koriscenje neke promenljive
		//u ovom trenutku ja ne pravim nikakav objektni cvor. Ne moram ni da radim
		//find jer sam sigurna da postoji vec ovo polje u ts, i u njegovom  cvoru 
		//smo u obj zapamtili njegov objekat
		FactorDesignOne.struct = FactorDesignOne.getDesignator().obj.getType();
	}

	
	
	//Factor = "new" Type "[" Expr "]".
    //- Tip neterminala Expr mora biti int.
	@Override
	public void visit(FactTypeTwo FactTypeTwo) {
		Struct expr = FactTypeTwo.getExpr().struct;
		if (expr != Tab.intType) {
			//report_error("Indeks niza mora biti int", FactTypeTwo);
			report_error("Greska: izraz mora biti tipa int!", FactTypeTwo);
		}
		FactTypeTwo.struct = new Struct(Struct.Array, currentType);
	}


	//---------------------------------------------------------------------//
	// termov-i
	

	@Override
	public void visit(TermFactor TermFactor) {
		//ovde moram da postavim u termov struct kog je tipa ovaj term, jer ce se on koristiti
		//dalje u Expr kada se tamo budu poredili tipovi
		TermFactor.struct = TermFactor.getFactor().struct;
	}

	//Term = Term Mulop Factor.
    // - Term i Factor moraju biti tipa int.
	@Override
	public void visit(TermMulopFactor TermMulopFactor) {
		//moram da dohvatim tip Terma i tip Factora, a zatim treba da proverim da li su oba int
		Struct term = TermMulopFactor.getTerm().struct;
		Struct fact = TermMulopFactor.getFactor().struct;
		if (term != Tab.intType) {
			report_error("Greska: levi operand nije int tipa", TermMulopFactor);
		}
		if (fact != Tab.intType) {
			report_error("Greska: desni operand nije int tipa!", TermMulopFactor);
		}
		TermMulopFactor.struct = Tab.intType;
	}

	
	
	//---------------------------------------------------------------------//
	// Expr
	
	
	
	
	@Override
	public void visit(ExprTerm ExprTerm) {
		ExprTerm.struct = ExprTerm.getTerm().struct;
	}
	
	//----------------------------------------------//
	//Expr = "-" Term.  ===> Term mora biti tipa int//
	//----------------------------------------------//
	@Override
	public void visit(NegExprTerm NegExprTerm) {      
		Struct term = NegExprTerm.getTerm().struct;
		if(term != Tab2.intType){
			report_error("Greska: negativni izraz nije tipa int!", NegExprTerm);
		}
		NegExprTerm.struct = NegExprTerm.getTerm().struct;
	}  
	
	@Override
	public void visit(ExprI ExprI) {
		ExprI.struct = ExprI.getExprIzraz().struct;
	}

	//----------------------------------------------------------------//
	//Expr = Expr Addop Term.  ===> Expr i Term moraju biti tipa int. //
	//U svakom slucaju, tipovi za Expr i Term moraju biti komatibilni.//
	//----------------------------------------------------------------//	
	@Override
	public void visit(ExprAddopTerm ExprAddopTerm) {
		Struct expr = ExprAddopTerm.getExprIzraz().struct;
		Struct term = ExprAddopTerm.getTerm().struct;
		if (expr != Tab.intType) {
			report_error("Greska: levi operand izraza nije tipa int!", ExprAddopTerm);
		}
		if (term != Tab.intType) {
			report_error("Greska: desni operand izraza nije tipa int!", ExprAddopTerm);
		}
		//if (!levi.compatibleWith(desni)) {
		//	report_error("Greska: Nekompatibilni tipovi", ExprAddopTerm);
		//}
		ExprAddopTerm.struct = Tab.intType;
	}

	
	
	
	//---------------------------------------------------------------------//
	// Cond

	@Override
	public void visit(ConditionUslov ConditionUslov) {
		Condition cond = ConditionUslov.getCondition();
		Struct pom = cond.struct;
		ConditionUslov.struct = pom;
		if (pom != Tab2.boolType) {
			report_error("GRESKA: Neispravno napisan uslov!", ConditionUslov);
		}
	}

	@Override
	public void visit(ConditionTwoOR ConditionTwoOR) {
		Condition condition = ConditionTwoOR.getCondition();
		CondTerm condTerm = ConditionTwoOR.getCondTerm();
		Struct cond = condition.struct;
		Struct term = condTerm.struct;

		if (cond != Tab2.boolType) {
			report_error("Greska: u napisanom uslovu, los tip (cond)!", ConditionTwoOR);
		} else if (term != Tab2.boolType) {
			report_error("Greska: u napisanom uslovu , los tip (term)!", ConditionTwoOR);
		} else {
			ConditionTwoOR.struct = Tab2.boolType;
		}
	}
	
	@Override
	public void visit(ConditionOneOR ConditionOneOR) {
		CondTerm condTerm = ConditionOneOR.getCondTerm();
		Struct pom = condTerm.struct;
		ConditionOneOR.struct = pom;
	}
	
	@Override
	public void visit(CondTermTwoAND CondTermTwoAND) {
		//moram da proverim da li su bool tipa oba
		CondTerm condT = CondTermTwoAND.getCondTerm();
		CondFact condF = CondTermTwoAND.getCondFact();
		Struct condTerm = condT.struct;
		Struct condFact = condF.struct;
		if (condTerm != Tab2.boolType) {
			report_error("Greska: u napisanom uslovu, los tip (term)!", CondTermTwoAND);
		} else if (condFact != Tab2.boolType) {
			report_error("Greska: u napisanom uslovu , los tip (fact)!", CondTermTwoAND);
		} else {
			CondTermTwoAND.struct = Tab2.boolType;
		}
	}

	@Override
	public void visit(CondTermOneAND CondTermOneAND) {
		//dohvaticu tip i Factora
		CondFact condFact = CondTermOneAND.getCondFact();
		Struct pom = condFact.struct;
		CondTermOneAND.struct = pom;
	}

	@Override
	public void visit(CondFactTwoExpr CondFactTwoExpr) {
		//moram da proverim da li su tipovi kompatibilni
		Expr e1 = CondFactTwoExpr.getExpr();
		Expr e2 = CondFactTwoExpr.getExpr1();
		Struct expr1 = e1.struct;
		Struct expr2 = e2.struct;
		
		if (expr1.compatibleWith(expr2)) {
			CondFactTwoExpr.struct = Tab2.boolType;
		}else {
			CondFactTwoExpr.struct = Tab.noType;
			report_error("Greska: nekompatibilnost tipova relacionog operatora!", CondFactTwoExpr);
			
		}
	}

	@Override
	public void visit(CondFactOneExpr CondFactOneExpr) {
		//ovaj izraz mora biti samo tipa bool jer se zove u if-u, ne int
		Expr expr = CondFactOneExpr.getExpr();
		Struct pom = expr.struct;
		CondFactOneExpr.struct = pom;
		if (pom != Tab2.boolType)
			report_error("Greska: napisani uslov nije tipa boolean!", CondFactOneExpr);
	}

	
	// ***********************************************************************************//	
	
	@Override
	public void visit(MulopMod MulopMod) {
		MulopMod.obj = new Obj(Code.rem, "", null); // kind = Code.rem (opcode)
	}

	@Override
	public void visit(MulopDiv MulopDiv) {
		MulopDiv.obj = new Obj(Code.div, "", null);
	}

	@Override
	public void visit(MulopMul MulopMul) {
		MulopMul.obj = new Obj(Code.mul, "", null);
	}

	@Override
	public void visit(AddopMinus AddopMinus) {
		AddopMinus.obj = new Obj(Code.sub, "", null);

	}

	@Override
	public void visit(AddopPlus AddopPlus) {
		AddopPlus.obj = new Obj(Code.add, "", null);
	}
	
	
	private boolean kompatibilniTipovi(Struct tempL, Struct tempR) {
		if (tempL == Tab.noType && (tempR.getKind() == Struct.Array))
			return true;

		if (tempL.getKind() == Struct.Array && tempR.getKind() == Struct.Array) {
			tempL = tempL.getElemType();
			tempR = tempR.getElemType();
		}

		if (tempL != tempR) {
			while (tempR.getKind() == Struct.Class) {
				tempR = tempR.getElemType();
				if (tempR == tempL)
					return true;
			}

			return false;
		}
		return true;
	}

}
