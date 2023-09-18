package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.*;
import org.apache.log4j.*;

public class RuleVisitor extends VisitorAdaptor {

	Logger log = Logger.getLogger(getClass());
	
	int printCallCount = 0;
	
	public void visit(StatementPrintOne StatementPrintOne ) {
		printCallCount++;
		log.info("Prepoznata naredba print!");
	}
	
	public void visit(StatementPrintTwo StatementPrintTwo ) {
		printCallCount++;
		log.info("Prepoznata naredba print!");
	}
}
