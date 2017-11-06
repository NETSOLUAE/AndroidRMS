package rmsllcoman.com.Adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import rmsllcoman.com.Model.FAQAnswers;
import rmsllcoman.com.Model.FAQQuestions;
import rmsllcoman.com.R;

/**
 * Created by macmini on 10/9/17.
 */

public class FAQAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private ArrayList<FAQQuestions> groups;

    public FAQAdapter(Activity context, ArrayList<FAQQuestions> groups) {
        this.context = context;
        this.groups = groups;
    }

    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.faq_child, null);
        }
        FAQQuestions groupObj = groups.get(groupPosition);
        FAQAnswers child = groupObj.getFaqAnswers().get(
                childPosition);
        TextView descriptionText = (TextView) convertView.findViewById(R.id.faq_child_webview);
        String description = child.getDescription();
        if ((description == null) || (description.equals("")) || (description.equals("null"))) {
            descriptionText.setText("");
        } else {
            descriptionText.setText(Html.fromHtml(description));
        }

        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        ArrayList<FAQAnswers> chList = groups.get(groupPosition)
                .getFaqAnswers();
        return chList.size();
    }

    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    public int getGroupCount() {
        return groups.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.faq_group,
                    null);
        }

        FAQQuestions groupObj = groups.get(groupPosition);
        TextView questionText = (TextView) convertView.findViewById(R.id.faq_question);
        ImageView indicator = (ImageView) convertView.findViewById(R.id.group_indicator_faq);

        String questions = groupObj.getTitle();
        if ((questions == null) || (questions.equals("")) || (questions.equals("null"))) {
            questionText.setText("");
        } else {
            questionText.setText(questions);
        }

        if (isExpanded)
            indicator.setImageResource(R.drawable.group_indicator);
        else
            indicator.setImageResource(R.drawable.group_indicator_closed);
        return convertView;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
