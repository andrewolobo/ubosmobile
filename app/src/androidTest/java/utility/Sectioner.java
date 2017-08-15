package utility;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.ubos.apps.ubosstat.config.global;
import org.ubos.apps.ubosstat.json.GeneralText;
import org.ubos.apps.ubosstat.json.Graphs;
import org.ubos.apps.ubosstat.json.Header;
import org.ubos.apps.ubosstat.json.JsonPacket;
import org.ubos.apps.ubosstat.json.Order;
import org.ubos.apps.ubosstat.json.SpecialStats;
import org.ubos.apps.ubosstat.json.Statistics;
import org.ubos.apps.ubosstat.json.Table;
import org.ubos.apps.ubosstat.json.Titles;
import org.ubos.apps.ubosstat.widgets.BarChart;
import org.ubos.apps.ubosstat.widgets.Datatable;
import org.ubos.apps.ubosstat.widgets.GeneralTextView;
import org.ubos.apps.ubosstat.widgets.HeaderView;
import org.ubos.apps.ubosstat.widgets.SpecialStatistic;
import org.ubos.apps.ubosstat.widgets.Statistic;
import org.ubos.apps.ubosstat.widgets.Title;

import java.lang.reflect.Type;
import java.util.ArrayList;



/**
 * Created by Andrew on 14-06-2017.
 */

public class Sectioner {
    public ArrayList<JsonPacket> payload;
    public Integer position = 0;
    public Sectioner(){
        if(global.data!=null){
            payload = global.data;
        }
    }
    public void Section(ArrayList<JsonPacket> payload){
        for(JsonPacket packet: payload){
            for(Order order: packet.Order){

            }
        }
    }
    public Integer Section(String section){
        Integer place = 0;
        for(JsonPacket packet: payload){
            if (packet.Section.name.equals(section)) {
                position = place;
                return position;
            }
            place ++;
        }
        return position;
    }
    public View generateLayout(Order order, ViewGroup layout, Context context, Activity activity, Typeface customFont){
        position = global.position;
        System.out.println("Section:"+order.section);
        if(global.section_map.get(order.section).equals("Titles")){
            for(Titles m:global.data.get(position).Titles ){
                if(m.position == order.position){
                    System.out.println("did:"+global.section_map.get(order.section));
                    Title main_title = new Title(context, activity, layout);
                    Titles title =  m;
                    main_title.buildTitle(title);
                    layout.addView(main_title.getTitle());
                }
            }

        }else if(global.section_map.get(order.section).equals("Statistics")){
            System.out.println("did:"+global.section_map.get(order.section));
            for(Statistics c : global.data.get(position).Statistics){
                if(c.position==order.position){
                    System.out.println(new Gson().toJson(c));
                    Statistic str = new Statistic(activity,layout,customFont);
                    str.buildStatistic(c);
                    layout.addView(str.getInfo_layout());
                }
            }


        }else if(global.section_map.get(order.section).equals("Tables")){
            for(Table t : global.data.get(position).Tables) {
                System.out.println("did:"+global.section_map.get(order.section));
                if (t.position == order.position) {
                    System.out.println("did:"+global.section_map.get(order.section));
                    Datatable datatable = new Datatable(context);
                    boolean color_t = false;
                    datatable.setColorPallet(t.color);
                    Table table = t;
                    datatable.setHeader(table.title);
                    Gson g = new Gson();
                    Type listType = new TypeToken<ArrayList<String[]>>() {}.getType();
                    ArrayList<String[]> result = g.fromJson(table.data,listType);
                    datatable.addRow(result);
                    layout.addView(datatable);
                }
            }

        }else if(global.section_map.get(order.section).equals("SpecialStats")){
            System.out.println("did:"+global.section_map.get(order.section));
            for(SpecialStats t : global.data.get(position).SpecialStats) {
                if (t.position == order.position) {
                    System.out.println("did:"+global.section_map.get(order.section));
                    SpecialStatistic special = new SpecialStatistic(context,activity,layout,customFont);
                    SpecialStats stat =t;
                    special.buildStatistic(stat);

                    layout.addView(special.getSpecialStatistic());
                }

            }
        }else if(global.section_map.get(order.section).equals("Graphs")){
            System.out.println("did:"+global.section_map.get(order.section));
            for(Graphs gr: global.data.get(position).Graphs){
                if(gr.position==order.position){
                    System.out.println("did:"+global.section_map.get(order.section));
                    BarChart chart1 = new BarChart(context,gr);
                    layout.addView(chart1.getChart(), new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 300));
                }
            }


        }
        else if(global.section_map.get(order.section).equals("Headers")){
            System.out.println("did:"+global.section_map.get(order.section));
            for(Header gr: global.data.get(position).Headers){
                if(gr.position==order.position){
                    System.out.println("did:"+global.section_map.get(order.section));
                    HeaderView hr = new HeaderView(activity, layout, gr.header_text, customFont);
                    layout.addView(hr.getText_header());
                }
            }


        }
        else if(global.section_map.get(order.section).equals("GeneralText")){
            System.out.println("did:"+global.section_map.get(order.section));
            for(GeneralText gt: global.data.get(position).GeneralText){
                if(gt.position==order.position){
                    System.out.println("did:"+global.section_map.get(order.section));
                    GeneralTextView view = new GeneralTextView(activity, layout, gt.text, customFont);
                    layout.addView(view.getText_header());
                }
            }


        }
        return layout;

    }

}
