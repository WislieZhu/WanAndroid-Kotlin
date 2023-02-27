package com.wislie.click
import com.android.build.api.instrumentation.*
import org.objectweb.asm.ClassVisitor

abstract class NoDoubleClickTransform : AsmClassVisitorFactory<InstrumentationParameters.None> {
    override fun createClassVisitor(classContext: ClassContext, nextClassVisitor: ClassVisitor): ClassVisitor {
        return NoDoubleClickVisitor(nextClassVisitor,classContext.currentClassData.className)
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
        return (classData.className.contains("com.wislie.wanandroid"))
    }
}