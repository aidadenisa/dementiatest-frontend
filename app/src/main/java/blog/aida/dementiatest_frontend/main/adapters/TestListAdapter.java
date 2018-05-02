package blog.aida.dementiatest_frontend.main.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import blog.aida.dementiatest_frontend.R;
import blog.aida.dementiatest_frontend.main.activities.PersonalInformationTestActivity;
import blog.aida.dementiatest_frontend.main.activities.TestsBoardActivity;
import blog.aida.dementiatest_frontend.main.models.Question;
import blog.aida.dementiatest_frontend.main.models.Test;
import blog.aida.dementiatest_frontend.main.models.TestConfiguration;

/**
 * Created by aida on 01-May-18.
 */

public class TestListAdapter extends RecyclerView.Adapter<TestListAdapter.ViewHolder> {

    private List<TestConfiguration> tests;
    private Context parentContext;
    private ViewGroup parent;
    private TestsBoardActivity activity;

    public TestListAdapter(List<TestConfiguration> tests, TestsBoardActivity activity) {
        this.tests = tests;
        this.activity = activity;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public View layout;
        public TextView testName;
        public TextView testCallToAction;

        public ViewHolder(View itemView) {
            super(itemView);
            layout = itemView;
            testName = (TextView) itemView.findViewById(R.id.test_name_on_board);
            testCallToAction = (TextView) itemView.findViewById(R.id.test_action_on_board);
        }

    }

    @Override
    public TestListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        parentContext = parent.getContext();
        this.parent = parent;

        View view;
        LayoutInflater inflater = LayoutInflater.from(parentContext);
        view = inflater.inflate(R.layout.list_item_test_board, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TestListAdapter.ViewHolder holder, int position) {

        TestConfiguration currentTest = tests.get(position);

        holder.testName.setText(currentTest.getName());

        holder.testCallToAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 //TODO: GO TO TEST VIEW
            }
        });
    }


    @Override
    public int getItemCount() {
        return tests.size();
    }

//    @Override
//    public int getItemViewType(int position) {
//
//    }
}
