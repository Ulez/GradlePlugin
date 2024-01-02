package com.lcy.plugin

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter

class MethodRecordClassVisitor(nextVisitor: ClassVisitor, private val className: String) :
    ClassVisitor(Opcodes.ASM9, nextVisitor) {

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        val methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions)
        return object : AdviceAdapter(Opcodes.ASM9, methodVisitor, access, name, descriptor) {
            override fun onMethodEnter() {
                if (isNeedVisiMethod()){
                    visitLdcInsn(name)
                    visitMethodInsn(INVOKESTATIC, "com/lcy/gradleplugin/Recorder", "i", "(Ljava/lang/String;)V", false);
                }
                super.onMethodEnter()
            }

            override fun onMethodExit(opcode: Int) {
                if (isNeedVisiMethod()){
                    visitLdcInsn(name)
                    visitMethodInsn(INVOKESTATIC, "com/lcy/gradleplugin/Recorder", "o", "(Ljava/lang/String;)V", false);
                }
                super.onMethodExit(opcode)
            }
        }
    }

    private fun isNeedVisiMethod(): Boolean {
        val filterClassList = listOf("Recorder")
        return filterClassList.none { className.contains(it) }
    }
}
