package com.plafond.jwiki;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;

import com.petebevin.markdown.MarkdownProcessor;

public class JWiki {

	private static String WIKI_PATH = "/home/pat/Wiki/wiki";
	private static String WIKI_PROTOCOL = "file:";
	private static String WIKI = WIKI_PROTOCOL + WIKI_PATH + "/@.txt";

	public static void main(String args[]) {

		run();
		//runEdit();
		// runTerminal();
	}
	
	private static URL convertBaseOnProtocol(URL url)
	{
		String source = url.toString();
		
		return convertBasedOnProtocol(source);
	}
	
	private static URL convertBasedOnProtocol(String source)
	{
		URL url = null;
		try{			
			url = new URL(source);
			if(source.contains(WIKI_PROTOCOL))
			{
				String html = generateHTML(source.replace(WIKI_PROTOCOL, ""));
				String fName = source.split(".txt")[0];
				fName = fName.replaceAll("/", "_");
				writeFile(createTempFile(fName), html);
				url = new URL("file:" + fName);
			}
			
			return url;
		}
		catch(Exception ex)
		{
			Log.log("Error when checknig if using file: protocl", ex);
		}
		
		return url;
	}
	
	private static void run() {
		final JFrame frame = new JFrame("JWiki");
		String home = WIKI_PATH + "/Home.txt";
		String initialURL = WIKI_PROTOCOL + home;
		final JEditorPane ed;

		JLabel lblURL = new JLabel("Address Bar");
		final JTextField txtURL = new JTextField(initialURL, 30);
		final JButton btnBrowse = new JButton("go");
		final JButton btnEdit = new JButton("Edit");
		final JButton btnView = new JButton("View");

		btnView.setVisible(false);
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		panel.add(lblURL);
		panel.add(txtURL);
		panel.add(btnBrowse);
		panel.add(btnEdit);
		panel.add(btnView);

		try {			
			// TODO - remove need of file
			URL url = convertBasedOnProtocol(initialURL);
			ed = new JEditorPane(url);
			ed.setEditable(false);

			ed.addHyperlinkListener(new HyperlinkListener() {

				public void hyperlinkUpdate(HyperlinkEvent e) {

					try {
						if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
							//JEditorPane pane = (JEditorPane) e.getSource();
							if (e instanceof HTMLFrameHyperlinkEvent) {
								HTMLFrameHyperlinkEvent evt = (HTMLFrameHyperlinkEvent) e;
								//HTMLDocument doc = (HTMLDocument) pane
								//		.getDocument();
								// doc.processHTMLFrameHyperlinkEvent(evt);
								String s = "";
								s = "" + 1;
								//ed.setPage(checkForFile(txtURL.getText().trim()));

								//ed.setPage();
							} else {
								try {
								//	pane.setPage(checkForFile(e.getURL().getFile()));
									ed.setPage(convertBaseOnProtocol(e.getURL()));
									txtURL.setText(e.getURL().toString());
								} catch (Throwable t) {
									t.printStackTrace();
								}

							}
						}
					} catch (Exception ex) {
						//TODO - LOG
						Log.log("Error in link EVENT handler", ex);
					}
				}
			});

			btnBrowse.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					try {
						ed.setPage(convertBasedOnProtocol(txtURL.getText().trim()));
					} catch (IOException ex) {
						Log.log("Error in button EVENT handler", ex);
					}
				}
			});
			
			btnEdit.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					try {
						ed.setPage(txtURL.getText().trim());
						ed.setEditable(true);
						btnEdit.setVisible(false);
						btnView.setVisible(true);
					} catch (IOException ex) {
						Log.log("Error in button EVENT handler", ex);
					}
				}
			});

			btnView.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					try {
						ed.setPage(convertBasedOnProtocol(txtURL.getText().trim()));
						ed.setEditable(false);
						btnEdit.setVisible(true);
						btnView.setVisible(false);
					} catch (IOException ex) {
						Log.log("Error in button EVENT handler", ex);
					}
				}
			});
			JScrollPane sp = new JScrollPane(ed);
			frame.setLayout(new BorderLayout());
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.getContentPane().add(panel, BorderLayout.NORTH);
			frame.getContentPane().add(sp, BorderLayout.CENTER);

			frame.setSize(600, 480);
			frame.setVisible(true);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private static void runTerminal() {
		System.out.println("********************");
		System.out.println("********MENU********");
		System.out.println("********************");
		System.out.println("");
		System.out.println("1. Home");
		System.out.println("2. Search");
		System.out.println("Q. Quit");
		Scanner in = new Scanner(System.in);

		String option = in.nextLine();

		if ("1".equals(option)) {
			renderPage();
			runTerminal();
		} else if ("2".equals(option)) {
			System.out.println("\nNot Yet");
			System.exit(0);
		} else if ("Q".equals(option)) {
			System.exit(0);
		} else {
			System.out.println("\nError:" + option + " is not a valid option");
			runTerminal();
		}

	}

	private static void renderPage() {
		renderPage("Home.txt");
	}

	private static void renderPage(String path) {

		// String html = generateHTML(path);
		// // TODO - remove need of file
		// writeFile("tmp", html);
		// try {
		// URL url = new URL("file:tmp");
		//
		// JEditorPane tp = new JEditorPane(url);
		// // JTextPane tp = new JTextPane();
		// tp.setPage(url);
		// JScrollPane js = new JScrollPane();
		// js.getViewport().add(tp);
		// JFrame jf = new JFrame();
		// jf.getContentPane().add(js);
		// jf.pack();
		// jf.setSize(400, 500);
		// jf.setVisible(true);
		//
		// // URI uri = new URI(html);
		// } catch (Exception ex) {
		// Log.log("Error writting html to TextPane", ex);
		// }
	}

	private static String convertLinks(String source) {
		Pattern p = Pattern.compile(".*\\{*\\}.*");
		Matcher matcher = p.matcher(source);
		List<String> fileLinks = new ArrayList<String>();
		while (matcher.find()) {
			fileLinks.add(matcher.group());
		}

		// [link text here](link.address.here "link title here")
		String linkReplace = "";
		for (String s2 : fileLinks) {
			String word = s2.split("\\}")[0];
			word = word.split("\\{")[1];

			String s3 = s2.replace('{', '[');
			s3 = s3.replace('}', ']');

			linkReplace = s3 + "(" + WIKI.replace("@", word) + " \"" + word
					+ "\")";
			source = source.replace(s2, linkReplace);
		}

		return source;
	}

	private static String generateHTML(String source) {

		MarkdownProcessor m = new MarkdownProcessor();
		// "/home/pat/Wiki/wiki/Home.txt"
		
		String html = readFile(source);
		
		html = convertLinks(html);
		html = m.markdown(html);
		html = "<html>" + html + "</html>";

		return html;
	}

	private static String readFile(String path) {
		StringBuilder fileContent = new StringBuilder();
		File f1 = new File(path);
		FileReader fr = null;
		BufferedReader br = null;

		try {
			fr = new FileReader(f1);
			br = new BufferedReader(fr);

			String line = null;
			while ((line = br.readLine()) != null) {
				fileContent.append(line + "\n");
			}
		} catch (Exception ex) {
			Log.log("Error reading from File", ex);
		} finally {
			try {
				br.close();
				fr.close();
			} catch (Exception ex) {
				Log.log("Error closing dis/fis connections of dictionary reader",
						ex);
			}
		}

		return fileContent.toString();
	}

	private static File createTempFile(String fileName)
	{
		File f1 = new File(fileName);
		f1.deleteOnExit();
		
		return f1;
	}
	private static void writeFile(File f1, String content) {

		FileWriter fw = null;
		BufferedWriter bw = null;

		try {
			fw = new FileWriter(f1);
			bw = new BufferedWriter(fw);

			bw.write(content);
		} catch (Exception ex) {
			Log.log("Error writing File", ex);
		} finally {
			try {
				bw.close();
				fw.close();
			} catch (Exception ex) {
				Log.log("Error closing dis/fis connections of dictionary reader",
						ex);
			}
		}
	}
}
