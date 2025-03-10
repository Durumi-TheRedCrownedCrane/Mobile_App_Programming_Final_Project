package edu.skku.cs.moappfinal

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "dbs", strict = false)
data class Performances(
    @field:ElementList(inline = true, entry = "db")
    var performanceList: List<Performance>? = null
)

@Root(name = "db", strict = false)
data class Performance(
    @field:Element(name = "mt20id")
    var id: String = "",

    @field:Element(name = "prfnm")
    var title: String = "",

    @field:Element(name = "prfpdfrom")
    var startDate: String = "",

    @field:Element(name = "prfpdto")
    var endDate: String = "",

    @field:Element(name = "fcltynm")
    var place: String = "",

    @field:Element(name = "poster")
    var poster: String = "",

    @field:Element(name = "genrenm")
    var genre: String = "",

    @field:Element(name = "openrun")
    var openrun: String = "",

    @field:Element(name = "prfstate")
    var state: String = ""
)