// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.actions;

import static org.openstreetmap.josm.gui.help.HelpUtil.ht;
import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.event.KeyEvent;
import java.io.File;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.gui.ExtendedDialog;
import org.openstreetmap.josm.gui.layer.GpxLayer;
import org.openstreetmap.josm.gui.layer.Layer;
import org.openstreetmap.josm.tools.Shortcut;

/**
 * Export the data as an OSM xml file.
 *
 * @author imi
 */
public final class SaveAction extends SaveActionBase {
    private static SaveAction instance = new SaveAction();

    /**
     * Construct the action with "Save" as label.
     */
    private SaveAction() {
        super(tr("Save"), "save", tr("Save the current data."),
                Shortcut.registerShortcut("system:save", tr("File: {0}", tr("Save")), KeyEvent.VK_S, Shortcut.CTRL));
        putValue("help", ht("/Action/Save"));
    }

    public static SaveAction getInstance() {
        return instance;
    }

    @Override public File getFile(Layer layer) {
        File f = layer.getAssociatedFile();
        if (f != null && !f.exists()) {
            f=null;
        }

        // Ask for overwrite in case of GpxLayer: GpxLayers usually are imports
        // and modifying is an error most of the time.
        if(f != null && layer instanceof GpxLayer) {
            ExtendedDialog dialog = new ExtendedDialog(
                    Main.parent,
                    tr("Overwrite"),
                    new String[] {tr("Overwrite"), tr("Cancel")}
            );
            dialog.setButtonIcons(new String[] {"save_as", "cancel"});
            dialog.setContent(tr("File {0} exists. Overwrite?", f.getName()));
            dialog.showDialog();
            int ret = dialog.getValue();
            if (ret != 1) {
                f = null;
            }
        }
        return f == null ? layer.createAndOpenSaveFileChooser() : f;
    }
}
