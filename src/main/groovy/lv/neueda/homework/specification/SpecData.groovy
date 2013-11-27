package lv.neueda.homework.specification

import groovy.transform.ToString


@ToString(includePackage = false)
class SpecData {
    String name
    List<TestSuite> testSuites = []



}
