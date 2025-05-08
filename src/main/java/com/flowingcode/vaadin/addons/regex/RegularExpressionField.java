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

import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import java.util.Collection;
import java.util.regex.PatternSyntaxException;

/**
 * A component for creating and testing regular expressions in real-time.
 *
 * <h2>Features:</h2>
 * <ul>
 * <li>Dropdown selection of predefined pattern types: {@code STARTS_WITH}, {@code ENDS_WITH},
 * {@code CONTAINS}.</li>
 * <li>An {@code ADVANCED} mode for custom regular expressions.</li>
 * <li>Text input field for defining the pattern.</li>
 * <li>Real-time validation of words in the list when in advanced mode.</li>
 * </ul>
 *
 * @author Javier Godoy
 */
@SuppressWarnings("serial")
public class RegularExpressionField extends CustomField<RegularExpression> {

  private final Select<RegularExpressionOperator> operatorField;

  private final TextField inputField;

  private final RegularExpressionTestField testField;

  private boolean hasPatternSyntaxError;

  private boolean testFieldEnabled;

  /** Creates a new instance of {@code RegularExpressionField}. */
  public RegularExpressionField() {
    operatorField = new Select<>();
    operatorField.setItems(RegularExpressionOperator.values());
    inputField = new TextField();

    testField = new RegularExpressionTestField();
    testField.setVisible(false);
    add(new HorizontalLayout(operatorField, inputField), testField);

    operatorField.addValueChangeListener(ev -> setTestFieldEnabled(testFieldEnabled));
    addValueChangeListener(ev -> testField.setPattern(getValue()));
  }

  @Override
  protected RegularExpression generateModelValue() {
    if (hasPatternSyntaxError) {
      onPatternSyntaxException(null, 0);
      hasPatternSyntaxError = false;
    }
    if (!inputField.isEmpty() && !operatorField.isEmpty()) {
      try {
        var r = new RegularExpression(operatorField.getValue(), inputField.getValue());
        return r;
      } catch (PatternSyntaxException e) {
        onPatternSyntaxException(e.getDescription(), e.getIndex());
        hasPatternSyntaxError = true;
        return null;
      }
    } else {
      return null;
    }
  }

  private void onPatternSyntaxException(String description, int index) {
    String errorMessage = description;
    if (description != null && index >= 0) {
      errorMessage += " near index " + index;
    }
    setErrorMessage(errorMessage);
    inputField.setInvalid(errorMessage != null);
    setInvalid(errorMessage != null);
  }

  @Override
  protected void setPresentationValue(RegularExpression newPresentationValue) {
    inputField.setValue(newPresentationValue.getInput());
    operatorField.setValue(newPresentationValue.getOperator());
  }

  @Override
  public boolean isInvalid() {
    return super.isInvalid() || inputField.isInvalid();
  }

  /**
   * Enables or disables the test field based on the given flag.
   *
   * <p>
   * If enabled, the test field is visible only when the selected operator is
   * {@link RegularExpressionOperator#ADVANCED}.
   * </p>
   *
   * @param enabled {@code true} to enable the test field, {@code false} to disable it
   */
  public void setTestFieldEnabled(boolean enabled) {
    testFieldEnabled = enabled;
    testField.setVisible(
        testFieldEnabled && operatorField.getValue() == RegularExpressionOperator.ADVANCED);
  }

  /**
   * Checks whether the test field is enabled.
   *
   * @return {@code true} if the test field is enabled, otherwise {@code false}
   */
  public boolean isTestFieldEnabled() {
    return testFieldEnabled;
  }

  /**
   * Sets the regular expression operator for this component.
   *
   * @param operator the {@link RegularExpressionOperator} to set
   */
  public void setOperator(RegularExpressionOperator operator) {
    operatorField.setValue(operator);
  }

  /**
   * Sets the test strings for validation using a variable-length argument array.
   *
   * The provided strings will be set as selectable test cases in the test field.
   *
   * @param strings the test strings to set
   */
  public void setTestStrings(String... strings) {
    testField.setItems(strings);
  }

  /**
   * Sets the test strings for validation using a collection.
   *
   * The provided strings will be set as selectable test cases in the test field.
   *
   * @param strings the collection of test strings to set
   */
  public void setTestStrings(Collection<String> strings) {
    testField.setItems(strings);
  }

}
