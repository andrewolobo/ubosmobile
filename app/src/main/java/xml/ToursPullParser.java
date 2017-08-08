package xml;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.util.Log;

import org.ubos.apps.ubosstat.R;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import model.Indicator;

public class ToursPullParser {

	private static final String LOGTAG = "UBOSAPP";
	
	private static final String INDICATOR_ID = "indicatorId";
	private static final String INDICATOR_TITLE = "indicatorTitle";
	private static final String INDICATOR_DESC = "description";
	private static final String INDICATOR_PERIOD = "period";
	private static final String INDICATOR_UNIT = "unit";
	
	private Indicator currentIndicator  = null;
	private String currentTag = null;
	List<Indicator> indicators = new ArrayList<Indicator>();

	public List<Indicator> parseXML(Context context) {

		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			
			InputStream stream = context.getResources().openRawResource(R.raw.indicators);
			xpp.setInput(stream, null);

			int eventType = xpp.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {
					handleStartTag(xpp.getName());
				} else if (eventType == XmlPullParser.END_TAG) {
					currentTag = null;
				} else if (eventType == XmlPullParser.TEXT) {
					handleText(xpp.getText());
				}
				eventType = xpp.next();
			}

		} catch (NotFoundException e) {
			Log.d(LOGTAG, e.getMessage());
		} catch (XmlPullParserException e) {
			Log.d(LOGTAG, e.getMessage());
		} catch (IOException e) {
			Log.d(LOGTAG, e.getMessage());
		}

		return indicators;
	}

	private void handleText(String text) {
		String xmlText = text;
		if (currentIndicator != null && currentTag != null) {
			if (currentTag.equals(INDICATOR_ID)) {
				Integer id = Integer.parseInt(xmlText);
				currentIndicator.setId(id);
			} 
			else if (currentTag.equals(INDICATOR_TITLE)) {
				currentIndicator.setTitle(xmlText);
			}
			else if (currentTag.equals(INDICATOR_DESC)) {
				currentIndicator.setDescription(xmlText);
			}
			else if (currentTag.equals(INDICATOR_PERIOD)) {
				currentIndicator.setPeriod(xmlText);
			}
			else if (currentTag.equals(INDICATOR_UNIT)) {
				currentIndicator.setUnit(xmlText);
			}
		}
	}

	private void handleStartTag(String name) {
		if (name.equals("tour")) {
			currentIndicator = new Indicator();
			indicators.add(currentIndicator);
		}
		else {
			currentTag = name;
		}
	}
}
