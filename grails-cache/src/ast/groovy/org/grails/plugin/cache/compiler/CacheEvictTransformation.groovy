/*
 * Copyright 2016 the original author or authors.
 *
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
 */
package org.grails.plugin.cache.compiler

import grails.plugin.cache.CacheEvict
import groovy.transform.CompileStatic
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.expr.Expression
import org.codehaus.groovy.ast.expr.MethodCallExpression
import org.codehaus.groovy.ast.expr.VariableExpression
import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.GroovyASTTransformation

import static org.codehaus.groovy.ast.ClassHelper.make
import static org.codehaus.groovy.ast.tools.GeneralUtils.block
import static org.codehaus.groovy.ast.tools.GeneralUtils.callX
import static org.codehaus.groovy.ast.tools.GeneralUtils.ifS
import static org.codehaus.groovy.ast.tools.GeneralUtils.notNullX
import static org.codehaus.groovy.ast.tools.GeneralUtils.stmt
import static org.codehaus.groovy.ast.tools.GeneralUtils.varX

/**
 * Implementation of {@link CacheEvict}
 *
 * @author Graeme Rocher
 * @author Jeff Brown
 * @since 4.0.0
 */
@GroovyASTTransformation(phase = CompilePhase.CANONICALIZATION)
@CompileStatic
class CacheEvictTransformation extends AbstractCacheTransformation {

    public static final ClassNode ANNOTATION_TYPE = make(CacheEvict)

    @Override
    protected Expression buildDelegatingMethodCall(SourceUnit sourceUnit, AnnotationNode annotationNode, ClassNode classNode, MethodNode methodNode, MethodCallExpression originalMethodCallExpr, BlockStatement newMethodBody) {
        VariableExpression cacheManagerVariableExpression = varX(GRAILS_CACHE_MANAGER_PROPERTY_NAME)
        BlockStatement cachingBlock = block()


        // Cache $_cache_cacheVariable = this.grailsCacheManager.getCache("...");
        VariableExpression cacheDeclaration = declareCache(annotationNode, cacheManagerVariableExpression, cachingBlock)

        // $_cache_cacheVariable.clear()
        cachingBlock.addStatement(
            stmt(callX(cacheDeclaration, 'clear'))
        )


        newMethodBody.addStatement(
                // if(grailsCacheManager != null)
                ifS(notNullX(varX(GRAILS_CACHE_MANAGER_PROPERTY_NAME)),
                        cachingBlock
                )
        )
        return originalMethodCallExpr
    }

    @Override
    protected ClassNode getAnnotationType() {
        return ANNOTATION_TYPE
    }
}
