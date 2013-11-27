package lv.neueda.homework.specification

import groovy.transform.ToString


@ToString(includePackage = false)
class TestSuite {
    String name
    Request request
    List<TestCase> testCases = []
}
