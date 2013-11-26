package lv.neueda.task

import groovy.json.JsonBuilder
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import lv.neueda.task.parser.Spec
import lv.neueda.task.parser.SpecByMindMap

class Main {
    static main(args) {
        assert args.size() > 0: "Please provide full path to mind map file "
        Spec spec = new SpecByMindMap()
        def data = spec.parseSpecification(args[0])
        def errors = []
        def restClient = new HTTPBuilder("http://neueda.jelastic.dogado.eu")
        println "Processing ${data.name}"

        data.testSuites.each { test ->
            println "Processing ${test.name}"

            def method
            switch (test.request.method.toLowerCase()) {
                case 'post': method = Method.POST
                    break
                default:
                    throw new IllegalArgumentException('Method ${test.request.method.toLowerCase()} is not supported')
            }
            def path = test.request.path

            test.testCases.each { testCase ->
                println "Running test case ${testCase.name}"
                def jsonBuilder = new JsonBuilder()
                jsonBuilder(testCase.variables)
                restClient.request(method, ContentType.JSON) { request ->
                    uri.path = '/calculator' + path
                    body = jsonBuilder.toString()


                    response.success = { resp, xml ->
                        assert xml != null
                        if (testCase.result != xml.get("result")) {
                            errors.add("Error while processing test case ${testCase.name} in test suit ${test.name} \n expected ${testCase.result} but was  ${xml.get("result")}")
                        }
                        println ""
                    }
                }
            }
        }
        println "Errors found ${errors.size()}"
        if (errors.size() == 0) println "Congratulation no errors found in REST calculator"
    }
}
