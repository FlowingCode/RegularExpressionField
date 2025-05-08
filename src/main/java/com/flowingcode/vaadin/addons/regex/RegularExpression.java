/*-
 * #%L
 * Regular Expression Field Add-on
 * %%
 * Copyright (C) 2025 Flowing Code
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.flowingcode.vaadin.addons.regex;

import static com.flowingcode.vaadin.addons.regex.RegularExpressionOperator.ADVANCED;
import static com.flowingcode.vaadin.addons.regex.RegularExpressionOperator.CONTAINS;
import static com.flowingcode.vaadin.addons.regex.RegularExpressionOperator.ENDS_WITH;
import static com.flowingcode.vaadin.addons.regex.RegularExpressionOperator.STARTS_WITH;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

/**
 * Represents a regular expression.
 *
 * <p>
 * This class assists in the creation and handling of regular expressions by providing pre-defined
 * operators for common patterns such as "starts with", "ends with", and "contains". It also
 * supports an advanced mode where users can input custom regular expressions.
 * </p>
 *
 * <p>
 * Instances of this class are immutable and validated upon creation.
 * </p>
 *
 * @author Javier Godoy
 */
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class RegularExpression {

  private static final String ANY = ".*";

  /** The operator defining the type of regular expression. */
  @EqualsAndHashCode.Include
  private final RegularExpressionOperator operator;

  /** The input string used for generating the pattern. */
  @EqualsAndHashCode.Include
  private final String input;

  /** The compiled {@code Pattern} for the regular expression. */
  private final Pattern pattern;

  /**
   * Creates a new {@code RegularExpression} based on the specified {@code operator} and
   * {@code input}.
   *
   * @param operator the type of regular expression (e.g.,
   *        {@link RegularExpressionOperator#CONTAINS})
   * @param input the string to be used for pattern creation
   * @throws PatternSyntaxException if the resulting pattern is invalid.
   */
  public RegularExpression(@NonNull RegularExpressionOperator operator, @NonNull String input)
      throws PatternSyntaxException {
    this.operator = operator;
    this.input = input;

    String regex = switch (operator) {
      case ADVANCED -> input;
      case CONTAINS -> ANY + quote(input) + ANY;
      case ENDS_WITH -> ANY + quote(input);
      case STARTS_WITH -> quote(input) + ANY;
    };

    pattern = Pattern.compile(regex);
  }

  private final static String CHARS = ".?+*\\[({$^|\\\\";

  private final static Pattern SIMPLE_PATTERN =
      Pattern.compile("(?:[^CHARS]|\\\\[CHARS])+".replace("CHARS", CHARS));

  private final static Pattern ESCAPE_PATTERN =
      Pattern.compile("[CHARS]".replace("CHARS", CHARS));

  private final static Pattern UNQUOTE_PATTERN =
      Pattern.compile("\\\\([CHARS])".replace("CHARS", CHARS));

  private static String quote(String input) {
    String s1 = ESCAPE_PATTERN.matcher(input).replaceAll("\\\\$0");
    String s2 = "\\Q" + input.replace("\\E", "\\E\\\\E\\Q") + "\\E";
    return (s1.length() < s2.length()) ? s1 : s2;
  }

  /**
   * Creates a {@code RegularExpression} instance from a given {@link Pattern}.
   *
   * <p>
   * This method attempts to determine if the pattern corresponds to a simple
   * {@link RegularExpressionOperator} like "starts with", "ends with", or "contains". If it does, a
   * corresponding {@code RegularExpression} is returned. Otherwise, an advanced mode expression is
   * created.
   * </p>
   *
   * @param pattern the pattern to analyze
   *
   * @throws NullPointerException if {@code pattern} is {@code null}.
   *
   * @return a {@code RegularExpression} instance
   */
  public static RegularExpression of(Pattern pattern) {

    if (pattern == null) {
      throw new NullPointerException("Pattern cannot be null");
    }

    String regex = pattern.pattern();
    boolean hasLeadingWildcard = false;
    boolean hasTrailingWildcard = false;
    if (regex.startsWith(ANY)) {
      hasLeadingWildcard = true;
      regex = regex.substring(2);
    }

    if (regex.endsWith(ANY)) {
      hasTrailingWildcard = true;
      regex = regex.substring(0, regex.length() - 2);
    }

    String input = null;
    if (hasLeadingWildcard || hasTrailingWildcard) {
      if (SIMPLE_PATTERN.matcher(regex).matches()) {
        input = UNQUOTE_PATTERN.matcher(regex).replaceAll("$1");
      } else if (regex.startsWith("\\Q") && regex.endsWith("\\E")) {
        input = regex.substring(2, regex.length() - 2).replace("\\E\\\\E\\Q", "\\E");
      }
    }

    if (input != null) {
      if (hasLeadingWildcard && hasTrailingWildcard) {
        return new RegularExpression(CONTAINS, input);
      } else if (hasLeadingWildcard) {
        return new RegularExpression(ENDS_WITH, input);
      } else if (hasTrailingWildcard) {
        return new RegularExpression(STARTS_WITH, input);
      }
    }

    return new RegularExpression(ADVANCED, pattern.pattern(), pattern);
  }

  @Override
  public String toString() {
    return operator + " " + input;
  }

}
