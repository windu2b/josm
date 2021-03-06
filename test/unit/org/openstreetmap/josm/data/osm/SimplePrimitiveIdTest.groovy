package org.openstreetmap.josm.data.osm

class SimplePrimitiveIdTest extends GroovyTestCase {

    void testNode() {
        assert SimplePrimitiveId.fromString("node/123") == new SimplePrimitiveId(123, OsmPrimitiveType.NODE)
        assert SimplePrimitiveId.fromString("n123") == new SimplePrimitiveId(123, OsmPrimitiveType.NODE)
        assert SimplePrimitiveId.fromString("node123") == new SimplePrimitiveId(123, OsmPrimitiveType.NODE)
        assert SimplePrimitiveId.fromString("n123456789123456789") == new SimplePrimitiveId(123456789123456789, OsmPrimitiveType.NODE)
    }

    void testWay() {
        assert SimplePrimitiveId.fromString("way/123") == new SimplePrimitiveId(123, OsmPrimitiveType.WAY)
        assert SimplePrimitiveId.fromString("w123") == new SimplePrimitiveId(123, OsmPrimitiveType.WAY)
        assert SimplePrimitiveId.fromString("way123") == new SimplePrimitiveId(123, OsmPrimitiveType.WAY)
        assert SimplePrimitiveId.fromString("w123456789123456789") == new SimplePrimitiveId(123456789123456789, OsmPrimitiveType.WAY)
    }

    void testRelation() {
        assert SimplePrimitiveId.fromString("relation/123") == new SimplePrimitiveId(123, OsmPrimitiveType.RELATION)
        assert SimplePrimitiveId.fromString("r123") == new SimplePrimitiveId(123, OsmPrimitiveType.RELATION)
        assert SimplePrimitiveId.fromString("rel123") == new SimplePrimitiveId(123, OsmPrimitiveType.RELATION)
        assert SimplePrimitiveId.fromString("relation123") == new SimplePrimitiveId(123, OsmPrimitiveType.RELATION)
    }

    void testFuzzy() {
        assert SimplePrimitiveId.fuzzyParse("foo relation/123 bar").toString() == "[relation 123]"
        assert SimplePrimitiveId.fuzzyParse("foo relation/123 and way/345 but also node/789").toString() == "[relation 123, way 345, node 789]"
    }

    void testMultipleIDs() {
        assert SimplePrimitiveId.fuzzyParse("node/123-125").toString() == "[node 123, node 124, node 125]"
        assert SimplePrimitiveId.fuzzyParse("n/123-125").toString() == "[node 123, node 124, node 125]"
        assert SimplePrimitiveId.fuzzyParse("node123-125").toString() == "[node 123, node 124, node 125]"
        assert SimplePrimitiveId.fuzzyParse("way/123-125").toString() == "[way 123, way 124, way 125]"
        assert SimplePrimitiveId.fuzzyParse("w/123-125").toString() == "[way 123, way 124, way 125]"
        assert SimplePrimitiveId.fuzzyParse("way123-125").toString() == "[way 123, way 124, way 125]"
        assert SimplePrimitiveId.fuzzyParse("relation/123-125").toString() == "[relation 123, relation 124, relation 125]"
        assert SimplePrimitiveId.fuzzyParse("r/123-125").toString() == "[relation 123, relation 124, relation 125]"
        assert SimplePrimitiveId.fuzzyParse("relation123-125").toString() == "[relation 123, relation 124, relation 125]"

    void testFromCopyAction() {
        assert SimplePrimitiveId.fromString("node 123") == new SimplePrimitiveId(123, OsmPrimitiveType.NODE)
        assert SimplePrimitiveId.fromString("way 123") == new SimplePrimitiveId(123, OsmPrimitiveType.WAY)
        assert SimplePrimitiveId.fromString("relation 123") == new SimplePrimitiveId(123, OsmPrimitiveType.RELATION)
    }
}
