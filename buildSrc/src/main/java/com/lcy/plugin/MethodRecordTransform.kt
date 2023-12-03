package com.lcy.plugin

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationParameters
import org.objectweb.asm.ClassVisitor

abstract class MethodRecordTransform: AsmClassVisitorFactory<InstrumentationParameters.None> {
    override fun createClassVisitor(classContext: ClassContext, nextClassVisitor: ClassVisitor): ClassVisitor {
        System.out.println("lcyy createClassVisitor")
        return MethodRecordClassVisitor(nextClassVisitor,classContext.currentClassData.className)
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
       return (classData.className.contains("com.lcy.gradleplugin"))
    }
}