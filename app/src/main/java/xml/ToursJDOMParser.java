package xml;

import android.content.Context;
import android.util.Log;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.ubos.apps.ubosstat.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import model.Indicator;

public class ToursJDOMParser {

	private static final String LOGTAG = "UBOSAPP";

	private static final String INDICATOR_TAG = "indicator";
	private static final String INDICATOR_ID = "indicatorId";
	private static final String INDICATOR_TITLE = "indicatorTitle";
	private static final String INDICATOR_DESC = "description";
	private static final String INDICATOR_PERIOD = "period";
	private static final String INDICATOR_UNIT = "unit";
	

	public List<Indicator> parseXML(Context context) {

		InputStream stream = context.getResources().openRawResource(R.raw.indicators);
		SAXBuilder builder = new SAXBuilder();
		List<Indicator> tours = new ArrayList<Indicator>();

		try {

			Document document = (Document) builder.build(stream);
			org.jdom2.Element rootNode = document.getRootElement();
			List<org.jdom2.Element> list = rootNode.getChildren(INDICATOR_TAG);

			for (Element node : list) {
				Indicator indicator = new Indicator();
				indicator.setId(Integer.parseInt(node.getChildText(INDICATOR_ID)));
				indicator.setTitle(node.getChildText(INDICATOR_TITLE));
				indicator.setDescription(node.getChildText(INDICATOR_DESC));
				indicator.setPeriod(node.getChildText(INDICATOR_PERIOD));
				indicator.setUnit(node.getChildText(INDICATOR_UNIT));
				tours.add(indicator);
			}

		} catch (IOException e) {
			Log.i(LOGTAG, e.getMessage());
		} catch (JDOMException e) {
			Log.i(LOGTAG, e.getMessage());
		}
		return tours;
	}

}
