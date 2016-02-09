
import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
//import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Text;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class XMLEditor extends JFrame implements ActionListener {


	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				new XMLEditor();
			}
		});
	}

	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenu editMenu;
	private JMenu helpMenu;
	private JMenuItem newJMenuItem;
	private JMenuItem openJMenuItem;
	private JMenuItem saveJMenuItem;
	private JMenuItem saveAsJMenuItem;
	private JMenuItem quitJMenuItem;
	private JMenuItem undoJMenuItem;
	private JMenuItem redoJMenuItem;
	private JMenuItem cutJMenuItem;
	private JMenuItem copyJMenuItem;
	private JMenuItem pasteJMenuItem;
	private JMenu addJMenu;
	private JMenuItem attributeJMenuItem;
	private JMenuItem elementJMenuItem;
	private JMenuItem textJMenuItem;
	private JMenuItem aboutMenuItem;
	private JFileChooser fileChooser = new JFileChooser();
	private JToolBar toolBar;
	private JButton newButton;
	static JButton openButton;
	private JButton saveButton;
	private JButton undoButton;
	private JButton redoButton;
	private JButton cutButton;
	private JButton copyButton;
	private JButton pasteButton;
	private JButton attributeJbutton;
	private JButton elementJButton;
	private JButton textButton;
	private JButton quitButton;
	private JScrollPane scrollPane;
	private FileFilter filter;
	private SAXBuilder builder = new SAXBuilder();
	private Document document;
	private JTree tree;
	private boolean isNewButtonClicked = false;
	private boolean isOpenButtonClicked = false;
	private TreePath path;
	private DefaultTreeModel undoModel;
	private DefaultTreeModel redoModel;
	private String fileName = null;
	private File file;
	private Element root = null;
	//private TreeModel model;
	private Element element = null;
	String filepath = null;
	private DefaultMutableTreeNode copiedNode = new DefaultMutableTreeNode();

	XMLEditor() {
		super("XML Editor");

		int width = Toolkit.getDefaultToolkit().getScreenSize().width;
		int height = Toolkit.getDefaultToolkit().getScreenSize().height;

		setBounds(width / 8, height / 8, width / 4 * 3, height / 4 * 3);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setResizable(false);
		setVisible(true);

		menuBar = new JMenuBar();
		fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);

		newJMenuItem = new JMenuItem("New");
		newJMenuItem.addActionListener(this);
		newJMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_MASK));

		openJMenuItem = new JMenuItem("Open");
		openJMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_MASK));

		saveJMenuItem = new JMenuItem("Save");
		saveJMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,KeyEvent.CTRL_MASK));
		saveJMenuItem.addActionListener(this);

		saveAsJMenuItem = new JMenuItem("Save As");
		saveAsJMenuItem.addActionListener(this);

		quitJMenuItem = new JMenuItem("Quit");
		quitJMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_MASK));
		quitJMenuItem.addActionListener(this);

		openJMenuItem.addActionListener(this);




		fileMenu.add(newJMenuItem);
		fileMenu.add(openJMenuItem);
		fileMenu.add(saveJMenuItem);
		fileMenu.add(saveAsJMenuItem);
		fileMenu.add(quitJMenuItem);

		editMenu = new JMenu("Edit");
		editMenu.setMnemonic(KeyEvent.VK_E);

		undoJMenuItem = new JMenuItem("Undo");
		undoJMenuItem.addActionListener(this);
		undoJMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_MASK));

		redoJMenuItem = new JMenuItem("Redo");
		redoJMenuItem.addActionListener(this);
		redoJMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_MASK));

		cutJMenuItem = new JMenuItem("Cut");
		cutJMenuItem.addActionListener(this);
		cutJMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_MASK));
		copyJMenuItem = new JMenuItem("Copy");
		copyJMenuItem.addActionListener(this);
		copyJMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_MASK));
		pasteJMenuItem = new JMenuItem("Paste");
		pasteJMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_MASK));

		addJMenu = new JMenu("Add");
		attributeJMenuItem = new JMenuItem("Attribute");
		attributeJMenuItem.addActionListener(this);
		elementJMenuItem = new JMenuItem("Element");
		elementJMenuItem.addActionListener(this);
		textJMenuItem = new JMenuItem("Text");
		textJMenuItem.addActionListener(this);

		addJMenu.add(attributeJMenuItem);
		addJMenu.add(elementJMenuItem);
		addJMenu.add(textJMenuItem);

		editMenu.add(undoJMenuItem);
		editMenu.add(redoJMenuItem);
		editMenu.add(cutJMenuItem);
		editMenu.add(copyJMenuItem);
		editMenu.add(pasteJMenuItem);
		pasteJMenuItem.addActionListener(this);
		editMenu.add(addJMenu);

		helpMenu = new JMenu("Help");
		helpMenu.setMnemonic(KeyEvent.VK_H);

		aboutMenuItem = new JMenuItem("About");
		aboutMenuItem.addActionListener(this);
		helpMenu.add(aboutMenuItem);

		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(helpMenu);

//Look and feel. Does not work well with my mac, I have to find out why.
		
		
//		try{
//			for(LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
//				if(info.getName().equals("Nimbus")) {
//					UIManager.setLookAndFeel(info.getClassName());
//					break;
//				}
//
//				else {
//					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//				}
//			}
//
//		}
//
//		catch (Exception e) {
//			 
//			return;
//		}


		toolBar = new JToolBar();
		newButton = new JButton("New");
		newButton.addActionListener(this);
		newButton.setMnemonic(KeyEvent.VK_N);
		

		openButton = new JButton("Open...");
		openButton.setMnemonic(KeyEvent.VK_O);
		openButton.addActionListener(this);

		toolBar.add(newButton);
		toolBar.add(openButton);

		toolBar.addSeparator();
		saveButton = new JButton("Save");
		saveButton.setMnemonic(KeyEvent.VK_S);
		saveButton.addActionListener(this);

		toolBar.add(saveButton);

		toolBar.addSeparator();

		undoButton = new JButton("Undo");
		undoButton.addActionListener(this);

		redoButton = new JButton("Redo");
		redoButton.addActionListener(this);

		toolBar.add(undoButton);
		toolBar.add(redoButton);

		toolBar.addSeparator();

		cutButton = new JButton("Cut");
		cutButton.addActionListener(this);
		copyButton = new JButton("Copy");
		copyButton.addActionListener(this);
		pasteButton = new JButton("Paste");
		pasteButton.addActionListener(this);

		toolBar.add(cutButton);
		toolBar.add(copyButton);
		toolBar.add(pasteButton);

		toolBar.addSeparator();

		attributeJbutton = new JButton("Attribute");
		attributeJbutton.addActionListener(this);
		elementJButton = new JButton("Element");
		elementJButton.addActionListener(this);
		textButton = new JButton("Text");
		textButton.addActionListener(this);

		toolBar.add(attributeJbutton);
		toolBar.add(elementJButton);
		toolBar.add(textButton);

		toolBar.addSeparator();

		quitButton = new JButton("Quit");
		quitButton.addActionListener(this);
		quitButton.setMnemonic(KeyEvent.VK_Q);

		toolBar.add(quitButton);

		setJMenuBar(menuBar);
		add(toolBar, BorderLayout.NORTH);
		add(toolBar, new BorderLayout().NORTH);


		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				performQuitAction();
			}
		});



		activateButtons(false);

	}

	private void performQuitAction() {
		int choice = JOptionPane.showConfirmDialog(null, "Are you sure?", "Quit",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (choice == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}

	public void actionPerformed(ActionEvent e) {

		String aboutMessage = "This app was designed by Taslim Olawale Salisu";
		if (e.getSource() == quitJMenuItem || e.getSource() == quitButton) {
			performQuitAction();
		} else if (e.getSource() == aboutMenuItem) {
			JOptionPane.showMessageDialog(this, aboutMessage, "About",
					JOptionPane.INFORMATION_MESSAGE);
		}

		else if (e.getSource() == openJMenuItem || e.getSource() == openButton) {
			performOpenAction();
		}

		else if (e.getSource() == newJMenuItem || e.getSource() == newButton) {
			performNewAction();
		}
		else if (e.getSource() == elementJButton || e.getSource() == elementJMenuItem) {
			addElement();
		}

		else if(e.getSource() == cutButton || e.getSource() == cutJMenuItem) {
			cutAction();
		}

		else if(e.getSource() == copyButton || e.getSource() == copyJMenuItem){
			copyAction();
		}

		else if(e.getSource() == pasteButton || e.getSource() == pasteJMenuItem) {
			pasteAction();
		}

		else if(e.getSource() == textButton || e.getSource() == textJMenuItem) {
			addText();
		}

		else if(e.getSource() == attributeJbutton || e.getSource() == attributeJMenuItem) {
			addAttribute();
		}

		else if (e.getSource() == undoButton || e.getSource() == undoJMenuItem) {
			performUndoAction();

		}

		else if (e.getSource() == redoButton || e.getSource() == redoJMenuItem) {
			performRedoAction();
		}

		else if (e.getSource() == saveAsJMenuItem) {
			performSaveAsAction();
		}

		else if (e.getSource() == saveButton || e.getSource() == saveJMenuItem) {
			performSaveAction();
		}

	}


	public void performOpenAction() {

		try {
			if (isNewButtonClicked == false && isOpenButtonClicked == false) {

				Object[] options = {"From Local Source", "URL", "Cancel"};
				int choice = JOptionPane.showOptionDialog(this, "Please select source.", "Open", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);

				if(choice == 0) {

					filter = new FileNameExtensionFilter("XML filter",  "xml");
					fileChooser = new JFileChooser();
					fileChooser.setFileFilter(filter);
					fileChooser.setDialogTitle("Choose XML file");
					fileChooser.setApproveButtonText("Choose file");
					int returnVal = fileChooser.showOpenDialog(null);


					if(returnVal == JFileChooser.APPROVE_OPTION) {

						isOpenButtonClicked = true;
						isNewButtonClicked = true;
						
						filepath = fileChooser.getSelectedFile().getAbsolutePath();
						//System.out.println(filepath);
						

						try{
							document = builder.build(new File(fileChooser.getSelectedFile().getAbsolutePath()));
							root = document.getRootElement();
							fileName = fileChooser.getSelectedFile().getAbsolutePath();

							if(fileChooser.getSelectedFile().getName() != null){
								setTitle(fileChooser.getSelectedFile().getName());
							}

						}
						catch(Exception e) {
							 
							return;
						}
					}

					else {
						return;
						//					JOptionPane.showMessageDialog(null, "Please select a file");
					}

				}																							

				else if (choice == 1) {
					String urlString = JOptionPane.showInputDialog(null, "Please enter URL.");

					isOpenButtonClicked = true;
					isNewButtonClicked = true; 

					if(urlString != null && urlString.length() > 0) {
						try {
							URL url = new URL(urlString);
							document = builder.build(url);		
							root = document.getRootElement();

							setTitle(urlString);



						} catch (Exception e) {
							 
							return;
						}
					}

					else {
						activateButtons(false);
						isOpenButtonClicked = false;
						isNewButtonClicked = false;
						return;

					}
				}

				else {
					activateButtons(false);
					isOpenButtonClicked = false;
					isNewButtonClicked = false;
					return;
				}

				try {

					tree = new JTree(getTree(root));
					if(scrollPane != null) {
						remove(scrollPane);
					}
					scrollPane = new JScrollPane(tree);
					add(scrollPane, BorderLayout.CENTER);

					activateButtons(true);
					revalidate();
					expandTree();
					tree.updateUI();
					repaint();
				} catch (Exception e) {
					 
					return;
				}
			}

			else {
				int choice = JOptionPane.showConfirmDialog(null, "Do you want to save?", "Save?", JOptionPane.YES_NO_CANCEL_OPTION);
				if(choice == JOptionPane.YES_OPTION) {
					performSaveAction();
					isOpenButtonClicked = false;
					isNewButtonClicked = false;
					performOpenAction();
				}
				else if(choice == JOptionPane.NO_OPTION) {
					isNewButtonClicked = false;
					isOpenButtonClicked = false;
					performOpenAction();

				}
				else {
					return;
				}
			}
		} catch (Exception e) {
			 
			return;
		}

	}

	public DefaultMutableTreeNode getTree(Element root) {

		DefaultMutableTreeNode rootnode = null;
		try {
			//root = document.getRootElement();
			rootnode = new DefaultMutableTreeNode(root);
			getTreeChildren(root, rootnode);

		}
		catch(NullPointerException e) {
			 
		}
		catch (Exception e) {
			 
		}

		return rootnode;
	}

	private void getTreeChildren(Element element, DefaultMutableTreeNode rootNode) {
		List<Element> elementList = element.getChildren();
		for (Element e : elementList) {
			DefaultMutableTreeNode dmtn = new DefaultMutableTreeNode(e);
			rootNode.add(dmtn);
			List<Attribute> attributeList = e.getAttributes();
			for (Attribute a : attributeList) {
				dmtn.add(new DefaultMutableTreeNode(new Attribute(a.getName(), a.getValue())));
			}
			String t = e.getTextTrim();
			if (!t.equals("")) {
				dmtn.add(new DefaultMutableTreeNode(new Text(t)));
			}
			int numChildren = e.getChildren().size();
			if (numChildren>0) getTreeChildren(e, dmtn);
		}
	}

	public void performNewAction() {
		try {
			if(isNewButtonClicked == false && isOpenButtonClicked == false) {


				String rootString = JOptionPane.showInputDialog(null, "Please enter root element.");
				if(rootString != null && rootString.length() > 0) {
					try {

						if (rootString.contains(" ") || rootString.contains(",")) {
							JOptionPane.showMessageDialog(null, "Invalid element name.");
							performNewAction();
						}
						else {
							 element = new Element(rootString.trim());
							DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(element);
							tree = new JTree(rootNode); 
							root = element;
							tree.updateUI();
							if (scrollPane!=null) {
								remove(scrollPane);
							}
							scrollPane = new JScrollPane(tree);
							add(scrollPane, BorderLayout.CENTER);
							revalidate();
							isNewButtonClicked = true;
							isOpenButtonClicked = true;

							activateButtons(true);
						}
					} catch (Exception e) {
						 
						return;
					}

				}

				else {
					JOptionPane.showMessageDialog(null, "Please enter the root node.");

				}

			}

			else {

				int choice = JOptionPane.showConfirmDialog(null, "Do you want to save?", "Save?", JOptionPane.YES_NO_CANCEL_OPTION);
				if (choice == JOptionPane.YES_OPTION) {
					isNewButtonClicked = false;
					isOpenButtonClicked = false;
					performSaveAction();
					performNewAction();

				}
				else if(choice == JOptionPane.NO_OPTION) {
					isNewButtonClicked = false;
					isOpenButtonClicked = false;
					performNewAction();
				}
				else {
					return;
				}
			}
		} catch (Exception e) {
			 
			return;
		}


	}

	public void activateButtons(boolean b) {
		saveAsJMenuItem.setEnabled(b);
		saveJMenuItem.setEnabled(b);
		saveButton.setEnabled(b);
		editMenu.setEnabled(b);
		attributeJbutton.setEnabled(b);
		elementJButton.setEnabled(b);
		textButton.setEnabled(b);
		cutButton.setEnabled(b);
		copyButton.setEnabled(b);
		pasteButton.setEnabled(b);
		undoButton.setEnabled(b);
		redoButton.setEnabled(b);
	}

	public void addElement() {
		try {
			path = tree.getSelectionPath();
			if(path != null) {
				undoModel = new DefaultTreeModel(getTree(root));

				DefaultMutableTreeNode parent = (DefaultMutableTreeNode) path.getLastPathComponent();
				if (parent.getUserObject() instanceof Element) {

					String childString = JOptionPane.showInputDialog(null, "Please input element name.");
					if (childString != null) {

						if(childString.contains(" ") || childString.contains(",")) {
							JOptionPane.showMessageDialog(null, "Invalid element name.");
							addElement();
						}
						else {
							Element elements = new Element(childString);
							DefaultMutableTreeNode child = new DefaultMutableTreeNode(elements);
							parent.add(child);
							tree.expandPath(path);
							expandTree();
							tree.updateUI();
							revalidate();
						}

					}
				}
				else {
					JOptionPane.showMessageDialog(null, "You can't add Element to Text or Attribute.", "Add Error", JOptionPane.ERROR_MESSAGE);
				}

			}

			else {
				JOptionPane.showMessageDialog(null, "Please select a node.");
			}
		} catch (Exception e) {
			 
			return;
		}
	}



	public void cutAction() {
		try {
			path = tree.getSelectionPath();
			if(path != null){

				undoModel = new DefaultTreeModel(getTree(root));
				DefaultTreeModel treeModel = (DefaultTreeModel) tree.getModel();
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent(); // tree.getLastSelectedPathComponent
				if(selectedNode.isRoot()){
					JOptionPane.showMessageDialog(null, "Root node cannot be cut.", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else{
					copiedNode = deepClone(selectedNode);
					treeModel.removeNodeFromParent(selectedNode);

					expandTree();

				}
			}

			else {
				JOptionPane.showMessageDialog(null, "Please select a node to cut.");
			}
		} catch (Exception e) {
			 
			return;
		}
	}

	public void copyAction(){
		try {
			path = tree.getSelectionPath();
			if(path != null) {

			//	DefaultTreeModel treeModel = (DefaultTreeModel) tree.getModel();
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();  // tree.getLastSelectedPathComponent
				copiedNode = deepClone(selectedNode);
				expandTree();
				JOptionPane.showMessageDialog(null, copiedNode.toString() + " copied", "Copied", JOptionPane.INFORMATION_MESSAGE);
			}

			else {
				JOptionPane.showMessageDialog(null, "Please select a node to copy.");
			}
		} catch (Exception e) {
			 
			return;
		}
	}

	public void pasteAction() {

		try {
			path = tree.getSelectionPath();
			if(path != null) {

				undoModel = new DefaultTreeModel(getTree(root));
				DefaultMutableTreeNode parent = (DefaultMutableTreeNode) path.getLastPathComponent();
				if(copiedNode != null) {
				parent.add(copiedNode);
				expandTree();
				tree.expandPath(path);
				expandTree();
				tree.updateUI();
				revalidate();
				}
				else {
					JOptionPane.showMessageDialog(null, "There is nothing to paste");
				}
				
			}

			else {
				JOptionPane.showMessageDialog(null, "Please select a node to paste to.");
			}
		} catch (Exception e) {
			 
			return;
		}

	}

	public void addAttribute() {
		try {
			path = tree.getSelectionPath();

			if (path != null) {
				if(root != null) {
					undoModel = new DefaultTreeModel(getTree(root));
					}


				DefaultMutableTreeNode parent = (DefaultMutableTreeNode) path.getLastPathComponent();
				if (parent.getUserObject() instanceof Element) {

					JTextField name = new JTextField();
					JTextField value = new JTextField();

					Object[] fields = {"Name", name, "Value", value};
					int choice = JOptionPane.showConfirmDialog(null, fields, "Please input name and value of attribute.", JOptionPane.OK_CANCEL_OPTION);


					if (choice == JOptionPane.OK_OPTION) {
						String nameString = name.getText();
						String valueString = value.getText();

						if (nameString != null && valueString != null && nameString.length() > 0 && valueString.length() > 0) {

							if (nameString.contains(" ") || valueString.contains(" ") || nameString.contains(",") || valueString.contains(",")){
								JOptionPane.showMessageDialog(null, "Invalid attribute name");
								addAttribute();
							}

							else {
								Attribute attribute = new Attribute(nameString, valueString);
								DefaultMutableTreeNode child = new DefaultMutableTreeNode(attribute);

								parent.add(child);
								tree.expandPath(path);
								expandTree();
								tree.updateUI();
								revalidate();
								tree.repaint();
							}
						}
						else {
							JOptionPane.showMessageDialog(null, "Please enter both values");
							addAttribute();

						}
					}

					else {
						return;
					}

				}

				else {
					JOptionPane.showMessageDialog(null, "You can't add attribute to Text or Attribute.", "Add Error", JOptionPane.ERROR_MESSAGE);
				}

			}
			else {
				JOptionPane.showMessageDialog(null, "Please select a node to enter an attribute.");
			}
		} catch (Exception e) {
			 
			return;
		}
	}


	public void addText() {

		try {
			path = tree.getSelectionPath();

			if(path != null) {

				undoModel = new DefaultTreeModel(getTree(root));
				
				DefaultMutableTreeNode parent = (DefaultMutableTreeNode) path.getLastPathComponent();
				if (parent.getUserObject() instanceof Element) {
					String textString = JOptionPane.showInputDialog(null, "Please enter text.");
					if(textString != null) {
						Text text = new Text(textString);



						//System.out.println(parent.getUserObject().getClass());
						DefaultMutableTreeNode textNode = new DefaultMutableTreeNode(text);
						parent.add(textNode);
						tree.expandPath(path);
						expandTree();
						tree.updateUI();

						revalidate();
					}

					else {
						return;
					}
				}

				else {
					JOptionPane.showMessageDialog(null, "You can't add text to Text or Attribute.", "Add Error", JOptionPane.ERROR_MESSAGE);
				}


			}

			else {
				JOptionPane.showMessageDialog(null, "Please select a node to enter a text.");
			}
		} catch (Exception e) {
			 
			return;
		}
	}

	private void expandTree() {
		for(int i = 0; i < tree.getRowCount(); i++) {
			tree.expandRow(i);
		}
	}

	private void performUndoAction() {
		try {
			if (tree.getModel() != null) {
				redoModel = (DefaultTreeModel) tree.getModel();
			}

			if(undoModel != null) {
				tree.setModel(undoModel);
				expandTree();
				tree.updateUI();
				revalidate();
				repaint();
			}
		} catch (Exception e) {
			 
			return;
		}


	}

	private void performRedoAction() {
		try {
			tree.setModel(redoModel);
			expandTree();
			tree.updateUI();
			revalidate();
			repaint();
		} catch (Exception e) {
			 
			return;
		}
	}

		private DefaultMutableTreeNode deepClone(DefaultMutableTreeNode selectedNode) {
			DefaultMutableTreeNode newNode = (DefaultMutableTreeNode) selectedNode.clone();
	
			for (Enumeration selectedNodeEnumeration = selectedNode.children(); selectedNodeEnumeration.hasMoreElements();) {
				newNode.add(deepClone((DefaultMutableTreeNode) selectedNodeEnumeration.nextElement()));
			}
			return newNode;
		}	

	private void performSaveAction() {
		if (fileName != null && fileName.length() > 0) {
			try{
				//model = tree.getModel();

				file = new File(fileName);
				file.delete();
				File tempFile = new File(fileName);
				
				if(!tempFile.exists())
				{
					tempFile.createNewFile();
				}
				BufferedWriter bufferWriter = new BufferedWriter(new FileWriter(tempFile, false));
				XMLOutputter xmloutput = new XMLOutputter();
				xmloutput.setFormat(Format.getPrettyFormat());
				xmloutput.output(root, bufferWriter );
				//tempFile.renameTo(new File(fileName));
//				JOptionPane.showMessageDialog(null, fileChooser.getSelectedFile().getName() + " Saved");

				bufferWriter.close();
				JOptionPane.showMessageDialog(null, file.getName() + " saved.", "Saved!", JOptionPane.INFORMATION_MESSAGE);
			}

			catch(Exception e){
				return;
			}
		}

		else {
			performSaveAsAction();
		}


	}

	public void performSaveAsAction() {
		try{
			FileNameExtensionFilter filter = new FileNameExtensionFilter("XML Files", "xml");
			fileChooser.setFileFilter(filter);		
			fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			


			int returnVal = fileChooser.showSaveDialog(null);
			//model = tree.getModel();
			if(returnVal == JFileChooser.APPROVE_OPTION)
			{
				fileName = fileChooser.getSelectedFile().getAbsolutePath();
				file = new File(fileName);
				if(!file.exists())
				{
					file.createNewFile();
				}
				BufferedWriter bufferWriter = new BufferedWriter(new FileWriter(file));
				XMLOutputter xmloutput = new XMLOutputter();
				xmloutput.setFormat(Format.getPrettyFormat());
				
				xmloutput.output(root, bufferWriter);
				bufferWriter.close();
				JOptionPane.showMessageDialog(null, file.getName() + " saved.", "Saved!", JOptionPane.INFORMATION_MESSAGE);
			}

		}

		catch(Exception e){
			 
			return;
		}
	}
}
