package lv.neueda.task.specification

import groovy.transform.ToString


@ToString(includePackage = false)
class TestCase {
    String name
    def variables = [:]
    def result
}
