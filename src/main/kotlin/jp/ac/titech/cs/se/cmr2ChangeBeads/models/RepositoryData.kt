package jp.ac.titech.cs.se.cmr2ChangeBeads.models

import jp.ac.titech.cs.se.cmr2ChangeBeads.execute
import jp.ac.titech.cs.se.cmr2ChangeBeads.initRepository

data class RepositoryData(
    val repositoryPath: String,
    val globalUser: String,
    val globalEmail: String
)

fun createRepositoryData(): RepositoryData {
    val repositoryPath = initRepository() ?: throw IllegalStateException("Failed to init repository.")
    val globalUser = execute(listOf("git", "config", "--global", "user.name")).toString().trim()
    val globalEmail = execute(listOf("git", "config", "--global", "user.email")).toString().trim()

    return RepositoryData(repositoryPath, globalUser, globalEmail)
}
