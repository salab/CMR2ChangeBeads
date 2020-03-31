package jp.ac.titech.cs.se.cmr2ChangeBeads

import java.io.BufferedReader
import java.nio.file.Files
import java.io.InputStreamReader
import java.lang.Exception
import java.nio.file.Paths
import kotlin.system.exitProcess

fun initRepository(): String? {
    val globalEmail = execute(listOf("git", "config", "--global", "user.email")).toString().trim()

    val projectPath = ArgOption.argOption.destPath
    Files.createDirectories(projectPath)
    val projectPathStr = projectPath.toString()
    execute(listOf("git", "init", projectPathStr))
    execute(listOf("git", "-C", projectPathStr, "clean", "-df"))
    execute(listOf("git", "-C", projectPathStr, "config", "user.name", committerName))
    execute(listOf("git", "-C", projectPathStr, "config", "user.email", globalEmail))

    return projectPathStr
}

// 外部オプションを実行
// cmd: コマンドと引数のリスト
fun execute(cmd: List<String>): StringBuilder{
    lateinit var process: Process

    try{
        process = ProcessBuilder(cmd).start()
    }catch(e: Exception){
        System.err.println("cmd : $cmd")
        e.printStackTrace()
        exitProcess(1)
    }

    val sBuilder = StringBuilder()
    BufferedReader(InputStreamReader(process.inputStream)).use {
        var line = it.readLine()
        while(line != null){
            sBuilder.append(line).append('\n')
            line = it.readLine()
        }
    }
    val exitCode = process.waitFor()
    return sBuilder
}
