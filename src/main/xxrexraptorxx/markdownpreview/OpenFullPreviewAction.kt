package xxrexraptorxx.markdownpreview


import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileSystem
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.ide.lightEdit.LightEdit
import com.intellij.openapi.vfs.LightFileSystem
import com.intellij.testFramework.LightVirtualFile
import com.intellij.openapi.util.Key


class OpenFullPreviewAction : AnAction() {
override fun actionPerformed(e: AnActionEvent) {
val project = e.project ?: return
val editor = e.getData(com.intellij.openapi.actionSystem.CommonDataKeys.EDITOR)
val vf = e.getData(com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE)
if (vf == null) {
Messages.showInfoMessage(project, "Please open a markdown file first", "No file")
return
}


val path = vf.path


// initialize preview manager (set up VFS listener)
PreviewManager.initialize(project)


// create a LightVirtualFile that will be handled by our FileEditorProvider
val previewName = vf.name + ".md.preview"
val light = com.intellij.testFramework.LightVirtualFile(previewName, "")
light.putUserData(PreviewFileEditorProvider.KEY_SOURCE_PATH, path)


// open in split-right
val manager = FileEditorManager.getInstance(project)
// split current window and open
manager.openFile(light, true)
// Note: users can manually split if desired. Opening the LightVirtualFile as a new tab
// allows both the original editor and the preview to remain open independently.
}
}