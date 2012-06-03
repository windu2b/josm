// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.gui.dialogs.changeset;

import static org.openstreetmap.josm.tools.I18n.tr;
import static org.openstreetmap.josm.tools.I18n.trc;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.actions.AutoScaleAction;
import org.openstreetmap.josm.data.osm.Changeset;
import org.openstreetmap.josm.data.osm.ChangesetCache;
import org.openstreetmap.josm.data.osm.OsmPrimitive;
import org.openstreetmap.josm.gui.HelpAwareOptionPane;
import org.openstreetmap.josm.gui.MapView;
import org.openstreetmap.josm.gui.MapView.EditLayerChangeListener;
import org.openstreetmap.josm.gui.help.HelpUtil;
import org.openstreetmap.josm.gui.layer.OsmDataLayer;
import org.openstreetmap.josm.tools.ImageProvider;

/**
 * This panel displays the properties of the currently selected changeset in the
 * {@link ChangesetCacheManager}.
 *
 */
public class ChangesetDetailPanel extends JPanel implements PropertyChangeListener{

    private JTextField tfID;
    private JTextArea taComment;
    private JTextField tfOpen;
    private JTextField tfUser;
    private JTextField tfCreatedOn;
    private JTextField tfClosedOn;
    private DonwloadChangesetContentAction actDownloadChangesetContent;
    private UpdateChangesetAction actUpdateChangesets;
    private RemoveFromCacheAction actRemoveFromCache;
    private SelectInCurrentLayerAction actSelectInCurrentLayer;
    private ZoomInCurrentLayerAction actZoomInCurrentLayerAction;

    private Changeset current = null;

    protected JPanel buildActionButtonPanel() {
        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JToolBar tb = new JToolBar(JToolBar.VERTICAL);
        tb.setFloatable(false);

        // -- remove from cache action
        tb.add(actRemoveFromCache = new RemoveFromCacheAction());
        actRemoveFromCache.initProperties(current);

        // -- changeset update
        tb.add(actUpdateChangesets = new UpdateChangesetAction());
        actUpdateChangesets.initProperties(current);

        // -- changeset content download
        tb.add(actDownloadChangesetContent =new DonwloadChangesetContentAction());
        actDownloadChangesetContent.initProperties(current);

        tb.add(actSelectInCurrentLayer = new SelectInCurrentLayerAction());
        MapView.addEditLayerChangeListener(actSelectInCurrentLayer);

        tb.add(actZoomInCurrentLayerAction = new ZoomInCurrentLayerAction());
        MapView.addEditLayerChangeListener(actZoomInCurrentLayerAction);

        addComponentListener(
                new ComponentAdapter() {
                    @Override
                    public void componentHidden(ComponentEvent e) {
                        // make sure the listener is unregistered when the panel becomes
                        // invisible
                        MapView.removeEditLayerChangeListener(actSelectInCurrentLayer);
                        MapView.removeEditLayerChangeListener(actZoomInCurrentLayerAction);
                    }
                }
        );

        pnl.add(tb);
        return pnl;
    }

    protected JPanel buildDetailViewPanel() {
        JPanel pnl = new JPanel(new GridBagLayout());

        GridBagConstraints gc = new GridBagConstraints();
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.insets = new Insets(0,0,2,3);

        //-- id
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 0.0;
        pnl.add(new JLabel(tr("ID:")), gc);

        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 0.0;
        gc.gridx = 1;
        pnl.add(tfID = new JTextField(10), gc);
        tfID.setEditable(false);

        //-- comment
        gc.gridx = 0;
        gc.gridy = 1;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 0.0;
        pnl.add(new JLabel(tr("Comment:")), gc);

        gc.fill = GridBagConstraints.BOTH;
        gc.weightx = 1.0;
        gc.weighty = 1.0;
        gc.gridx = 1;
        pnl.add(taComment= new JTextArea(5,40), gc);
        taComment.setEditable(false);

        //-- Open/Closed
        gc.gridx = 0;
        gc.gridy = 2;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 0.0;
        gc.weighty = 0.0;
        pnl.add(new JLabel(tr("Open/Closed:")), gc);

        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.gridx = 1;
        pnl.add(tfOpen= new JTextField(10), gc);
        tfOpen.setEditable(false);

        //-- Created by:
        gc.gridx = 0;
        gc.gridy = 3;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 0.0;
        pnl.add(new JLabel(tr("Created by:")), gc);

        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0;
        gc.gridx = 1;
        pnl.add(tfUser= new JTextField(""), gc);
        tfUser.setEditable(false);

        //-- Created On:
        gc.gridx = 0;
        gc.gridy = 4;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 0.0;
        pnl.add(new JLabel(tr("Created on:")), gc);

        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.gridx = 1;
        pnl.add(tfCreatedOn= new JTextField(20), gc);
        tfCreatedOn.setEditable(false);

        //-- Closed On:
        gc.gridx = 0;
        gc.gridy = 5;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 0.0;
        pnl.add(new JLabel(tr("Closed on:")), gc);

        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.gridx = 1;
        pnl.add(tfClosedOn= new JTextField(20), gc);
        tfClosedOn.setEditable(false);

        return pnl;
    }

    protected void build() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(3,3,3,3));
        add(buildDetailViewPanel(), BorderLayout.CENTER);
        add(buildActionButtonPanel(), BorderLayout.WEST);
    }

    protected void clearView() {
        tfID.setText("");
        taComment.setText("");
        tfOpen.setText("");
        tfUser.setText("");
        tfCreatedOn.setText("");
        tfClosedOn.setText("");
    }

    protected void updateView(Changeset cs) {
        String msg;
        if (cs == null) return;
        tfID.setText(Integer.toString(cs.getId()));
        String comment = cs.get("comment");
        taComment.setText(comment == null ? "" : comment);

        if (cs.isOpen()) {
            msg = trc("changeset.state", "Open");
        } else {
            msg = trc("changeset.state", "Closed");
        }
        tfOpen.setText(msg);

        if (cs.getUser() == null) {
            msg = tr("anonymous");
        } else {
            msg = cs.getUser().getName();
        }
        tfUser.setText(msg);
        SimpleDateFormat sdf = new SimpleDateFormat();

        tfCreatedOn.setText(cs.getCreatedAt() == null ? "" : sdf.format(cs.getCreatedAt()));
        tfClosedOn.setText(cs.getClosedAt() == null ? "" : sdf.format(cs.getClosedAt()));
    }

    public ChangesetDetailPanel() {
        build();
    }

    protected void setCurrentChangeset(Changeset cs) {
        current = cs;
        if (cs == null) {
            clearView();
        } else {
            updateView(cs);
        }
        actDownloadChangesetContent.initProperties(current);
        actUpdateChangesets.initProperties(current);
        actRemoveFromCache.initProperties(current);
        actSelectInCurrentLayer.updateEnabledState();
        actZoomInCurrentLayerAction.updateEnabledState();
    }

    /* ---------------------------------------------------------------------------- */
    /* interface PropertyChangeListener                                             */
    /* ---------------------------------------------------------------------------- */
    public void propertyChange(PropertyChangeEvent evt) {
        if (! evt.getPropertyName().equals(ChangesetCacheManagerModel.CHANGESET_IN_DETAIL_VIEW_PROP))
            return;
        Changeset cs = (Changeset)evt.getNewValue();
        setCurrentChangeset(cs);
    }

    /**
     * The action for removing the currently selected changeset from the changeset cache
     */
    class RemoveFromCacheAction extends AbstractAction {
        public RemoveFromCacheAction() {
            putValue(NAME, tr("Remove from cache"));
            putValue(SMALL_ICON, ImageProvider.get("dialogs", "delete"));
            putValue(SHORT_DESCRIPTION, tr("Remove the changeset in the detail view panel from the local cache"));
        }

        public void actionPerformed(ActionEvent evt) {
            if (current == null)
                return;
            ChangesetCache.getInstance().remove(current);
        }

        public void initProperties(Changeset cs) {
            setEnabled(cs != null);
        }
    }

    /**
     * Removes the selected changesets from the local changeset cache
     *
     */
    class DonwloadChangesetContentAction extends AbstractAction{
        public DonwloadChangesetContentAction() {
            putValue(NAME, tr("Download content"));
            putValue(SMALL_ICON, ImageProvider.get("dialogs/changeset","downloadchangesetcontent"));
            putValue(SHORT_DESCRIPTION, tr("Download the changeset content from the OSM server"));
        }

        public void actionPerformed(ActionEvent evt) {
            if (current == null) return;
            ChangesetContentDownloadTask task = new ChangesetContentDownloadTask(ChangesetDetailPanel.this,current.getId());
            ChangesetCacheManager.getInstance().runDownloadTask(task);
        }

        public void initProperties(Changeset cs) {
            if (cs == null) {
                setEnabled(false);
                return;
            } else {
                setEnabled(true);
            }
            if (cs.getContent() == null) {
                putValue(NAME, tr("Download content"));
                putValue(SMALL_ICON, ImageProvider.get("dialogs/changeset","downloadchangesetcontent"));
                putValue(SHORT_DESCRIPTION, tr("Download the changeset content from the OSM server"));
            } else {
                putValue(NAME, tr("Update content"));
                putValue(SMALL_ICON, ImageProvider.get("dialogs/changeset","updatechangesetcontent"));
                putValue(SHORT_DESCRIPTION, tr("Update the changeset content from the OSM server"));
            }
        }
    }

    /**
     * Updates the current changeset from the OSM server
     *
     */
    class UpdateChangesetAction extends AbstractAction{
        public UpdateChangesetAction() {
            putValue(NAME, tr("Update changeset"));
            putValue(SMALL_ICON,ImageProvider.get("dialogs/changeset","updatechangeset"));
            putValue(SHORT_DESCRIPTION, tr("Update the changeset from the OSM server"));
        }

        public void actionPerformed(ActionEvent evt) {
            if (current == null) return;
            Main.worker.submit(
                    new ChangesetHeaderDownloadTask(
                            ChangesetDetailPanel.this,
                            Collections.singleton(current.getId())
                    )
            );
        }

        public void initProperties(Changeset cs) {
            if (cs == null) {
                setEnabled(false);
                return;
            } else {
                setEnabled(true);
            }
        }
    }

    /**
     * Selects the primitives in the content of this changeset in the current
     * data layer.
     *
     */
    class SelectInCurrentLayerAction extends AbstractAction implements EditLayerChangeListener{

        public SelectInCurrentLayerAction() {
            putValue(NAME, tr("Select in layer"));
            putValue(SMALL_ICON, ImageProvider.get("dialogs", "select"));
            putValue(SHORT_DESCRIPTION, tr("Select the primitives in the content of this changeset in the current data layer"));
            updateEnabledState();
        }

        protected void alertNoPrimitivesToSelect(Collection<OsmPrimitive> primitives) {
            HelpAwareOptionPane.showOptionDialog(
                    ChangesetDetailPanel.this,
                    tr("<html>None of the objects in the content of changeset {0} is available in the current<br>"
                            + "edit layer ''{1}''.</html>",
                            current.getId(),
                            Main.main.getEditLayer().getName()
                    ),
                    tr("Nothing to select"),
                    JOptionPane.WARNING_MESSAGE,
                    HelpUtil.ht("/Dialog/ChangesetCacheManager#NothingToSelectInLayer")
            );
        }

        public void actionPerformed(ActionEvent arg0) {
            if (!isEnabled())
                return;
            if (Main.main == null || Main.main.getEditLayer() == null) return;
            OsmDataLayer layer = Main.main.getEditLayer();
            Set<OsmPrimitive> target = new HashSet<OsmPrimitive>();
            for (OsmPrimitive p: layer.data.allPrimitives()) {
                if (p.isUsable() && p.getChangesetId() == current.getId()) {
                    target.add(p);
                }
            }
            if (target.isEmpty()) {
                alertNoPrimitivesToSelect(target);
                return;
            }
            layer.data.setSelected(target);
        }

        public void updateEnabledState() {
            if (Main.main == null || Main.main.getEditLayer() == null){
                setEnabled(false);
                return;
            }
            setEnabled(current != null);
        }

        public void editLayerChanged(OsmDataLayer oldLayer, OsmDataLayer newLayer) {
            updateEnabledState();
        }
    }

    /**
     * Zooms to the primitives in the content of this changeset in the current
     * data layer.
     *
     */
    class ZoomInCurrentLayerAction extends AbstractAction implements EditLayerChangeListener{

        public ZoomInCurrentLayerAction() {
            putValue(NAME, tr("Zoom to in layer"));
            putValue(SMALL_ICON, ImageProvider.get("dialogs/autoscale", "selection"));
            putValue(SHORT_DESCRIPTION, tr("Zoom to the objects in the content of this changeset in the current data layer"));
            updateEnabledState();
        }

        protected void alertNoPrimitivesToZoomTo() {
            HelpAwareOptionPane.showOptionDialog(
                    ChangesetDetailPanel.this,
                    tr("<html>None of the objects in the content of changeset {0} is available in the current<br>"
                            + "edit layer ''{1}''.</html>",
                            current.getId(),
                            Main.main.getEditLayer().getName()
                    ),
                    tr("Nothing to zoom to"),
                    JOptionPane.WARNING_MESSAGE,
                    HelpUtil.ht("/Dialog/ChangesetCacheManager#NothingToZoomTo")
            );
        }

        public void actionPerformed(ActionEvent arg0) {
            if (!isEnabled())
                return;
            if (Main.main == null || Main.main.getEditLayer() == null) return;
            OsmDataLayer layer = Main.main.getEditLayer();
            Set<OsmPrimitive> target = new HashSet<OsmPrimitive>();
            for (OsmPrimitive p: layer.data.allPrimitives()) {
                if (p.isUsable() && p.getChangesetId() == current.getId()) {
                    target.add(p);
                }
            }
            if (target.isEmpty()) {
                alertNoPrimitivesToZoomTo();
                return;
            }
            layer.data.setSelected(target);
            AutoScaleAction.zoomToSelection();
        }

        public void updateEnabledState() {
            if (Main.main == null || Main.main.getEditLayer() == null){
                setEnabled(false);
                return;
            }
            setEnabled(current != null);
        }

        public void editLayerChanged(OsmDataLayer oldLayer, OsmDataLayer newLayer) {
            updateEnabledState();
        }
    }
}
