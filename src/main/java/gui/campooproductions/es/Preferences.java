package gui.campooproductions.es;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;

import configuration.campooproductions.es.Configuration;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.WindowEvent;

public class Preferences extends JFrame {

	private static final long serialVersionUID = 6622975063384949328L;
	private final static String SETTINGS_ICON_FILE = "src/main/resources/icons/settings.png";
	private JFrame parent;
	private JPanel contentPane;
	private JPanel pnAppearance;
	private JPanel pnSettings;
	private JCheckBox chckbxAutoSave;
	private JComboBox<Theme> cbThemes;
	private JLabel lblTheme;
	private JPanel pnAppearanceCenter;
	private JPanel pnAppearanceButtons;
	private JButton btnApply;
	private JCheckBox chckbxAutoLoad;
	private JLabel lblSaved;
	private JPanel pnCbThemes;
	private JLabel lblRequiresUi;
	private JButton btnClose;
	private JLabel lblWarning;

	/**
	 * Create the frame.
	 */
	public Preferences(JFrame parent) {
		setResizable(false);
		this.parent = parent;
		setTitle("Preferences");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		this.setIconImage(new ImageIcon(SETTINGS_ICON_FILE).getImage());
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
		contentPane.add(getPnSettings());
		contentPane.add(getPnAppearance());
//	    this.setUndecorated(true);
		this.pack();
		this.setMinimumSize(this.getPreferredSize());
		
	}

	private JPanel getPnAppearance() {
		if (pnAppearance == null) {
			pnAppearance = new JPanel();
			pnAppearance.setBorder(new CompoundBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Appearance", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)), new EmptyBorder(5, 5, 5, 5)));
			pnAppearance.setLayout(new BorderLayout(0, 0));
			pnAppearance.add(getPnAppearanceCenter(), BorderLayout.CENTER);
			pnAppearance.add(getPnAppearanceButtons(), BorderLayout.SOUTH);
		}
		return pnAppearance;
	}
	private JPanel getPnSettings() {
		if (pnSettings == null) {
			pnSettings = new JPanel();
			pnSettings.setBorder(new CompoundBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Main", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)), new EmptyBorder(5, 5, 5, 5)));
			pnSettings.setLayout(new GridLayout(0, 1, 0, 0));
			pnSettings.add(getChckbxAutoSave());
			pnSettings.add(getChckbxAutoLoad());
			pnSettings.add(getLblWarning());
		}
		return pnSettings;
	}
	private JCheckBox getChckbxAutoSave() {
		if (chckbxAutoSave == null) {
			chckbxAutoSave = new JCheckBox("Auto Save");
			chckbxAutoSave.setMnemonic('s');
			chckbxAutoSave.setToolTipText("If activated, the index will be saved before exiting");
			chckbxAutoSave.setSelected(Configuration.getAutoSave());
			chckbxAutoSave.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//If auto save was on, turn it off
					if(Configuration.getAutoSave())
					{
						Configuration.setAutoSave(false);
						chckbxAutoSave.setSelected(false);
					}
					else
					{
						Configuration.setAutoSave(true);
						chckbxAutoSave.setSelected(true);
					}
					if(chckbxAutoSave.isSelected() && !getChckbxAutoLoad().isSelected())
					{
						getLblWarning().setVisible(true);
						pack();
					}
					else
					{
						getLblWarning().setVisible(false);
						pack();
					}
				}
			});
		}
		return chckbxAutoSave;
	}
	
	private JCheckBox getChckbxAutoLoad() {
		if (chckbxAutoLoad == null) {
			chckbxAutoLoad = new JCheckBox("Auto Load");
			chckbxAutoLoad.setMnemonic('l');
			chckbxAutoLoad.setToolTipText("If activated, the index will be loaded from backup file at start");
			chckbxAutoLoad.setSelected(Configuration.getAutoLoad());
			chckbxAutoLoad.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//If auto load was on, turn it off
					if(Configuration.getAutoLoad())
					{
						Configuration.setAutoLoad(false);
						chckbxAutoLoad.setSelected(false);
					}
					else
					{
						Configuration.setAutoLoad(true);
						chckbxAutoLoad.setSelected(true);
					}
					if(getChckbxAutoSave().isSelected() && !chckbxAutoLoad.isSelected())
					{
						getLblWarning().setVisible(true);
						pack();
					}
					else
					{
						getLblWarning().setVisible(false);
						pack();
					}
				}
			});
		}
		return chckbxAutoLoad;
	}
	
	private void applyTheme(String path)
	{
		
		try {
			if(path.equalsIgnoreCase("default"))
			{
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				SwingUtilities.updateComponentTreeUI(this);
				SwingUtilities.updateComponentTreeUI(parent);
			}
			else
				UIManager.setLookAndFeel(path);
			SwingUtilities.updateComponentTreeUI(parent);
			SwingUtilities.updateComponentTreeUI(this);
			parent.pack();
			this.pack();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}
	private JComboBox<Theme> getCbThemes() {
		if (cbThemes == null) {
			cbThemes = new JComboBox<Theme>(new DefaultComboBoxModel<>(Theme.values()));
			Theme selected = Theme.valueOf(Configuration.getTheme());
			cbThemes.setSelectedItem(selected);
			cbThemes.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					applyTheme(Theme.valueOf(cbThemes.getSelectedItem().toString()).getPath());
					getBtnApply().setEnabled(true);
				}
			});
		}
		return cbThemes;
	}
	private JLabel getLblTheme() {
		if (lblTheme == null) {
			lblTheme = new JLabel("Theme:");
			lblTheme.setLabelFor(getCbThemes());
		}
		return lblTheme;
	}
	private JPanel getPnAppearanceCenter() {
		if (pnAppearanceCenter == null) {
			pnAppearanceCenter = new JPanel();
			pnAppearanceCenter.add(getLblTheme());
			pnAppearanceCenter.add(getPnCbThemes());
		}
		return pnAppearanceCenter;
	}
	private JPanel getPnAppearanceButtons() {
		if (pnAppearanceButtons == null) {
			pnAppearanceButtons = new JPanel();
			pnAppearanceButtons.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
			pnAppearanceButtons.add(getLblSaved());
			pnAppearanceButtons.add(getBtnApply());
			pnAppearanceButtons.add(getBtnClose());
		}
		return pnAppearanceButtons;
	}
	private JButton getBtnApply() {
		if (btnApply == null) {
			btnApply = new JButton("Apply");
			btnApply.setEnabled(false);
			btnApply.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String selectedTheme = getCbThemes().getSelectedItem().toString();
					Configuration.setTheme(selectedTheme);
					if(selectedTheme.charAt(0) == '$')
						Configuration.setDecorated(false);
					else
						Configuration.setDecorated(true);
					getLblSaved().setText(getCbThemes().getSelectedItem().toString() + " applied!");
					getBtnClose().setEnabled(true);
					pack();
				}
			});
			btnApply.setMnemonic('p');
		}
		return btnApply;
	}
	
	private JLabel getLblSaved() {
		if (lblSaved == null) {
			lblSaved = new JLabel("");
		}
		return lblSaved;
	}
	private JPanel getPnCbThemes() {
		if (pnCbThemes == null) {
			pnCbThemes = new JPanel();
			pnCbThemes.setLayout(new GridLayout(2, 0, 0, 0));
			pnCbThemes.add(getCbThemes());
			pnCbThemes.add(getLblRequiresUi());
		}
		return pnCbThemes;
	}
	private JLabel getLblRequiresUi() {
		if (lblRequiresUi == null) {
			lblRequiresUi = new JLabel("$ Requires UI restart");
		}
		return lblRequiresUi;
	}
	private JButton getBtnClose() {
		if (btnClose == null) {
			btnClose = new JButton("Restart");
			btnClose.setEnabled(false);
			btnClose.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
					parent.dispatchEvent(new WindowEvent(parent, WindowEvent.WINDOW_CLOSING));
				}
			});
		}
		return btnClose;
	}
	private JLabel getLblWarning() {
		if (lblWarning == null) {
			lblWarning = new JLabel("<html><body><B>WARNING</B>: You may overwrite<br> your backup with an empty file</body></html>");
			lblWarning.setIcon(new ImageIcon(Preferences.class.getResource("/javax/swing/plaf/metal/icons/ocean/warning.png")));
			if(getChckbxAutoSave().isSelected() && !getChckbxAutoLoad().isSelected())
			{
				lblWarning.setVisible(true);
				pack();
			}
			else
			{
				lblWarning.setVisible(false);
				pack();
			}
		}
		return lblWarning;
	}
}
