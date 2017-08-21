package model;


public class Indicator {
	private long id;
	private String title;
	private String headline;
	private String summary;
	private String description;
	private String data;
	private String period;
	private String unit;
	private String url;
	private String updated_on;
	private String change_type;
	private String change_value;
	private String change_desc;
	private String index_value;
	private String cat_id;
	private String timestamp;


	private boolean prime;

	public Indicator() {
	}

	public Indicator(long id, String title, String headline, String summary, String description,  String data , String period , String unit,String url, String updated_on, String change_type, String change_value, String change_desc, String index_value , String cat_id) {



		this.id = id;
		this.title = title;
		this.headline = headline;
		this.summary = summary;
		this.unit = unit;
		this.description = description;
		this.data = data;
		this.period = period;
		this.url = url;
		this.updated_on = updated_on;

		this.change_type = change_type;
		this.change_value = change_value;
		this.change_desc = change_desc;
		this.index_value = index_value;
		this.cat_id = cat_id;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	private int imageID;

	public String getHeadline() { return headline; }

	public void setHeadline(String headline) {
		this.headline = headline;
	}

	public String getSummary() { return summary; }

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public String getData() { return data; }

	public void setData(String data) {
		this.data = data;
	}


	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}

	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getUrl() { return url; }
	public void setUrl(String url) {
		this.url = url;
	}

	public String getChangeType() { return change_type; }

	public void setChangeType(String change_type) {
		this.change_type = change_type;
	}

	public String getUpdated_on() { return updated_on; }
	public void setUpdated_on(String updated_on) {
		this.updated_on = updated_on;
	}

	public String getChange_value() { return change_value; }
	public void setChange_value(String change_value) {
		this.change_value = change_value;
	}

	public String getChange_desc() { return change_desc; }
	public void setChange_desc(String change_desc) {
		this.change_desc = change_desc;
	}

	public String getIndex_value() { return index_value; }
	public void setIndex_value(String index_value) {
		this.index_value = index_value;
	}

	public String getCat_id() { return cat_id; }
	public void setCat_id(String cat_id) {
		this.cat_id = cat_id;
	}

	public boolean isPrime() {
		return prime;
	}

	public void setPrime(boolean prime) {
		this.prime = prime;
	}

	public int getImageID() {
		return imageID;
	}

	public void setImageID(int imageID) {
		this.imageID = imageID;
	}
}
