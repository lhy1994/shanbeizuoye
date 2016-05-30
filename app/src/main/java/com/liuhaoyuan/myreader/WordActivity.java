package com.liuhaoyuan.myreader;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.liuhaoyuan.myreader.R;
import com.liuhaoyuan.myreader.utils.DatabaseUtils;

import java.util.ArrayList;

public class WordActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<String> wordList;
    private WordListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);
        listView = (ListView) findViewById(R.id.list_word);
        initData();
    }
    public void initData(){
        wordList= DatabaseUtils.queryWords(this);
        adapter = new WordListAdapter();
        listView.setAdapter(adapter);
    }

    class WordListAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return wordList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView==null){
                convertView=View.inflate(WordActivity.this,R.layout.word_list_item,null);
                holder=new ViewHolder();
                holder.word= (TextView) convertView.findViewById(R.id.tv_wordnote);
                holder.delete= (ImageButton) convertView.findViewById(R.id.btn_delete);
                convertView.setTag(holder);
            }else {
                holder= (ViewHolder) convertView.getTag();
            }
            holder.word.setText(wordList.get(position));
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseUtils.deleteWord(WordActivity.this,holder.word.getText().toString());
                    Toast.makeText(WordActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                    wordList=DatabaseUtils.queryWords(WordActivity.this);
                    adapter.notifyDataSetChanged();
                }
            });
            return convertView;
        }
    }
    static class ViewHolder{
        TextView word;
        ImageButton delete;
    }
}
