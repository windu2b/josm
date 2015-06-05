// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.data.validation.tests;

import static org.openstreetmap.josm.tools.I18n.tr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Pattern;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.data.osm.OsmPrimitive;
import org.openstreetmap.josm.data.validation.Severity;
import org.openstreetmap.josm.data.validation.Test.TagTest;
import org.openstreetmap.josm.data.validation.TestError;
import org.openstreetmap.josm.tools.Predicates;
import org.openstreetmap.josm.tools.Utils;

/**
 * Test that validates {@code maxspeed:} tags.
 *
 */
public class Maxspeed extends TagTest {
    protected static Pattern maxspeedPattern = Pattern.compile("^((\\+?\\d*\\.?\\d*)(\\s(mph|knots|kmh))?|signals|none|walk)$");

    protected static Pattern sourceOrZoneMaxspeedPattern = Pattern.compile("^([A-Z]{2}):([0-9a-zA-Z_-]+)(:[0-9a-zA-Z_-]+)+$");

    private static final String[] BLACKLIST = {
    };

    /**
     * Constructs a new {@code Maxspeed} test.
     */
    public Maxspeed() {
        super(tr("Maxspeed"),
              tr("This tests for maxspeeds, which are usually errors."));
    }

    protected void checkMaxspeedByKey(final OsmPrimitive p, String maxspeedKey, Pattern pattern, int errorCode, String errorMessage) {
        final Collection<String> keysForPattern = new ArrayList<>(Utils.filter(p.keySet(),
                Predicates.stringContainsPattern(Pattern.compile(maxspeedKey))));
        keysForPattern.removeAll(Arrays.asList(BLACKLIST));
        if (keysForPattern.isEmpty()) {
            // nothing to check
            return;
        }
        String value = p.get(keysForPattern.iterator().next());
        if( !pattern.matcher(value).find()) {
            errors.add(new TestError(this, Severity.WARNING, tr(errorMessage, maxspeedKey), errorCode, p));
        }
    }

    protected void checkMaxspeed(final OsmPrimitive p) {
        final Float backward = Float.parseFloat(Utils.firstNonNull(p.get("maxspeed:backward"), 0.0).toString());
        final Float forward = Float.parseFloat(Utils.firstNonNull(p.get("maxspeed:forward"), 0.0).toString());
        try {
        if (Float.compare(backward, forward) == 0 && backward != 0) {
            System.out.println("backward:" + backward);
            System.out.println("forward:" + forward);
            errors.add(new TestError(this, Severity.WARNING,
                    tr("Value of {0} and {1} are equals. You should merge them in one tag : {2}", "maxspeed:backward", "maxspeed:forward", "maxspeed"), 3610, p));
            }
        } catch (NumberFormatException ignore) {
            Main.debug(ignore.getMessage());
        }
    }

    @Override
    public void check(OsmPrimitive p) {
        checkMaxspeedByKey(p, "maxspeed", maxspeedPattern, 3601, "Value of {0} is not correct");
        checkMaxspeedByKey(p, "maxspeed:backward", maxspeedPattern, 3601, "Value of {0} is not correct");
        checkMaxspeedByKey(p, "maxspeed:forward", maxspeedPattern, 3601, "Value of {0} is not correct");
        checkMaxspeedByKey(p, "source:maxspeed", sourceOrZoneMaxspeedPattern, 3602, "Value of {0} is not correct");
        checkMaxspeedByKey(p, "zone:maxspeed", sourceOrZoneMaxspeedPattern, 3603, "Value of {0} is not correct");
        checkMaxspeed(p);
    }

}
