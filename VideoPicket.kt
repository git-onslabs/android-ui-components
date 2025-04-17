
@Composable
fun VideoPicker(
    onVideoSelected: (Uri) -> Unit,
    maxDurationSeconds: Int = 60
) {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(context, it)
            val durationMillis =
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull()
            retriever.release()

            if (durationMillis != null && durationMillis <= maxDurationSeconds * 1000) {
                onVideoSelected(it)
            } else {
                Toast.makeText(
                    context,
                    "Please select a video shorter than $maxDurationSeconds seconds.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    LaunchedEffect(Unit) {
        launcher.launch("video/*")
    }
}
