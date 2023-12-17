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
                visitMethodInsn(
                    INVOKESTATIC,
                    "java/lang/System",
                    "currentTimeMillis",
                    "()J",
                    false
                );
                visitVarInsn(LSTORE, 1);
                super.onMethodEnter()
            }

            override fun onMethodExit(opcode: Int) {
                visitLdcInsn("lcyy");
                visitTypeInsn(NEW, "java/lang/StringBuilder");
                visitInsn(DUP);
                visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
                visitLdcInsn("method cost = ");
                visitMethodInsn(
                    INVOKEVIRTUAL,
                    "java/lang/StringBuilder",
                    "append",
                    "(Ljava/lang/String;)Ljava/lang/StringBuilder;",
                    false
                );
                visitMethodInsn(
                    INVOKESTATIC,
                    "java/lang/System",
                    "currentTimeMillis",
                    "()J",
                    false
                );
                visitVarInsn(LLOAD, 1);
                visitInsn(LSUB);
                visitMethodInsn(
                    INVOKEVIRTUAL,
                    "java/lang/StringBuilder",
                    "append",
                    "(J)Ljava/lang/StringBuilder;",
                    false
                );
                visitLdcInsn("ms,  $className:$methodDesc");
                visitMethodInsn(
                    INVOKEVIRTUAL,
                    "java/lang/StringBuilder",
                    "append",
                    "(Ljava/lang/String;)Ljava/lang/StringBuilder;",
                    false
                );
                visitMethodInsn(
                    INVOKEVIRTUAL,
                    "java/lang/StringBuilder",
                    "toString",
                    "()Ljava/lang/String;",
                    false
                );
                visitMethodInsn(
                    INVOKESTATIC,
                    "android/util/Log",
                    "i",
                    "(Ljava/lang/String;Ljava/lang/String;)I",
                    false
                );
                visitInsn(Opcodes.POP); // 弹出返回值
                super.onMethodExit(opcode);
            }
        }
    }

    private fun isNeedVisiMethod(): Boolean {
        return true
    }
}
