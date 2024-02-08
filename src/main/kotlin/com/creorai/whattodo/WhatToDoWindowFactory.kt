package com.creorai.whattodo

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.LogicalPosition
import com.intellij.openapi.editor.ScrollType
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.JBColor
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.content.ContentFactory
import com.intellij.ui.treeStructure.Tree
import java.awt.Color
import javax.swing.tree.DefaultMutableTreeNode

class WhatToDoWindowFactory : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val scanService = ScanService(project)
        val root = scanService.scanProjectForTodos()

        val todoIcon = IconLoader.getIcon("/icons/todo.png", this.javaClass.classLoader)
        val fixmeIcon = IconLoader.getIcon("/icons/fixme.png", this.javaClass.classLoader)
        val noteIcon = IconLoader.getIcon("/icons/note.png", this.javaClass.classLoader)

        val fixmeColor = JBColor(Color(226, 62, 87), Color(226, 62, 87).darker())
        val noteColor = JBColor(Color(255, 195, 0), Color(255, 195, 0).darker())
        val todoColor = JBColor(Color(33, 150, 243), Color(33, 150, 243).darker())

        val tree = Tree(root).apply {
            isRootVisible = false
            cellRenderer = CustomTreeCellRenderer(todoIcon, fixmeIcon, noteIcon, todoColor, fixmeColor, noteColor)
            addTreeSelectionListener { event ->
                val node = event.path.lastPathComponent as? DefaultMutableTreeNode
                val todoItem = node?.userObject as? TodoItem
                todoItem?.let { navigateToFileLine(project, it) }
            }
        }

        val scrollPane = JBScrollPane(tree)
        val contentFactory = ContentFactory.getInstance()
        val content = contentFactory.createContent(scrollPane, "", false)
        toolWindow.contentManager.addContent(content)
    }

    private fun navigateToFileLine(project: Project, todoItem: TodoItem) {
        LocalFileSystem.getInstance().findFileByPath(todoItem.filepath)?.let { file ->
            FileEditorManager.getInstance(project).openFile(file, true)
            FileEditorManager.getInstance(project).selectedTextEditor?.let { editor ->
                val logicalPosition = LogicalPosition(todoItem.line - 1, 0)
                editor.caretModel.moveToLogicalPosition(logicalPosition)
                editor.scrollingModel.scrollTo(logicalPosition, ScrollType.CENTER)
            }
        }
    }
}
