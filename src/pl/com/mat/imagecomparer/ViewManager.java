package pl.com.mat.imagecomparer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

public class ViewManager extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private static final int x = 977, y = 720;
	
	private ViewPanel viewPanel;
	private JButton method1;
	private JButton method2;
	private JButton clear;
	
	private JMenuBar menu;
	private JMenu m1;
	private JMenuItem p1, p2;
	
	private JFileChooser fc;

	public ViewManager() {
		super("Graphics");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(x, y);
		setLocationRelativeTo(null);
		setResizable(false); 
		
		menu = new JMenuBar();                        // menu górne
		m1 = new JMenu("File");
		p1 = new JMenuItem("Load First");
		p2 = new JMenuItem("Load Second");
		m1.add(p1);
		m1.add(p2);
		menu.add(m1);
		setJMenuBar(menu);
		
		fc = new JFileChooser();
		FileFilter fil = new FileFilter() {

			@Override
			public String getDescription() {
				return "Images";
			}

			@Override
			public boolean accept(File file) {
				if (file.isDirectory()) {
					return true;
				} else {
					String path = file.getAbsolutePath().toLowerCase();
					if (path.endsWith(".png")) {
						return true;
					}
				}
				return false;
			}
		};
		fc.setFileFilter(fil);
		
		viewPanel = new ViewPanel();
		
		method1 = new JButton("Zgodnoœæ S¹siedztwa");
		JPanel bottom = new JPanel();
		bottom.setBackground(Color.WHITE);
		bottom.add(method1);
		
		method2 = new JButton("Ransac");
		bottom.add(method2);
		
		clear = new JButton("Wyczyœæ");
		bottom.add(clear);
		
		getContentPane().add(BorderLayout.CENTER, viewPanel);
		getContentPane().add(BorderLayout.SOUTH, bottom);
		
		setVisible(true); 
	}
	
	public void addListeners(ActionListener listener) {
		p1.setActionCommand("load_first");
		p1.addActionListener(listener);
		p2.setActionCommand("load_second");
		p2.addActionListener(listener);
		
		method1.setActionCommand("start_m1");
		method1.addActionListener(listener);
		method2.setActionCommand("start_m2");
		method2.addActionListener(listener);
		
		clear.setActionCommand("clear");
		clear.addActionListener(listener);
	}
	
	public int showFileChooser() {
		return fc.showOpenDialog(this);
	}

	public File getSelectedFile() {
		return fc.getSelectedFile();
	}

	public void cleanFileChooser() {
		fc.setSelectedFile(new File(""));
	}
	
	public void visualize(Model model, int method) {
		if(method == 0)
			viewPanel.visualize(model.getCoherentNeighbors(), model.getFirstImage().getPoints() , model.getSecondImage().getPoints() , model.getMutualNeighbors());
		else
			viewPanel.visualize(model.getRansacPairs(), model.getFirstImage().getPoints() , model.getSecondImage().getPoints() , model.getMutualNeighbors());
	}
	
	public void loadFirstImage(File file) {
		viewPanel.loadFirstImage(file);
	}

	public void loadSecondImage(File file) {
		viewPanel.loadSecondImage(file);
	}
	
	public BufferedImage getFirstImage() {
		return viewPanel.getFirst();
	}
	
	public BufferedImage getSecondImage() {
		return viewPanel.getSecond();
	}
	
	public void clear() {
		viewPanel.clear();
	}

}