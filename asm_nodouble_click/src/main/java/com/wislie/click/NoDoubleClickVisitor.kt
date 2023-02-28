package com.wislie.click

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Handle
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.*

class NoDoubleClickVisitor(
    private val nextClassVisitor: ClassVisitor,
    private val className: String,
    private val cn: ClassNode = ClassNode()
) :
    ClassVisitor(Opcodes.ASM9, cn) {

    override fun visitEnd() {
        forEachMethods()
        super.visitEnd()
        nextClassVisitor?.run {
            cn.accept(this)
        }
    }

    //node集合
    private val methodNodeList = mutableListOf<MethodNode>()

    private val invokeDynamicInsnNodeList = mutableListOf<InvokeDynamicInsnNode>()

    //有onClick,onItemClick(todo 描述的不准确)
    private val clickNodeList = mutableListOf(ClickNode("onClick", "(Landroid/view/View;)V"))

    private val annoNode = AnnoNode("Lcom/wislie/common/asm/CheckViewOnClick;", "(Landroid/view/View;)V")

    private val dynamicNode = DynamicNode("onClick", "Landroid/view/View\$OnClickListener;")

    private fun forEachMethods() {
        for (mn in cn.methods){

            if (mn.instructions.size() == 0) continue
            //先搞onClick
            filterOnClickMethods(mn)
            //再搞注解
            filterAnnotationNode(mn)
            //再搞lambda
            filterInvokeDynamicInsnNode(mn)
        }

        filterInvokeDynamicMethods(cn.methods)
        methodNodeList.forEach { methodNode ->
            NoDoubleClickUtil.interceptClick(methodNode)
        }
    }

    private fun filterOnClickMethods(mn: MethodNode) {
        for (clickNode in clickNodeList) {
            if (clickNode.name == mn.name && clickNode.desc == mn.desc) {
                methodNodeList.add(mn)
                break
            }
        }
    }

    private fun filterAnnotationNode(mn: MethodNode) {
        mn.visibleAnnotations?.run {
            for (visibleAnnotation in this) {
                if (annoNode.annotationSign == visibleAnnotation.desc && annoNode.methodDesc == mn.desc) {
                    methodNodeList.add(mn)
                    break
                }
            }
        }
    }

    private fun filterInvokeDynamicInsnNode(mn: MethodNode) {
        val instructions = mn.instructions
        val itr = instructions.iterator()
        while (itr.hasNext()) {
            val node: AbstractInsnNode = itr.next()
            if (node is InvokeDynamicInsnNode) {
                val nodeName = node.name
                val nodeDesc = node.desc
                if (nodeName == dynamicNode.name && nodeDesc.endsWith(dynamicNode.desc)) { //TODO 2.lambda表达式
                    invokeDynamicInsnNodeList.add(node)
                }
            }
        }
    }

    private fun filterInvokeDynamicMethods(methodList: List<MethodNode>) {
        for (dynamicNode in invokeDynamicInsnNodeList) {
            val handle = dynamicNode.bsmArgs[1] as? Handle
            if (handle != null) {
                //动态节点的name和Desc
                val nameWithDesc = handle.name + handle.desc
                for (methodNode in methodList) {
                    val methodNameWithDesc = methodNode.name + methodNode.desc
                    if (nameWithDesc == methodNameWithDesc) {
                        println("wislieZhu nameWithDesc = $nameWithDesc")
                        methodNodeList.add(methodNode)
                        break
                    }
                }
            }
        }
    }
}



