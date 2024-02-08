package com.creorai.whattodo

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindowManager

class WhatToDoListAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project: Project? = e.project
        project?.let {
            val toolWindowManager = ToolWindowManager.getInstance(it)
            val reviewAIToolWindow = toolWindowManager.getToolWindow("What ToDo?")
            reviewAIToolWindow?.show { }
        }
    }
}
