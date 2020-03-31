package jp.ac.titech.cs.se.cmr2ChangeBeads.models

import jp.ac.titech.cs.se.cmr2ChangeBeads.ArgOption
import jp.ac.titech.cs.se.cmr2ChangeBeads.models.operations.AbstractOperation
import jp.ac.titech.cs.se.cmr2ChangeBeads.models.operations.DocumentOperation
import jp.ac.titech.cs.se.cmr2ChangeBeads.models.operations.FileOperation
import java.lang.Exception
import java.nio.file.Files
import javax.xml.bind.annotation.*


@XmlRootElement(name="OperationHistory")
@XmlAccessorType(XmlAccessType.FIELD)
class OperationHistory {
    @XmlElementWrapper(name="operations")
    @XmlElements(
        XmlElement(name="fileOperation", type= FileOperation::class),
        XmlElement(name="documentOperation", type= DocumentOperation::class)
    )
    var operations = mutableListOf<AbstractOperation>()

    fun getInitialTextMap(): MutableMap<String, StringBuilder> {
        val textMap = mutableMapOf<String, StringBuilder>()
        operations.forEach {
            if (it !is FileOperation) return@forEach
            if (it.action != "OPENED") return@forEach
            textMap[it.path.toString()] = StringBuilder(it.code)
            try {
                Files.createDirectories(ArgOption.argOption.destPath.resolve(it.path).parent)
            } catch (e: Exception) {
                println(e.stackTrace)
            }
        }
        return textMap
    }

    fun getDocumentOperationList(): MutableList<DocumentOperation> {
        val docOpList = mutableListOf<DocumentOperation>()
        // DocumentOperationへのキャストを行う必要があるためfilterを使わず手動で作成する
        operations.forEach {
            if (it is DocumentOperation) docOpList.add(it)
        }
        return docOpList
    }

    fun getRepositoryData(): RepositoryData {
        return createRepositoryData()
    }

    fun getProjectName(): String {
        val firstFileOperation = operations.find {
            if (it !is FileOperation) return@find false
            if (it.action != "OPENED") return@find false
            return@find true
        } ?: throw IllegalStateException("FileOperation with open action is not exist")
        return firstFileOperation.project
    }

    fun add(addedHistory: OperationHistory) {
        this.operations.addAll(addedHistory.operations)
    }

    fun sortOperation() {
        this.operations.sortBy { it.time }
    }
}
