package com.github.bhamiltoncx;

import static com.google.common.truth.Truth.assertThat;
import org.junit.Test;

public final class TestUnicodeGraphemeParsing {
  @Test
  public void empty() {
    assertThat(UnicodeGraphemeParsing.parse("")).isEmpty();
  }

  @Test
  public void latin() {
    assertThat(UnicodeGraphemeParsing.parse("yo"))
        .containsExactly(
            nonEmojiResultAt(0, 1),
            nonEmojiResultAt(1, 1)
        );
  }

  @Test
  public void poopAndFireEmoji() {
    String emoji = new StringBuilder()
        .appendCodePoint(0x1F4A9)
        .appendCodePoint(0x1F525)
        .toString();
    assertThat(UnicodeGraphemeParsing.parse(emoji))
        .containsExactly(
            emojiResultAt(0, 2),
            emojiResultAt(2, 2)
        );
  }

  @Test
  public void emojiFlagSequence() {
    // http://emojipedia.org/flag-for-scotland/
    String emoji = new StringBuilder()
        .appendCodePoint(0x1F3F4)
        .appendCodePoint(0xE0067)
        .appendCodePoint(0xE0062)
        .appendCodePoint(0xE0073)
        .appendCodePoint(0xE0063)
        .appendCodePoint(0xE0074)
        .appendCodePoint(0xE007F)
        .toString();
    assertThat(UnicodeGraphemeParsing.parse(emoji))
        .containsExactly(
            emojiResultAt(0, 14)
        );
  }

  @Test
  public void emojiWithSkinToneAndGenderVariations() {
    String emoji = new StringBuilder()
        .appendCodePoint(0x1F46E)
        .appendCodePoint(0x1F3FF)
        .appendCodePoint(0x200D)
        .appendCodePoint(0x2640)
        .appendCodePoint(0xFE0F)
        .toString();
    assertThat(UnicodeGraphemeParsing.parse(emoji))
        .containsExactly(
            emojiResultAt(0, 7)
        );
  }

  @Test
  public void emojiFamilySequence() {
    // http://www.iemoji.com/view/emoji/1712/smileys-people/family-of-two-men-with-two-girls
    String emoji = new StringBuilder()
        .appendCodePoint(0x1F468)
        .appendCodePoint(0x200D)
        .appendCodePoint(0x1F468)
        .appendCodePoint(0x200D)
        .appendCodePoint(0x1F467)
        .appendCodePoint(0x200D)
        .appendCodePoint(0x1F467)
        .toString();
    assertThat(UnicodeGraphemeParsing.parse(emoji))
        .containsExactly(
            emojiResultAt(0, 11)
        );
  }

  private static UnicodeGraphemeParsing.Result nonEmojiResultAt(int offset, int length) {
    return new UnicodeGraphemeParsing.Result(UnicodeGraphemeParsing.Result.Type.NON_EMOJI, offset, length);
  }

  private static UnicodeGraphemeParsing.Result emojiResultAt(int offset, int length) {
    return new UnicodeGraphemeParsing.Result(UnicodeGraphemeParsing.Result.Type.EMOJI, offset, length);
  }
}
