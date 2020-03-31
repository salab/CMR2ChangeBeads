package jp.ac.titech.cs.se.cmr2ChangeBeads.models.operations

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlElement


@XmlAccessorType(XmlAccessType.FIELD)
data class DocumentOperation(
    @field:XmlAttribute(name="offset")
    var offset: Int = 0,
    @field:XmlElement(name="inserted")
    var inserted: String = "",
    @field:XmlElement(name="deleted")
    var deleted: String = ""
): AbstractOperation() {
    override fun toString(): String {
        val formattedTime = time.format(marshalDateTimeFormat)
        return "[Doc $formattedTime] $path $offset"
    }

    fun updateTextMap(textMap: MutableMap<String, StringBuilder>): StringBuilder {
        val updatedText = textMap[path.toString()] ?: throw IllegalStateException("Uninitialized file ($path) appeared in DocumentOperation.")
        if (deleted.isNotEmpty()) {
            updatedText.delete(offset, offset+deleted.length)
        }
        if (inserted.isNotEmpty()) {
            updatedText.insert(offset, inserted)
        }
        return updatedText
    }

    fun getStringTime(): String {
        return this.time.format(marshalDateTimeFormat)
    }
}
