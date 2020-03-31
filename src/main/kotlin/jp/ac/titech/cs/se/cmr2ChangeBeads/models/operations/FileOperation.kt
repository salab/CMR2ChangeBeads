package jp.ac.titech.cs.se.cmr2ChangeBeads.models.operations
import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlElement


@XmlAccessorType(XmlAccessType.FIELD)
data class FileOperation(
    @field:XmlAttribute(name="action")
    var action: String = "",
    @field:XmlElement(name = "code")
    var code: String = ""
): AbstractOperation() {
    override fun toString(): String {
        return "[FileOperation $action $time] $path"
    }
}