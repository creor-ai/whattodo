package com.creorai.whattodo

import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import com.intellij.psi.search.GlobalSearchScope
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.MutableTreeNode

class ScanService(private val project: Project) {
    private val keywords = listOf("TODO", "FIXME", "NOTE")

    fun scanProjectForTodos(): DefaultMutableTreeNode {
        val root = DefaultMutableTreeNode("Todos")
        val psiManager = PsiManager.getInstance(project)
        val projectScope = GlobalSearchScope.projectScope(project)

        ProjectRootManager.getInstance(project).fileIndex.iterateContent { virtualFile ->
            if (!projectScope.contains(virtualFile)) return@iterateContent true
            val psiFile = psiManager.findFile(virtualFile) ?: return@iterateContent true

            psiFile.text.lines().forEachIndexed { index, line ->
                keywords.forEach { keyword ->
                    if (line.contains("$keyword:", ignoreCase = true)) {
                        val commentText = line.substringAfter("$keyword:").trim()
                        val todoItem = TodoItem(virtualFile.name, virtualFile.path, index + 1, commentText, keyword)
                        addToTree(root, todoItem)
                    }
                }
            }
            true
        }
        return root
    }

    private fun addToTree(root: DefaultMutableTreeNode, item: TodoItem) {
        val typeNode = findOrCreateChildNode(root, item.type)
        val fileNode = findOrCreateChildNode(typeNode, item.filename)
        val commentNode = DefaultMutableTreeNode("${item.comment} (Line ${item.line})").apply {
            userObject = item
        }
        fileNode.add(commentNode as MutableTreeNode)
    }

    private fun findOrCreateChildNode(parent: DefaultMutableTreeNode, childName: String): DefaultMutableTreeNode {
        parent.children().asSequence().forEach {
            if ((it as DefaultMutableTreeNode).userObject == childName) {
                return it
            }
        }
        return DefaultMutableTreeNode(childName).apply {
            parent.add(this as MutableTreeNode)
        }
    }
}
