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

/**
 * Defines the available operators for creating regular expressions in a
 * {@link RegularExpressionField}.
 *
 * @author Javier Godoy
 */
public enum RegularExpressionOperator {

  /** Matches strings that start with a given substring. */
  STARTS_WITH,

  /** Matches strings that end with a given substring. */
  ENDS_WITH,

  /** Matches strings that contain a given substring. */
  CONTAINS,

  /** Enables advanced custom regular expressions with validation support. */
  ADVANCED;

}
