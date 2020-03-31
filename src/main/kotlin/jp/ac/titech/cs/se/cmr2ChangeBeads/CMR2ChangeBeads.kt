package jp.ac.titech.cs.se.cmr2ChangeBeads

import jp.ac.titech.cs.se.cmr2ChangeBeads.models.OperationHistory
import jp.ac.titech.cs.se.cmr2ChangeBeads.models.RepositoryData
import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import org.kohsuke.args4j.CmdLineException
import org.kohsuke.args4j.CmdLineParser
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import javax.xml.bind.JAXB
import kotlin.system.exitProcess


/*

 */
fun main(args: Array<String>){
    ArgOption.argOption = ArgOption()
    val cmdLineParser = CmdLineParser(ArgOption.argOption)
    try {
        cmdLineParser.parseArgument(args.toList())
    } catch (e: CmdLineException) {
        cmdLineParser.printUsage(System.err)
        exitProcess(1)
    }

    val operationHistory = getObjFromFileList(ArgOption.argOption.argPathList)
    walkObject(operationHistory)
}

fun getObjFromFileList(pathList: List<String>): OperationHistory {
    val operationHistory = OperationHistory()

    pathList.forEach {
        val path = Paths.get(it)
        if (Files.isRegularFile(path)) {
            if (!isCMRLogFile(path)) return@forEach
            operationHistory.add(getObjFromFile(path))
        }else if(Files.isDirectory(path)) {
            operationHistory.add(getObjFromDir(path))
        }
    }

    // operationを時間でソートする
    operationHistory.sortOperation()

    return operationHistory
}

fun isCMRLogFile(filePath: Path): Boolean {
    if (!Files.isRegularFile(filePath)) return false
    val ext = FilenameUtils.getExtension(filePath.fileName.toString())
    if (ext != CMRLogExt) return false

    return true
}

fun getObjFromDir(dirPath: Path): OperationHistory {
    val operationHistory = OperationHistory()
    val files = Files.list(dirPath)
    files.forEach { filePath ->
        if (!isCMRLogFile(filePath)) return@forEach
        operationHistory.add(getObjFromFile(filePath))
    }
    return operationHistory
}

fun getObjFromFile(filePath: Path): OperationHistory {
    return JAXB.unmarshal(filePath.toFile(), OperationHistory::class.java)
}

fun walkObject(operationHistory: OperationHistory) {

    val textMap = operationHistory.getInitialTextMap()
    val docOpList = operationHistory.getDocumentOperationList()
    val beforeTextMap = mutableMapOf<String, String>()
    val outputProjectName = "${operationHistory.getProjectName()}-cbt"
    // プロジェクト名を取得し出力先を設定
    ArgOption.argOption.destPath = ArgOption.argOption.destPath.resolve(outputProjectName)

    // 出力先のディレクトリを作成
    Files.createDirectories(ArgOption.argOption.destPath)

    // 出力先のディレクトリ内のファイル群を削除
    FileUtils.cleanDirectory(ArgOption.argOption.destPath.toFile())

    // リポジトリを初期化し必要な情報を収集
    val repositoryData = operationHistory.getRepositoryData()

    // ファイルを生成するディレクトリを作成
    textMap.keys.forEach { filePath ->
        Files.createDirectories(ArgOption.argOption.destPath.resolve(filePath).parent)
    }

    docOpList.forEach {
        val updatedStringBuilder = it.updateTextMap(textMap)
        val updatedText = updatedStringBuilder.toString()
        // 文法的に正しくない場合コミットをスキップ
        if (hasError(updatedText, it.path.toString(), it.getStringTime())) {
            return@forEach
        }
        // 前のファイル内容から変化が生じていない場合スキップ
        if (beforeTextMap.containsKey(it.path.toString())) {
            if (beforeTextMap[it.path.toString()] == updatedText) {
                return@forEach
            }
        }

        beforeTextMap[it.path.toString()] = updatedText
        val filePath = ArgOption.argOption.destPath.resolve(it.path.toString()).toString()
        val file = File(filePath)
        file.writeText(updatedText)
        gitCommit(repositoryData, it.getStringTime(), it.path.toString())
    }
    if (ArgOption.argOption.log) println(expCount)
}

var expCount = 0
// gitへのaddとcommitを実行する
private fun gitCommit(repositoryData: RepositoryData, editDate: String, filePath:String){
    val start = System.currentTimeMillis()
    execute(listOf("git", "-C", repositoryData.repositoryPath, "add", ".", "-f"))
    execute(listOf("git", "-C", repositoryData.repositoryPath, "commit", "--allow-empty", "-m", "\"$editDate\"",
        "--author", "\"${repositoryData.globalUser} <${repositoryData.globalEmail}>\"", "--date", "\"$editDate\""))
    val duration = System.currentTimeMillis() - start

    expCount++
    if (ArgOption.argOption.log) println("$expCount [$filePath] $duration")
}
