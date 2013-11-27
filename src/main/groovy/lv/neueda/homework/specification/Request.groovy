package lv.neueda.homework.specification

import groovy.transform.ToString


@ToString(includePackage = false)
class Request {
    String method
    String path
}
