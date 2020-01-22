// Generated from /Users/yuliiaf/softserve/Easy/orm-implementation/src/main/resources/Easyql.g4 by ANTLR 4.8
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link EasyqlParser}.
 */
public interface EasyqlListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link EasyqlParser#eql}.
	 * @param ctx the parse tree
	 */
	void enterEql(EasyqlParser.EqlContext ctx);
	/**
	 * Exit a parse tree produced by {@link EasyqlParser#eql}.
	 * @param ctx the parse tree
	 */
	void exitEql(EasyqlParser.EqlContext ctx);
	/**
	 * Enter a parse tree produced by {@link EasyqlParser#query}.
	 * @param ctx the parse tree
	 */
	void enterQuery(EasyqlParser.QueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link EasyqlParser#query}.
	 * @param ctx the parse tree
	 */
	void exitQuery(EasyqlParser.QueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link EasyqlParser#selectq}.
	 * @param ctx the parse tree
	 */
	void enterSelectq(EasyqlParser.SelectqContext ctx);
	/**
	 * Exit a parse tree produced by {@link EasyqlParser#selectq}.
	 * @param ctx the parse tree
	 */
	void exitSelectq(EasyqlParser.SelectqContext ctx);
	/**
	 * Enter a parse tree produced by {@link EasyqlParser#updateq}.
	 * @param ctx the parse tree
	 */
	void enterUpdateq(EasyqlParser.UpdateqContext ctx);
	/**
	 * Exit a parse tree produced by {@link EasyqlParser#updateq}.
	 * @param ctx the parse tree
	 */
	void exitUpdateq(EasyqlParser.UpdateqContext ctx);
	/**
	 * Enter a parse tree produced by {@link EasyqlParser#deleteq}.
	 * @param ctx the parse tree
	 */
	void enterDeleteq(EasyqlParser.DeleteqContext ctx);
	/**
	 * Exit a parse tree produced by {@link EasyqlParser#deleteq}.
	 * @param ctx the parse tree
	 */
	void exitDeleteq(EasyqlParser.DeleteqContext ctx);
	/**
	 * Enter a parse tree produced by {@link EasyqlParser#where_clause}.
	 * @param ctx the parse tree
	 */
	void enterWhere_clause(EasyqlParser.Where_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link EasyqlParser#where_clause}.
	 * @param ctx the parse tree
	 */
	void exitWhere_clause(EasyqlParser.Where_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link EasyqlParser#agr}.
	 * @param ctx the parse tree
	 */
	void enterAgr(EasyqlParser.AgrContext ctx);
	/**
	 * Exit a parse tree produced by {@link EasyqlParser#agr}.
	 * @param ctx the parse tree
	 */
	void exitAgr(EasyqlParser.AgrContext ctx);
	/**
	 * Enter a parse tree produced by {@link EasyqlParser#pair}.
	 * @param ctx the parse tree
	 */
	void enterPair(EasyqlParser.PairContext ctx);
	/**
	 * Exit a parse tree produced by {@link EasyqlParser#pair}.
	 * @param ctx the parse tree
	 */
	void exitPair(EasyqlParser.PairContext ctx);
	/**
	 * Enter a parse tree produced by {@link EasyqlParser#condition}.
	 * @param ctx the parse tree
	 */
	void enterCondition(EasyqlParser.ConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link EasyqlParser#condition}.
	 * @param ctx the parse tree
	 */
	void exitCondition(EasyqlParser.ConditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link EasyqlParser#limit_clause}.
	 * @param ctx the parse tree
	 */
	void enterLimit_clause(EasyqlParser.Limit_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link EasyqlParser#limit_clause}.
	 * @param ctx the parse tree
	 */
	void exitLimit_clause(EasyqlParser.Limit_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link EasyqlParser#set_clause}.
	 * @param ctx the parse tree
	 */
	void enterSet_clause(EasyqlParser.Set_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link EasyqlParser#set_clause}.
	 * @param ctx the parse tree
	 */
	void exitSet_clause(EasyqlParser.Set_clauseContext ctx);
}