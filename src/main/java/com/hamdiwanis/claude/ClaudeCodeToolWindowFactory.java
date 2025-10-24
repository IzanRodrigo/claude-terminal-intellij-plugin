package com.hamdiwanis.claude;

import com.intellij.notification.NotificationType;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.terminal.ShellTerminalWidget;
import org.jetbrains.plugins.terminal.TerminalView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

public class ClaudeCodeToolWindowFactory implements ToolWindowFactory {
    public static final String TOOL_WINDOW_ID = "Claude Code";
    public static final com.intellij.openapi.util.Key<ShellTerminalWidget> WIDGET_KEY =
            com.intellij.openapi.util.Key.create("CLAUDE_CODE_WIDGET");
    private static final com.intellij.openapi.util.Key<Boolean> AUTORUN_DONE_KEY =
            com.intellij.openapi.util.Key.create("CLAUDE_CODE_AUTORUN_DONE");

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        JPanel panel = new JPanel(new BorderLayout());
        String workDir = project.getBasePath() != null ? project.getBasePath() : System.getProperty("user.home");

        // 仍用 TerminalView 创建 widget（最兼容），但随后马上隐藏底部 Terminal 工具窗
        ShellTerminalWidget widget = TerminalView.getInstance(project)
                .createLocalShellWidget(workDir, TOOL_WINDOW_ID);

        // 获取终端组件并添加键盘监听器，处理 Shift+Enter 和 Command/Ctrl+Enter
        JComponent terminalComponent = widget.getComponent();
        setupKeyboardShortcuts(terminalComponent, widget);

        panel.add(terminalComponent, BorderLayout.CENTER);

        Content content = ContentFactory.getInstance().createContent(panel, "", false);
        content.putUserData(WIDGET_KEY, widget);
        toolWindow.getContentManager().addContent(content);

        // 关键：把底部 Terminal 工具窗收起，避免用户看到两个终端
        ToolWindow term = ToolWindowManager.getInstance(project).getToolWindow("Terminal");
        if (term != null && term.isVisible()) {
            term.hide(null);
        }

        if (Boolean.TRUE.equals(content.getUserData(AUTORUN_DONE_KEY))) return;
        content.putUserData(AUTORUN_DONE_KEY, true);

        ApplicationManager.getApplication().invokeLater(() -> autorun(project, widget, workDir));
    }

    /**
     * 配置终端组件的键盘快捷键，处理 Shift+Enter 和 Command/Ctrl+Enter
     */
    private void setupKeyboardShortcuts(JComponent terminalComponent, ShellTerminalWidget widget) {
        terminalComponent.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                int modifiers = e.getModifiersEx();

                // 检测 Shift+Enter
                if (keyCode == KeyEvent.VK_ENTER && (modifiers & KeyEvent.SHIFT_DOWN_MASK) != 0) {
                    e.consume(); // 阻止默认的 Enter 行为
                    insertNewline(widget);
                }
                // 检测 Command+Enter (Mac) 或 Ctrl+Enter (Windows/Linux)
                else if (keyCode == KeyEvent.VK_ENTER &&
                    ((modifiers & KeyEvent.META_DOWN_MASK) != 0 || (modifiers & KeyEvent.CTRL_DOWN_MASK) != 0)) {
                    e.consume(); // 阻止默认的 Enter 行为
                    insertNewline(widget);
                }
            }
        });
    }

    /**
     * 在终端中插入换行符，用于多行输入
     */
    @SuppressWarnings("deprecation")
    private void insertNewline(ShellTerminalWidget widget) {
        try {
            // 发送换行符到终端，但不执行命令
            // 注意：getTerminalStarter() 虽然已废弃，但在 IntelliJ 2023.2 中仍是兼容的方式
            var starter = widget.getTerminalStarter();
            if (starter != null) {
                starter.sendString("\n", false);
            }
        } catch (Exception e) {
            // 静默处理异常，避免中断用户操作
        }
    }

    @SuppressWarnings("unused")
    private void autorun(Project project, ShellTerminalWidget widget, String workDir) {
        ClaudeCodeUtils.exec(project, widget, "claude");
    }
}