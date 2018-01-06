package pyxis.uzuki.live.naraeimagepickerdemo;

import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import pyxis.uzuki.live.naraeimagepicker.Constants;
import pyxis.uzuki.live.naraeimagepicker.NaraeImagePicker;
import pyxis.uzuki.live.naraeimagepicker.impl.OnPickResultListener;
import pyxis.uzuki.live.naraeimagepicker.widget.AdjustableGridItemDecoration;
import pyxis.uzuki.live.pyxinjector.PyxInjector;
import pyxis.uzuki.live.pyxinjector.annotation.BindView;
import pyxis.uzuki.live.pyxinjector.annotation.OnClick;
import pyxis.uzuki.live.pyxinjector.base.InjectActivity;

/**
 * NaraeImagePicker
 * Class: MainActivity
 * Created by Pyxis on 1/6/18.
 * <p>
 * Description:
 */
public class MainActivity extends InjectActivity {
    private @BindView RecyclerView recyclerView;
    private ArrayList<String> itemList = new ArrayList<>();
    private ListAdapter adapter = new ListAdapter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RectF rectF = AdjustableGridItemDecoration.getRectFObject(this);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.addItemDecoration(new AdjustableGridItemDecoration(rectF, itemList, 3));
        recyclerView.setAdapter(adapter);
    }

    @OnClick(R.id.btnSelect)
    private void clickSelect() {
        NaraeImagePicker.instance.start(this, Constants.LIMIT_UNLIMITED, new OnPickResultListener() {
            @Override
            public void onSelect(int resultCode, @NotNull ArrayList<String> imageList) {
                if (resultCode == NaraeImagePicker.PICK_SUCCESS) {
                    itemList.addAll(imageList);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "failed to pick image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class ListAdapter extends RecyclerView.Adapter<ListHolder> {

        @Override
        public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ListHolder(LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main_row, parent, false));
        }

        @Override
        public void onBindViewHolder(ListHolder holder, int position) {
            String url = itemList.get(position);
            Glide.with(MainActivity.this).load(url).into(holder.imgThumbnail);
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }
    }

    private class ListHolder extends RecyclerView.ViewHolder {
        private @BindView ImageView imgThumbnail;

        public ListHolder(View itemView) {
            super(itemView);
            PyxInjector.getInstance().execute(MainActivity.this, this, itemView);
        }
    }
}
