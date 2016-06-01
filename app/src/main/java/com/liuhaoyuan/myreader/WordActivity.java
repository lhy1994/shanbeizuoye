package com.liuhaoyuan.myreader;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.huxq17.swipecardsview.BaseCardAdapter;
import com.huxq17.swipecardsview.SwipeCardsView;
import com.liuhaoyuan.myreader.utils.DatabaseUtils;

import java.util.ArrayList;
import java.util.List;

public class WordActivity extends AppCompatActivity {

    private ArrayList<String> wordList;
    private MyCardAdapter cardAdapter;
    private SwipeCardsView swipeCardsView;
    private Button look;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);
        swipeCardsView = (SwipeCardsView) findViewById(R.id.swipCardsView);
        look = (Button) findViewById(R.id.btn_look);
        initData();
    }

    public void initData() {
        wordList = DatabaseUtils.queryWords(this);
//        adapter = new WordListAdapter();
//        listView.setAdapter(adapter);
        cardAdapter = new MyCardAdapter(wordList, this);
        swipeCardsView.setAdapter(cardAdapter);
        look.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipeCardsView.setAdapter(cardAdapter);
                swipeCardsView.notifyDatasetChanged(0);
            }
        });
    }

    public class MyCardAdapter extends BaseCardAdapter {
        private List<String> datas;
        private Context context;

        public MyCardAdapter(List<String> datas, Context context) {
            this.datas = datas;
            this.context = context;

        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public int getCardLayoutId() {
            return R.layout.word_list_item;
        }

        @Override
        public void onBindData(final int position, View cardview) {
            if (datas == null||datas.size()==0) {
                return;
            }

            final TextView textView = (TextView) cardview.findViewById(R.id.tv_wordnote);
            final ImageButton button = (ImageButton) cardview.findViewById(R.id.btn_delete);

//            if (position==datas.size()-1){
//                datas.add("没有生词了，快去添加！");
//                button.setVisibility(View.GONE);
//            }
            textView.setText(datas.get(position));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseUtils.deleteWord(WordActivity.this, textView.getText().toString());
                    Toast.makeText(WordActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                    datas.remove(position);
                    button.setVisibility(View.GONE);
                    swipeCardsView.setAdapter(cardAdapter);
                    swipeCardsView.notifyDatasetChanged(0);
                }
            });
        }

        /**
         * 如果可见的卡片数是3，则可以不用实现这个方法
         *
         * @return
         */
        @Override
        public int getVisibleCardCount() {
            return datas.size();
        }
    }
}
