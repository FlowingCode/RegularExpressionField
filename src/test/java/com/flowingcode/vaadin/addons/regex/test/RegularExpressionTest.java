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
package com.flowingcode.vaadin.addons.regex.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import com.flowingcode.vaadin.addons.regex.RegularExpression;
import com.flowingcode.vaadin.addons.regex.RegularExpressionOperator;
import org.junit.Test;

public class RegularExpressionTest {

  @Test
  public void testAdvanced() {
    var r = new RegularExpression(RegularExpressionOperator.ADVANCED, ".*");
    assertThat(r.getPattern().pattern(), equalTo(".*"));
    assertThat(r.getInput(), equalTo(".*"));

    var q = RegularExpression.of(r.getPattern());
    assertThat(q, equalTo(r));
    assertThat(q.getPattern().pattern(), equalTo(r.getPattern().pattern()));
  }

  @Test
  public void testStartsWith() {
    var r = new RegularExpression(RegularExpressionOperator.STARTS_WITH, "[");
    assertThat(r.getPattern().pattern(), equalTo("\\[.*"));
    assertThat(r.getInput(), equalTo("["));

    var q = RegularExpression.of(r.getPattern());
    assertThat(q, equalTo(r));
    assertThat(q.getPattern().pattern(), equalTo(r.getPattern().pattern()));
  }

  @Test
  public void testEndsWith() {
    var r = new RegularExpression(RegularExpressionOperator.ENDS_WITH, "[");
    assertThat(r.getPattern().pattern(), equalTo(".*\\["));
    assertThat(r.getInput(), equalTo("["));

    var q = RegularExpression.of(r.getPattern());
    assertThat(q, equalTo(r));
    assertThat(q.getPattern().pattern(), equalTo(r.getPattern().pattern()));
  }

  @Test
  public void testContains() {
    var r = new RegularExpression(RegularExpressionOperator.CONTAINS, "[");
    assertThat(r.getPattern().pattern(), equalTo(".*\\[.*"));
    assertThat(r.getInput(), equalTo("["));

    var q = RegularExpression.of(r.getPattern());
    assertThat(q, equalTo(r));
    assertThat(q.getPattern().pattern(), equalTo(r.getPattern().pattern()));
  }

  @Test
  public void testShort() {
    var r = new RegularExpression(RegularExpressionOperator.CONTAINS, "a");
    assertThat(r.getPattern().pattern(), equalTo(".*a.*"));
    assertThat(r.getInput(), equalTo("a"));

    var q = RegularExpression.of(r.getPattern());
    assertThat(q, equalTo(r));
    assertThat(q.getPattern().pattern(), equalTo(r.getPattern().pattern()));
  }

  @Test
  public void testQuoteE() {
    var r = new RegularExpression(RegularExpressionOperator.CONTAINS, ".........\\E");
    assertThat(r.getPattern().pattern(), equalTo(".*\\Q.........\\E\\\\E\\Q\\E.*"));
    assertThat(r.getInput(), equalTo(".........\\E"));

    var q = RegularExpression.of(r.getPattern());
    assertThat(q, equalTo(r));
    assertThat(q.getPattern().pattern(), equalTo(r.getPattern().pattern()));
  }

  @Test
  public void testQuoteMode1() {
    var r = new RegularExpression(RegularExpressionOperator.CONTAINS, "...");
    assertThat(r.getPattern().pattern(), equalTo(".*\\.\\.\\..*"));
    assertThat(r.getInput(), equalTo("..."));

    var q = RegularExpression.of(r.getPattern());
    assertThat(q, equalTo(r));
    assertThat(q.getPattern().pattern(), equalTo(r.getPattern().pattern()));
  }

  @Test
  public void testQuoteMode2() {
    var r = new RegularExpression(RegularExpressionOperator.CONTAINS, "....");
    assertThat(r.getPattern().pattern(), equalTo(".*\\Q....\\E.*"));
    assertThat(r.getInput(), equalTo("...."));

    var q = RegularExpression.of(r.getPattern());
    assertThat(q, equalTo(r));
    assertThat(q.getPattern().pattern(), equalTo(r.getPattern().pattern()));
  }

  @Test
  public void testQuoteChars() {
    for (char c : ".?+*[({$^|\\".toCharArray()) {
      var r = new RegularExpression(RegularExpressionOperator.CONTAINS, Character.toString(c));
      assertThat(r.getPattern().pattern(), equalTo(".*\\" + c + ".*"));
      assertThat(r.getInput(), equalTo(Character.toString(c)));

      var q = RegularExpression.of(r.getPattern());
      assertThat(q, equalTo(r));
      assertThat(q.getPattern().pattern(), equalTo(r.getPattern().pattern()));
    }
  }

}
