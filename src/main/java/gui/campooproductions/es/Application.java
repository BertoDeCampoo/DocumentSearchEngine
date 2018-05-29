package gui.campooproductions.es;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Image;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.FlowLayout;
import javax.swing.JScrollPane;

import index.campooproductions.es.InvertedIndex;

import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JList;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import configuration.campooproductions.es.Configuration;

import javax.swing.event.ListSelectionEvent;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * 
 * @author Alberto Gutiérrez Arroyo
 *
 */
public class Application {

	private final static String MAIN_ICON_FILE = "src/main/resources/icons/logo.png";
	private final static String ABOUT_ICON_FILE = "src/main/resources/icons/about.png";
	private final static String APACHE_ICON_FILE = "src/main/resources/icons/apache.png";
	private final static String SEARCH_ICON_FILE = "src/main/resources/icons/searchicon.png";
	private final static String SETTINGS_ICON_FILE = "src/main/resources/icons/settings.png";
	private final static String BROWSER_ICON_FILE = "src/main/resources/icons/browser.png";
	
	private JFrame mainFrame;
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenuItem mntmIndex;
	private JSeparator separator;
	private JMenuItem mntmExit;
	private JPanel pnMain;
	private JPanel pnSearch;
	private JPanel pnLogo;
	private JLabel lblDocumentSearchEngine;
	private JLabel lblLogo;
	private JPanel pnSearchField;
	private JTextField txSearch;
	private JButton btnSearch;
	private JPanel pnStatus;
	private JLabel lblTerm;
	private JPanel pnResults;
	private JScrollPane scrollPaneResults;
	private DefaultListModel<String> dlm;
	private DefaultListModel<Integer> modelPages;
	
	
	private InvertedIndex index;
	private JList<String> listResults;
	private JLabel lblDocumentsContainingTerm;
	private JMenu mnView;
	private JMenuItem mntmIndexedFiles;
	private JPanel pnResultsPages;
	private JScrollPane scrollPaneResultsPages;
	private JList<Integer> listPages;
	private JLabel lblPage;
	private JMenu mnHelp;
	private JMenuItem mntmAbout;
	private JMenu mnWindow;
	private JPanel pnResultsDocuments;
	private JMenuItem mntmExport;
	private JSeparator separator_1;
	private JMenuItem mntmImport;
	private JMenuItem mntmPreferences;
	
	private About about;
	private DocumentManager dm;
	private DocumentIndexer di;
	private Preferences preferences;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Application window = new Application();
					window.mainFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Application() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(){
		JFrame.setDefaultLookAndFeelDecorated(Configuration.getDecorated());
		index = new InvertedIndex();
		mainFrame = new JFrame();
		mainFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		mainFrame.setIconImage(new ImageIcon(MAIN_ICON_FILE).getImage());
		mainFrame.setMinimumSize(new Dimension(640, 360));
		mainFrame.setTitle("Document Search Engine");
		mainFrame.setBounds(100, 100, 660, 400);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setJMenuBar(getMenuBar());
		mainFrame.getContentPane().add(getPnMain(), BorderLayout.CENTER);
		mainFrame.getContentPane().add(getPnStatus(), BorderLayout.SOUTH);
		mainFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if(Configuration.getAutoSave())
					index.exportIndex(Configuration.getBackupFile());
				System.exit(0);
			}
		});
		this.loadConfiguration();
	}

	private void loadConfiguration() {
		applyTheme(Theme.valueOf(Configuration.getTheme()).getPath());
		if(Configuration.getAutoLoad())
		{
			try {
				index.importIndex(Configuration.getBackupFile());
				String aux;
				if(index.getNumberOfDocuments() == 0)
					aux = "No documents restored from backup file";
				else if (index.getNumberOfDocuments() == 1)
					aux = "Content of one document restored successfully!";
				else
					 aux = "Content of " + index.getNumberOfDocuments() + " documents restored successfully!";
				JOptionPane.showMessageDialog(null, aux, "Import finished", JOptionPane.INFORMATION_MESSAGE);
			} catch(FileNotFoundException fnfe)
			{
				JOptionPane.showMessageDialog (mainFrame, "Unable to load configuration from " + Configuration.getBackupFile(), 
						"Auto Load failed", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
	
	private void applyTheme(String path)
	{
		try {
			if(path.equalsIgnoreCase("default"))
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			else
				UIManager.setLookAndFeel(path);
			SwingUtilities.updateComponentTreeUI(mainFrame);
			mainFrame.pack();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}

	private JMenuBar getMenuBar() {
		if (menuBar == null) {
			menuBar = new JMenuBar();
			menuBar.add(getMnFile());
			menuBar.add(getMnView());
			menuBar.add(getMnWindow());
			menuBar.add(getMnHelp());
		}
		return menuBar;
	}
	private JMenu getMnFile() {
		if (mnFile == null) {
			mnFile = new JMenu("File");
			mnFile.setMnemonic('F');
			mnFile.add(getMntmIndex());
			mnFile.add(getSeparator_1());
			mnFile.add(getMntmImport());
			mnFile.add(getMntmExport());
			mnFile.add(getSeparator());
			mnFile.add(getMntmExit());
		}
		return mnFile;
	}
	private JMenuItem getMntmIndex() {
		if (mntmIndex == null) {
			mntmIndex = new JMenuItem("Index...");
			mntmIndex.setIcon(new ImageIcon(Application.class.getResource("/com/sun/javafx/scene/web/skin/OrderedListNumbers_16x16_JFX.png")));
			mntmIndex.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(di == null)
						di = new DocumentIndexer(index);
					di.setVisible(true);
					di.addWindowListener(new WindowAdapter() {
						@Override
						public void windowClosing(WindowEvent e) {
							di = new DocumentIndexer(index);
						}
					});
				}
			});
			mntmIndex.setMnemonic('n');
		}
		return mntmIndex;
	}
	private JSeparator getSeparator() {
		if (separator == null) {
			separator = new JSeparator();
		}
		return separator;
	}
	private JMenuItem getMntmExit() {
		if (mntmExit == null) {
			mntmExit = new JMenuItem("Exit");
			mntmExit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					mainFrame.dispose();
				}
			});
			mntmExit.setMnemonic('X');
		}
		return mntmExit;
	}
	private JPanel getPnMain() {
		if (pnMain == null) {
			pnMain = new JPanel();
			pnMain.setBorder(new EmptyBorder(5, 5, 5, 5));
			pnMain.setLayout(new BorderLayout(0, 0));
			pnMain.add(getPnSearch(), BorderLayout.NORTH);
			pnMain.add(getPnResults(), BorderLayout.CENTER);
		}
		return pnMain;
	}
	private JPanel getPnSearch() {
		if (pnSearch == null) {
			pnSearch = new JPanel();
			pnSearch.setBorder(new EmptyBorder(5, 5, 5, 5));
			pnSearch.setLayout(new BorderLayout(0, 0));
			pnSearch.add(getPnLogo(), BorderLayout.NORTH);
			pnSearch.add(getPnSearchField(), BorderLayout.CENTER);
		}
		return pnSearch;
	}
	private JPanel getPnLogo() {
		if (pnLogo == null) {
			pnLogo = new JPanel();
			pnLogo.setBorder(new EmptyBorder(5, 5, 5, 5));
			pnLogo.add(getLblDocumentSearchEngine());
			pnLogo.add(getLblLogo());
		}
		return pnLogo;
	}
	private JLabel getLblDocumentSearchEngine() {
		if (lblDocumentSearchEngine == null) {
			lblDocumentSearchEngine = new JLabel("Document Search Engine");
			lblDocumentSearchEngine.setFont(new Font("Arial", Font.PLAIN, 20));
		}
		return lblDocumentSearchEngine;
	}
	private JLabel getLblLogo() {
		if (lblLogo == null) {
			lblLogo = new JLabel("");
			ImageIcon imagen = new ImageIcon(APACHE_ICON_FILE);
			Image img = imagen.getImage();
			Image newimg = img.getScaledInstance(-1, 50,  java.awt.Image.SCALE_SMOOTH);
			lblLogo.setIcon(new ImageIcon(newimg));
		}
		return lblLogo;
	}
	
	private JPanel getPnSearchField() {
		if (pnSearchField == null) {
			pnSearchField = new JPanel();
			pnSearchField.setBorder(new EmptyBorder(5, 5, 5, 5));
			pnSearchField.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			pnSearchField.add(getLblTerm());
			pnSearchField.add(getTxSearch());
			pnSearchField.add(getBtnSearch());
		}
		return pnSearchField;
	}
	private JTextField getTxSearch() {
		if (txSearch == null) {
			txSearch = new JTextField();
			txSearch.setToolTipText("Type a word to search");
			txSearch.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					super.keyReleased(e);
		            if(txSearch.getText().length() > 0)
		            {
		            	//To lowercase
		            	int pos = txSearch.getCaretPosition();
		            	txSearch.setText(txSearch.getText().toLowerCase());
		            	txSearch.setCaretPosition(pos);
		                
		                
		            	ImageIcon imagen = new ImageIcon(SEARCH_ICON_FILE);
		    			Image img = imagen.getImage();
		    			Image newimg = img.getScaledInstance(-1, 18,  java.awt.Image.SCALE_SMOOTH);
		    			getBtnSearch().setToolTipText("Press enter or clic here to search");
		            	getBtnSearch().setIcon(new ImageIcon(newimg));
		                getBtnSearch().setEnabled(true);
		            }
		            else
		            {
		            	getBtnSearch().setIcon(new ImageIcon(Application.class.getResource("/com/sun/java/swing/plaf/motif/icons/Error.gif")));
		            	getBtnSearch().setToolTipText("No text on search box");
		            	getBtnSearch().setEnabled(false);
		            }
				}
				@Override
				public void keyPressed(KeyEvent e) {
					if(e.getKeyCode() == KeyEvent.VK_ENTER)
						getBtnSearch().doClick();
				}
				@Override
				public void keyTyped(KeyEvent e)
				{
					char c = e.getKeyChar();
					if(!((c >= 'A') && (c <= 'Z') ||(c >= 'a') && (c <= 'z') || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE)))
					{
						e.consume();
					}
				}
			});
			txSearch.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					
					if(e.getKeyChar() == KeyEvent.VK_ENTER)
						getBtnSearch().doClick();
					
				}
			});
			txSearch.setColumns(20);
		}
		return txSearch;
	}
	private JButton getBtnSearch() {
		if (btnSearch == null) {
			btnSearch = new JButton("Search");
			btnSearch.setFont(new Font("Tahoma", Font.PLAIN, 12));
			btnSearch.setIcon(new ImageIcon(Application.class.getResource("/com/sun/java/swing/plaf/motif/icons/Error.gif")));
			btnSearch.setToolTipText("No text on search box");
			btnSearch.setMnemonic('S');
			btnSearch.setEnabled(false);
			btnSearch.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					populateListResults();
				}
			});
		}
		return btnSearch;
	}
	
	private void populateListResults() {
		dlm.removeAllElements();
		List<String> elements = index.searchOnDocuments(getTxSearch().getText());
		if(elements.isEmpty())
		{
			listResults.setToolTipText("The word was not found in the document library");
			dlm.addElement("Word not found in the document library");
		}
		else
			listResults.setToolTipText("Documents containing the word");
		for(String element : elements)
		{
			dlm.addElement(element);
		}
	}

	private JPanel getPnStatus() {
		if (pnStatus == null) {
			pnStatus = new JPanel();
			pnStatus.setBorder(new EmptyBorder(5, 5, 5, 5));
			pnStatus.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			
		}
		return pnStatus;
	}
	private JLabel getLblTerm() {
		if (lblTerm == null) {
			lblTerm = new JLabel("Term:");
			lblTerm.setLabelFor(getTxSearch());
			lblTerm.setDisplayedMnemonic('t');
			lblTerm.setFont(new Font("Tahoma", Font.PLAIN, 13));
		}
		return lblTerm;
	}
	private JPanel getPnResults() {
		if (pnResults == null) {
			pnResults = new JPanel();
			pnResults.setBorder(new EmptyBorder(5, 5, 5, 5));
			pnResults.setLayout(new BoxLayout(pnResults, BoxLayout.X_AXIS));
			pnResults.add(getPnResultsDocuments());
			pnResults.add(getPnResultsPages());
		}
		return pnResults;
	}
	private JScrollPane getScrollPaneResults() {
		if (scrollPaneResults == null) {
			scrollPaneResults = new JScrollPane(getListResults());
			scrollPaneResults.setToolTipText("The documents containing the given word will appear here");
			scrollPaneResults.setColumnHeaderView(getLblDocumentsContainingTerm());
		}
		return scrollPaneResults;
	}
	private JList<String> getListResults() {
		if (listResults == null) {
			dlm = new DefaultListModel<String>();
			listResults = new JList<String>(dlm);
			listResults.setToolTipText("Search results will appear here");
			listResults.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					String resaltado = listResults.getSelectedValue();
					fillPages(getTxSearch().getText(), resaltado);
				}
			});
		}
		return listResults;
	}
	
	private void fillPages(String word, String document) {
		modelPages.removeAllElements();
		List<Integer> pages = index.getPageNumbers(word, document);
		for(Integer page : pages)
		{
			modelPages.addElement(page);
		}
			
	}
	
	private JLabel getLblDocumentsContainingTerm() {
		if (lblDocumentsContainingTerm == null) {
			lblDocumentsContainingTerm = new JLabel("Documents containing term (TF-IDF)");
			lblDocumentsContainingTerm.setFont(new Font("Tahoma", Font.PLAIN, 13));
		}
		return lblDocumentsContainingTerm;
	}
	private JMenu getMnView() {
		if (mnView == null) {
			mnView = new JMenu("View");
			mnView.setMnemonic('v');
			mnView.add(getMntmIndexedFiles());
		}
		return mnView;
	}
	private JMenuItem getMntmIndexedFiles() {
		if (mntmIndexedFiles == null) {
			mntmIndexedFiles = new JMenuItem("Indexed Files");
			mntmIndexedFiles.setMnemonic('d');
			mntmIndexedFiles.setIcon(new ImageIcon(new ImageIcon(BROWSER_ICON_FILE).getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));
			mntmIndexedFiles.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(dm == null)
						dm = new DocumentManager(index);
					dm.setVisible(true);
					dm.addWindowListener(new WindowAdapter() {
						@Override
						public void windowClosing(WindowEvent e) {
							dm = new DocumentManager(index);
						}
					});
				}
			});
		}
		return mntmIndexedFiles;
	}
	private JPanel getPnResultsPages() {
		if (pnResultsPages == null) {
			pnResultsPages = new JPanel();
			pnResultsPages.setLayout(new BoxLayout(pnResultsPages, BoxLayout.X_AXIS));
			pnResultsPages.add(getScrollPaneResultsPages());
		}
		return pnResultsPages;
	}
	private JScrollPane getScrollPaneResultsPages() {
		if (scrollPaneResultsPages == null) {
			scrollPaneResultsPages = new JScrollPane(getListPages());
			scrollPaneResultsPages.setColumnHeaderView(getLblPage());
		}
		return scrollPaneResultsPages;
	}
	private JList<Integer> getListPages() {
		if (listPages == null) {
			modelPages = new DefaultListModel<Integer>();
			listPages = new JList<Integer>(modelPages);
			listPages.setToolTipText("The pages in which the search term appears in the document will appear here");
			listPages.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					if(listPages.hasFocus())
					{
						showPageInfo();
					}
				}

				private void showPageInfo() {
					Integer page = listPages.getSelectedValue();
					String word = getTxSearch().getText();
					String document = getListResults().getSelectedValue();
					
					if(page != null && word != null && document != null)
					{
						int times = index.getOccurrencesOnPage(page, word, document);
						StringBuilder sb = new StringBuilder();
						sb.append("<HTML><BODY><B><I>");
						sb.append(getTxSearch().getText());
						sb.append("</B></I> appears ");
						if(times == 1)
							sb.append("once on page ");
						else
						{
							sb.append(times);
							sb.append(" times on page ");
						}
						sb.append(page);
						sb.append("<BR>");
						sb.append("<b>TF: " + index.getTf(word, document));
						sb.append("</b><BR>");
						sb.append("<b>IDF: " + index.getIdf(word));
						sb.append("</b><BR>");
						sb.append("<b>TF-IDF: " + index.getTfIdf(word, document));
						sb.append("</b></HTML></BODY>");
						JOptionPane.showMessageDialog (mainFrame, sb.toString(), 
							document, JOptionPane.INFORMATION_MESSAGE);
					}
				}
			});
		}
		return listPages;
	}
	private JLabel getLblPage() {
		if (lblPage == null) {
			lblPage = new JLabel("Pages");
		}
		return lblPage;
	}
	private JMenu getMnHelp() {
		if (mnHelp == null) {
			mnHelp = new JMenu("Help");
			mnHelp.setMnemonic('H');
			mnHelp.add(getMntmAbout());
		}
		return mnHelp;
	}
	private JMenuItem getMntmAbout() {
		if (mntmAbout == null) {
			mntmAbout = new JMenuItem("About");
			mntmAbout.setMnemonic('b');
			mntmAbout.setIcon(new ImageIcon(new ImageIcon(ABOUT_ICON_FILE).getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));
			mntmAbout.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(about == null)
						about = new About();
					about.setVisible(true);
					about.addWindowListener(new WindowAdapter() {
						@Override
						public void windowClosing(WindowEvent e) {
							about = new About();
						}
					});
				}
			});
		}
		return mntmAbout;
	}
	private JMenu getMnWindow() {
		if (mnWindow == null) {
			mnWindow = new JMenu("Window");
			mnWindow.setMnemonic('w');
			mnWindow.add(getMntmPreferences());
		}
		return mnWindow;
	}
	private JPanel getPnResultsDocuments() {
		if (pnResultsDocuments == null) {
			pnResultsDocuments = new JPanel();
			pnResultsDocuments.setLayout(new BoxLayout(pnResultsDocuments, BoxLayout.X_AXIS));
			pnResultsDocuments.add(getScrollPaneResults());
		}
		return pnResultsDocuments;
	}
	private JMenuItem getMntmExport() {
		if (mntmExport == null) {
			mntmExport = new JMenuItem("Export...");
			mntmExport.setIcon(new ImageIcon(Application.class.getResource("/com/sun/javafx/scene/web/skin/DecreaseIndent_16x16_JFX.png")));
			mntmExport.setMnemonic('p');
			mntmExport.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					FileNameExtensionFilter filter = new FileNameExtensionFilter("Java Serialized Object File", "ser");
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setFileFilter(filter);
					fileChooser.setDialogTitle("Specify a location to export");   
					int userSelection = fileChooser.showSaveDialog(null);
					 
					if (userSelection == JFileChooser.APPROVE_OPTION) {
					    File fileToSave = fileChooser.getSelectedFile();
					    if(JOptionPane.showConfirmDialog(null, 
					    		"Are you sure do you want to export the index? \n"
					    		+ "The program will be locked while exporting", 
								"Index export confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
						{
					    	System.out.println("Exporting file: " + fileToSave.getAbsolutePath());
						    index.exportIndex(fileToSave.getAbsolutePath());
						    JOptionPane.showMessageDialog(null, "Content of " + index.getNumberOfDocuments() + " documents exported succesfully!", 
						    		"Export finished", JOptionPane.INFORMATION_MESSAGE);
						}
					    
					    
					}
				}
			});
		}
		return mntmExport;
	}
	private JSeparator getSeparator_1() {
		if (separator_1 == null) {
			separator_1 = new JSeparator();
		}
		return separator_1;
	}
	private JMenuItem getMntmImport() {
		if (mntmImport == null) {
			mntmImport = new JMenuItem("Import...");
			mntmImport.setMnemonic('m');
			mntmImport.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					FileNameExtensionFilter filter = new FileNameExtensionFilter("Java Serialized Object File", "ser");
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setFileFilter(filter);
					fileChooser.setDialogTitle("Specify a file to import");   
					 
					int userSelection = fileChooser.showOpenDialog(null);
					 
					if (userSelection == JFileChooser.APPROVE_OPTION) {
					    File fileToImport = fileChooser.getSelectedFile();
					    if(JOptionPane.showConfirmDialog(null, 
					    		"Are you sure do you want to import this index? \n"
					    		+ "The contents of the current index will\n"
					    		+ "be lost and replaced with the new ones!\n"
					    		+ "The program will be locked while importing", 
								"Index import confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
						{
					    	try {
					    		index.importIndex(fileToImport.getAbsolutePath());
					    		String aux;
								if(index.getNumberOfDocuments() == 0)
									aux = "No documents imported, file damaged or unreadable";
								else if (index.getNumberOfDocuments() == 1)
									aux = "Content of one document imported successfully!";
								else
									 aux = "Content of " + index.getNumberOfDocuments() + " documents imported successfully!";
					    		JOptionPane.showMessageDialog(null, aux, "Import finished", JOptionPane.INFORMATION_MESSAGE);
					    	} catch(FileNotFoundException fnfe)
							{
								JOptionPane.showMessageDialog (mainFrame, "Unable to load configuration from " + Configuration.getBackupFile(), 
										"Error importing", JOptionPane.INFORMATION_MESSAGE);
							}
						}
					}
				}
			});
			mntmImport.setIcon(new ImageIcon(Application.class.getResource("/com/sun/javafx/scene/web/skin/IncreaseIndent_16x16_JFX.png")));
		}
		return mntmImport;
	}
	private JMenuItem getMntmPreferences() {
		if (mntmPreferences == null) {
			mntmPreferences = new JMenuItem("Preferences");
			mntmPreferences.setIcon(new ImageIcon(new ImageIcon(SETTINGS_ICON_FILE).getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));
			mntmPreferences.setMnemonic('c');
			mntmPreferences.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(preferences == null)
						preferences = new Preferences(mainFrame);
					preferences.setVisible(true);
					preferences.addWindowListener(new WindowAdapter() {
						@Override
						public void windowClosing(WindowEvent e) {
							preferences = new Preferences(mainFrame);
						}
					});
				}
			});
		}
		return mntmPreferences;
	}
}
