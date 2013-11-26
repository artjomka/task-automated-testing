package lv.neueda.task.util

import groovyx.net.http.Method
import lv.neueda.task.specification.Request
import spock.lang.Shared
import spock.lang.Specification


class MainHelperTest extends Specification {

    @Shared
    private MainHelper mainHelper = new MainHelper()

    def "Test post method "() {
        def method = mainHelper.getHttpRequestMethod(new Request(method: "Post"))
        when:
        method
        then:
        method != null
        method == Method.POST
    }

    def "Test other method"() {
        when:
        def method = mainHelper.getHttpRequestMethod(new Request(method: "somethingelse"))
        then:
        thrown IllegalArgumentException

    }
}