package indi.yume.demo.renderdemo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import indi.yume.tools.adapter_renderer.recycler.BaseRenderer;

/**
 * Created by yume on 16-2-19.
 */
public class CreateCompanyJobItemRenderer extends BaseRenderer<TestModel> {
    @Bind(R.id.item_title_tv)
    TextView itemTitleTv;
    @Bind(R.id.item_sub_title_tv)
    TextView itemSubTitleTv;
    @Bind(R.id.move_up_btn)
    ImageView moveUpBtn;
    @Bind(R.id.move_down_btn)
    ImageView moveDownBtn;
    @Bind(R.id.delete_item_btn)
    ImageView deleteItemBtn;

    @Override
    public void render() {
        TestModel data = getContent();

        itemTitleTv.setText(data.getCompanyName());

        moveUpBtn.setBackgroundResource(
                getAdapterPosition() == 0 ? R.drawable.icon_not_move_left : R.drawable.icon_up);
        moveDownBtn.setBackgroundResource(
                getAdapterPosition() == getContentLength() - 1 ? R.drawable.icon_not_move_right : R.drawable.icon_down);
    }

    @Override
    protected View inflate(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        return layoutInflater.inflate(R.layout.create_resume_skill_item_layout, viewGroup, false);
    }

    @Override
    protected void findView(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    protected void setListener(View view) {
        moveUpBtn.setOnClickListener(v -> {
            moveUp();
        });
        moveDownBtn.setOnClickListener(v -> {
            moveDown();
        });
        deleteItemBtn.setOnClickListener(v -> remove());
    }
}
