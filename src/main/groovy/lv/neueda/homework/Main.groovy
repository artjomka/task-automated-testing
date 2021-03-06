package lv.neueda.homework

import groovy.json.JsonBuilder
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import lv.neueda.homework.service.Spec
import lv.neueda.homework.service.SpecByMindMap
import lv.neueda.homework.util.MainHelper

import static lv.neueda.homework.Constants.*

class Main {
    static main(args) {
        assert args.size() > 0: "Please provide full path to mind map file "
        Spec spec = new SpecByMindMap()
        def data = spec.parseSpecification(args[0])
        def errors = []
        def restClient = new HTTPBuilder(SERVER_LOCATION)
        println "Processing ${data.name}"
        def methodHelper = new MainHelper()

        data.testSuites.each { testSuite ->
            println "\tProcessing test suite ${testSuite.name}"
            println ""
            def method
            method = methodHelper.getHttpRequestMethod(testSuite.request)
            def path = testSuite.request.path

            testSuite.testCases.each { testCase ->
                println "\t\tRunning test case ${testCase.name}"
                def jsonBuilder = new JsonBuilder()
                jsonBuilder(testCase.variables)
                restClient.request(method, ContentType.JSON) { request ->
                    uri.path = REST_SERVICE + path
                    body = jsonBuilder.toString()


                    response.success = { resp, xml ->
                        assert xml != null
                        if (testCase.result != xml.get(RESULT_KEY)) {
                            errors.add("Error while processing test case ${testCase.name} in test suit ${testSuite.name} " +
                                    " \n expected ${testCase.result}, but was ${xml.get(RESULT_KEY)}")
                        }
                        println ""
                    }
                }
            }
        }

        if (errors.size() == 0) println "Congratulation no errors found in REST calculator"
        else errors.each { println it }
    }
}
