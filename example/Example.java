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
