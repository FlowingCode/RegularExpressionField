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

import com.flowingcode.vaadin.addons.demo.DemoSource;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.util.Optional;
import java.util.regex.Pattern;

@DemoSource
@PageTitle("Regular Expression Field Add-on Demo")
@SuppressWarnings("serial")
@Route(value = "regular-expression-field/demo", layout = RegularExpressionFieldView.class)
public class RegularExpressionFieldDemo extends Div {

  public RegularExpressionFieldDemo() {
    RegularExpressionField field = new RegularExpressionField();
    field.setTestFieldEnabled(true);
    field.setValue(RegularExpression.of(Pattern.compile("he.*[od]")));
    field.setTestStrings("hello", "hero", "help", "held", "world", "gold");
    add(field);
    // #if vaadin eq 0
    Div div1 = new Div();
    Div div2 = new Div();
    add(div1, div2);
    field.addValueChangeListener(
        ev -> {
          div1.setText(Optional.ofNullable(ev.getValue()).map(Object::toString).orElse(""));
          div2.setText(Optional.ofNullable(ev.getValue()).map(RegularExpression::getPattern)
              .map(Pattern::pattern).orElse(""));
        });
    // #endif
  }
}
