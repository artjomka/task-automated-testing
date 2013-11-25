package lv.neueda.task.io

import spock.lang.Specification



class SpecificationParseMindMapTest extends Specification {


    def "Read specification from mind map file"() {
        SpecificationParse specificationParse = new SpecificationParseMindMap()

        def result = specificationParse.parseData("C:/Users/neo1/git/task-automated-testing/out/test/task-automated-testing/lv/neueda/task/io/mindmap.xml")
        when:
        result
        then:
        result != null
        result.name == "Calculator tests"
        result.tests != null
        result.tests.size() == 4


    }
}