# README for unicode-graphemes

This is a sample Java client for the ANTLR 4 Unicode grapheme cluster parser
grammar:

https://github.com/antlr/grammars-v4/tree/master/unicode/graphemes

# To Build and Install

```
% mvn install
```

# Usage Example

```
import com.github.bhamiltoncx.UnicodeGraphemeParsing;

public class Example {
  public static void main(String[] strings) {
    for (String string : strings) {
      System.out.format("Parsing string: %s\n", string);
      for (UnicodeGraphemeParsing.Result grapheme : UnicodeGraphemeParsing.parse(string)) {
        String s = string.substring(grapheme.stringOffset, grapheme.stringOffset + grapheme.stringLength);
        String type = (grapheme.type == UnicodeGraphemeParsing.Result.Type.EMOJI ? "Emoji" : "Non-Emoji");
        System.out.format("%s: [%s] (offset=%d, length=%d)\n", type, s, grapheme.stringOffset, grapheme.stringLength);
      }
    }
  }
}
```

# Full example

```
% mvn install
% javac -cp target/unicode-graphemes-0.1-SNAPSHOT.jar example/Example.java
% alias parse-graphemes="java -cp $HOME/.m2/repository/org/antlr/antlr4-runtime/4.7/antlr4-runtime-4.7.jar:$HOME/.m2//repository/com/github/bhamiltoncx/unicode-graphemes/0.1-SNAPSHOT/unicode-graphemes-0.1-SNAPSHOT.jar:example Example"
% parse-graphemes abc😀💩👮🏿‍♀️👩‍👩‍👧‍👧🇫🇮
Parsing string: abc😀💩👮🏿‍♀️👩‍👩‍👧‍👧🇫🇮
Non-Emoji: [a] (offset=0, length=1)
Non-Emoji: [b] (offset=1, length=1)
Non-Emoji: [c] (offset=2, length=1)
Emoji: [😀] (offset=3, length=2)
Emoji: [💩] (offset=5, length=2)
Emoji: [👮🏿‍♀️] (offset=7, length=7)
Emoji: [👩‍👩‍👧‍👧] (offset=14, length=11)
Emoji: [🇫🇮] (offset=25, length=4)
```
