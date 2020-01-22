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
		T__0=1, T__1=2, T__2=3, SELECT=4, DELETE=5, UPDATE=6, FROM=7, SPACE=8, 
		WHERE=9, AND=10, OR=11, LIKE=12, EQUAL=13, NOTEQUAL=14, ANYNAME=15, VALUE=16, 
		SET=17, COMMA=18;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "SELECT", "DELETE", "UPDATE", "FROM", "SPACE", 
			"WHERE", "AND", "OR", "LIKE", "EQUAL", "NOTEQUAL", "ANYNAME", "VALUE", 
			"SET", "COMMA"
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\24\u00af\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\3\2\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3"+
		"\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5"+
		"\5\5G\n\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\5\6U\n\6\3\7"+
		"\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\5\7c\n\7\3\b\3\b\3\b\3\b"+
		"\3\b\3\b\3\b\3\b\5\bm\n\b\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n"+
		"\5\nz\n\n\3\13\3\13\3\13\3\13\3\13\3\13\5\13\u0082\n\13\3\f\3\f\3\f\3"+
		"\f\5\f\u0088\n\f\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\5\r\u0092\n\r\3\16\3"+
		"\16\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3"+
		"\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\22\5\22\u00ac\n\22\3\23\3\23"+
		"\2\2\24\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17"+
		"\35\20\37\21!\22#\23%\24\3\2\2\2\u00b7\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2"+
		"\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2"+
		"\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3"+
		"\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\3\'\3\2\2\2\5-\3"+
		"\2\2\2\7\63\3\2\2\2\tF\3\2\2\2\13T\3\2\2\2\rb\3\2\2\2\17l\3\2\2\2\21n"+
		"\3\2\2\2\23y\3\2\2\2\25\u0081\3\2\2\2\27\u0087\3\2\2\2\31\u0091\3\2\2"+
		"\2\33\u0093\3\2\2\2\35\u0095\3\2\2\2\37\u0098\3\2\2\2!\u00a2\3\2\2\2#"+
		"\u00ab\3\2\2\2%\u00ad\3\2\2\2\'(\7N\2\2()\7K\2\2)*\7O\2\2*+\7K\2\2+,\7"+
		"V\2\2,\4\3\2\2\2-.\7n\2\2./\7k\2\2/\60\7o\2\2\60\61\7k\2\2\61\62\7v\2"+
		"\2\62\6\3\2\2\2\63\64\7]\2\2\64\65\7\62\2\2\65\66\7/\2\2\66\67\7;\2\2"+
		"\678\7_\2\289\7-\2\29\b\3\2\2\2:;\7U\2\2;<\7G\2\2<=\7N\2\2=>\7G\2\2>?"+
		"\7E\2\2?G\7V\2\2@A\7u\2\2AB\7g\2\2BC\7n\2\2CD\7g\2\2DE\7e\2\2EG\7v\2\2"+
		"F:\3\2\2\2F@\3\2\2\2G\n\3\2\2\2HI\7F\2\2IJ\7G\2\2JK\7N\2\2KL\7G\2\2LM"+
		"\7V\2\2MU\7G\2\2NO\7f\2\2OP\7g\2\2PQ\7n\2\2QR\7g\2\2RS\7v\2\2SU\7g\2\2"+
		"TH\3\2\2\2TN\3\2\2\2U\f\3\2\2\2VW\7W\2\2WX\7R\2\2XY\7F\2\2YZ\7C\2\2Z["+
		"\7V\2\2[c\7G\2\2\\]\7w\2\2]^\7r\2\2^_\7f\2\2_`\7c\2\2`a\7v\2\2ac\7g\2"+
		"\2bV\3\2\2\2b\\\3\2\2\2c\16\3\2\2\2de\7H\2\2ef\7T\2\2fg\7Q\2\2gm\7O\2"+
		"\2hi\7h\2\2ij\7t\2\2jk\7q\2\2km\7o\2\2ld\3\2\2\2lh\3\2\2\2m\20\3\2\2\2"+
		"n\22\3\2\2\2op\7y\2\2pq\7j\2\2qr\7g\2\2rs\7t\2\2sz\7g\2\2tu\7Y\2\2uv\7"+
		"J\2\2vw\7G\2\2wx\7T\2\2xz\7G\2\2yo\3\2\2\2yt\3\2\2\2z\24\3\2\2\2{|\7C"+
		"\2\2|}\7P\2\2}\u0082\7F\2\2~\177\7c\2\2\177\u0080\7p\2\2\u0080\u0082\7"+
		"f\2\2\u0081{\3\2\2\2\u0081~\3\2\2\2\u0082\26\3\2\2\2\u0083\u0084\7Q\2"+
		"\2\u0084\u0088\7T\2\2\u0085\u0086\7q\2\2\u0086\u0088\7t\2\2\u0087\u0083"+
		"\3\2\2\2\u0087\u0085\3\2\2\2\u0088\30\3\2\2\2\u0089\u008a\7N\2\2\u008a"+
		"\u008b\7K\2\2\u008b\u008c\7M\2\2\u008c\u0092\7G\2\2\u008d\u008e\7n\2\2"+
		"\u008e\u008f\7k\2\2\u008f\u0090\7m\2\2\u0090\u0092\7g\2\2\u0091\u0089"+
		"\3\2\2\2\u0091\u008d\3\2\2\2\u0092\32\3\2\2\2\u0093\u0094\7?\2\2\u0094"+
		"\34\3\2\2\2\u0095\u0096\7#\2\2\u0096\u0097\7?\2\2\u0097\36\3\2\2\2\u0098"+
		"\u0099\7]\2\2\u0099\u009a\7c\2\2\u009a\u009b\7/\2\2\u009b\u009c\7|\2\2"+
		"\u009c\u009d\7C\2\2\u009d\u009e\7/\2\2\u009e\u009f\7\\\2\2\u009f\u00a0"+
		"\7_\2\2\u00a0\u00a1\7-\2\2\u00a1 \3\2\2\2\u00a2\u00a3\7\60\2\2\u00a3\u00a4"+
		"\7-\2\2\u00a4\"\3\2\2\2\u00a5\u00a6\7U\2\2\u00a6\u00a7\7G\2\2\u00a7\u00ac"+
		"\7V\2\2\u00a8\u00a9\7u\2\2\u00a9\u00aa\7g\2\2\u00aa\u00ac\7v\2\2\u00ab"+
		"\u00a5\3\2\2\2\u00ab\u00a8\3\2\2\2\u00ac$\3\2\2\2\u00ad\u00ae\7.\2\2\u00ae"+
		"&\3\2\2\2\f\2FTbly\u0081\u0087\u0091\u00ab\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}