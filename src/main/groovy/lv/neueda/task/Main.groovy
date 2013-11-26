package lv.neueda.task

import groovy.json.JsonBuilder
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import lv.neueda.task.parser.Spec
import lv.neueda.task.parser.SpecByMindMap
import lv.neueda.task.util.MainHelper

class Main {
    static main(args) {
        assert args.size() > 0: "Please provide full path to mind map file "
        Spec spec = new SpecByMindMap()
        def data = spec.parseSpecification(args[0])
        def errors = []
        def restClient = new HTTPBuilder("http://neueda.jelastic.dogado.eu")
        println "Processing ${data.name}"
        def methodHelper = new MainHelper()

        data.testSuites.each { testSuite ->
            println "Processing ${testSuite.name}"

            def method
            method = methodHelper.getHttpRequestMethod(testSuite.request)
            def path = testSuite.request.path

            testSuite.testCases.each { testCase ->
                println "Running testSuite case ${testCase.name}"
                def jsonBuilder = new JsonBuilder()
                jsonBuilder(testCase.variables)
                restClient.request(method, ContentType.JSON) { request ->
                    uri.path = '/calculator' + path
                    body = jsonBuilder.toString()


                    response.success = { resp, xml ->
                        assert xml != null
                        if (testCase.result != xml.get("result")) {
                            errors.add("Error while processing testSuite case ${testCase.name} in testSuite suit ${testSuite.name} \n expected ${testCase.result} but was  ${xml.get("result")}")
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
