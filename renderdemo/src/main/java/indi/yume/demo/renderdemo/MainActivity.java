package indi.yume.demo.renderdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import org.solovyev.android.views.llm.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import indi.yume.tools.adapter_renderer.recycler.RendererAdapter;
import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setItemAnimator(new FadeInLeftAnimator());
        RecyclerView.Adapter adapter = new RendererAdapter<>(getTestList(), this, CreateCompanyJobItemRenderer.class);
        recyclerView.setAdapter(adapter);

        recyclerView.setNestedScrollingEnabled(false);
    }

    private List<TestModel> getTestList() {
        List<TestModel> list = new ArrayList<>();
        for(int i = 0; i < 5; i ++)
            list.add(new TestModel("Test111" + i + i + i));
        return list;
    }
}
