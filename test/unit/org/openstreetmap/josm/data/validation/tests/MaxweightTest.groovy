
package org.openstreetmap.josm.data.validation.tests

import org.openstreetmap.josm.JOSMFixture
import org.openstreetmap.josm.data.osm.OsmPrimitive
import org.openstreetmap.josm.data.osm.OsmUtils
import org.openstreetmap.josm.data.osm.Way


class MaxweightTest extends GroovyTestCase {

    Maxweight lanes = new Maxweight()

    @Override
    void setUp() {
        JOSMFixture.createUnitTestFixture().init()
        lanes.initialize()
        lanes.startTest(null)
    }

    void test1() {
        lanes.check(OsmUtils.createPrimitive("way maxweight=5"))
        assert lanes.errors.size() == 0
    }

    void test2() {
        lanes.check(OsmUtils.createPrimitive("way maxweight=3.5"))
        assert lanes.errors.size() == 0
    }

    void test3() {
        lanes.check(OsmUtils.createPrimitive("way maxweight=5 t"))
        assert lanes.errors.size() == 0
    }

    void test4() {
        lanes.check(OsmUtils.createPrimitive("way maxweight=3500 kg"))
        assert lanes.errors.size() == 0
    }

    void test5() {
        lanes.check(OsmUtils.createPrimitive("way maxweight=3.5 t"))
        assert lanes.errors.size() == 0
    }

    void test6() {
        lanes.check(OsmUtils.createPrimitive("way maxweight=5t"))
        assert lanes.errors.get(0).getMessage() == "Value of maxweight is not correct"
    }

    void test7() {
        lanes.check(OsmUtils.createPrimitive("way maxweight=3500kg"))
        assert lanes.errors.get(0).getMessage() == "Value of maxweight is not correct"
    }

    void test8() {
        lanes.check(OsmUtils.createPrimitive("way maxweight=5 to"))
        assert lanes.errors.get(0).getMessage() == "Value of maxweight is not correct"
    }

    void test9() {
        lanes.check(OsmUtils.createPrimitive("way maxweight=5. t"))
        assert lanes.errors.get(0).getMessage() == "Value of maxweight is not correct"
    }

    void test10() {
        OsmPrimitive p = new Way();
        p.put("maxweight:forward", "5 t");
        p.put("maxweight:backward", "5 t");
        lanes.check(p)
        assert lanes.errors.get(0).getMessage() == "Value of maxweight:backward and maxweight:forward are equals. You should merge them in one tag : maxweight"
    }
}
