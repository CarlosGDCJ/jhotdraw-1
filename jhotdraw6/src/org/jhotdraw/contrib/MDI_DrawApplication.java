/*
 * @(#)MDI_DrawApplication.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	� by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package CH.ifa.draw.contrib;

import CH.ifa.draw.application.*;
import CH.ifa.draw.framework.*;
import CH.ifa.draw.standard.*;
import CH.ifa.draw.contrib.dnd.DragNDropTool;

import javax.swing.*;

/**
 * Many applications have the ability to deal with multiple internal windows.
 * MDI_DrawApplications provides the basic facilities to make use of MDI in
 * JHotDraw. Its main tasks are to create a content for DrawApplications, which
 * is embedded in internal frames, to maintain a list with all internal frames
 * and to manage the switching between them.
 *
 * @author  Wolfram Kaiser <mrfloppy@sourceforge.net>
 * @version <$CURRENT_VERSION$>
 */
public class MDI_DrawApplication extends DrawApplication {

	/**
	 * Constructs a drawing window with a default title.
	 */
	public MDI_DrawApplication() {
		this("JHotDraw");
	}

	/**
	 * Constructs a drawing window with the given title.
	 */
	public MDI_DrawApplication(String title) {
		super(title);
	}

	/**
	 * Factory method which can be overriden by subclasses to
	 * create an instance of their type.
	 *
	 * @return	newly created application
	 */
	protected DrawApplication createApplication() {
		return new MDI_DrawApplication();
	}

	/**
	 * Creates the tools. By default only the selection tool is added.
	 * Override this method to add additional tools.
	 * Call the inherited method to include the selection tool.
	 * @param palette the palette where the tools are added.
	 */
	protected void createTools(JToolBar palette) {
		super.createTools(palette);
		Tool tool = new DragNDropTool(this);
		ToolButton tb = createToolButton(IMAGES+"SEL", "Drag N Drop Tool", tool);
		palette.add( tb );
	}

	/**
	 * Opens a new internal frame containing a new drawing.
	 */
	public void promptNew() {
		newWindow(createDrawing());
	}

	/**
	 * Method to create a new internal frame.  Applications that want
	 * to create a new internal drawing view should call this method.
	 */
	public void newWindow(Drawing newDrawing) {
		DrawingView newView = createDrawingView(newDrawing);
		getDesktop().addToDesktop(newView, Desktop.PRIMARY);
		toolDone();
	}

	protected DrawingView createInitialDrawingView() {
		return NullDrawingView.getManagedDrawingView(this);
	}

	public void newView() {
		if (!view().isInteractive()) {
			return;
		}
		String copyTitle = getDrawingTitle();
		DrawingView newView = createDrawingView();
		newView.setDrawing(view().drawing());
		getDesktop().addToDesktop(newView, Desktop.PRIMARY);
		if (copyTitle != null) {
			setDrawingTitle(copyTitle);
		}
		else {
			setDrawingTitle(getDefaultDrawingTitle());
		}
		toolDone();
	}

	protected Desktop createDesktop() {
		return new MDIDesktopPane(this);
	}

	/**
	 * Returns all the views in the application
	 */
	public DrawingView[] views() {
		return getDesktop().getAllFromDesktop(Desktop.PRIMARY);
	}
}