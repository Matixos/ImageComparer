package pl.com.mat.imagecomparer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;

public class Controller implements ActionListener {
	
	private ViewManager win;
	private Model mod;
	
	private int returnVal;

	@Override
	public void actionPerformed(ActionEvent ev) {
		if (ev.getActionCommand().equals("start_m1")) {
			mod.start(0);
			win.visualize(mod, 0);
		} else if (ev.getActionCommand().equals("start_m2")) {
			mod.start(1);
			win.visualize(mod, 1);
		} else if (ev.getActionCommand().equals("load_first")) {
			returnVal = win.showFileChooser();

			switch (returnVal) {
			case JFileChooser.APPROVE_OPTION:
				File selectedFile = win.getSelectedFile();
				win.loadFirstImage(selectedFile);
				mod.loadFirstImage(selectedFile, win.getFirstImage().getWidth(), win.getFirstImage().getHeight());
				break;
			}
			win.cleanFileChooser();
		} else if (ev.getActionCommand().equals("load_second")) {
			returnVal = win.showFileChooser();

			switch (returnVal) {
			case JFileChooser.APPROVE_OPTION:
				File selectedFile = win.getSelectedFile();
				win.loadSecondImage(selectedFile);
				mod.loadSecondImage(selectedFile, win.getSecondImage().getWidth(), win.getSecondImage().getHeight());
				break;
			}
			win.cleanFileChooser();
		} else {
			win.clear();
		}
	}
	
	public Controller() {
		win = new ViewManager();
		mod = new Model(win);
		win.addListeners(this);
	}
	
	public static void main(String[] args) {
		new Controller();
	}

}