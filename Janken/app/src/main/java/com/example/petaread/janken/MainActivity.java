package com.example.petaread.janken;


        import android.content.res.Resources;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;
        import android.widget.Toast;
        import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    enum ResultId{
        WIN,
        LOSE,
        DRAW;
    }
    enum Pattern{
        ROCK,
        SCISSORS,
        PAPER;
    }

    TextView t_display;
    TextView t_counter;
    Button bt[] = new Button[3];
    Button bt_r;

    Pattern select;
    Pattern cpu_select;
    ResultId result;
    boolean play_flag = false;

    //-----カウンタ関係-----
    int round = 0;
    int counter_W = 0;
    int counter_L = 0;
    int counter_D = 0;
    //----------------------

    //じゃんけんの「手」の定義
    class Hand{
        public String name;
        private Hand strongerThan;

        //コンストラクタ
        Hand(String name){
            this.name = name;
        }

        //強弱判定の設定
        public void setStrongerThan(Hand h){
            this.strongerThan = h;
        }

        //勝敗判定
        public ResultId judge(Hand h){
            if(h == this)
                return ResultId.DRAW;
            else if(this.strongerThan == h)
                return ResultId.WIN;
            else
                return ResultId.LOSE;
        }

        public String toString(){
            return name;
        }
    }

    //じゃんけんの結果を出す
    class Battle{
        Hand r = new Hand(getString(R.string.hand_r));
        Hand s = new Hand(getString(R.string.hand_s));
        Hand p = new Hand(getString(R.string.hand_p));
        Hand[] hands = {r,s,p};
        Pattern[] pt = Pattern.values();

        int cpu_h;
        Pattern you;
        ResultId result;

        //コンストラクタ
        Battle(Pattern you){
            this.you = you;
            r.setStrongerThan(s);
            s.setStrongerThan(p);
            p.setStrongerThan(r);
            cpu_h = new Random().nextInt(hands.length);
            result = hands[you.ordinal()].judge(hands[cpu_h]);
        }

        //結果の取得
        public ResultId getResult(){
            return result;
        }

        //相手が選択した手の取得
        public Pattern GetCPUpattern(){
            return pt[cpu_h];
        }

    }

    //enum内容の文字列表示
    public String toStringResultId(ResultId id){
        if(id == ResultId.WIN)
            return getString(R.string.res_w);
        if(id == ResultId.LOSE)
            return getString(R.string.res_l);
        if(id == ResultId.DRAW)
            return getString(R.string.res_d);
        return null;
    }
    public String toStringPattern(Pattern id){
        if(id == Pattern.ROCK)
            return getString(R.string.hand_r);
        if(id == Pattern.SCISSORS)
            return getString(R.string.hand_s);
        if(id == Pattern.PAPER)
            return getString(R.string.hand_p);
        return null;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //リソースIDの設定
        bt[0] = (Button)findViewById(R.id.button_R);
        bt[1] = (Button)findViewById(R.id.button_S);
        bt[2] = (Button)findViewById(R.id.button_P);
        bt_r = (Button)findViewById(R.id.button_reset);
        t_display = (TextView)findViewById(R.id.textview01);
        t_counter = (TextView)findViewById(R.id.textview02);

        //Listener設定
        for(int i=0;i<bt.length;i++)
            bt[i].setOnClickListener(this);
        bt_r.setOnClickListener(this);

        //TextViewの初期化
        t_display.setText(getString(R.string.mes01));
        t_counter.setText(getString(R.string.counter,0,0,0,0));

    }

    @Override
    public void onClick(View view){
        //押したボタンに応じて判定
        switch(view.getId()){
            case R.id.button_R:{
                select = Pattern.ROCK;
                play_flag = true;
                break;
            }
            case R.id.button_S:{
                select = Pattern.SCISSORS;
                play_flag = true;
                break;
            }
            case R.id.button_P:{
                select = Pattern.PAPER;
                play_flag = true;
                break;
            }
            case R.id.button_reset:{
                Toast.makeText(this ,getString(R.string.mes_res) ,Toast.LENGTH_SHORT).show();
                play_flag = false;
                break;
            }
        }

        //内部数値更新
        if(play_flag){
            //じゃんけんの手を選択した場合
            Battle bt = new Battle(select);
            result = bt.getResult();
            cpu_select = bt.GetCPUpattern();

            round++;
            if(result == ResultId.WIN ) counter_W++;
            if(result == ResultId.LOSE) counter_L++;
            if(result == ResultId.DRAW) counter_D++;
        }
        else{
            //リセットボタンを押した場合
            round = 0;
            counter_W = 0;
            counter_L = 0;
            counter_D = 0;
        }


        //表示部分更新
        if(play_flag)
            t_display.setText(getString(R.string.mes_play,
                    toStringPattern(select),
                    toStringPattern(cpu_select),
                    toStringResultId(result)));
        else
            t_display.setText(getString(R.string.mes01));

        t_counter.setText(getString(R.string.counter,
                round,counter_W,
                counter_L,
                counter_D));


    }
}