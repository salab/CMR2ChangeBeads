package jp.ac.titech.cs.se.cmr2ChangeBeads

import org.kohsuke.args4j.Argument
import org.kohsuke.args4j.Option
import java.nio.file.InvalidPathException
import java.nio.file.Path
import java.nio.file.Paths

class ArgOption {
    companion object {
        lateinit var argOption: ArgOption
    }

    @Argument(required = true, metaVar = "[path ...]", usage = "ChangeMacroRecorder log file path or directory path")
    var argPathList: MutableList<String> = mutableListOf()

    var destPath: Path = Paths.get("")

    @Option(name = "-l", aliases = ["--log"], usage = "output log to stdout")
    var log: Boolean = false

    @Option(required = true, name = "-d", aliases = ["--dest"], metaVar = "<path>", usage = "path to output repository")
    fun setDestPathFromStr(strPath: String) {
        try {
            destPath = Paths.get(strPath).toAbsolutePath()
        } catch (e: InvalidPathException) {
            System.err.println("Path of -d option ($strPath) is invalid.")
        }
    }
}