package com.liuhaoyuan.myreader;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.liuhaoyuan.myreader.bean.ArticleInfo;
import com.liuhaoyuan.myreader.utils.DatabaseUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<ArticleInfo> infoList;
    private FloatingActionButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        button = (FloatingActionButton) findViewById(R.id.fab_word);
        init();
    }
    private void init(){
        infoList = DatabaseUtils.getArticleInfoList();
        MyAdapter adapter=new MyAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(MainActivity.this,DetailActivity.class);
                intent.putExtra("content",infoList.get(position).getLesson());
                startActivity(intent);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,WordActivity.class));
            }
        });
    }
    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return infoList.size();
        }

        @Override
        public Object getItem(int position) {
            return infoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView==null){
                convertView=View.inflate(MainActivity.this,R.layout.list_item,null);
                holder=new ViewHolder();
                holder.unit= (TextView) convertView.findViewById(R.id.tv_unit);
                holder.lesson= (TextView) convertView.findViewById(R.id.tv_lesson);
                holder.title= (TextView) convertView.findViewById(R.id.tv_title);
                convertView.setTag(holder);
            }else {
                holder= (ViewHolder) convertView.getTag();
            }
            holder.title.setText(infoList.get(position).getTitle());
            holder.unit.setText("Unit "+infoList.get(position).getUnit());
            holder.lesson.setText("lesson "+infoList.get(position).getLesson());
            return convertView;
        }
    }
    class ViewHolder{
        TextView unit;
        TextView lesson;
        TextView title;
    }
}
