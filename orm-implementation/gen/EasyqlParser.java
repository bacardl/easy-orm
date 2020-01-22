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
		T__0=1, T__1=2, T__2=3, SELECT=4, DELETE=5, UPDATE=6, FROM=7, SPACE=8, 
		WHERE=9, AND=10, OR=11, LIKE=12, EQUAL=13, NOTEQUAL=14, ANYNAME=15, VALUE=16, 
		SET=17, COMMA=18;
	public static final int
		RULE_eql = 0, RULE_query = 1, RULE_selectq = 2, RULE_updateq = 3, RULE_deleteq = 4, 
		RULE_where_clause = 5, RULE_agr = 6, RULE_pair = 7, RULE_condition = 8, 
		RULE_limit_clause = 9, RULE_set_clause = 10;
	private static String[] makeRuleNames() {
		return new String[] {
			"eql", "query", "selectq", "updateq", "deleteq", "where_clause", "agr", 
			"pair", "condition", "limit_clause", "set_clause"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'LIMIT'", "'limit'", "'[0-9]+'", null, null, null, null, "'s*'", 
			null, null, null, null, "'='", "'!='", "'[a-zA-Z]+'", "'.+'", null, "','"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, "SELECT", "DELETE", "UPDATE", "FROM", "SPACE", 
			"WHERE", "AND", "OR", "LIKE", "EQUAL", "NOTEQUAL", "ANYNAME", "VALUE", 
			"SET", "COMMA"
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

	public static class EqlContext extends ParserRuleContext {
		public QueryContext query() {
			return getRuleContext(QueryContext.class,0);
		}
		public EqlContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_eql; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EasyqlListener ) ((EasyqlListener)listener).enterEql(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EasyqlListener ) ((EasyqlListener)listener).exitEql(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof EasyqlVisitor ) return ((EasyqlVisitor<? extends T>)visitor).visitEql(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EqlContext eql() throws RecognitionException {
		EqlContext _localctx = new EqlContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_eql);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(22);
			query();
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
		enterRule(_localctx, 2, RULE_query);
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
		public List<TerminalNode> SPACE() { return getTokens(EasyqlParser.SPACE); }
		public TerminalNode SPACE(int i) {
			return getToken(EasyqlParser.SPACE, i);
		}
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
		enterRule(_localctx, 4, RULE_selectq);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(29);
			match(SELECT);
			setState(30);
			match(SPACE);
			setState(31);
			match(FROM);
			setState(32);
			match(SPACE);
			setState(33);
			match(ANYNAME);
			setState(35);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WHERE) {
				{
				setState(34);
				where_clause();
				}
			}

			setState(38);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__0 || _la==T__1) {
				{
				setState(37);
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
		public List<TerminalNode> SPACE() { return getTokens(EasyqlParser.SPACE); }
		public TerminalNode SPACE(int i) {
			return getToken(EasyqlParser.SPACE, i);
		}
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
		enterRule(_localctx, 6, RULE_updateq);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(40);
			match(UPDATE);
			setState(41);
			match(SPACE);
			setState(42);
			match(ANYNAME);
			setState(43);
			match(SPACE);
			setState(44);
			set_clause();
			setState(46);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WHERE) {
				{
				setState(45);
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
		public TerminalNode SPACE() { return getToken(EasyqlParser.SPACE, 0); }
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
		enterRule(_localctx, 8, RULE_deleteq);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(48);
			match(DELETE);
			setState(49);
			match(SPACE);
			setState(50);
			match(FROM);
			setState(51);
			match(ANYNAME);
			setState(53);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WHERE) {
				{
				setState(52);
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
		public TerminalNode SPACE() { return getToken(EasyqlParser.SPACE, 0); }
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
		enterRule(_localctx, 10, RULE_where_clause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(55);
			match(WHERE);
			setState(56);
			match(SPACE);
			setState(57);
			pair();
			setState(63);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==AND || _la==OR) {
				{
				{
				setState(58);
				agr();
				setState(59);
				pair();
				}
				}
				setState(65);
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
		enterRule(_localctx, 12, RULE_agr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(66);
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

	public static class PairContext extends ParserRuleContext {
		public TerminalNode ANYNAME() { return getToken(EasyqlParser.ANYNAME, 0); }
		public List<TerminalNode> SPACE() { return getTokens(EasyqlParser.SPACE); }
		public TerminalNode SPACE(int i) {
			return getToken(EasyqlParser.SPACE, i);
		}
		public ConditionContext condition() {
			return getRuleContext(ConditionContext.class,0);
		}
		public TerminalNode VALUE() { return getToken(EasyqlParser.VALUE, 0); }
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
		enterRule(_localctx, 14, RULE_pair);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(68);
			match(ANYNAME);
			setState(69);
			match(SPACE);
			setState(70);
			condition();
			setState(71);
			match(SPACE);
			setState(72);
			match(VALUE);
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
		enterRule(_localctx, 16, RULE_condition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(74);
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

	public static class Limit_clauseContext extends ParserRuleContext {
		public TerminalNode SPACE() { return getToken(EasyqlParser.SPACE, 0); }
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
		enterRule(_localctx, 18, RULE_limit_clause);
		try {
			setState(80);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__0:
				enterOuterAlt(_localctx, 1);
				{
				setState(76);
				match(T__0);
				}
				break;
			case T__1:
				enterOuterAlt(_localctx, 2);
				{
				setState(77);
				match(T__1);
				setState(78);
				match(SPACE);
				setState(79);
				match(T__2);
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

	public static class Set_clauseContext extends ParserRuleContext {
		public TerminalNode SET() { return getToken(EasyqlParser.SET, 0); }
		public List<TerminalNode> SPACE() { return getTokens(EasyqlParser.SPACE); }
		public TerminalNode SPACE(int i) {
			return getToken(EasyqlParser.SPACE, i);
		}
		public List<PairContext> pair() {
			return getRuleContexts(PairContext.class);
		}
		public PairContext pair(int i) {
			return getRuleContext(PairContext.class,i);
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
		enterRule(_localctx, 20, RULE_set_clause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(82);
			match(SET);
			setState(83);
			match(SPACE);
			setState(84);
			pair();
			setState(85);
			match(SPACE);
			setState(91);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(86);
				match(COMMA);
				setState(87);
				match(SPACE);
				setState(88);
				pair();
				}
				}
				setState(93);
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

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\24a\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t\13\4"+
		"\f\t\f\3\2\3\2\3\3\3\3\3\3\5\3\36\n\3\3\4\3\4\3\4\3\4\3\4\3\4\5\4&\n\4"+
		"\3\4\5\4)\n\4\3\5\3\5\3\5\3\5\3\5\3\5\5\5\61\n\5\3\6\3\6\3\6\3\6\3\6\5"+
		"\68\n\6\3\7\3\7\3\7\3\7\3\7\3\7\7\7@\n\7\f\7\16\7C\13\7\3\b\3\b\3\t\3"+
		"\t\3\t\3\t\3\t\3\t\3\n\3\n\3\13\3\13\3\13\3\13\5\13S\n\13\3\f\3\f\3\f"+
		"\3\f\3\f\3\f\3\f\7\f\\\n\f\f\f\16\f_\13\f\3\f\2\2\r\2\4\6\b\n\f\16\20"+
		"\22\24\26\2\4\3\2\f\r\3\2\16\20\2^\2\30\3\2\2\2\4\35\3\2\2\2\6\37\3\2"+
		"\2\2\b*\3\2\2\2\n\62\3\2\2\2\f9\3\2\2\2\16D\3\2\2\2\20F\3\2\2\2\22L\3"+
		"\2\2\2\24R\3\2\2\2\26T\3\2\2\2\30\31\5\4\3\2\31\3\3\2\2\2\32\36\5\6\4"+
		"\2\33\36\5\n\6\2\34\36\5\b\5\2\35\32\3\2\2\2\35\33\3\2\2\2\35\34\3\2\2"+
		"\2\36\5\3\2\2\2\37 \7\6\2\2 !\7\n\2\2!\"\7\t\2\2\"#\7\n\2\2#%\7\21\2\2"+
		"$&\5\f\7\2%$\3\2\2\2%&\3\2\2\2&(\3\2\2\2\')\5\24\13\2(\'\3\2\2\2()\3\2"+
		"\2\2)\7\3\2\2\2*+\7\b\2\2+,\7\n\2\2,-\7\21\2\2-.\7\n\2\2.\60\5\26\f\2"+
		"/\61\5\f\7\2\60/\3\2\2\2\60\61\3\2\2\2\61\t\3\2\2\2\62\63\7\7\2\2\63\64"+
		"\7\n\2\2\64\65\7\t\2\2\65\67\7\21\2\2\668\5\f\7\2\67\66\3\2\2\2\678\3"+
		"\2\2\28\13\3\2\2\29:\7\13\2\2:;\7\n\2\2;A\5\20\t\2<=\5\16\b\2=>\5\20\t"+
		"\2>@\3\2\2\2?<\3\2\2\2@C\3\2\2\2A?\3\2\2\2AB\3\2\2\2B\r\3\2\2\2CA\3\2"+
		"\2\2DE\t\2\2\2E\17\3\2\2\2FG\7\21\2\2GH\7\n\2\2HI\5\22\n\2IJ\7\n\2\2J"+
		"K\7\22\2\2K\21\3\2\2\2LM\t\3\2\2M\23\3\2\2\2NS\7\3\2\2OP\7\4\2\2PQ\7\n"+
		"\2\2QS\7\5\2\2RN\3\2\2\2RO\3\2\2\2S\25\3\2\2\2TU\7\23\2\2UV\7\n\2\2VW"+
		"\5\20\t\2W]\7\n\2\2XY\7\24\2\2YZ\7\n\2\2Z\\\5\20\t\2[X\3\2\2\2\\_\3\2"+
		"\2\2][\3\2\2\2]^\3\2\2\2^\27\3\2\2\2_]\3\2\2\2\n\35%(\60\67AR]";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}