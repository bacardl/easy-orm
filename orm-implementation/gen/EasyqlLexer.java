// Generated from /Users/yuliiaf/softserve/Easy/orm-implementation/src/main/resources/Easyql.g4 by ANTLR 4.8
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class EasyqlLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, SELECT=3, DELETE=4, UPDATE=5, FROM=6, WHERE=7, AND=8, 
		OR=9, LIKE=10, EQUAL=11, NOTEQUAL=12, ANYNAME=13, SET=14, COMMA=15, WS=16, 
		NUMBER=17;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "SELECT", "DELETE", "UPDATE", "FROM", "WHERE", "AND", 
			"OR", "LIKE", "EQUAL", "NOTEQUAL", "ANYNAME", "SET", "COMMA", "WS", "NUMBER"
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


	public EasyqlLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Easyql.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\23\u00a6\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\3\2\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4"+
		"\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\5\4>\n\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5"+
		"\3\5\3\5\3\5\3\5\3\5\5\5L\n\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6"+
		"\3\6\3\6\5\6Z\n\6\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\5\7d\n\7\3\b\3\b\3\b"+
		"\3\b\3\b\3\b\3\b\3\b\3\b\3\b\5\bp\n\b\3\t\3\t\3\t\3\t\3\t\3\t\5\tx\n\t"+
		"\3\n\3\n\3\n\3\n\5\n~\n\n\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\5\13"+
		"\u0088\n\13\3\f\3\f\3\r\3\r\3\r\3\16\6\16\u0090\n\16\r\16\16\16\u0091"+
		"\3\17\3\17\3\17\3\17\3\17\3\17\5\17\u009a\n\17\3\20\3\20\3\21\6\21\u009f"+
		"\n\21\r\21\16\21\u00a0\3\21\3\21\3\22\3\22\2\2\23\3\3\5\4\7\5\t\6\13\7"+
		"\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23\3\2\5\4"+
		"\2C\\c|\5\2\13\f\17\17\"\"\3\2\62;\2\u00b0\2\3\3\2\2\2\2\5\3\2\2\2\2\7"+
		"\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2"+
		"\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2"+
		"\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\3%\3\2\2\2\5+\3\2\2\2\7"+
		"=\3\2\2\2\tK\3\2\2\2\13Y\3\2\2\2\rc\3\2\2\2\17o\3\2\2\2\21w\3\2\2\2\23"+
		"}\3\2\2\2\25\u0087\3\2\2\2\27\u0089\3\2\2\2\31\u008b\3\2\2\2\33\u008f"+
		"\3\2\2\2\35\u0099\3\2\2\2\37\u009b\3\2\2\2!\u009e\3\2\2\2#\u00a4\3\2\2"+
		"\2%&\7N\2\2&\'\7K\2\2\'(\7O\2\2()\7K\2\2)*\7V\2\2*\4\3\2\2\2+,\7n\2\2"+
		",-\7k\2\2-.\7o\2\2./\7k\2\2/\60\7v\2\2\60\6\3\2\2\2\61\62\7U\2\2\62\63"+
		"\7G\2\2\63\64\7N\2\2\64\65\7G\2\2\65\66\7E\2\2\66>\7V\2\2\678\7u\2\28"+
		"9\7g\2\29:\7n\2\2:;\7g\2\2;<\7e\2\2<>\7v\2\2=\61\3\2\2\2=\67\3\2\2\2>"+
		"\b\3\2\2\2?@\7F\2\2@A\7G\2\2AB\7N\2\2BC\7G\2\2CD\7V\2\2DL\7G\2\2EF\7f"+
		"\2\2FG\7g\2\2GH\7n\2\2HI\7g\2\2IJ\7v\2\2JL\7g\2\2K?\3\2\2\2KE\3\2\2\2"+
		"L\n\3\2\2\2MN\7W\2\2NO\7R\2\2OP\7F\2\2PQ\7C\2\2QR\7V\2\2RZ\7G\2\2ST\7"+
		"w\2\2TU\7r\2\2UV\7f\2\2VW\7c\2\2WX\7v\2\2XZ\7g\2\2YM\3\2\2\2YS\3\2\2\2"+
		"Z\f\3\2\2\2[\\\7H\2\2\\]\7T\2\2]^\7Q\2\2^d\7O\2\2_`\7h\2\2`a\7t\2\2ab"+
		"\7q\2\2bd\7o\2\2c[\3\2\2\2c_\3\2\2\2d\16\3\2\2\2ef\7y\2\2fg\7j\2\2gh\7"+
		"g\2\2hi\7t\2\2ip\7g\2\2jk\7Y\2\2kl\7J\2\2lm\7G\2\2mn\7T\2\2np\7G\2\2o"+
		"e\3\2\2\2oj\3\2\2\2p\20\3\2\2\2qr\7C\2\2rs\7P\2\2sx\7F\2\2tu\7c\2\2uv"+
		"\7p\2\2vx\7f\2\2wq\3\2\2\2wt\3\2\2\2x\22\3\2\2\2yz\7Q\2\2z~\7T\2\2{|\7"+
		"q\2\2|~\7t\2\2}y\3\2\2\2}{\3\2\2\2~\24\3\2\2\2\177\u0080\7N\2\2\u0080"+
		"\u0081\7K\2\2\u0081\u0082\7M\2\2\u0082\u0088\7G\2\2\u0083\u0084\7n\2\2"+
		"\u0084\u0085\7k\2\2\u0085\u0086\7m\2\2\u0086\u0088\7g\2\2\u0087\177\3"+
		"\2\2\2\u0087\u0083\3\2\2\2\u0088\26\3\2\2\2\u0089\u008a\7?\2\2\u008a\30"+
		"\3\2\2\2\u008b\u008c\7#\2\2\u008c\u008d\7?\2\2\u008d\32\3\2\2\2\u008e"+
		"\u0090\t\2\2\2\u008f\u008e\3\2\2\2\u0090\u0091\3\2\2\2\u0091\u008f\3\2"+
		"\2\2\u0091\u0092\3\2\2\2\u0092\34\3\2\2\2\u0093\u0094\7U\2\2\u0094\u0095"+
		"\7G\2\2\u0095\u009a\7V\2\2\u0096\u0097\7u\2\2\u0097\u0098\7g\2\2\u0098"+
		"\u009a\7v\2\2\u0099\u0093\3\2\2\2\u0099\u0096\3\2\2\2\u009a\36\3\2\2\2"+
		"\u009b\u009c\7.\2\2\u009c \3\2\2\2\u009d\u009f\t\3\2\2\u009e\u009d\3\2"+
		"\2\2\u009f\u00a0\3\2\2\2\u00a0\u009e\3\2\2\2\u00a0\u00a1\3\2\2\2\u00a1"+
		"\u00a2\3\2\2\2\u00a2\u00a3\b\21\2\2\u00a3\"\3\2\2\2\u00a4\u00a5\t\4\2"+
		"\2\u00a5$\3\2\2\2\16\2=KYcow}\u0087\u0091\u0099\u00a0\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}