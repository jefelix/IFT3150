package ca.umontreal.ift3150.js.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.ViewPart;

import ca.umontreal.ift3150.js.Activator;

/**
 * Crée une vue pour Eclipse.
 * 
 * Elle contient un tableau qui permet 
 * de choisir quelles métriques afficher
 * ou colorer.
 * 
 * Le modèle associé au tableau
 * est la classe Data.
 *
 */
public class PluginView extends ViewPart{

	public static final String ID = "ca.umontreal.ift3150.js.ui.PluginView";
	public static TableViewer viewer;
	private Action runAction;
	private Action removeAction;
	private final Image CHECKED = getImageDescriptor("checked.gif").createImage();
	private final Image UNCHECKED = getImageDescriptor("unchecked.gif").createImage();

	public PluginView(){
		super();
	}

	public void createPartControl(Composite parent) {
		GridLayout layout = new GridLayout(2, false);
		parent.setLayout(layout);
		createViewer(parent);
		createActions();
		createToolbar();
	}

	private void createViewer(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		createColumns(parent, viewer);
		final Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		viewer.setContentProvider(new ArrayContentProvider());
		// Get the content for the viewer, setInput will call getElements in the
		// contentProvider
		viewer.setInput(ModelProvider.data);
		// Make the selection available to other views
		getSite().setSelectionProvider(viewer);

		// Layout the viewer
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 2;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		viewer.getControl().setLayoutData(gridData);
	}

	public TableViewer getViewer() {
		return viewer;
	}

	private void createColumns(final Composite parent, final TableViewer viewer) {
		String[] titles = { "Text information", "Color", "Metric title", "Metric type", "Values", "Settings" };
		int[] bounds = { 130, 50, 200, 200, 200, 100};

		// Text checkbox
		TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return null;
			}

			@Override
			public Image getImage(Object element) {
				if (((Data) element).getIsTextChecked()) {
					return CHECKED;
				} else {
					return UNCHECKED;
				}
			}
		});
		col.setEditingSupport(new CheckboxTextEditingSupport(viewer));

		// Color checkbox
		col = createTableViewerColumn(titles[1], bounds[1], 1);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return null;
			}

			@Override
			public Image getImage(Object element) {
				if (((Data) element).getIsColorChecked()) {
					return CHECKED;
				} else {
					return UNCHECKED;
				}
			}
		});
		col.setEditingSupport(new CheckboxColorEditingSupport(viewer));
		
		// Metric title
		col = createTableViewerColumn(titles[2], bounds[2], 2);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Data p = (Data) element;
				return p.getMetricTitle();
			}
		});

		// Metric type
		col = createTableViewerColumn(titles[3], bounds[3], 3);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Data p = (Data) element;
				return p.getMetricType();
			}
		});

		// Values
		col = createTableViewerColumn(titles[4], bounds[4], 4);
		col.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				cell.setText(((Data) cell.getElement()).getMinValue()+" - "+((Data) cell.getElement()).getMaxValue());
			}
		});

		// Settings
		col = createTableViewerColumn(titles[5], bounds[5], 5);
		
		col.setLabelProvider(new ColumnLabelProvider(){
			Map<Object, Button> buttons = new HashMap<Object, Button>();
			Button button;
			@Override
			public void update(ViewerCell cell) {
				TableItem item = (TableItem) cell.getItem();
				final Data data = (Data) cell.getElement();
				
				if(buttons.containsKey(cell.getElement()))
				{
					button = buttons.get(cell.getElement());
				}
				else
				{
					button = new Button((Composite) cell.getViewerRow().getControl(),SWT.PUSH);
					button.setText("Settings");
					if(!data.getSupportColor()){
						button.setEnabled(false);
					}
					button.addSelectionListener(new SelectionListener() {
						public void widgetSelected(SelectionEvent event) {
							ColorSettingsWindow window = new ColorSettingsWindow(parent.getShell(), data);
							window.setBlockOnOpen(true);
							window.open();
						}
						public void widgetDefaultSelected(SelectionEvent event) {
							
						}
					});
					buttons.put(cell.getElement(), button);
				}
				
				TableEditor editor = new TableEditor(item.getParent());
				editor.grabHorizontal  = true;
				editor.grabVertical = true;
				editor.setEditor(button , item, cell.getColumnIndex());
				editor.layout();
			}
		});
	}

	private TableViewerColumn createTableViewerColumn(String title, int bounds, final int colNumber) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bounds);
		column.setResizable(true);
		column.setMoveable(true);
		return viewerColumn;
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	private void createActions() {
		runAction = new Action("Run") {
			public void run() {
				ModelProvider.parser.printTextInfo(getMetricsToMark());
				ModelProvider.parser.printNumericInfo(getMetricsToColor());
			}
		};
		runAction.setImageDescriptor(getImageDescriptor("run.png"));
		
		removeAction = new Action("Clear editor") {
			public void run() {
				ModelProvider.parser.clear();
			}
		};
		removeAction.setImageDescriptor(getImageDescriptor("remove.png"));
	}

	private void createToolbar() {
		IToolBarManager mgr = getViewSite().getActionBars().getToolBarManager();
		mgr.add(runAction);
		mgr.add(removeAction);
	}

	/**
	 * Returns the image descriptor with the given relative path.
	 */
	private ImageDescriptor getImageDescriptor(String relativePath) {
		String iconPath = "icons/";
		Activator plugin = Activator.getDefault();
		return plugin.imageDescriptorFromPlugin(plugin.PLUGIN_ID, iconPath+relativePath);
	}

	public ArrayList<Data> getMetricsToMark(){
		ArrayList<Data> metricsToMark = new ArrayList<Data>();
		for(Data data : ModelProvider.data){
			if(data.getIsTextChecked()){
				metricsToMark.add(data);
			}
		}
		return metricsToMark;
	}
	
	public ArrayList<Data> getMetricsToColor(){
		ArrayList<Data> metricsToColor = new ArrayList<Data>();
		for(Data data : ModelProvider.data){
			if(data.getIsColorChecked()){
				metricsToColor.add(data);
			}
		}
		return metricsToColor;
	}

}
