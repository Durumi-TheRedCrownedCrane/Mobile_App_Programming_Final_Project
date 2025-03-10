package edu.skku.cs.moappfinal

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "dbs", strict = false)
data class PerformanceDetails(
    @field:Element(name = "db", required = false)
    var dbList: PerformanceDetail? = null
)

@Root(name = "db", strict = false)
data class PerformanceDetail(
    @field:Element(name = "mt20id", required = false)
    var mt20id: String = "",

    @field:Element(name = "prfnm", required = false)
    var prfnm: String = "",

    @field:Element(name = "prfpdfrom", required = false)
    var prfpdfrom: String = "",

    @field:Element(name = "prfpdto", required = false)
    var prfpdto: String = "",

    @field:Element(name = "fcltynm", required = false)
    var fcltynm: String = "",

    @field:Element(name = "prfcast", required = false)
    var prfcast: String = "",

    @field:Element(name = "prfruntime", required = false)
    var prfruntime: String = "",

    @field:Element(name = "prfage", required = false)
    var prfage: String = "",

    @field:Element(name = "pcseguidance", required = false)
    var pcseguidance: String = "",

    @field:Element(name = "poster", required = false)
    var poster: String = "",

    @field:Element(name = "area", required = false)
    var area: String = "",

    @field:Element(name = "genrenm", required = false)
    var genrenm: String = "",

    @field:Element(name = "prfstate", required = false)
    var prfstate: String = "",

    @field:Element(name = "updatedate", required = false)
    var updatedate: String = "",

    @field:Element(name = "dtguidance", required = false)
    var dtguidance: String = "",

    @field:Element(name = "mt10id", required = false)
    var mt10id: String = "",

    @field:ElementList(name = "styurls", required = false)
    var styurls: List<StyleUrl>? = null,

    @field:ElementList(name = "relates", required = false)
    var relates: List<Relate>? = null
)

@Root(name = "styurl", strict = false)
data class StyleUrl(
    @field:Element(name = "styurl", required = false)
    var url: String = ""
)

@Root(name = "relate", strict = false)
data class Relate(
    @field:Element(name = "relatenm", required = false)
    var name: String = "",

    @field:Element(name = "relateurl", required = false)
    var url: String = ""
)

