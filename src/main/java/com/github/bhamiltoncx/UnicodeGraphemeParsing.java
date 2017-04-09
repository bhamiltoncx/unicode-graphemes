package com.github.bhamiltoncx;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class UnicodeGraphemeParsing {
  public static final class Result {
    public enum Type {
        EMOJI,
        NON_EMOJI
    }

    public final Type type;
    public final int stringOffset;
    public final int stringLength;

    public Result(Type type, int stringOffset, int stringLength) {
      this.type = type;
      this.stringOffset = stringOffset;
      this.stringLength = stringLength;
    }

    @Override
    public boolean equals(Object other) {
      if (!(other instanceof Result)) {
        return false;
      }

      Result that = (Result) other;
      return
          Objects.equals(this.type, that.type) &&
          this.stringOffset == that.stringOffset &&
          this.stringLength == that.stringLength;
    }

    @Override
    public int hashCode() {
      return Objects.hash(type, stringOffset, stringLength);
    }

    @Override
    public String toString() {
      return String.format("%s type=%s stringOffset=%d stringLength=%d", super.toString(), type, stringLength, stringOffset);
    }
  }

  // Handles converting code points to Java String UTF-16 offsets.
  private static final class CodePointCounter {
    private final String input;
    public int inputIndex;
    public int codePointIndex;

    public CodePointCounter(String input) {
      this.input = input;
      this.inputIndex = 0;
      this.codePointIndex = 0;
    }

    public int advanceToIndex(int newCodePointIndex) {
      while (codePointIndex < newCodePointIndex) {
        int codePoint = Character.codePointAt(input, inputIndex);
        inputIndex += Character.charCount(codePoint);
        codePointIndex++;
      }
      return inputIndex;
    }
  }

  private static final class GraphemesParsingListener extends GraphemesBaseListener {
    private final CodePointCounter codePointCounter;
    private Result.Type clusterType;
    private int clusterStringStartIndex;
    public final List<Result> result;

    public GraphemesParsingListener(String string) {
      this.codePointCounter = new CodePointCounter(string);
      this.result = new ArrayList<>();
    }

    @Override
    public void enterGrapheme_cluster(GraphemesParser.Grapheme_clusterContext ctx) {
      clusterType = Result.Type.NON_EMOJI;
      clusterStringStartIndex =
          codePointCounter.advanceToIndex(ctx.getStart().getStartIndex());
    }

    @Override
    public void enterEmoji_sequence(GraphemesParser.Emoji_sequenceContext ctx) {
      clusterType = Result.Type.EMOJI;
    }

    @Override
    public void exitGrapheme_cluster(GraphemesParser.Grapheme_clusterContext ctx) {
      int clusterStringStopIndex = codePointCounter.advanceToIndex(
          ctx.getStop().getStopIndex() + 1);
      int clusterStringLength = clusterStringStopIndex - clusterStringStartIndex;
      result.add(
          new Result(clusterType, clusterStringStartIndex, clusterStringLength));
    }
  }

  public static List<Result> parse(String string) {
    try {
      GraphemesLexer lexer = new GraphemesLexer(CharStreams.fromString(string));
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      GraphemesParser parser = new GraphemesParser(tokens);
      GraphemesParser.GraphemesContext tree = parser.graphemes();
      GraphemesParsingListener listener = new GraphemesParsingListener(string);
      ParseTreeWalker.DEFAULT.walk(listener, tree);
      return listener.result;
    } catch (RecognitionException re) {
      System.err.format("Exception %s when parsing\n", re);
      throw re;
    }
  }
}
