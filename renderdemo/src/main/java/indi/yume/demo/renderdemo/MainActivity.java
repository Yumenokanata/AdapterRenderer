package indi.yume.demo.renderdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.solovyev.android.views.llm.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import indi.yume.tools.adapter_renderer.recycler.OnItemClickListener;
import indi.yume.tools.adapter_renderer.recycler.RendererAdapter;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.set_button)
    Button setButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setItemAnimator(new FadeInLeftAnimator());
        RendererAdapter<CreateCompanyJobItemRenderer, TestModel> adapter = new RendererAdapter<>(getTestList(), this, CreateCompanyJobItemRenderer.class);
        recyclerView.setAdapter(adapter);

        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.setOnItemClickListener(new OnItemClickListener<TestModel>() {
                    @Override
                    public void onItemClick(View view, TestModel content, int position) {
                        Toast.makeText(MainActivity.this, content.getCompanyName(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        recyclerView.setNestedScrollingEnabled(false);
    }

    private List<TestModel> getTestList() {
        List<TestModel> list = new ArrayList<>();
        for (int i = 0; i < 5; i++)
            list.add(new TestModel("Test111" + i + i + i));
        return list;
    }
}
