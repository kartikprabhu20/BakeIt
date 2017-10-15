package com.bakingapp.kprabhu.bakeit;

import android.arch.lifecycle.LiveData;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bakingapp.kprabhu.bakeit.image.GlideApp;
import com.bakingapp.kprabhu.bakeit.image.VideoThumbnailUrl;
import com.bakingapp.kprabhu.bakeit.model.Recipe;
import com.bakingapp.kprabhu.bakeit.model.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kprabhu on 9/17/17.
 */

class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder>{

    private static final String TAG = "RecipeAdapter";
    private ClickListener clickListener;
    private List<Recipe> recipes;


    public RecipeAdapter(LiveData<List<Recipe>> recipes, ClickListener onRecipeClicked ) {
        this.recipes = recipes.getValue();
        clickListener = onRecipeClicked;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_card_view, parent, false);
        return new ViewHolder(view);    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder != null) {
            Recipe recipe = recipes.get(position);
            holder.recipeTitle.setText(recipe.name);
            if (!recipes.get(position).image.isEmpty()) {
                GlideApp.with(holder.itemView)
                        .load(recipes.get(position).image)
                        .centerInside()
                        .placeholder(R.drawable.recipe_icon_md)
                        .into(holder.recipeImage);
            } else {
                Step lastStepWithVideo = getLastStepWithVideo(recipe.steps);
                if (lastStepWithVideo == null) {
                    Log.i(TAG, "onBindViewHolder: Video Url not found");
                    return;
                }
                GlideApp.with(holder.itemView)
                        .load(new VideoThumbnailUrl(lastStepWithVideo.videoURL))
                        .centerInside()
                        .placeholder(R.drawable.recipe_icon_md)
                        .into(holder.recipeImage);
            }

        }
    }

    private Step getLastStepWithVideo(List<Step> steps) {
       /* return steps.stream()
                .filter(s -> s.videoURL != null && !s.videoURL.isEmpty())
                .reduce((a, b) -> b).orElse(null);
*/
        Step temp = null;
        for (Step step : steps) {
            if(step.videoURL != null && !step.videoURL.isEmpty()){
                temp = step;
            }
        }
        return temp;
    }

    @Override
    public int getItemCount() {
        if (recipes != null) {
            return recipes.size();
        }
        return 0;    }

    public void updateRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.recipe_image)
        ImageView recipeImage;

        @BindView(R.id.recipe_title)
        TextView recipeTitle;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.accept(getLayoutPosition());
        }

    }

    interface ClickListener{
        void accept(int position);
    }
}
