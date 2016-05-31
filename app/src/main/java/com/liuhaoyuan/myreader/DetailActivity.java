package com.liuhaoyuan.myreader;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatPopupWindow;
import android.support.v7.widget.PopupMenu;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.liuhaoyuan.myreader.bean.Word;
import com.liuhaoyuan.myreader.utils.DatabaseUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by liuhaoyuan on 2016/5/28.
 */
public class DetailActivity extends AppCompatActivity {

    private String lesson;
    private String content;
    private String newContent;
    private TextView contentTextView;
    private Intent intent;
    private ArrayList<Word> mWordList;
    private FloatingActionButton fab;
    private boolean highLight = true;
    private SeekBar seekBar;
    private TextView levelTextView;
    private int progress = 0;
    private PopupWindow popupWindow;
    private ImageButton dismissButton;
    private AppCompatButton addWordButton;
    private TextView word;
    private int width;
    private int height;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_detail);

        intent = getIntent();
        lesson = intent.getStringExtra("content");
        contentTextView = (TextView) findViewById(R.id.tv_content);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        levelTextView = (TextView) findViewById(R.id.tv_level);

        init();
        initPopupWindow();
    }

    private void init() {
        width = getWindowManager().getDefaultDisplay().getWidth();
        height = getWindowManager().getDefaultDisplay().getHeight();
        content = DatabaseUtils.getContent(lesson);
        mWordList = DatabaseUtils.getWordList();

        if (content != null && contentTextView != null) {
            contentTextView.setText(content, TextView.BufferType.SPANNABLE);
            contentTextView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onGlobalLayout() {
                    contentTextView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    newContent = autoSplitText(contentTextView);
                    if (!TextUtils.isEmpty(newContent)) {
                        contentTextView.setText(newContent);
                        getWords(contentTextView);
                        contentTextView.setMovementMethod(LinkMovementMethod.getInstance());
                    }
                }
            });
        } else {
            Log.e("myError", " no content or TextView found");
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (highLight) {
                    highLight = false;
                    spanWord(-1);
                } else {
                    highLight = true;
                    spanWord(progress);
                }
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                levelTextView.setText("Level " + progress);

                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(100, 100);
                if (progress == 0) {
                    layoutParams.setMargins(0, 7*height/10, 0, 0);
                } else {
                    layoutParams.setMargins(width * progress / 5 - 100, 7*height/10, 0, 0);
                }
                levelTextView.setLayoutParams(layoutParams);
                if (highLight) {
                    spanWord(progress);
                }
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                levelTextView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                progress = seekBar.getProgress();
                if (highLight) {
                    spanWord(progress);
                }
                levelTextView.setVisibility(View.GONE);
            }
        });
    }

    private void spanWord(int level) {
        Spannable style = (Spannable) contentTextView.getText();
        ForegroundColorSpan[] spans = style.getSpans(0, style.length(), ForegroundColorSpan.class);
        for (int i = 0; i < spans.length; i++) {
            style.removeSpan(spans[i]);
        }
        for (int j = 0; j < mWordList.size(); j++) {
            Word word = mWordList.get(j);
            if (word.getLevel() <= level) {
                String wordString = word.getWord();
                StringBuilder expression = new StringBuilder();
                expression.append("\\s");
                expression.append("\\n?");
                for (int i = 0; i < wordString.length(); i++) {
                    expression.append(wordString.charAt(i));
                    expression.append("\\n?");
                }
                expression.append("\\s");
//                Pattern p = Pattern.compile("\\s" + word.getWord() + "\\s");
                Pattern p = Pattern.compile(expression.toString());
                Matcher m = p.matcher(style);
                while (m.find()) {
                    int start = m.start() + 1;
                    int end = m.end() - 1;
                    style.setSpan(new ForegroundColorSpan(Color.RED), start,
                            end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
    }

    public void getWords(TextView textView) {
        Spannable spans = (Spannable) textView.getText();
        Pattern p = Pattern.compile("（?’?“?，?。?：?、?？?\\(?" + "\\s?-?" + "\\w+" + "(\\n\\w+)?");
        Matcher m = p.matcher(spans);
        while (m.find()) {
            int start = m.start() + 1;
            int end = m.end();
            ClickableSpan clickSpan = getClickableSpan();
            spans.setSpan(clickSpan, start, end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        }
        contentTextView.setHighlightColor(Color.YELLOW);
    }


    private ClickableSpan getClickableSpan() {
        return new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                TextView tv = (TextView) widget;
                String s = tv
                        .getText()
                        .subSequence(tv.getSelectionStart(),
                                tv.getSelectionEnd()).toString();
                Log.d("tapped on:", s);
                popupWindow.dismiss();
                popupWindow.showAsDropDown(seekBar,0,-height/5);
                word.setText(s);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(Color.BLACK);
                ds.setUnderlineText(false);
            }
        };
    }
    public void initPopupWindow(){
        View view = getLayoutInflater().inflate(R.layout.popup, null);
        popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        dismissButton = (ImageButton) view.findViewById(R.id.btn_dismiss);
        addWordButton = (AppCompatButton) view.findViewById(R.id.btn_add);
        word = (TextView) view.findViewById(R.id.tv_word);
        popupWindow.setAnimationStyle(R.anim.trans);
        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        addWordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseUtils.addWord(DetailActivity.this,word.getText().toString());
                Toast.makeText(DetailActivity.this,"已加入生词本",Toast.LENGTH_LONG).show();
            }
        });
    }

    public static String halfToFull(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 32) //半角空格
            {
                c[i] = (char) 12288;
                continue;
            }

            //根据实际情况，过滤不需要转换的符号
            //if (c[i] == 46) //半角点号，不转换
            // continue;

            if (c[i] > 32 && c[i] < 127)    //其他符号都转换为全角
                c[i] = (char) (c[i] + 65248);
        }
        return new String(c);
    }

    private String autoSplitText(final TextView tv) {
        final String rawText = tv.getText().toString(); //原始文本
        final Paint tvPaint = tv.getPaint(); //paint，包含字体等信息
        final float tvWidth = tv.getWidth() - tv.getPaddingLeft() - tv.getPaddingRight(); //控件可用宽度

        //将原始文本按行拆分
        String[] rawTextLines = rawText.replaceAll("\r", "").split("\n");
        StringBuilder sbNewText = new StringBuilder();
        for (String rawTextLine : rawTextLines) {
            if (tvPaint.measureText(rawTextLine) <= tvWidth) {
                //如果整行宽度在控件可用宽度之内，就不处理了
                sbNewText.append(rawTextLine);
            } else {
                //如果整行宽度超过控件可用宽度，则按字符测量，在超过可用宽度的前一个字符处手动换行
                float lineWidth = 0;
                for (int cnt = 0; cnt != rawTextLine.length(); ++cnt) {
                    char ch = rawTextLine.charAt(cnt);
                    lineWidth += tvPaint.measureText(String.valueOf(ch));
                    if (lineWidth <= tvWidth) {
                        sbNewText.append(ch);
                    } else {
                        sbNewText.append("\n");
                        lineWidth = 0;
                        --cnt;
                    }
                }
            }
            sbNewText.append("\n");
        }

        //把结尾多余的\n去掉
        if (!rawText.endsWith("\n")) {
            sbNewText.deleteCharAt(sbNewText.length() - 1);
        }

        return sbNewText.toString();
    }

}
