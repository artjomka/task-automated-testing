package lv.neueda.task.io

import lv.neueda.task.specification.Data
import lv.neueda.task.specification.Request
import lv.neueda.task.specification.Test
import lv.neueda.task.specification.TestInfo


class SpecByMindMap implements Spec {


    @Override
    def parseSpecification(specificationPath) {
        File file = new File(specificationPath)
        assert file.exists(): "File ${file} not found"
        assert file.isFile(): "File ${file} is not file"
        def rootNode = new XmlParser().parse(file)

        getSpecificationData(rootNode.node[0])
    }

    def getSpecificationData(Node rootNode) {
        Data data = new Data(name: rootNode.@TEXT)
        rootNode.children().each { testNode ->
            Test test = new Test(name: testNode.@TEXT)
            def requestNode = testNode.find{node -> node.@TEXT.tokenize(':')[0] == "Request"}
            assert requestNode != null: "request node can't be null"
            Request request = new Request(method: requestNode.node[0].@TEXT.tokenize(':')[1].trim(), path: requestNode.node[1].@TEXT.tokenize(':')[1].trim())
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
