package com.wislie.click

import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.tree.*

object NoDoubleClickUtil {

    fun interceptClick(methodNode: MethodNode) {
        val instructions = methodNode.instructions
        if (instructions != null && instructions.size() > 0) {
            val newInsnList = InsnList()

            println("interceptClick desc=${methodNode.desc}")
            val argumentTypes = Type.getArgumentTypes(methodNode.desc)
            val viewArgumentIndex = argumentTypes?.indexOfFirst {
                it.descriptor == "Landroid/view/View;"
            } ?: -1
            val isStaticMethod = methodNode.access and Opcodes.ACC_STATIC != 0
            println("viewArgumentIndex=$viewArgumentIndex isStaticMethod=$isStaticMethod")
            newInsnList.add(
                VarInsnNode(
                    Opcodes.ALOAD, getVisitPosition(
                        argumentTypes,
                        viewArgumentIndex,
                        isStaticMethod
                    )
                )
            )

            newInsnList.add(
                MethodInsnNode(
                    Opcodes.INVOKESTATIC,
                    "com/wislie/common/asm/CheckNetwork",
                    "isAvailable",
                    "(Landroid/view/View;)Z",
                    false
                )
            )

            val label0 = LabelNode()
            newInsnList.add(JumpInsnNode(Opcodes.IFNE, label0))
            newInsnList.add(InsnNode(Opcodes.RETURN))
            newInsnList.add(label0)

            println("xxxxxxxxxxxxxxxxxxx")
            newInsnList.add(
                VarInsnNode(
                    Opcodes.ALOAD, getVisitPosition(
                        argumentTypes,
                        viewArgumentIndex,
                        isStaticMethod
                    )
                )
            )
            newInsnList.add(
                MethodInsnNode(
                    Opcodes.INVOKESTATIC,
                    "com/wislie/common/asm/ClickInstance",
                    "isNotRepeatClick",
                    "(Landroid/view/View;)Z",
                    false
                )
            )
            val label1 = LabelNode()
            newInsnList.add(JumpInsnNode(Opcodes.IFNE, label1))
            newInsnList.add(InsnNode(Opcodes.RETURN))
            newInsnList.add(label1)

            methodNode.instructions.insert(newInsnList)
        }
    }

     private fun getVisitPosition(
        argumentTypes: Array<Type>,
        parameterIndex: Int,
        isStaticMethod: Boolean
    ): Int {
        if (parameterIndex < 0 || parameterIndex >= argumentTypes.size) {
            throw Error("getVisitPosition error")
        }
        return if (parameterIndex == 0) { //形参的view在参数中处于第一个位置
            if (isStaticMethod) { //静态方法在索引0的位置 [android.view.View]
                0
            } else { //非静态方法在索引 1 的位置 [this,android.view.View]
                1
            }
        } else { //形参的view在参数中不处于第一个位置
            getVisitPosition(
                argumentTypes,
                parameterIndex - 1,
                isStaticMethod
            ) + argumentTypes[parameterIndex - 1].size
        }
    }
}
