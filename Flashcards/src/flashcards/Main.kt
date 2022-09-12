package flashcards

import kotlin.math.min

fun main(args: Array<String>) {
    val parsedArgs = parseArguments(args)

    val db = Flashcards(parsedArgs.import, parsedArgs.export)
    db.takeActions()
}

private fun parseArguments(args: Array<String>): ImportExportFilenames {
    var import: String? = null
    var export: String? = null

    val arguments = Array<String>(4, { i -> "" })
    for (i in 0 until min(args.size, arguments.size)) {
        arguments[i] = args[i]
    }

    if (arguments[0].equals("-import", true)) {
        import = arguments[1]
    } else if (arguments[2].equals("-import", true)) {
        import = arguments[3]
    }

    if (arguments[0].equals("-export", true)) {
        export = arguments[1]
    } else if (arguments[2].equals("-export", true)) {
        export = arguments[3]
    }

    return ImportExportFilenames(import, export)
}

data class ImportExportFilenames(val import: String?, val export: String?)






