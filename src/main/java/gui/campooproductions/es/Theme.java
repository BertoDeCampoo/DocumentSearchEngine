package gui.campooproductions.es;

public enum Theme
{
	ACRYL("com.jtattoo.plaf.acryl.AcrylLookAndFeel"),
	AERO("com.jtattoo.plaf.aero.AeroLookAndFeel"),
	ALUMINIUM("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel"),
	BERNSTEIN("com.jtattoo.plaf.bernstein.BernsteinLookAndFeel"),
	$CLASSIC("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel"),
	$DEFAULT("default"),
	FAST("com.jtattoo.plaf.fast.FastLookAndFeel"),
	GRAPHITE("com.jtattoo.plaf.graphite.GraphiteLookAndFeel"),
	HIFI("com.jtattoo.plaf.hifi.HiFiLookAndFeel"),
	LUNA("com.jtattoo.plaf.luna.LunaLookAndFeel"),
	MCWIN("com.jtattoo.plaf.mcwin.McWinLookAndFeel"),
	METAL("javax.swing.plaf.metal.MetalLookAndFeel"),
	MINT("com.jtattoo.plaf.mint.MintLookAndFeel"),
	$MOTIF("com.sun.java.swing.plaf.motif.MotifLookAndFeel"),
	$NIMBUS("javax.swing.plaf.nimbus.NimbusLookAndFeel"),
	NOIRE("com.jtattoo.plaf.noire.NoireLookAndFeel"),
	SMART("com.jtattoo.plaf.smart.SmartLookAndFeel"),
	TEXTURE("com.jtattoo.plaf.texture.TextureLookAndFeel"),
	$WINDOWS("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"),
	
	;
	
	private String path;
	
	private Theme(String path)
	{
		this.path = path;
	}
	
	public String getPath()
	{
		return path;
	}
}
