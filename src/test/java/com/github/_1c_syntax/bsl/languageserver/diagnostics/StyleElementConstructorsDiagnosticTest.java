/*
 * This file is a part of BSL Language Server.
 *
 * Copyright © 2018-2020
 * Alexey Sosnoviy <labotamy@gmail.com>, Nikita Gryzlov <nixel2007@gmail.com> and contributors
 *
 * SPDX-License-Identifier: LGPL-3.0-or-later
 *
 * BSL Language Server is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * BSL Language Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with BSL Language Server.
 */
package com.github._1c_syntax.bsl.languageserver.diagnostics;

import org.eclipse.lsp4j.Diagnostic;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github._1c_syntax.bsl.languageserver.util.Assertions.assertThat;

class StyleElementConstructorsDiagnosticTest extends AbstractDiagnosticTest<StyleElementConstructorsDiagnostic> {
  StyleElementConstructorsDiagnosticTest() {
    super(StyleElementConstructorsDiagnostic.class);
  }

  @Test
  void test() {

    List<Diagnostic> diagnostics = getDiagnostics();

    assertThat(diagnostics).hasSize(9);
    assertThat(diagnostics, true)
      .hasRange(2, 12, 2, 37)
      .hasRange(3, 12, 3, 33)
      .hasRange(4, 12, 4, 25)
      .hasRange(8, 9, 8, 33)
      .hasRange(9, 9, 9, 31)
      .hasRange(10, 9, 10, 19)
      .hasRange(12, 9, 12, 23)
      .hasRange(13, 9, 13, 33)
      .hasRange(14, 9, 14, 37);

  }
}