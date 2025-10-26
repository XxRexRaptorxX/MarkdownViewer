package xxrexraptorxx.markdownpreview


// Map source path -> set of open preview editors
private val previews = ConcurrentHashMap<String, MutableSet<com.example.markdownpreview.PreviewFileEditor>>()


private var connection: MessageBusConnection? = null


fun registerPreview(sourcePath: String?, editor: com.example.markdownpreview.PreviewFileEditor) {
if (sourcePath == null) return
previews.computeIfAbsent(sourcePath) { ConcurrentHashMap.newKeySet() }.add(editor)
}


fun unregisterPreview(sourcePath: String?, editor: com.example.markdownpreview.PreviewFileEditor) {
if (sourcePath == null) return
previews[sourcePath]?.remove(editor)
}


fun initialize(project: Project) {
if (connection != null) return
connection = project.messageBus.connect()
connection!!.subscribe(VirtualFileManager.VFS_CHANGES, object : BulkFileListener {
override fun after(events: MutableList<out VFileEvent>) {
for (e in events) {
val path = e.path
if (previews.containsKey(path)) {
requestRenderAndUpdate(project, path)
}
}
}
})
}


fun requestRenderAndUpdate(project: Project, sourcePath: String?) {
if (sourcePath == null) return
ApplicationManager.getApplication().executeOnPooledThread {
val vFile = LocalFileSystem.getInstance().findFileByPath(sourcePath) ?: return@executeOnPooledThread
try {
val bytes = vFile.contentsToByteArray(false)
val text = String(bytes, StandardCharsets.UTF_8)
val html = renderFullHtml(text, sourcePath)
// Update UI on EDT
ApplicationManager.getApplication().invokeLater {
previews[sourcePath]?.forEach { it.setHtml(html) }
}
} catch (_: Exception) {}
}
}


private fun renderFullHtml(markdown: String, sourcePath: String): String {
val body = renderer.render(parser.parse(markdown))
return "<!doctype html><html><head><meta charset=\\\"utf-8\\\"><meta name=\\\"viewport\\\" content=\\\"width=device-width,initial-scale=1\\\">" +
"<style>body{font-family:system-ui,-apple-system,Segoe UI,Roboto,Arial;margin:1.5rem auto;max-width:1000px;padding:0 1rem;} pre{background:#f6f8fa;padding:12px;border-radius:6px;overflow:auto}</style>" +
"<title>Preview — ${sourcePath}</title></head><body>" + body + "</body></html>"
}
}