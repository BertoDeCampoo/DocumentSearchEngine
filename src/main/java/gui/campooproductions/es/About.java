package gui.campooproductions.es;

import java.awt.BorderLayout;
import java.io.*;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JTextPane;

import java.awt.Font;

import javax.swing.JScrollPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import java.awt.Image;

public class About extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	public final static String version = "0.09"; 
	private final static String versionName = " Early Access";
	private final static String TIKA_ICON_FILE = "src/main/resources/icons/tika.png";
	private final static String LICENSE_FILE = "src/main/resources/LICENSE-2.0.txt";
	private final static String ABOUT_ICON_FILE = "src/main/resources/icons/about.png";
	private JTextPane txInformation;
	private JScrollPane scrollPane;
	private JLabel lblLogo;
	private JPanel pnLogos;
	private JScrollPane scrollPaneLogos;
	/**
	 * Create the dialog.
	 */
	public About() {
		setResizable(false);
		setTitle("About");
		setBounds(100, 100, 670, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		setIconImage(new ImageIcon(ABOUT_ICON_FILE).getImage());
		contentPanel.add(getScrollPane());
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(0, 230, 664, 35);
			contentPanel.add(buttonPane);
			buttonPane.setLayout(null);
			{
				JButton okButton = new JButton("Accept");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				okButton.setBounds(532, 5, 120, 25);
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
		contentPanel.add(getScrollPaneLogos());
	}
	private JTextPane getTxInformation() {
		if (txInformation == null) {
			txInformation = new JTextPane();
			txInformation.setOpaque(false);
			txInformation.setEditable(false);
			txInformation.setFont(new Font("Arial", Font.PLAIN, 13));
			txInformation.setContentType("text/html");
			txInformation.setText(generateText());
			txInformation.setCaretPosition(0);
			txInformation.setBounds(163, 13, 269, 109);
		}
		return txInformation;
	}
	private String generateText() {
		
		StringBuilder sb = new StringBuilder();
		sb.append("<HTML><BODY><CENTER>");
		
		
		sb.append("<B>Document Search Engine</B> ");
		sb.append("<BR><I>V");
		sb.append(version);
		sb.append(" ");
		sb.append(versionName);
		sb.append("</I><BR>Copyright 2017");
		sb.append("<BR>Alberto Gutiérrez Arroyo");
		sb.append("<BR>Licensed under the Apache License, Version 2.0 (the \"License\");");
		sb.append("you may not use this file except in compliance with the License.");
		sb.append("You may obtain a copy of the License at");
		sb.append("<BR><a href=http://www.apache.org/licenses/LICENSE-2.0>http://www.apache.org/licenses/LICENSE-2.0</a>");
		sb.append("<BR>Unless required by applicable law or agreed to in writing, software");
		sb.append("distributed under the License is distributed on an \"AS IS\" BASIS,");
		sb.append("WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.");
		sb.append("See the License for the specific language governing permissions and");
		sb.append("limitations under the License.<BR><BR>");
		sb.append(readLicense());
		sb.append("</CENTER></BODY></HTML>");
		
		return sb.toString();
		
	}
	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane(getTxInformation());
			scrollPane.setOpaque(false);
			scrollPane.setBounds(228, 13, 424, 204);
			scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		}
		return scrollPane;
	}
	private JLabel getLblLogo() {
		if (lblLogo == null) {
			lblLogo = new JLabel("");
			ImageIcon imagen = new ImageIcon(TIKA_ICON_FILE);
			Image img = imagen.getImage();
			Image newimg = img.getScaledInstance(195, -1,  java.awt.Image.SCALE_SMOOTH);
			lblLogo.setIcon(new ImageIcon(newimg));
		}
		return lblLogo;
	}
	
	public String readLicense() 
	{
		StringBuffer stringBuffer = new StringBuffer();
	    try {
	    	File file = new File(LICENSE_FILE);
	    	FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			String line;
			while ((line = bufferedReader.readLine()) != null) 
			{
				stringBuffer.append("<BR>");
				stringBuffer.append(line.trim());
				
			}
			fileReader.close();
		} catch (FileNotFoundException fnfe) {
    		System.err.println("File not found");
    	}
    	catch (IOException ioe) {
    		System.err.println("I/O error");
    	}
    	catch (Exception e) {
    		System.err.println("Fatal error");
    	}
	    return stringBuffer.toString();
	}
	private JPanel getPnLogos() {
		if (pnLogos == null) {
			pnLogos = new JPanel();
			pnLogos.setBounds(12, 13, 197, 204);
			pnLogos.setLayout(new GridLayout(0, 1, 0, 0));
			pnLogos.add(getLblLogo());
		}
		return pnLogos;
	}
	private JScrollPane getScrollPaneLogos() {
		if (scrollPaneLogos == null) {
			scrollPaneLogos = new JScrollPane(getPnLogos());
			scrollPaneLogos.setBounds(11, 13, 220, 204);
		}
		return scrollPaneLogos;
	}
}
