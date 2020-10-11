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
package com.github._1c_syntax.bsl.languageserver.context.callee;

import com.github._1c_syntax.bsl.languageserver.context.DocumentContext;
import com.github._1c_syntax.bsl.languageserver.context.symbol.MethodSymbol;
import com.github._1c_syntax.bsl.languageserver.context.symbol.SymbolTree;
import com.github._1c_syntax.bsl.languageserver.utils.MdoRefBuilder;
import com.github._1c_syntax.bsl.parser.BSLParser;
import com.github._1c_syntax.bsl.parser.BSLParserBaseVisitor;
import com.github._1c_syntax.bsl.parser.BSLParserRuleContext;
import com.github._1c_syntax.mdclasses.metadata.additional.ModuleType;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.Token;
import org.eclipse.lsp4j.Location;
import org.eclipse.lsp4j.Range;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CalleeStorageFiller extends BSLParserBaseVisitor<BSLParserRuleContext> {

  private static final Set<ModuleType> DEFAULT_MODULE_TYPES =
    EnumSet.of(ModuleType.ManagerModule, ModuleType.CommonModule);

  private final CalleeStorage storage;

  private DocumentContext documentContext;

  public void fill(DocumentContext documentContext) {
    this.documentContext = documentContext;
    visitFile(documentContext.getAst());
  }


  @Override
  public BSLParserRuleContext visitCallStatement(BSLParser.CallStatementContext ctx) {

    String mdoRef = MdoRefBuilder.getMdoRef(documentContext, ctx);
    if (mdoRef.isEmpty()) {
      return super.visitCallStatement(ctx);
    }

    getMethodName(ctx).ifPresent(methodName -> checkCall(mdoRef, methodName));

    return super.visitCallStatement(ctx);
  }

  @Override
  public BSLParserRuleContext visitComplexIdentifier(BSLParser.ComplexIdentifierContext ctx) {

    String mdoRef = MdoRefBuilder.getMdoRef(documentContext, ctx);
    if (mdoRef.isEmpty()) {
      return super.visitComplexIdentifier(ctx);
    }

    getMethodName(ctx).ifPresent(methodName -> checkCall(mdoRef, methodName));

    return super.visitComplexIdentifier(ctx);
  }

  @Override
  public BSLParserRuleContext visitGlobalMethodCall(BSLParser.GlobalMethodCallContext ctx) {

    var methodName = ctx.methodName().getStart();
    var methodNameText = methodName.getText();

    documentContext.getSymbolTree().getMethods().stream()
      .filter(methodSymbol -> methodSymbol.isDeprecated()
        && methodSymbol.getName().equalsIgnoreCase(methodNameText))
      .findAny()
      .ifPresent(methodSymbol -> addMethodCall(methodSymbol));

    return super.visitGlobalMethodCall(ctx);
  }

  private void checkCall(String mdoRef, Token methodName) {
    var documentContexts = documentContext.getServerContext().getDocuments(mdoRef);
    String methodNameText = methodName.getText();

//    OUTER:
    for (Map.Entry<ModuleType, DocumentContext> entry : documentContexts.entrySet()) {
      ModuleType moduleType = entry.getKey();
      if (!DEFAULT_MODULE_TYPES.contains(moduleType)) {
        continue;
      }
      DocumentContext value = entry.getValue();
      SymbolTree symbolTree = value.getSymbolTree();
      for (MethodSymbol symbol : symbolTree.getMethods()) {
        if (symbol.getName().equalsIgnoreCase(methodNameText)) {
          addMethodCall(moduleType, symbol);
          break;
        }
      }
    }
  }

  private void addMethodCall(ModuleType moduleType, MethodSymbol methodSymbol, Range range) {
    String mdoRef = methodSymbol.getMdoRef();
    String methodName = methodSymbol.getName();

    Location location = new Location(documentContext.getUri().toString(), range);

      // List<Location> ?

    storage.callees.put(mdoRef, moduleType, methodName, location);
//
//
////    var methodNameText = methodName.getText();
//
//    var deprecationInfo = methodSymbol.getDescription()
//      .map(MethodDescription::getDeprecationInfo)
//      .orElse("");

    diagnosticStorage.addDiagnostic(methodName, info.getMessage(methodName, deprecationInfo));
  }

  private static Optional<Token> getMethodName(BSLParser.CallStatementContext ctx) {
    var modifiers = ctx.modifier();
    var methodName = getMethodName(ctx.accessCall());

    if (modifiers.isEmpty()) {
      return methodName;
    } else {
      return getMethodName(modifiers).or(() -> methodName);
    }
  }

  private static Optional<Token> getMethodName(BSLParser.AccessCallContext ctx) {
    return Optional.of(ctx.methodCall().methodName().getStart());
  }

  private static Optional<Token> getMethodName(BSLParser.ComplexIdentifierContext ctx) {
    return getMethodName(ctx.modifier());
  }

  private static Optional<Token> getMethodName(List<? extends BSLParser.ModifierContext> modifiers) {
    return modifiers.stream()
      .map(BSLParser.ModifierContext::accessCall)
      .filter(Objects::nonNull)
      .map(CalleeStorageFiller::getMethodName)
      .findFirst()
      .orElse(Optional.empty());
  }
}
