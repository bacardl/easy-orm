// Generated from /Users/yuliiaf/softserve/Easy/orm-implementation/src/main/resources/Easyql.g4 by ANTLR 4.8
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class EasyqlParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, SELECT=3, DELETE=4, UPDATE=5, FROM=6, WHERE=7, AND=8, 
		OR=9, LIKE=10, EQUAL=11, NOTEQUAL=12, ANYNAME=13, SET=14, COMMA=15, WS=16, 
		NUMBER=17;
	public static final int
		RULE_query = 0, RULE_selectq = 1, RULE_updateq = 2, RULE_deleteq = 3, 
		RULE_where_clause = 4, RULE_agr = 5, RULE_condition = 6, RULE_value = 7, 
		RULE_set_clause = 8, RULE_equalpair = 9, RULE_pair = 10, RULE_limit_clause = 11;
	private static String[] makeRuleNames() {
		return new String[] {
			"query", "selectq", "updateq", "deleteq", "where_clause", "agr", "condition", 
			"value", "set_clause", "equalpair", "pair", "limit_clause"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'LIMIT'", "'limit'", null, null, null, null, null, null, null, 
			null, "'='", "'!='", null, null, "','"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, "SELECT", "DELETE", "UPDATE", "FROM", "WHERE", "AND", 
			"OR", "LIKE", "EQUAL", "NOTEQUAL", "ANYNAME", "SET", "COMMA", "WS", "NUMBER"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "Easyql.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public EasyqlParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class QueryContext extends ParserRuleContext {
		public SelectqContext selectq() {
			return getRuleContext(SelectqContext.class,0);
		}
		public DeleteqContext deleteq() {
			return getRuleContext(DeleteqContext.class,0);
		}
		public UpdateqContext updateq() {
			return getRuleContext(UpdateqContext.class,0);
		}
		public QueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_query; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EasyqlListener ) ((EasyqlListener)listener).enterQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EasyqlListener ) ((EasyqlListener)listener).exitQuery(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EasyqlVisitor ) return ((EasyqlVisitor<? extends T>)visitor).visitQuery(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QueryContext query() throws RecognitionException {
		QueryContext _localctx = new QueryContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_query);
		try {
			setState(27);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SELECT:
				enterOuterAlt(_localctx, 1);
				{
				setState(24);
				selectq();
				}
				break;
			case DELETE:
				enterOuterAlt(_localctx, 2);
				{
				setState(25);
				deleteq();
				}
				break;
			case UPDATE:
				enterOuterAlt(_localctx, 3);
				{
				setState(26);
				updateq();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SelectqContext extends ParserRuleContext {
		public TerminalNode SELECT() { return getToken(EasyqlParser.SELECT, 0); }
		public TerminalNode FROM() { return getToken(EasyqlParser.FROM, 0); }
		public TerminalNode ANYNAME() { return getToken(EasyqlParser.ANYNAME, 0); }
		public Where_clauseContext where_clause() {
			return getRuleContext(Where_clauseContext.class,0);
		}
		public Limit_clauseContext limit_clause() {
			return getRuleContext(Limit_clauseContext.class,0);
		}
		public SelectqContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_selectq; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EasyqlListener ) ((EasyqlListener)listener).enterSelectq(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EasyqlListener ) ((EasyqlListener)listener).exitSelectq(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EasyqlVisitor ) return ((EasyqlVisitor<? extends T>)visitor).visitSelectq(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SelectqContext selectq() throws RecognitionException {
		SelectqContext _localctx = new SelectqContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_selectq);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(29);
			match(SELECT);
			setState(30);
			match(FROM);
			setState(31);
			match(ANYNAME);
			setState(33);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WHERE) {
				{
				setState(32);
				where_clause();
				}
			}

			setState(36);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__0 || _la==T__1) {
				{
				setState(35);
				limit_clause();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class UpdateqContext extends ParserRuleContext {
		public TerminalNode UPDATE() { return getToken(EasyqlParser.UPDATE, 0); }
		public TerminalNode ANYNAME() { return getToken(EasyqlParser.ANYNAME, 0); }
		public Set_clauseContext set_clause() {
			return getRuleContext(Set_clauseContext.class,0);
		}
		public Where_clauseContext where_clause() {
			return getRuleContext(Where_clauseContext.class,0);
		}
		public UpdateqContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_updateq; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EasyqlListener ) ((EasyqlListener)listener).enterUpdateq(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EasyqlListener ) ((EasyqlListener)listener).exitUpdateq(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EasyqlVisitor ) return ((EasyqlVisitor<? extends T>)visitor).visitUpdateq(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UpdateqContext updateq() throws RecognitionException {
		UpdateqContext _localctx = new UpdateqContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_updateq);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(38);
			match(UPDATE);
			setState(39);
			match(ANYNAME);
			setState(40);
			set_clause();
			setState(42);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WHERE) {
				{
				setState(41);
				where_clause();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DeleteqContext extends ParserRuleContext {
		public TerminalNode DELETE() { return getToken(EasyqlParser.DELETE, 0); }
		public TerminalNode FROM() { return getToken(EasyqlParser.FROM, 0); }
		public TerminalNode ANYNAME() { return getToken(EasyqlParser.ANYNAME, 0); }
		public Where_clauseContext where_clause() {
			return getRuleContext(Where_clauseContext.class,0);
		}
		public DeleteqContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_deleteq; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EasyqlListener ) ((EasyqlListener)listener).enterDeleteq(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EasyqlListener ) ((EasyqlListener)listener).exitDeleteq(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EasyqlVisitor ) return ((EasyqlVisitor<? extends T>)visitor).visitDeleteq(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeleteqContext deleteq() throws RecognitionException {
		DeleteqContext _localctx = new DeleteqContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_deleteq);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(44);
			match(DELETE);
			setState(45);
			match(FROM);
			setState(46);
			match(ANYNAME);
			setState(48);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WHERE) {
				{
				setState(47);
				where_clause();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Where_clauseContext extends ParserRuleContext {
		public TerminalNode WHERE() { return getToken(EasyqlParser.WHERE, 0); }
		public List<PairContext> pair() {
			return getRuleContexts(PairContext.class);
		}
		public PairContext pair(int i) {
			return getRuleContext(PairContext.class,i);
		}
		public List<AgrContext> agr() {
			return getRuleContexts(AgrContext.class);
		}
		public AgrContext agr(int i) {
			return getRuleContext(AgrContext.class,i);
		}
		public Where_clauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_where_clause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EasyqlListener ) ((EasyqlListener)listener).enterWhere_clause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EasyqlListener ) ((EasyqlListener)listener).exitWhere_clause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EasyqlVisitor ) return ((EasyqlVisitor<? extends T>)visitor).visitWhere_clause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Where_clauseContext where_clause() throws RecognitionException {
		Where_clauseContext _localctx = new Where_clauseContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_where_clause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(50);
			match(WHERE);
			setState(51);
			pair();
			setState(57);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==AND || _la==OR) {
				{
				{
				setState(52);
				agr();
				setState(53);
				pair();
				}
				}
				setState(59);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AgrContext extends ParserRuleContext {
		public TerminalNode AND() { return getToken(EasyqlParser.AND, 0); }
		public TerminalNode OR() { return getToken(EasyqlParser.OR, 0); }
		public AgrContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_agr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EasyqlListener ) ((EasyqlListener)listener).enterAgr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EasyqlListener ) ((EasyqlListener)listener).exitAgr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EasyqlVisitor ) return ((EasyqlVisitor<? extends T>)visitor).visitAgr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AgrContext agr() throws RecognitionException {
		AgrContext _localctx = new AgrContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_agr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(60);
			_la = _input.LA(1);
			if ( !(_la==AND || _la==OR) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConditionContext extends ParserRuleContext {
		public TerminalNode LIKE() { return getToken(EasyqlParser.LIKE, 0); }
		public TerminalNode EQUAL() { return getToken(EasyqlParser.EQUAL, 0); }
		public TerminalNode NOTEQUAL() { return getToken(EasyqlParser.NOTEQUAL, 0); }
		public ConditionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_condition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EasyqlListener ) ((EasyqlListener)listener).enterCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EasyqlListener ) ((EasyqlListener)listener).exitCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EasyqlVisitor ) return ((EasyqlVisitor<? extends T>)visitor).visitCondition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConditionContext condition() throws RecognitionException {
		ConditionContext _localctx = new ConditionContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_condition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(62);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LIKE) | (1L << EQUAL) | (1L << NOTEQUAL))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ValueContext extends ParserRuleContext {
		public TerminalNode ANYNAME() { return getToken(EasyqlParser.ANYNAME, 0); }
		public TerminalNode NUMBER() { return getToken(EasyqlParser.NUMBER, 0); }
		public ValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_value; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EasyqlListener ) ((EasyqlListener)listener).enterValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EasyqlListener ) ((EasyqlListener)listener).exitValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EasyqlVisitor ) return ((EasyqlVisitor<? extends T>)visitor).visitValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueContext value() throws RecognitionException {
		ValueContext _localctx = new ValueContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_value);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(64);
			_la = _input.LA(1);
			if ( !(_la==ANYNAME || _la==NUMBER) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Set_clauseContext extends ParserRuleContext {
		public TerminalNode SET() { return getToken(EasyqlParser.SET, 0); }
		public List<EqualpairContext> equalpair() {
			return getRuleContexts(EqualpairContext.class);
		}
		public EqualpairContext equalpair(int i) {
			return getRuleContext(EqualpairContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(EasyqlParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(EasyqlParser.COMMA, i);
		}
		public Set_clauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_set_clause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EasyqlListener ) ((EasyqlListener)listener).enterSet_clause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EasyqlListener ) ((EasyqlListener)listener).exitSet_clause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EasyqlVisitor ) return ((EasyqlVisitor<? extends T>)visitor).visitSet_clause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Set_clauseContext set_clause() throws RecognitionException {
		Set_clauseContext _localctx = new Set_clauseContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_set_clause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(66);
			match(SET);
			setState(67);
			equalpair();
			setState(72);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(68);
				match(COMMA);
				setState(69);
				equalpair();
				}
				}
				setState(74);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EqualpairContext extends ParserRuleContext {
		public TerminalNode ANYNAME() { return getToken(EasyqlParser.ANYNAME, 0); }
		public TerminalNode EQUAL() { return getToken(EasyqlParser.EQUAL, 0); }
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public EqualpairContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_equalpair; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EasyqlListener ) ((EasyqlListener)listener).enterEqualpair(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EasyqlListener ) ((EasyqlListener)listener).exitEqualpair(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EasyqlVisitor ) return ((EasyqlVisitor<? extends T>)visitor).visitEqualpair(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EqualpairContext equalpair() throws RecognitionException {
		EqualpairContext _localctx = new EqualpairContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_equalpair);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(75);
			match(ANYNAME);
			setState(76);
			match(EQUAL);
			setState(77);
			value();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PairContext extends ParserRuleContext {
		public TerminalNode ANYNAME() { return getToken(EasyqlParser.ANYNAME, 0); }
		public ConditionContext condition() {
			return getRuleContext(ConditionContext.class,0);
		}
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public PairContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pair; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EasyqlListener ) ((EasyqlListener)listener).enterPair(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EasyqlListener ) ((EasyqlListener)listener).exitPair(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EasyqlVisitor ) return ((EasyqlVisitor<? extends T>)visitor).visitPair(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PairContext pair() throws RecognitionException {
		PairContext _localctx = new PairContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_pair);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(79);
			match(ANYNAME);
			setState(80);
			condition();
			setState(81);
			value();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Limit_clauseContext extends ParserRuleContext {
		public TerminalNode NUMBER() { return getToken(EasyqlParser.NUMBER, 0); }
		public Limit_clauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_limit_clause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EasyqlListener ) ((EasyqlListener)listener).enterLimit_clause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EasyqlListener ) ((EasyqlListener)listener).exitLimit_clause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EasyqlVisitor ) return ((EasyqlVisitor<? extends T>)visitor).visitLimit_clause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Limit_clauseContext limit_clause() throws RecognitionException {
		Limit_clauseContext _localctx = new Limit_clauseContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_limit_clause);
		try {
			setState(86);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__0:
				enterOuterAlt(_localctx, 1);
				{
				setState(83);
				match(T__0);
				}
				break;
			case T__1:
				enterOuterAlt(_localctx, 2);
				{
				setState(84);
				match(T__1);
				setState(85);
				match(NUMBER);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\23[\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t\13\4"+
		"\f\t\f\4\r\t\r\3\2\3\2\3\2\5\2\36\n\2\3\3\3\3\3\3\3\3\5\3$\n\3\3\3\5\3"+
		"\'\n\3\3\4\3\4\3\4\3\4\5\4-\n\4\3\5\3\5\3\5\3\5\5\5\63\n\5\3\6\3\6\3\6"+
		"\3\6\3\6\7\6:\n\6\f\6\16\6=\13\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\n\3"+
		"\n\7\nI\n\n\f\n\16\nL\13\n\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\r\3\r"+
		"\3\r\5\rY\n\r\3\r\2\2\16\2\4\6\b\n\f\16\20\22\24\26\30\2\5\3\2\n\13\3"+
		"\2\f\16\4\2\17\17\23\23\2W\2\35\3\2\2\2\4\37\3\2\2\2\6(\3\2\2\2\b.\3\2"+
		"\2\2\n\64\3\2\2\2\f>\3\2\2\2\16@\3\2\2\2\20B\3\2\2\2\22D\3\2\2\2\24M\3"+
		"\2\2\2\26Q\3\2\2\2\30X\3\2\2\2\32\36\5\4\3\2\33\36\5\b\5\2\34\36\5\6\4"+
		"\2\35\32\3\2\2\2\35\33\3\2\2\2\35\34\3\2\2\2\36\3\3\2\2\2\37 \7\5\2\2"+
		" !\7\b\2\2!#\7\17\2\2\"$\5\n\6\2#\"\3\2\2\2#$\3\2\2\2$&\3\2\2\2%\'\5\30"+
		"\r\2&%\3\2\2\2&\'\3\2\2\2\'\5\3\2\2\2()\7\7\2\2)*\7\17\2\2*,\5\22\n\2"+
		"+-\5\n\6\2,+\3\2\2\2,-\3\2\2\2-\7\3\2\2\2./\7\6\2\2/\60\7\b\2\2\60\62"+
		"\7\17\2\2\61\63\5\n\6\2\62\61\3\2\2\2\62\63\3\2\2\2\63\t\3\2\2\2\64\65"+
		"\7\t\2\2\65;\5\26\f\2\66\67\5\f\7\2\678\5\26\f\28:\3\2\2\29\66\3\2\2\2"+
		":=\3\2\2\2;9\3\2\2\2;<\3\2\2\2<\13\3\2\2\2=;\3\2\2\2>?\t\2\2\2?\r\3\2"+
		"\2\2@A\t\3\2\2A\17\3\2\2\2BC\t\4\2\2C\21\3\2\2\2DE\7\20\2\2EJ\5\24\13"+
		"\2FG\7\21\2\2GI\5\24\13\2HF\3\2\2\2IL\3\2\2\2JH\3\2\2\2JK\3\2\2\2K\23"+
		"\3\2\2\2LJ\3\2\2\2MN\7\17\2\2NO\7\r\2\2OP\5\20\t\2P\25\3\2\2\2QR\7\17"+
		"\2\2RS\5\16\b\2ST\5\20\t\2T\27\3\2\2\2UY\7\3\2\2VW\7\4\2\2WY\7\23\2\2"+
		"XU\3\2\2\2XV\3\2\2\2Y\31\3\2\2\2\n\35#&,\62;JX";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}