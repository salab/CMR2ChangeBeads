package jp.ac.titech.cs.se.cmr2ChangeBeads

import org.eclipse.core.runtime.NullProgressMonitor
import org.eclipse.jdt.core.JavaCore
import org.eclipse.jdt.core.dom.AST
import org.eclipse.jdt.core.dom.ASTParser
import org.eclipse.jdt.core.dom.CompilationUnit


fun hasError(code: String, filePath: String, chunkDate: String): Boolean {
    val parser = ASTParser.newParser(AST.JLS10)
    val options = JavaCore.getOptions()
    JavaCore.setComplianceOptions(JavaCore.VERSION_10, options)
    parser.setCompilerOptions(options)
    parser.setSource(code.toCharArray())
    val unit = parser.createAST(NullProgressMonitor()) as CompilationUnit
    val visitor = MyVisitor()
    unit.accept(visitor)
    val errProblems = unit.problems.filter { it.isError }
    if (errProblems.isNotEmpty()) {
        if (ArgOption.argOption.log) {
            val errorHeader = "┌-----error in $filePath ($chunkDate)-----┐"
            println(errorHeader)
            errProblems.forEach {
                println("line[${it.sourceLineNumber}] : ${it.sourceStart} - ${it.sourceEnd}")
                println(it.message)
            }
            println("└-----------------------------------------------------┘")
        }
    }

    return errProblems.isNotEmpty()
}