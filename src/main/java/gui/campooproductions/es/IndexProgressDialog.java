package gui.campooproductions.es;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import index.campooproductions.es.InvertedIndex;
import parsers.campooproductions.es.IParser;
import parsers.campooproductions.es.ParserFactory;
import parsers.campooproductions.es.UniversalParser;

import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map.Entry;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class IndexProgressDialog extends JDialog implements PropertyChangeListener {

	private static final long serialVersionUID = -2399375929671894024L;
	private final static String INDEXING_ICON_FILE = "src/main/resources/icons/indexing.gif";
	private final JPanel contentPanel = new JPanel();
	private JButton btnCancel;
	private JProgressBar progressBar;
	
	private JLabel lblFolder;
	private JLabel lblFilepath;
	private JLabel lblPages;
	
	private File[] selectedFiles;
	private InvertedIndex index;
	boolean universalParser;
	private IndexTask task;
	private JLabel lblName;
	
	/**
	 * Create the progress dialog
	 */
	public IndexProgressDialog(JDialog parent, File[] selectedFiles, InvertedIndex index, boolean universalParser) {
		super(parent, true);
		setModalityType(ModalityType.DOCUMENT_MODAL);
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		setIconImage(parent.getIconImages().get(0));
		this.selectedFiles = selectedFiles;
		this.index = index;
		this.universalParser = universalParser;
		
		setTitle("Indexing...");
		setResizable(false);
		setBounds(100, 100, 400, 200);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		contentPanel.add(getBtnCancel());
		contentPanel.add(getProgressBar());
		contentPanel.add(getLblFolder());
		contentPanel.add(getLblFilepath());
		contentPanel.add(getLblPage());
		contentPanel.add(getLblName());
		task = new IndexTask();
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
	
	class IndexTask extends SwingWorker<Void, Void> {
        int successIndex = 0;
        int pagesIndexed = 0;
        @Override
        public Void doInBackground() {
        	int totalProgress = 0;
        	int totalPages = calculatePages();
        	getProgressBar().setMaximum(100);
        	IParser parser = new UniversalParser();
    		for(int i = 0; i < selectedFiles.length; i++)
			{
    			getLblFilepath().setText(selectedFiles[i].getAbsolutePath());
    			getLblPage().setText("Calculating...");
    			getLblName().setText(selectedFiles[i].getName());
				
    			if(!universalParser)
    				parser = new ParserFactory().getParser(selectedFiles[i].getAbsolutePath());
				
    			if(isCancelled())
				{
					if(successIndex == 1)
		            	JOptionPane.showMessageDialog(null, "Cancelled. One file indexed succesfully!", "Cancelled", JOptionPane.INFORMATION_MESSAGE);
					else if(successIndex == 0)
		            	JOptionPane.showMessageDialog(null, "Cancelled. No files indexed", "Cancelled", JOptionPane.WARNING_MESSAGE);
		    		else
		    			JOptionPane.showMessageDialog(null, "Cancelled. " + successIndex + " files indexed successfully!", "Cancelled", JOptionPane.INFORMATION_MESSAGE);
		        	dispose();
		        	break;
				}
				HashMap<Integer, String> map = parser.parse(selectedFiles[i].getAbsolutePath());
				if(map != null && map.size() > 0)
				{
					for(Entry<Integer, String> entry : map.entrySet())
					{
						Integer page = entry.getKey();
				        String text = entry.getValue();
				        getLblPage().setText("Page " + page + " of " + map.entrySet().size());
				        index.addPageToIndex(page, text, selectedFiles[i].getName());
				        pagesIndexed++;
				        
				        int currentProgress = (totalProgress++ * 100)/ totalPages;
				        setProgress(currentProgress);
					}
					successIndex ++;
				}
			}
			return null;
        }
 
        /*
         * Executed in event dispatching thread
         */
        @Override
        public void done() {
            Toolkit.getDefaultToolkit().beep();
            setCursor(null); //turn off the wait cursor
            if(!isCancelled())
            {
            	if(successIndex == 1)
	            	JOptionPane.showMessageDialog(null, "One file indexed succesfully!", "Done!", JOptionPane.INFORMATION_MESSAGE);
            	else if(successIndex == 0)
            		JOptionPane.showMessageDialog(null, "No files indexed!", "Done!", JOptionPane.WARNING_MESSAGE);
	    		else
	    			JOptionPane.showMessageDialog(null, successIndex + " files indexed successfully!", "Done!", JOptionPane.INFORMATION_MESSAGE);
	        	dispose();
            }
        }
        private int calculatePages()
        {
        	int totalPages = 0;
        	IParser parser = new UniversalParser();
    		for(int i = 0; i < selectedFiles.length; i++)
			{
    			getLblFilepath().setText(selectedFiles.length + " files selected to import");
    			getLblPage().setText("Calculating pages");
    			getLblName().setText("Please, be patient");
				
				if(!universalParser)
    				parser = new ParserFactory().getParser(selectedFiles[i].getAbsolutePath());
				
				HashMap<Integer, String> map = parser.parse(selectedFiles[i].getAbsolutePath());
				if(map != null)
					totalPages += map.entrySet().size();
				if(isCancelled())
		        	break;
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
			lblFolder.setIcon(new ImageIcon(INDEXING_ICON_FILE));
		}
		return lblFolder;
	}
	
	private JLabel getLblFilepath() {
		if (lblFilepath == null) {
			lblFilepath = new JLabel("Calculating...");
			lblFilepath.setBounds(12, 100, 370, 16);
		}
		return lblFilepath;
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
            repaint();
        } 
		// listen for the worker to be done
//        if (SwingWorker.StateValue.DONE == evt.getNewValue()) {
////            dispose();
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
