// Generated from /Users/yuliiaf/softserve/Easy/orm-implementation/src/main/resources/Easyql.g4 by ANTLR 4.8
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link EasyqlParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface EasyqlVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link EasyqlParser#eql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEql(EasyqlParser.EqlContext ctx);
	/**
	 * Visit a parse tree produced by {@link EasyqlParser#query}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuery(EasyqlParser.QueryContext ctx);
	/**
	 * Visit a parse tree produced by {@link EasyqlParser#selectq}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectq(EasyqlParser.SelectqContext ctx);
	/**
	 * Visit a parse tree produced by {@link EasyqlParser#updateq}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpdateq(EasyqlParser.UpdateqContext ctx);
	/**
	 * Visit a parse tree produced by {@link EasyqlParser#deleteq}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeleteq(EasyqlParser.DeleteqContext ctx);
	/**
	 * Visit a parse tree produced by {@link EasyqlParser#where_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhere_clause(EasyqlParser.Where_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link EasyqlParser#agr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAgr(EasyqlParser.AgrContext ctx);
	/**
	 * Visit a parse tree produced by {@link EasyqlParser#pair}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPair(EasyqlParser.PairContext ctx);
	/**
	 * Visit a parse tree produced by {@link EasyqlParser#condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCondition(EasyqlParser.ConditionContext ctx);
	/**
	 * Visit a parse tree produced by {@link EasyqlParser#limit_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLimit_clause(EasyqlParser.Limit_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link EasyqlParser#set_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSet_clause(EasyqlParser.Set_clauseContext ctx);
}