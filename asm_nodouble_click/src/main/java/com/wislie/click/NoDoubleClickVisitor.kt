package com.wislie.click

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode

class NoDoubleClickVisitor(private val nextClassVisitor: ClassVisitor,
                           private val cn: ClassNode = ClassNode()): ClassVisitor(Opcodes.ASM9, cn) {
}