package com.study.android.ahu.experimentfour;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public class StaticActivity extends AppCompatActivity {

    private static final String STATICACTION = "com.study.android.ahu.experimentfour.staticreceiver";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static);

        String[] fruitName = new String[] {"Apple", "Banana", "Cherry", "Coco", "Kiwi", "Orange",
                "Pear", "Strawberry", "Watermelon"};
        int[] imageSource = new int[] {R.mipmap.apple, R.mipmap.banana, R.mipmap.cherry,
                R.mipmap.coco, R.mipmap.kiwi, R.mipmap.orange, R.mipmap.pear,
                R.mipmap.strawberry, R.mipmap.watermelon};

        final List<Fruit> data = new ArrayList<>();

        for (int i = 0; i < 9; i++) {
            Fruit temp = new Fruit(fruitName[i], imageSource[i]);
            data.add(temp);
        }

        ListView fruitList = (ListView) findViewById(R.id.fruitList);
        FruitAdapter adapter = new FruitAdapter(this, data);
        fruitList.setAdapter(adapter);

        fruitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Fruit clickedFruit = data.get(i);
                serializedItem item = new serializedItem();
                item.setName(clickedFruit.GetFruitName());
                item.setId(clickedFruit.GetImageId());

                Bundle bundle = new Bundle();
                bundle.putSerializable("object", item);
                Intent mesIntent = new Intent(STATICACTION);
                mesIntent.putExtras(bundle);

                sendBroadcast(mesIntent);
            }
        });
    }
}
