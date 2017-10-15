package com.bakingapp.kprabhu.bakeit.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.bakingapp.kprabhu.bakeit.R;
import com.bakingapp.kprabhu.bakeit.RecipeInfoWidgetManager;
import com.bakingapp.kprabhu.bakeit.model.Recipe;
import com.bakingapp.kprabhu.bakeit.widget.RecipeInfoWidgetRemoteViewService;

/**
 * Created by kprabhu on 9/18/17.
 */

public class RecipeInfoWidget extends AppWidgetProvider{
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, new RecipeInfoWidgetManager());
        }
    }

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                         int appWidgetId, RecipeInfoWidgetManager recipeInfoWidgetManager) {
        Recipe recipe = recipeInfoWidgetManager.getRecipe();
        String widgetText;
        if (recipe != null) {
            widgetText = recipe.name;
        } else {
            widgetText = context.getString(R.string.widget_no_recipe);
        }

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_recipe_info);
        views.setTextViewText(R.id.widget_text, widgetText);
        views.setRemoteAdapter(R.id.widget_list,
                new Intent(context, RecipeInfoWidgetRemoteViewService.class));

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}
