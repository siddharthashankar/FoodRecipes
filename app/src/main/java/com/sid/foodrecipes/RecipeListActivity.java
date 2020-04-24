package com.sid.foodrecipes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class RecipeListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RecipeListActivity.this,"clicked",Toast.LENGTH_LONG).show();
                if(mProgressBar.getVisibility() == View.VISIBLE){
                        showProgressBar(false);
                }else
                    showProgressBar(true);
            }
        });
    }

}
