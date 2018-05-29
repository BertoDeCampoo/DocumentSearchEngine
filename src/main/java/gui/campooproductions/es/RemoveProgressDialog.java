package gui.campooproductions.es;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import index.campooproductions.es.InvertedIndex;

import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class RemoveProgressDialog extends JDialog implements PropertyChangeListener {

	private static final long serialVersionUID = -2399375929671894024L;
	private final static String REMOVING_ICON_FILE = "src/main/resources/icons/deleting.gif";
	private final JPanel contentPanel = new JPanel();
	private JButton btnCancel;
	private JProgressBar progressBar;
	
	private JLabel lblFolder;
	private JLabel lblDocNum;
	private JLabel lblPages;
	
	private String[] selectedFiles;
	private InvertedIndex index;
	private RemoveTask task;
	private JLabel lblName;
	
	/**
	 * Create the 
	 */
	public RemoveProgressDialog(JFrame parent, String[] selectedFiles, InvertedIndex index) {
		super(parent, true);
		setModalityType(ModalityType.DOCUMENT_MODAL);
		setModal(true);
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		this.selectedFiles = selectedFiles;
		this.index = index;
		
		setTitle("Removing...");
		setIconImage(Toolkit.getDefaultToolkit().getImage(RemoveProgressDialog.class.getResource("/javax/measure/quantity/icons/close.png")));
		setResizable(false);
		setBounds(100, 100, 400, 200);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		contentPanel.add(getBtnCancel());
		contentPanel.add(getProgressBar());
		contentPanel.add(getLblFolder());
		contentPanel.add(getLblName());
		contentPanel.add(getLblPage());
		contentPanel.add(getLblDocumentNumber());
		task = new RemoveTask();
		task.addPropertyChangeListener(this);
		task.execute();
		setVisible(true);
        addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				getBtnCancel().doClick();
			}
		});
	}
	
	class RemoveTask extends SwingWorker<Void, Void> {
        int successIndex = 0;
        int pagesRemoved = 0;
        @Override
        public Void doInBackground() {
        	int totalPages = calculatePages();
        	int removedPages = 0;
        	getProgressBar().setMaximum(100);
    		for(int i = 0; i < selectedFiles.length; i++)
			{
    			getLblDocumentNumber().setText(selectedFiles[i]);
    			getLblPage().setText("Calculating...");
    			getLblName().setText("Document " + (i + 1) + " of " + selectedFiles.length);
				
    			if(isCancelled())
				{
					if(successIndex == 1)
		            	JOptionPane.showMessageDialog(null, "One document removed succesfully!", "Done!", JOptionPane.INFORMATION_MESSAGE);
		    		else
		    			JOptionPane.showMessageDialog(null, successIndex + " documents indexed successfully!", "Done!", JOptionPane.INFORMATION_MESSAGE);
					setCursor(null);
					dispose();
		        	break;
				}
    			Integer currentDocumentPages = index.getNumberOfPages(selectedFiles[i]);
    			index.remove(selectedFiles[i]);
    			removedPages += currentDocumentPages;
				getLblPage().setText("Pages removed: " + removedPages + " of " + totalPages);
				setProgress((removedPages * 100)/ totalPages);
				successIndex ++;
			}
			return null;
        }
 
        /*
         * Executed in event dispatching thread
         */
        @Override
        public void done() {
            Toolkit.getDefaultToolkit().beep();
            
            if(!isCancelled())
            {
            	setCursor(null); //turn off the wait cursor
            	if(successIndex == 1)
	            	JOptionPane.showMessageDialog(null, "One document removed succesfully!", "Done!", JOptionPane.INFORMATION_MESSAGE);
            	else if(successIndex == 0)
            		JOptionPane.showMessageDialog(null, "No documents were removed!", "Done!", JOptionPane.WARNING_MESSAGE);
	    		else
	    			JOptionPane.showMessageDialog(null, successIndex + " documents removed successfully!", "Done!", JOptionPane.INFORMATION_MESSAGE);
	        	dispose();
            }
        }
        private int calculatePages()
        {
        	int totalPages = 0;
        	for(int i = 0; i < selectedFiles.length; i++)
			{
    			getLblDocumentNumber().setText(selectedFiles.length + " files selected to remove");
    			getLblPage().setText("Calculating pages");
    			getLblName().setText("Please, be patient");
				
				totalPages += index.getNumberOfPages(selectedFiles[i]);
			}
    		return totalPages;
        }
    }
	
	private JButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new JButton("Cancel");
			btnCancel.setMnemonic('c');
			btnCancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					btnCancel.setToolTipText("The task will be stopped once the current document is fully indexed");
					btnCancel.setEnabled(false);
					btnCancel.setText("Cancelled");
					task.cancel(true);
				}
			});
			btnCancel.setBounds(285, 127, 97, 25);
		}
		return btnCancel;
	}
	private JProgressBar getProgressBar() {
		if (progressBar == null) {
			progressBar = new JProgressBar();
			progressBar.setIndeterminate(false);
			progressBar.setBounds(12, 127, 261, 14);
		}
		return progressBar;
	}
	private JLabel getLblFolder() {
		if (lblFolder == null) {
			lblFolder = new JLabel("");
			lblFolder.setBounds(12, 13, 370, 54);
			lblFolder.setIcon(new ImageIcon(REMOVING_ICON_FILE));
		}
		return lblFolder;
	}
	
	private JLabel getLblDocumentNumber() {
		if (lblDocNum == null) {
			lblDocNum = new JLabel("Calculating...");
			lblDocNum.setBounds(12, 100, 370, 16);
		}
		return lblDocNum;
	}
	private JLabel getLblPage() {
		if (lblPages == null) {
			lblPages = new JLabel("Page 0 of 0");
			lblPages.setBounds(12, 85, 370, 16);
		}
		return lblPages;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		SwingWorker<Void, Void> task = (SwingWorker<Void, Void>) evt.getSource();
		if ("progress".equalsIgnoreCase(evt.getPropertyName())) {
			int progress = task.getProgress();
            progressBar.setValue(progress);
        } 
		// listen for the worker to be done
//        if (SwingWorker.StateValue.DONE == evt.getNewValue()) {
//            dispose();
//        }
	}
	private JLabel getLblName() {
		if (lblName == null) {
			lblName = new JLabel("Calculating");
			lblName.setBounds(12, 70, 370, 16);
		}
		return lblName;
	}
}
