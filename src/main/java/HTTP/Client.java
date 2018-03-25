package HTTP;

import org.json.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.*;
import javax.swing.*;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class Client extends JPanel implements ActionListener {

	protected JTextField textField;
	protected JTextArea textArea;
	protected JTextArea textAreaNew;
	protected JButton button;
	private final static String newline = "\n";

	public Client() {
		super(new GridBagLayout());

		button = new JButton("Search");
		button.addActionListener(this);

		textField = new JTextField(20);
		textField.addActionListener(this);

		textArea = new JTextArea(10, 20);
		textArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(textArea);

		GridBagConstraints c = new GridBagConstraints();
		c.gridwidth = GridBagConstraints.REMAINDER;

		c.fill = GridBagConstraints.HORIZONTAL;
		add(textField, c);

		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;
		add(scrollPane, c);
		add(button, c);
	}

	public void actionPerformed(ActionEvent evt) {

		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(
				"http://countryapi.gear.host/v1/Country/getCountries?pName=" + textField.getText());
		HttpResponse response;
		
		{
			try {
				response = client.execute(request);
				HttpEntity entity = response.getEntity();

				if (entity != null) {
					
					String retSrc = EntityUtils.toString(entity);
					JSONObject result = new JSONObject(retSrc);
					JSONArray tokenList = result.getJSONArray("Response");
					JSONObject oj = tokenList.getJSONObject(0);
					
					String token = oj.getString("NativeName");
					String token2 = oj.getString("Latitude");
					String token3 = oj.getString("Longitude");
					String token4 = oj.getString("CurrencyCode");

					textArea.append(token + ", " + token2 + ", " + token3 + ", " + token4 + newline);
					textField.selectAll();
					textArea.setCaretPosition(textArea.getDocument().getLength());
				}
			} catch (Exception e) {
			}
		}
	}

	private static void createAndShowGUI() {

		JFrame frame = new JFrame("Country");
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new Client());
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}