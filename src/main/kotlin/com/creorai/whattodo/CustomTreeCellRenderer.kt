package com.creorai.whattodo

import com.intellij.ui.JBColor
import java.awt.Component
import javax.swing.Icon
import javax.swing.JTree
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeCellRenderer

class CustomTreeCellRenderer(
    private val todoIcon: Icon,
    private val fixmeIcon: Icon,
    private val noteIcon: Icon,
    private val todoColor: JBColor,
    private val fixmeColor: JBColor,
    private val noteColor: JBColor
) : DefaultTreeCellRenderer() {

    override fun getTreeCellRendererComponent(
        tree: JTree?,
        value: Any?,
        sel: Boolean,
        expanded: Boolean,
        leaf: Boolean,
        row: Int,
        hasFocus: Boolean
    ): Component {
        val c = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus) as DefaultTreeCellRenderer
        val node = value as DefaultMutableTreeNode

        c.font = tree?.font?.deriveFont(0, 12.0f)
        c.icon = null

        when (node.level) {
            1 -> {
                val userObject = node.userObject as String
                when (userObject) {
                    "NOTE" -> {
                        c.foreground = noteColor
                        c.font = tree?.font?.deriveFont(1, 14.0f)
                        c.icon = noteIcon
                    }
                    "TODO" -> {
                        c.foreground = todoColor
                        c.font = tree?.font?.deriveFont(1, 14.0f)
                        c.icon = todoIcon
                    }
                    "FIXME" -> {
                        c.foreground = fixmeColor
                        c.font = tree?.font?.deriveFont(1, 14.0f)
                        c.icon = fixmeIcon
                    }
                }
            }
            2 -> c.font = tree?.font?.deriveFont(1, 12.0f)
            else -> c.font = tree?.font?.deriveFont(0, 12.0f)
        }

        return this
    }
}
