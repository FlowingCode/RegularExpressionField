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

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import java.util.Collection;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * A component for managing a list of words with real-time validation.
 * <p>
 * This component allows users to add and remove words while providing immediate feedback on their
 * validity based on a predefined regular expression pattern. When a word matches the given pattern,
 * it is displayed with a green background; otherwise, it is marked with red.
 *
 * @author Javier Godoy
 */
@SuppressWarnings("serial")
@CssImport("./styles/fc-regex-test-strings.css")
public final class RegularExpressionTestField extends Div {

  private Pattern pattern;

  private final Grid<String[]> grid;

  public RegularExpressionTestField() {
    grid = new Grid<>();
    grid.setClassName("fc-regex-test-strings");
    Binder<String[]> binder = new Binder<>();
    grid.getEditor().setBinder(binder);
    grid.getEditor().setBuffered(false);
    grid.addThemeVariants(GridVariant.LUMO_COMPACT);

    TextField editField = new TextField();
    editField.setWidthFull();
    grid.addColumn(item -> item[0]).setEditorComponent(editField);
    binder.forField(editField).bind(item -> item[0], (item, value) -> item[0] = value);

    grid.addComponentColumn(item -> newButton(VaadinIcon.MINUS_CIRCLE, ev -> {
      grid.getListDataView().removeItem(item);
    })).setFooter(newButton(VaadinIcon.PLUS_CIRCLE, ev -> {
      if (grid.getListDataView().getItems().noneMatch(item -> item[0].isEmpty())) {
        String[] item = new String[] {""};
        grid.getListDataView().addItem(item);
        grid.getEditor().editItem(item);
      }
    })).setFlexGrow(0).setWidth("40px");

    grid.setAllRowsVisible(true);
    grid.addItemClickListener(ev -> grid.getEditor().editItem(ev.getItem()));
    editField.addBlurListener(ev -> {
      grid.getEditor().closeEditor();
    });
    grid.getEditor().addCloseListener(ev -> {
      if (ev.getItem()[0].isEmpty()) {
        grid.getListDataView().removeItem(ev.getItem());
      }
    });
    grid.getEditor().addOpenListener(ev -> {
      editField.focus();
    });
    add(grid);

    grid.setPartNameGenerator(item -> {
      if (pattern != null) {
        return pattern.matcher(item[0]).matches() ? "match-success" : "match-fail";
      } else {
        return null;
      }
    });

  }

  private Button newButton(VaadinIcon icon,
      ComponentEventListener<ClickEvent<Button>> clickListener) {
    var button = new Button(icon.create(), clickListener);
    button.addThemeVariants(ButtonVariant.LUMO_SMALL);
    return button;
  }

  private void setItems(Stream<String> items) {
    grid.setItems(items.map(s -> new String[] {s}).toArray(String[][]::new));
  }

  public void setItems(String... items) {
    setItems(Stream.of(items));
  }

  public void setItems(Collection<String> items) {
    setItems(items.stream());
  }

  public void setPattern(RegularExpression regex) {
    setPattern(Optional.ofNullable(regex).map(RegularExpression::getPattern).orElse(null));
  }

  public void setPattern(Pattern pattern) {
    this.pattern = pattern;
    grid.getDataProvider().refreshAll();
  }

}
