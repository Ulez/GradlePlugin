package com.lcy.plugin

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter

class MethodRecordClassVisitor(nextVisitor: ClassVisitor, private val className: String) :
    ClassVisitor(Opcodes.ASM9, nextVisitor) {

    override fun visitMethod(
        access: Int,
        name: String,
        descriptor: String,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        val methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions)
        // 判断是否为构造方法、getter、setter、toString、hashCode等方法
        val isIgnoredMethod = ("<init>" == name) || (name.startsWith("get") && descriptor.endsWith(
            "()${
                getTypeDescriptor(name.substring(3))
            }"
        )) ||
                (name.startsWith("set") && descriptor.endsWith(
                    "(${
                        getTypeDescriptor(
                            name.substring(
                                3
                            )
                        )
                    })V"
                )) ||
                ("toString" == name && "()Ljava/lang/String;" == descriptor) ||
                ("hashCode" == name && "()I" == descriptor)

        return object : AdviceAdapter(Opcodes.ASM9, methodVisitor, access, name, descriptor) {
            private val methodIdentifier =
                "${className}.${name}${getParametersDescriptor(descriptor)}"

            override fun onMethodEnter() {
                if (isNeedVisiMethod() && !isIgnoredMethod) {
                    visitLdcInsn(methodIdentifier)
                    visitMethodInsn(
                        INVOKESTATIC,
                        "com/lcy/gradleplugin/Recorder",
                        "i",
                        "(Ljava/lang/String;)V",
                        false
                    )
                }
                super.onMethodEnter()
            }

            override fun onMethodExit(opcode: Int) {
                if (isNeedVisiMethod() && !isIgnoredMethod) {
                    visitLdcInsn(methodIdentifier)
                    visitMethodInsn(
                        INVOKESTATIC,
                        "com/lcy/gradleplugin/Recorder",
                        "o",
                        "(Ljava/lang/String;)V",
                        false
                    )
                }
                super.onMethodExit(opcode)
            }
        }
    }

    private fun getParametersDescriptor(descriptor: String): String {
        val methodArguments =
            descriptor.substring(descriptor.indexOf("(") + 1, descriptor.indexOf(")"))
        return if (methodArguments.isEmpty()) {
            ""
        } else {
            ";$methodArguments"
        }
    }

    private fun getTypeDescriptor(type: String): String {
        return when (type) {
            "I" -> "I"
            "J" -> "J"
            "D" -> "D"
            "F" -> "F"
            "S" -> "S"
            "B" -> "B"
            "C" -> "C"
            "Z" -> "Z"
            else -> "Ljava/lang/Object;"
        }
    }

    private fun isNeedVisiMethod(): Boolean {
        val filterClassList = listOf("Recorder")
        return filterClassList.none { className.contains(it) }
    }
}
