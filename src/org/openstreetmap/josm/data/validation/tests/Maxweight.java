// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.data.validation.tests;

import static org.openstreetmap.josm.tools.I18n.tr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Pattern;

import org.openstreetmap.josm.data.osm.OsmPrimitive;
import org.openstreetmap.josm.data.validation.Severity;
import org.openstreetmap.josm.data.validation.Test.TagTest;
import org.openstreetmap.josm.data.validation.TestError;
import org.openstreetmap.josm.tools.Predicates;
import org.openstreetmap.josm.tools.Utils;

/**
 * Test that validates {@code maxweight:} tags.
 *
 */
public class Maxweight extends TagTest {
    protected static Pattern maxweightPattern = Pattern.compile("^((\\+?\\d*(\\.\\d+)?)(\\s(t|kg))?)$");

    private static final String[] BLACKLIST = {
    };

    /**
     * Constructs a new {@code Maxweight} test.
     */
    public Maxweight() {
        super(tr("Maxweight"),
              tr("This tests for maxweights, which are usually errors."));
    }

    protected void checkMaxweightByKey(final OsmPrimitive p, String maxweightKey, Pattern pattern, int errorCode, String errorMessage) {
        final Collection<String> keysForPattern = new ArrayList<>(Utils.filter(p.keySet(),
                Predicates.stringContainsPattern(Pattern.compile(maxweightKey))));
        keysForPattern.removeAll(Arrays.asList(BLACKLIST));
        if (keysForPattern.isEmpty()) {
            // nothing to check
            return;
        }
        String value = p.get(keysForPattern.iterator().next());
        if( !pattern.matcher(value).find()) {
            errors.add(new TestError(this, Severity.WARNING, tr(errorMessage, maxweightKey), errorCode, p));
        }
    }

    protected void checkMaxweight(final OsmPrimitive p) {
        final String backward = Utils.firstNonNull(p.get("maxweight:backward"), 0.0).toString();
        final String forward = Utils.firstNonNull(p.get("maxweight:forward"), 0.0).toString();
        try {
            if (Float.compare(Float.parseFloat(backward), Float.parseFloat(forward)) == 0 && Float.parseFloat(backward) > 0) {
                errors.add(new TestError(this, Severity.WARNING,
                        tr("Value of {0} and {1} are equals. You should merge them in one tag : {2}", "maxweight:backward", "maxweight:forward", "maxweight"), 3710, p));
            }
        } catch (NumberFormatException ignore) {
            if (backward.compareTo(forward) == 0) {
            errors.add(new TestError(this, Severity.WARNING,
                    tr("Value of {0} and {1} are equals. You should merge them in one tag : {2}", "maxweight:backward", "maxweight:forward", "maxweight"), 3710, p));
            }
        }
    }

    @Override
    public void check(OsmPrimitive p) {
        checkMaxweightByKey(p, "maxweight", maxweightPattern, 3701, "Value of {0} is not correct");
        checkMaxweightByKey(p, "maxweight:backward", maxweightPattern, 3701, "Value of {0} is not correct");
        checkMaxweightByKey(p, "maxweight:forward", maxweightPattern, 3701, "Value of {0} is not correct");
        checkMaxweight(p);
    }

}
