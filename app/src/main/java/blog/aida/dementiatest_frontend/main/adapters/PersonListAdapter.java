package blog.aida.dementiatest_frontend.main.adapters;

import android.app.Activity;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import blog.aida.dementiatest_frontend.R;
import blog.aida.dementiatest_frontend.main.activities.TestResultActivity;
import blog.aida.dementiatest_frontend.main.interfaces.Person;
import blog.aida.dementiatest_frontend.main.services.PatientService;

/**
 * Created by aida on 04-Jun-18.
 */

public class PersonListAdapter extends RecyclerView.Adapter<PersonListAdapter.ViewHolder> {

    private List<String> people;
    private boolean arePatients;
    private Activity activity;

    private PatientService patientService;

    public PersonListAdapter(List<String> people, boolean arePatients, Activity currentActivity) {
        this.people = people;
        this.arePatients = arePatients;
        this.activity = currentActivity;

        this.patientService = new PatientService();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public View layout;
        public TextView personName;
        public TextView personCallToAction;

        public ViewHolder(View itemView) {
            super(itemView);
            layout = itemView;
            personName = (TextView) itemView.findViewById(R.id.person_name);
            personCallToAction = (TextView) itemView.findViewById(R.id.person_call_to_action);
        }

    }

    @Override
    public PersonListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        view = inflater.inflate(R.layout.list_item_people, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {



        holder.personName.setText(people.get(position));

        if(arePatients) {
            holder.personCallToAction.setText("See patient test >");

        } else {
            holder.personCallToAction.setBackgroundColor(activity.getResources().getColor(R.color.buttonBgColor));
            holder.personCallToAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ((TestResultActivity)activity).savePatientOfDoctor(position);
                }
            });
            holder.personCallToAction.setText("Send to doctor >");
        }

    }

    @Override
    public int getItemCount() {
        return people.size();
    }
}
