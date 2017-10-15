package com.bakingapp.kprabhu.bakeit;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.bakingapp.kprabhu.bakeit.model.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bakingapp.kprabhu.bakeit.MainFragment.RECIPE;
import static com.bakingapp.kprabhu.bakeit.RecipeStepActivity.STEP;

/**
 * Created by kprabhu on 9/17/17.
 */

public class DetailActivity extends AppCompatActivity implements LifecycleOwner {

    private static final String TAG = "DetailActivity";

    LifecycleRegistry registry = new LifecycleRegistry(this);

    @BindView(R.id.recycler_view)
    RecyclerView recipeView;

    RecipeStepsAdapter adapter;

    private boolean isTwoPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Recipe recipe = getRecipe();
        Log.d(TAG, "onCreate:" + recipe);
        ButterKnife.bind(this);
        recipeView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecipeStepsAdapter(recipe, new RecipeStepsAdapter.ClickListener() {
            @Override
            public void click(int position) {
                if(isTwoPane){
                    RecipeStepViewModel viewModel = ViewModelProviders.of(DetailActivity.this).get(RecipeStepViewModel.class);
                    viewModel.setRecipe(recipe);
                    viewModel.setStep(recipe.steps.get(position));
                    RecipeStepPagerFragment fragment = new RecipeStepPagerFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.recipe_detail_container,fragment).commit();
                    return;
                }
                Log.i(TAG, "onCreate: clicked " + position);
                Intent intent = new Intent(DetailActivity.this, RecipeStepActivity.class);
                intent.putExtra(RECIPE,recipe);
                intent.putExtra(STEP, recipe.steps.get(position));
                startActivity(intent);

            }
        }
        );
        recipeView.setAdapter(adapter);
        if(findViewById(R.id.recipe_detail_container)!=null){
            isTwoPane = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.recipe_info,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add_to_widget:
                new RecipeInfoWidgetManager().updateWidgetRecipe(getRecipe());
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public Lifecycle getLifecycle() {
        return registry;
    }

    public Recipe getRecipe() {
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            throw new IllegalStateException();
        }
        if (!extras.containsKey(RECIPE)) {
            throw new IllegalStateException();
        }
        return (Recipe) extras.get(RECIPE);
    }
}
