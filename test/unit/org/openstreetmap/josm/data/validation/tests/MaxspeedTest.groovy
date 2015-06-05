package org.openstreetmap.josm.data.validation.tests

import org.openstreetmap.josm.JOSMFixture
import org.openstreetmap.josm.data.osm.OsmUtils

class MaxspeedTest extends GroovyTestCase {

    Maxspeed lanes = new Maxspeed()

    @Override
    void setUp() {
        JOSMFixture.createUnitTestFixture().init()
        lanes.initialize()
        lanes.startTest(null)
    }

    void test1() {
        lanes.check(OsmUtils.createPrimitive("way maxspeed=30z"))
        assert lanes.errors.get(0).getMessage() == "Value of maxspeed is not correct"
    }

    void test2() {
        lanes.check(OsmUtils.createPrimitive("way maxspeed=40knot"))
        assert lanes.errors.get(0).getMessage() == "Value of maxspeed is not correct"
    }

    void test3() {
        lanes.check(OsmUtils.createPrimitive("way maxspeed=50mph"))
        assert lanes.errors.get(0).getMessage() == "Value of maxspeed is not correct"
    }

    void test4() {
        lanes.check(OsmUtils.createPrimitive("way maxspeed=50"))
        assert lanes.errors.size() == 0
    }

    void test5() {
        lanes.check(OsmUtils.createPrimitive("way maxspeed=50 mph"))
        assert lanes.errors.size() == 0
    }

    void test6() {
        lanes.check(OsmUtils.createPrimitive("way maxspeed:forward=50 maxspeed:backward=50"))
        assert lanes.errors.get(0).getMessage() == "Value of maxspeed:backward and maxspeed:forward are equals. You should merge them in one tag : maxspeed"
    }

    void test7() {
        lanes.check(OsmUtils.createPrimitive("way maxspeed=50 source:maxspeed=50"))
        assert lanes.errors.get(0).getMessage() == "Value of source:maxspeed is not correct"
    }

    void test8() {
        lanes.check(OsmUtils.createPrimitive("way maxspeed=50 source:maxspeed=DE:"))
        assert lanes.errors.get(0).getMessage() == "Value of source:maxspeed is not correct"
    }

    void test9() {
        lanes.check(OsmUtils.createPrimitive("way maxspeed=50 source:maxspeed=DE:fr:"))
        assert lanes.errors.get(0).getMessage() == "Value of source:maxspeed is not correct"
    }
}
