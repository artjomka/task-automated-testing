package lv.neueda.task.io

import lv.neueda.task.specification.Data
import lv.neueda.task.specification.Request
import lv.neueda.task.specification.Test
import lv.neueda.task.specification.TestInfo


class SpecificationParseMindMap implements SpecificationParse {


    @Override
    def parseData(specificationPath) {
        def resource = SpecificationParseMindMap.getResource(specificationPath)
        File file = new File(specificationPath)
        assert file.exists()
        assert file.isFile()
        def rootNode = new XmlParser().parse(file)

        getSpecificationData(rootNode)
    }

    def getSpecificationData(Node rootNode) {
        Data data = new Data(name: rootNode.@TEXT)
        rootNode.children().each { testNode ->
            Test test = new Test(name: testNode.@TEXT)
            def requestNode = testNode.node[0]
            Request request = new Request(method: requestNode.node[0].@TEXT.tokenize(':')[1], path: requestNode.node[1].@TEXT.tokenize(':')[1])
            test.request = request
            def testsWithoutRequest = testNode.children().minus(requestNode)
            testsWithoutRequest.each { testInfo ->
                test.testInfos.add(new TestInfo(name: testInfo.@TEXT.tokenize(':')[1],
                        variableOne: testInfo.node[0].@TEXT.tokenize(":")[1],
                        variableTwo: testInfo.node[1].@TEXT.tokenize(":")[1],
                        result: testInfo.node[2].@TEXT.tokenize(":")[1]))
            }
            data.tests.add(test)

        }

        data
    }
}
