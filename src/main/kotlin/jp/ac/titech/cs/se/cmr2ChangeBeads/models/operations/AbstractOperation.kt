package jp.ac.titech.cs.se.cmr2ChangeBeads.models.operations

import java.nio.file.Path
import java.nio.file.Paths
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.adapters.XmlAdapter
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter


val marshalDateTimeFormat: DateTimeFormatter =
    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX'['VV']'")


@XmlAccessorType(XmlAccessType.FIELD)
abstract class AbstractOperation(
    @field:XmlAttribute(name = "path")
    @field:XmlJavaTypeAdapter(PathAdapter::class)
    var path: Path = Paths.get(""),
    @field:XmlAttribute(name = "time")
    @field:XmlJavaTypeAdapter(DateAdapter::class)
    var time: ZonedDateTime = ZonedDateTime.of(1990, 1, 1, 0, 0, 0, 0, ZoneId.systemDefault()),
    @field:XmlAttribute(name = "project")
    var project: String = ""
)


class PathAdapter: XmlAdapter<String, Path>() {
    override fun marshal(v: Path): String {
        return v.toString()
    }

    override fun unmarshal(v: String): Path {
        val path = Paths.get(v)
        return path.subpath(1, path.nameCount)
    }
}


class DateAdapter: XmlAdapter<String, ZonedDateTime>() {
    override fun marshal(zonedDateTime: ZonedDateTime): String {
        return zonedDateTime.format(marshalDateTimeFormat)
    }

    override fun unmarshal(str: String): ZonedDateTime {
        return ZonedDateTime.parse(str)
    }
}
