package gui.campooproductions.es;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Font;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;

import index.campooproductions.es.InvertedIndex;

import javax.swing.UIManager;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import java.awt.Toolkit;
import javax.swing.ImageIcon;

public class DocumentIndexer extends JDialog {

	/**
	 * 
	 */
	private InvertedIndex index;
	private static final long serialVersionUID = -7920387859860827858L;
	private JPanel contentPane;
	private JPanel pnMain;
	private JPanel pnTitle;
	private JLabel lblSelectAFile;
	private JPanel pnOptions;
	private JButton btnSelect;
	private File[] selectedFiles;
	private JCheckBox chckbxForceUniversalParser;
	private JPanel pnStatus;
	private JLabel lblInformation;
	private JPanel pnButtons;
	private JButton btnIndex;
	private JScrollPane scrlSelected;
	private JTextPane txSelected;
	private JPanel pnSelectedLabels;
	private JLabel lblFileSelected;
	private JLabel lblWarning;
	
	private IndexProgressDialog importProgressDialog;
	private JButton btnCancel;
	
	/**
	 * Create the frame.
	 */
	public DocumentIndexer(InvertedIndex index) {
		
		setIconImage(Toolkit.getDefaultToolkit().getImage(DocumentIndexer.class.getResource("/com/sun/javafx/scene/web/skin/OrderedListNumbers_16x16_JFX.png")));
		this.index = index;
		this.setMinimumSize(new Dimension(640, 360));
		setTitle("Document Indexer");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 530, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		contentPane.add(getPnMain(), BorderLayout.CENTER);
		contentPane.add(getPnButtons(), BorderLayout.SOUTH);
	}

	private void performIndex() {
		importProgressDialog = new IndexProgressDialog(this, selectedFiles, index, getChckbxForceUniversalParser().isSelected());
		importProgressDialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				dispose();
			}
		});
	}
	
	private JPanel getPnMain() {
		if (pnMain == null) {
			pnMain = new JPanel();
			pnMain.setBorder(new EmptyBorder(5, 5, 5, 5));
			pnMain.setLayout(new GridLayout(0, 1, 0, 0));
			pnMain.add(getPnTitle());
			pnMain.add(getPnOptions());
			pnMain.add(getPnStatus());
		}
		return pnMain;
	}
	private JPanel getPnTitle() {
		if (pnTitle == null) {
			pnTitle = new JPanel();
			pnTitle.setBorder(new EmptyBorder(5, 5, 5, 5));
			pnTitle.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
			pnTitle.add(getLblSelectAFile());
			pnTitle.add(getBtnSelect());
			pnTitle.add(getLblWarning());
		}
		return pnTitle;
	}
	private JLabel getLblSelectAFile() {
		if (lblSelectAFile == null) {
			lblSelectAFile = new JLabel("Select a file to index with Document Search Engine");
			lblSelectAFile.setFont(new Font("Tahoma", Font.PLAIN, 15));
		}
		return lblSelectAFile;
	}
	private JPanel getPnOptions() {
		if (pnOptions == null) {
			pnOptions = new JPanel();
			pnOptions.setBorder(new CompoundBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Options", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)), new EmptyBorder(5, 5, 5, 5)));
			pnOptions.setLayout(new GridLayout(0, 1, 0, 0));
			pnOptions.add(getChckbxForceUniversalParser());
		}
		return pnOptions;
	}
	private JButton getBtnSelect() {
		if (btnSelect == null) {
			btnSelect = new JButton("Browse...");
			btnSelect.setMnemonic('b');
			btnSelect.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					openFileChooser();
				}
			});
		}
		return btnSelect;
	}

	private void openFileChooser() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(true);
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		int result = fileChooser.showOpenDialog(getPnOptions());
		if (result == JFileChooser.APPROVE_OPTION) {
			selectedFiles = fileChooser.getSelectedFiles();
			
			if(selectedFiles.length == 1)
			{
				getLblFileSelected().setText("1 file selected");
			}
			else
				getLblFileSelected().setText(selectedFiles.length + " files selected");
			
			StringBuilder selectedFilesString = new StringBuilder();
			for(File file : selectedFiles)
			{
				selectedFilesString.append(file.getName());
				selectedFilesString.append("\n");
				System.out.println(file.getName());
			}
			getTxSelected().setText(selectedFilesString.toString());
			
			getLblInformation().setEnabled(true);
			getLblFileSelected().setEnabled(true);
			getBtnIndex().setEnabled(true);
			getTxSelected().setEnabled(true);
			getBtnIndex().requestFocus();
		}
		else if (result == JFileChooser.CANCEL_OPTION) {
			selectedFiles = new File[0];
			getLblFileSelected().setText("No file selected.");
			getLblFileSelected().setEnabled(false);
			getLblInformation().setEnabled(false);
			getBtnIndex().setEnabled(false);
			getTxSelected().setText("");
			getTxSelected().setEnabled(false);
			getBtnSelect().requestFocus();
		}
		
	}
	private JCheckBox getChckbxForceUniversalParser() {
		if (chckbxForceUniversalParser == null) {
			chckbxForceUniversalParser = new JCheckBox("Force Universal Parser");
			chckbxForceUniversalParser.setToolTipText("If checked, default AutoDetectParser of Apache Tika will be used instead of dedicated parsers");
			chckbxForceUniversalParser.setMnemonic('f');
		}
		return chckbxForceUniversalParser;
	}
	private JPanel getPnStatus() {
		if (pnStatus == null) {
			pnStatus = new JPanel();
			pnStatus.setBorder(new EmptyBorder(5, 5, 5, 5));
			pnStatus.setLayout(new BoxLayout(pnStatus, BoxLayout.X_AXIS));
			pnStatus.add(getPnSelectedLabels());
			pnStatus.add(getScrlSelected());
		}
		return pnStatus;
	}
	private JLabel getLblInformation() {
		if (lblInformation == null) {
			lblInformation = new JLabel("Selected: ");
			lblInformation.setEnabled(false);
		}
		return lblInformation;
	}
	private JPanel getPnButtons() {
		if (pnButtons == null) {
			pnButtons = new JPanel();
			pnButtons.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
			pnButtons.add(getBtnIndex());
			pnButtons.add(getBtnCancel());
		}
		return pnButtons;
	}
	private JButton getBtnIndex() {
		if (btnIndex == null) {
			btnIndex = new JButton("Index");
			btnIndex.setMnemonic('n');
			btnIndex.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					getBtnIndex().setEnabled(false);
					performIndex();
					selectedFiles = new File[0];
					getChckbxForceUniversalParser().setSelected(false);
					getLblInformation().setEnabled(false);
					getLblFileSelected().setEnabled(false);
					getTxSelected().setText("");
				}
			});
			btnIndex.setEnabled(false);
		}
		return btnIndex;
	}
	private JScrollPane getScrlSelected() {
		if (scrlSelected == null) {
			scrlSelected = new JScrollPane(getTxSelected());
		}
		return scrlSelected;
	}
	private JTextPane getTxSelected() {
		if (txSelected == null) {
			txSelected = new JTextPane();
			txSelected.setEnabled(false);
			txSelected.setEditable(false);
		}
		return txSelected;
	}
	private JPanel getPnSelectedLabels() {
		if (pnSelectedLabels == null) {
			pnSelectedLabels = new JPanel();
			pnSelectedLabels.setBorder(new EmptyBorder(5, 5, 5, 5));
			pnSelectedLabels.setLayout(new BoxLayout(pnSelectedLabels, BoxLayout.Y_AXIS));
			pnSelectedLabels.add(getLblInformation());
			pnSelectedLabels.add(getLblFileSelected());
		}
		return pnSelectedLabels;
	}
	private JLabel getLblFileSelected() {
		if (lblFileSelected == null) {
			lblFileSelected = new JLabel("No file selected");
			lblFileSelected.setEnabled(false);
		}
		return lblFileSelected;
	}
	private JLabel getLblWarning() {
		if (lblWarning == null) {
			lblWarning = new JLabel("Page system is only supported with PDF files");
			lblWarning.setIcon(new ImageIcon(DocumentIndexer.class.getResource("/javax/swing/plaf/metal/icons/ocean/warning.png")));
		}
		return lblWarning;
	}
	private JButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new JButton("Cancel");
			btnCancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					closeFrame();
				}
			});
		}
		return btnCancel;
	}

	protected void closeFrame() {
		this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		dispose();
	}
}
