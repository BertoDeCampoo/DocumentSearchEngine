package gui.campooproductions.es;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import index.campooproductions.es.InvertedIndex;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

import javax.swing.JPopupMenu;
import java.awt.Component;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;

public class DocumentManager extends JFrame {

	private static final long serialVersionUID = -7845364975164151119L;
	private final static String BROWSER_ICON_FILE = "src/main/resources/icons/browser.png";
	private final JPanel contentPanel = new JPanel();
	private JScrollPane scrollPane;
	private JList<String> listDocuments;
	private JLabel lblListOfDocuments;
	
	private InvertedIndex index;
	private DefaultListModel<String> dlm;
	private JPopupMenu popupMenu;
	private JMenuItem mntmRemove;
	private RemoveProgressDialog removeProgressDialog;
	private JMenuItem mntmRefresh;
	private String[] selectedFiles;
	
	/**
	 * Create the dialog.
	 */
	public DocumentManager(InvertedIndex index) {
		setResizable(false);
		setIconImage(new ImageIcon(BROWSER_ICON_FILE).getImage());
		this.index = index;
		this.dlm = new DefaultListModel<String>();
		setTitle("Document Manager");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		contentPanel.add(getScrollPane());
		populateTable();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	private void populateTable() {
		dlm.removeAllElements();
		String[] documents = index.getAllDocuments();
		for(String element : documents)
		{
			dlm.addElement(element);
		}
	}

	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setViewportView(getListDocuments());
			scrollPane.setColumnHeaderView(getLblListOfDocuments());
		}
		return scrollPane;
	}
	private JList<String> getListDocuments() {
		if (listDocuments == null) {
			listDocuments = new JList<String>(dlm);
			listDocuments.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					char key = e.getKeyChar();
					if(key == KeyEvent.VK_DELETE)
						getMntmRemove().doClick();
					else if(key == KeyEvent.VK_F5)
						getMntmRefresh().doClick();
				}
			});
			listDocuments.setToolTipText("List of files indexed");
			addPopup(listDocuments, getPopupMenu());
		}
		return listDocuments;
	}
	private JLabel getLblListOfDocuments() {
		if (lblListOfDocuments == null) {
			lblListOfDocuments = new JLabel("List of documents");
			lblListOfDocuments.setFont(new Font("Tahoma", Font.PLAIN, 15));
		}
		return lblListOfDocuments;
	}
	private JPopupMenu getPopupMenu() {
		if (popupMenu == null) {
			popupMenu = new JPopupMenu();
			popupMenu.add(getMntmRemove());
			popupMenu.add(getMntmRefresh());
		}
		return popupMenu;
	}
	private void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
	private JMenuItem getMntmRemove() {
		if (mntmRemove == null) {
			mntmRemove = new JMenuItem("Remove");
			mntmRemove.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
			mntmRemove.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String document = listDocuments.getSelectedValue();
					if(document != null)
						if(JOptionPane.showConfirmDialog(null, "Are you sure do you want to remove this document from the index? \nThis operation can't be undone!", 
								"Document removal confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
						{
							List<String> aux = getListDocuments().getSelectedValuesList();
							selectedFiles = new String[aux.size()];
							selectedFiles = getListDocuments().getSelectedValuesList().toArray(selectedFiles);
							performRemoval();
						}
					populateTable();
				}
			});
		}
		return mntmRemove;
	}
	
	private void performRemoval()
	{
		removeProgressDialog = new RemoveProgressDialog(this, selectedFiles, index);
		removeProgressDialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				populateTable();
			}
		});
	}
	
	private JMenuItem getMntmRefresh() {
		if (mntmRefresh == null) {
			mntmRefresh = new JMenuItem("Refresh");
			mntmRefresh.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					populateTable();
				}
			});
			mntmRefresh.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
		}
		return mntmRefresh;
	}
	
	public void export(String path) {
		try {
	         FileOutputStream fileOut =
	         new FileOutputStream("C://exportarinvertedindex.ser");
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(index);
	         out.close();
	         fileOut.close();
	         System.out.printf("Serialized data is saved in C://exportarinvertedindex.ser");
	      }catch(IOException i) {
	         i.printStackTrace();
	      }
	}
}
