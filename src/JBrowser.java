import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.Document;
import javax.swing.undo.UndoManager;

public class JBrowser extends JFrame implements UndoableEditListener, Printable {
	/**
	 * browser page
	 */
	private static final long serialVersionUID = 1L;
	private JButton backJB;
	private JButton forwardJB;
	private JTabbedPane tab;
	private ArrayList<Tab> tabAccess = new ArrayList<Tab>();
	private JTextField addressBar;
	private HypeLinKHandler HYHandler = new HypeLinKHandler();
	private boolean change = true;
	private boolean tabcreate = true;

	public JBrowser() {
		super("Browser");
		setSize(800, 600);
		setLayout(new BorderLayout());
		menuConfigure();
		toolsBarConfigure();
		tabPaneConfigure();
		setVisible(true);
	}

	/**
	 * kolye amaliyat marbut be tab pane ::>> ijad va bastan va neshan dadan
	 * change baraye taghir address createtab baraye jologiry az oftadan tu
	 * halghe
	 */
	private void tabPaneConfigure() {
		tab = new JTabbedPane();

		createTab("Google", "http://www.google.com");
		tab.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				if (tabcreate
						&& tab.getTabCount() - 1 == ((JTabbedPane) arg0
								.getSource()).getSelectedIndex()) {
					tabcreate = false;
					createTab("New Tab", "");
					tabcreate = false;
					tab.setSelectedIndex(tab.getTabCount() - 2);
					change = true;
					tabcreate = false;
				}
				tabcreate = true;

				if (!change) {
					change = true;
					return;
				}
				if (((JTabbedPane) arg0.getSource()).getSelectedIndex() >= tabAccess
						.size()
						|| (((JTabbedPane) arg0.getSource()).getSelectedIndex()) < 0) {
					return;
				}
				updateforwardImage();
				updateBackImage(null);
				Tab tabee = tabAccess.get(((JTabbedPane) arg0.getSource())
						.getSelectedIndex());

				if (tabee != null) {
					addressBar.setText(tabee.getAdress());
				}
				updateforwardImage();
				updateBackImage(null);

			}
		});
		tab.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		add(tab);

	}

	private void createTab(String title, String adress) {
		System.out.println("create tab");
		if (tab.getTabCount() > 0)
			tab.remove(tab.getTabCount() - 1);
		MyEditorPane pane = new MyEditorPane(adress);
		JScrollPane scPane = new JScrollPane(pane);
		pane.addHyperlinkListener(HYHandler);

		tab.addTab(title, scPane);
		addCloseButton(tab.getTabCount() - 1, title);
		pane.setEditable(false);
		tabAccess.add(new Tab(pane, new JTextField(adress)));
		if (adress != "")
			loadPage(pane, adress);
		tab.addTab("", new ImageIcon("newTab.png"), null);

	}

	/**
	 * ezafe kardane dokme close baraye tab
	 * 
	 * @param i
	 *            ;; inex tab
	 * @param title
	 *            ;; onvane tab
	 */
	private void addCloseButton(int i, String title) {
		JPanel tab2 = new JPanel();
		tab2.setOpaque(false);
		ImageIcon closeXIcon = new ImageIcon("close.png");
		MYJButton tabCloseButton = new MYJButton(closeXIcon, i);
		tabCloseButton.setBorder(null);
		tabCloseButton.setRolloverIcon(new ImageIcon("close2.png"));
		tabCloseButton.setContentAreaFilled(false);
		Dimension closeButtonSize = new Dimension(
				closeXIcon.getIconWidth() + 2, closeXIcon.getIconHeight() + 2);
		tabCloseButton.setPreferredSize(closeButtonSize);
		tab2.add(new JLabel(title), BorderLayout.WEST);
		tab2.add(tabCloseButton, BorderLayout.EAST);
		tabCloseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeTab(((MYJButton) e.getSource()).getIndex());
			}
		});
		tab.setTabComponentAt(i, tab2);
	}

	/**
	 * remove tab
	 * 
	 * @param i
	 */
	protected void removeTab(int i) {
		if (i == 0)
			System.exit(1);
		tab.remove(i - 1);
		tabcreate = false;
		tab.setSelectedIndex(tab.getTabCount() - 2);
		tabAccess.remove(i - 1);

	}

	/**
	 * config tools bar
	 */
	private void toolsBarConfigure() {

		JToolBar toolsBar = new JToolBar();
		toolsBar.setFloatable(false);
		toolsBar.setBackground(Color.gray);
		JButton back = new JButton(new ImageIcon("back2.png"));
		back.setBorder(null);
		back.setEnabled(false);
		back.setContentAreaFilled(false);
		back.setToolTipText("Back");
		backJB = back;
		back.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				if (((JButton) arg0.getSource()).isEnabled())
					back();
				updateBackImage(arg0);
				updateforwardImage();
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
		});
		toolsBar.add(back);
		back = new JButton(new ImageIcon("forward2.png"));
		back.setBorder(null);
		forwardJB = back;
		back.setEnabled(false);
		back.setContentAreaFilled(false);
		back.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
			}
			@Override
			public void mousePressed(MouseEvent e) {
				if (forwardJB.isEnabled())
					forward();
				updateBackImage(null);
				updateforwardImage();
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		back.setToolTipText("Forward");
		toolsBar.add(back);
		// **********************************************
		back = new JButton(new ImageIcon("home.png"));
		back.setBorder(null);
		back.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				Tab tabee = tabAccess.get(tab.getSelectedIndex());
				addressBar.setText("http://www.google.com");
				MyEditorPane pane = new MyEditorPane(addressBar.getText());
				pane.addHyperlinkListener(HYHandler);
				JScrollPane scPane = new JScrollPane(pane);
				tabee.addPane(pane);
				pane.setEditable(false);

				int i = tab.getSelectedIndex();
				tabcreate = false;
				change = false;
				tab.remove(i);
				tabcreate = false;
				change = false;
				tab.insertTab(tabee.getPane().getAddress(), null, scPane, null,
						i);
				tabcreate = false;
				change = false;
				addCloseButton(i, tabee.getPane().getAddress());
				tabcreate = false;
				change = false;
				tab.setSelectedIndex(i);
				change = false;
				tabcreate = false;
				loadPage(pane, tabee.getPane().getAddress());
				tabcreate = true;
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
		});
		back.setContentAreaFilled(false);
		back.setToolTipText("Home");
		toolsBar.add(back);
		back = new JButton(new ImageIcon("refresh2.png"));
		back.setBorder(null);
		back.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
			@Override
			public void mousePressed(MouseEvent arg0) {
				Tab tabee = tabAccess.get(tab.getSelectedIndex());
				MyEditorPane pane = new MyEditorPane(addressBar.getText());
				pane.addHyperlinkListener(HYHandler);
				JScrollPane scPane = new JScrollPane(pane);
				tabee.addPane(pane);
				pane.setEditable(false);

				int i = tab.getSelectedIndex();
				tabcreate = false;
				change = false;
				tab.remove(i);
				tabcreate = false;
				change = false;
				tab.insertTab(tabee.getPane().getAddress(), null, scPane, null,
						i);
				tabcreate = false;
				change = false;
				addCloseButton(i, tabee.getPane().getAddress());
				change = false;
				tab.setSelectedIndex(i);
				tabcreate = false;
				change = false;
				loadPage(pane, tabee.getPane().getAddress());
				tabcreate = true;
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
		});
		back.setEnabled(false);
		back.setContentAreaFilled(false);
		back.setToolTipText("Refresh");
		toolsBar.add(back);

		addressBar = new JTextField("http://www.google.com");
		addressBar.setToolTipText("Address");
		toolsBar.add(addressBar);
		Document doc = addressBar.getDocument();
		doc.addUndoableEditListener(this);
		addressBar.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent arg0) {
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				updateTabes(arg0);
			}
			@Override
			public void keyPressed(KeyEvent arg0) {
			}
		});
		// **********************************************
		back = new JButton(new ImageIcon("forward.png"));
		back.setBorder(null);
		back.setContentAreaFilled(false);
		back.setToolTipText("Go");
		back.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				Tab tabee = tabAccess.get(tab.getSelectedIndex());
				MyEditorPane pane = new MyEditorPane(addressBar.getText());
				pane.addHyperlinkListener(HYHandler);
				JScrollPane scPane = new JScrollPane(pane);
				tabee.addPane(pane);
				pane.setEditable(false);

				int i = tab.getSelectedIndex();
				tabcreate = false;
				change = false;
				tab.remove(i);
				tabcreate = false;
				change = false;
				tab.insertTab(tabee.getPane().getAddress(), null, scPane, null,
						i);
				tabcreate = false;
				change = false;
				addCloseButton(i, tabee.getPane().getAddress());
				tabcreate = false;
				change = false;
				tab.setSelectedIndex(i);
				change = false;
				tabcreate = false;
				loadPage(pane, tabee.getPane().getAddress());
				tabcreate = true;
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
		});
		toolsBar.add(back);

		getContentPane().add(toolsBar, BorderLayout.NORTH);
	}

	protected void updateforwardImage() {

		Tab tabee = tabAccess.get(tab.getSelectedIndex());

		if (tabee.getIndex() == tabee.size() - 1) {
			(forwardJB).setEnabled(false);
			(forwardJB).setIcon(new ImageIcon("forward2.png"));
		} else {
			(forwardJB).setEnabled(true);
			(forwardJB).setIcon(new ImageIcon("forward3.png"));
		}

	}

	protected void forward() {

		Tab tabee = tabAccess.get(tab.getSelectedIndex());
		MyEditorPane p = tabee.getAfterPane();

		JScrollPane scPane = new JScrollPane(p);

		int i = tab.getSelectedIndex();
		tabcreate = false;
		change = false;
		tab.remove(i);
		tabcreate = false;
		change = false;
		tab.insertTab(tabee.getPane().getAddress(), null, scPane, null, i);
		tabcreate = false;
		change = false;
		addressBar.setText(tabee.getPane().getAddress());
		addCloseButton(i, tabee.getPane().getAddress());
		tabcreate = false;
		change = false;
		tab.setSelectedIndex(i);
		tabcreate = false;
		change = false;
		tabcreate = true;
	}

	protected void updateBackImage(MouseEvent arg0) {

		Tab tabee = tabAccess.get(tab.getSelectedIndex());

		if (tabee.getIndex() == 0) {
			(backJB).setEnabled(false);
			(backJB).setIcon(new ImageIcon("back2.png"));
		} else {
			(backJB).setEnabled(true);
			(backJB).setIcon(new ImageIcon("back3.png"));
		}

	}

	protected void back() {

		Tab tabee = tabAccess.get(tab.getSelectedIndex());
		MyEditorPane p = tabee.getBeforPane();

		JScrollPane scPane = new JScrollPane(p);
		int i = tab.getSelectedIndex();
		tabcreate = false;
		change = false;
		tab.remove(i);
		change = false;
		tabcreate = false;
		tab.insertTab(tabee.getPane().getAddress(), null, scPane, null, i);
		addressBar.setText(tabee.getPane().getAddress());
		tabcreate = false;
		change = false;
		addCloseButton(i, tabee.getPane().getAddress());
		change = false;
		tabcreate = false;
		change = false;
		tab.setSelectedIndex(i);
		tabcreate = false;
		change = false;
		tabcreate = true;

	}

	protected void updateTabes(KeyEvent arg0) {
		Tab tabee = tabAccess.get(tab.getSelectedIndex());
		if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
			MyEditorPane pane = new MyEditorPane(addressBar.getText());
			pane.addHyperlinkListener(HYHandler);
			JScrollPane scPane = new JScrollPane(pane);
			tabee.addPane(pane);
			pane.setEditable(false);

			int i = tab.getSelectedIndex();

			tabcreate = false;
			tab.remove(i);
			tabcreate = false;
			change = false;
			tab.insertTab(tabee.getPane().getAddress(), null, scPane, null, i);
			tabcreate = false;
			addCloseButton(i, tabee.getPane().getAddress());
			change = false;
			tabcreate = false;
			tab.setSelectedIndex(i);
			change = false;
			tabcreate = false;

			loadPage(pane, tabee.getPane().getAddress());
			tabcreate = true;
			return;
		}
		tabee.setAddress(addressBar.getText());
	}

	private void loadPage(MyEditorPane pane, String address) {
		updateBackImage(null);
		updateforwardImage();

		try {
			pane.setPage(address);
		} catch (IOException e) {

			pane.setText("\n\n\n\t\tOops! this browser could not find \n\n\t\t\t"
					+ e.getMessage());
		}
	}

	private void menuConfigure() {
		JMenuBar menuBar = new JMenuBar();
		add(menuBar);
		setJMenuBar(menuBar);
		menuFileConfigure(menuBar);
		menuEditeConfigure(menuBar);
		menuViewConfigure(menuBar);
		menuHelpConfigure(menuBar);

	}

	private void menuEditeConfigure(JMenuBar menuBar) {

		JMenu menu_edit = new JMenu("Edit");
		menuBar.add(menu_edit);
		JMenuItem item = new JMenuItem("Undo");
		item.setMnemonic('U');
		item.setIcon(new ImageIcon("undo.png"));
		item.setAccelerator(KeyStroke.getKeyStroke('Z', ActionEvent.CTRL_MASK));
		menu_edit.add(item);
		item.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
			@Override
			public void mousePressed(MouseEvent arg0) {
				undo.undo();
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
		});
		// ***************************************
		item = new JMenuItem("Redo");
		item.setIcon(new ImageIcon("redo.png"));
		item.setMnemonic('R');
		item.setAccelerator(KeyStroke.getKeyStroke('Y', ActionEvent.CTRL_MASK));
		menu_edit.add(item);
		item.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				undo.redo();
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
		});

		menu_edit.addSeparator();
		// ***************************************
		item = new JMenuItem("Copy");
		item.setMnemonic('C');
		item.setIcon(new ImageIcon("copy.png"));
		item.setAccelerator(KeyStroke.getKeyStroke('C', ActionEvent.CTRL_MASK));
		item.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				Tab tabee = tabAccess.get(tab.getSelectedIndex());
				tabee.getPane().copy();
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
		});
		menu_edit.add(item);
		// *****************f**********************
		item = new JMenuItem("Cut");
		item.setMnemonic('C');
		item.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				addressBar.cut();
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
		});
		item.setIcon(new ImageIcon("cut.png"));
		item.setAccelerator(KeyStroke.getKeyStroke('X', ActionEvent.CTRL_MASK));
		menu_edit.add(item);
		// ***************************************
		item = new JMenuItem("Paste");
		item.setMnemonic('P');
		item.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				addressBar.paste();
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
		});
		item.setIcon(new ImageIcon("paste.png"));
		item.setAccelerator(KeyStroke.getKeyStroke('V', ActionEvent.CTRL_MASK));
		menu_edit.add(item);
		menu_edit.addSeparator();
		// ***************************************
		item = new JMenuItem("Find");
		item.setMnemonic('F');
		item.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				Tab tabee = tabAccess.get(tab.getSelectedIndex());
				 new Find(tabee.getPane());
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
		});
		item.setIcon(new ImageIcon("search.png"));
		item.setAccelerator(KeyStroke.getKeyStroke('F', ActionEvent.CTRL_MASK));
		menu_edit.add(item);

	}

	private void menuHelpConfigure(JMenuBar menuBar) {
		JMenu menu_view = new JMenu("View");
		menuBar.add(menu_view);
	}

	private void menuViewConfigure(JMenuBar menuBar) {

		JMenu menu_view = new JMenu("View");
		menuBar.add(menu_view);

		JMenuItem item = new JMenuItem("Back");
		item.setMnemonic('B');
		item.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				if ((backJB).isEnabled())
					back();
				updateBackImage(null);
				updateforwardImage();
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
		});
		item.setIcon(new ImageIcon("back.png"));
		item.setAccelerator(KeyStroke.getKeyStroke('B', ActionEvent.ALT_MASK));
		menu_view.add(item);
		// ***************************************
		item = new JMenuItem("Forward");
		item.setIcon(new ImageIcon("forward.png"));
		item.setMnemonic('F');
		item.setAccelerator(KeyStroke.getKeyStroke('F', ActionEvent.ALT_MASK));
		menu_view.add(item);
		menu_view.addSeparator();
		// ***************************************
		item = new JMenuItem("Refresh");
		item.setMnemonic('R');
		item.setIcon(new ImageIcon("refresh.png"));
		item.setAccelerator(KeyStroke.getKeyStroke('R', ActionEvent.CTRL_MASK));
		item.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				Tab tabee = tabAccess.get(tab.getSelectedIndex());
				MyEditorPane pane = new MyEditorPane(addressBar.getText());
				pane.addHyperlinkListener(HYHandler);
				JScrollPane scPane = new JScrollPane(pane);
				tabee.addPane(pane);
				pane.setEditable(false);
				int i = tab.getSelectedIndex();
				tabcreate = false;
				change = false;
				tab.remove(i);
				tabcreate = false;
				change = false;
				change = false;
				tab.insertTab(tabee.getPane().getAddress(), null, scPane, null,
						i);
				tabcreate = false;
				change = false;
				addCloseButton(i, tabee.getPane().getAddress());
				change = false;
				tabcreate = false;
				change = false;
				tab.setSelectedIndex(i);
				change = false;
				tabcreate = false;
				change = false;
				loadPage(pane, tabee.getPane().getAddress());
				tabcreate = true;
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
		});
		menu_view.add(item);

		menu_view.addSeparator();
		// *****************f**********************
		item = new JMenuItem("Page Source");
		item.setMnemonic('P');
		item.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				Tab tabee = tabAccess.get(tab.getSelectedIndex());
				PageSource pageSource = new PageSource(tabee.getPane());
				pageSource.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
		});
		item.setAccelerator(KeyStroke.getKeyStroke('P', ActionEvent.ALT_MASK));
		menu_view.add(item);
	}

	private void menuFileConfigure(JMenuBar menuBar) {
		JMenu menu_file = new JMenu("File");
		menuBar.add(menu_file);
		JMenuItem item = new JMenuItem("New Tab");
		item.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				createTab("New Tab", "");
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
		});
		item.setMnemonic('N');
		item.setAccelerator(KeyStroke.getKeyStroke('T', ActionEvent.CTRL_MASK));
		menu_file.add(item);
		// ***************************************
		item = new JMenuItem("New Windows");
		item.setMnemonic('N');
		item.setAccelerator(KeyStroke.getKeyStroke('N', ActionEvent.CTRL_MASK));
		item.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				JBrowser browser = new JBrowser();
				browser.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
		});
		menu_file.add(item);
		menu_file.addSeparator();
		// ***************************************
		item = new JMenuItem("Open");
		item.setIcon(new ImageIcon("open.png"));
		item.setMnemonic('O');
		item.setAccelerator(KeyStroke.getKeyStroke('O', ActionEvent.CTRL_MASK));
		item.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				Tab tabee = tabAccess.get(tab.getSelectedIndex());

				try {
					final JFileChooser fc = new JFileChooser();
					int returnVal = fc.showOpenDialog(null);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File selfile = fc.getSelectedFile();
						MyEditorPane pane = new MyEditorPane(selfile.getPath());
						pane.addHyperlinkListener(HYHandler);
						JScrollPane scPane = new JScrollPane(pane);
						tabee.addPane(pane);
						pane.setEditable(false);

						int i = tab.getSelectedIndex();
						tabcreate = false;
						change = false;
						tab.remove(i);
						tabcreate = false;
						change = false;
						tab.insertTab(tabee.getPane().getAddress(), null,
								scPane, null, i);
						tabcreate = false;
						change = false;
						addCloseButton(i, tabee.getPane().getAddress());
						change = false;
						tabcreate = false;
						tab.setSelectedIndex(i);
						change = false;
						tabcreate = false;
						Scanner in = new Scanner(selfile);
						while (in.hasNextLine()) {
							pane.setText(pane.getText() + in.nextLine());
						}
						tabcreate = true;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
		});

		menu_file.add(item);
		// *****************f**********************
		item = new JMenuItem("Save As");
		item.setIcon(new ImageIcon("save.png"));
		item.setMnemonic('S');
		item.setAccelerator(KeyStroke.getKeyStroke('S', ActionEvent.CTRL_MASK));
		menu_file.add(item);
		item.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				try {
					final JFileChooser fc = new JFileChooser();
					int returnVal = fc.showSaveDialog(null);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File selfile = fc.getSelectedFile();
						PrintStream print = new PrintStream(selfile);
						Tab tabee = tabAccess.get((tab).getSelectedIndex());
						print.print(tabee.getPane().getText());
						print.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
		});

		// ***************************************
		item = new JMenuItem("Print");
		item.setIcon(new ImageIcon("print.png"));
		item.setMnemonic('P');
		item.setAccelerator(KeyStroke.getKeyStroke('P', ActionEvent.CTRL_MASK));
		menu_file.add(item);
		item.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				PrinterJob printJob = PrinterJob.getPrinterJob();
				if (printJob.printDialog())
					try {
						printJob.print();
					} catch (PrinterException pe) {
					}
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
		});
		menu_file.addSeparator();
		// ***************************************
		item = new JMenuItem("Exit");
		item.setIcon(new ImageIcon("exit.png"));
		item.setMnemonic('E');
		menu_file.add(item);
		item.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				setVisible(false);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
		});
	}

	UndoManager undo = new UndoManager();

	@Override
	public void undoableEditHappened(UndoableEditEvent arg0) {
		undo.addEdit(arg0.getEdit());

	}

	@Override
	public int print(Graphics arg0, PageFormat arg1, int arg2)
			throws PrinterException {
		return 0;
	}

	class HypeLinKHandler implements HyperlinkListener {

		@Override
		public void hyperlinkUpdate(HyperlinkEvent e) {
			if (e.getEventType() != HyperlinkEvent.EventType.ACTIVATED)
				return;
			Tab tabee = tabAccess.get(tab.getSelectedIndex());
			addressBar.setText(e.getURL().toString());
			MyEditorPane pane = new MyEditorPane(addressBar.getText());
			pane.addHyperlinkListener(HYHandler);
			JScrollPane scPane = new JScrollPane(pane);
			tabee.addPane(pane);
			pane.setEditable(false);

			int i = tab.getSelectedIndex();
			tabcreate = false;
			change = false;
			tab.remove(i);
			change = false;
			tabcreate = false;
			tab.insertTab(tabee.getPane().getAddress(), null, scPane, null, i);
			addCloseButton(i, tabee.getPane().getAddress());
			change = false;
			tabcreate = false;
			tab.setSelectedIndex(i);
			change = false;
			tabcreate = false;
			loadPage(pane, tabee.getPane().getAddress());
			tabcreate = true;
		}

	}

}
