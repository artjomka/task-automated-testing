package lv.neueda.task.specification

import groovy.transform.ToString


@ToString(includePackage = false)
class Test {
    String name
    Request request
    List<TestCase> testCases = []
}
