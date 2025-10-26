package xxrexraptorxx.markdownpreview
import com.intellij.util.ui.JBUI


class PreviewFileEditor(
private val project: Project,
private val file: VirtualFile,
private val sourcePath: String?
) : FileEditor, Disposable {


private val component: JComponent
private var browser: JBCefBrowser? = null
private var editorPane: JEditorPane? = null


init {
val panel = JPanel()
panel.border = BorderFactory.createEmptyBorder(6, 6, 6, 6)
panel.layout = java.awt.BorderLayout()


if (JBCefApp.isSupported()) {
browser = JBCefBrowser()
panel.add(browser.component, java.awt.BorderLayout.CENTER)
} else {
editorPane = JEditorPane()
editorPane!!.isEditable = false
editorPane!!.contentType = "text/html"
editorPane!!.border = JBUI.Borders.empty(4)
panel.add(javax.swing.JScrollPane(editorPane), java.awt.BorderLayout.CENTER)
}


component = panel


// initial render (PreviewManager will call update when source exists)
PreviewManager.requestRenderAndUpdate(project, sourcePath)
}


fun setHtml(html: String) {
if (browser != null) {
browser!!.loadHTML(html)
} else if (editorPane != null) {
editorPane!!.text = html
}
}


override fun getComponent(): JComponent = component
override fun getPreferredFocusedComponent(): JComponent? = null
override fun getName(): String = "Markdown Full Preview"
override fun setState(state: com.intellij.openapi.fileEditor.FileEditorState) {}
override fun isModified(): Boolean = false
override fun isValid(): Boolean = true
override fun addPropertyChangeListener(listener: PropertyChangeListener) {}
override fun removePropertyChangeListener(listener: PropertyChangeListener) {}


override fun dispose() {
PreviewManager.unregisterPreview(sourcePath, this)
browser?.dispose()
}
}